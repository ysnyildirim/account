/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.config;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.MD5Util;
import com.yil.account.user.dto.CreateUserDto;
import com.yil.account.user.dto.CreateUserResponse;
import com.yil.account.user.service.UserTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Random;

@Configuration
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
public class MyJob {

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Scheduled(fixedDelay = 1, initialDelay = 3 * 1000)
    private void createUser() throws NoSuchAlgorithmException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders h2 = new HttpHeaders();
        h2.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        h2.add(ApiConstant.AUTHENTICATED_USER_ID, String.valueOf(1));
        HttpEntity httpEntity = new HttpEntity<>(generateUser(), h2);
        String uri = "http://localhost:8082/api/account/v1/users";
        ResponseEntity<CreateUserResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, CreateUserResponse.class);
        if (responseEntity.getStatusCode().equals(HttpStatus.CREATED))
            System.out.println("User created-> Userid=" + responseEntity.getBody().getId());
        else
            System.out.println("User not created !" + responseEntity.getStatusCode());
    }

    private CreateUserDto generateUser() throws NoSuchAlgorithmException {
        return CreateUserDto
                .builder()
                .userName(randomString(new Random().nextInt(1, 100)))
                .password(MD5Util.encode("admin"))
                .enabled(new Random().nextBoolean())
                .locked(new Random().nextBoolean())
                .mail(randomString(new Random().nextInt(1, 100)).toLowerCase() + "@gmail.com")
                .passwordNeedsChanged(new Random().nextBoolean())
                .userTypeId(UserTypeService.User)
                .build();
    }

    private String randomString(int i) {
        String s = "";
        for (int k = 0; k < i; k++)
            s += upper.toCharArray()[new Random().nextInt(upper.length())];
        return s;
    }
}
