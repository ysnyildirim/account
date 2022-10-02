/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.config;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.MD5Util;
import com.yil.account.user.dto.UserRequest;
import com.yil.account.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Random;

@Configuration
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
@SuppressWarnings({"unused", "unchecked"})
public class MyJob {

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public final String uri = "http://localhost:8082/api/account/v1";

    // @Scheduled(fixedDelay = 1, initialDelay = 5 * 1000)
    // @Async
    public void createUser() throws NoSuchAlgorithmException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders h2 = new HttpHeaders();
        h2.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        h2.add(ApiConstant.AUTHENTICATED_USER_ID, String.valueOf(1));
        HttpEntity httpEntity = new HttpEntity<>(generateUser(), h2);
        ResponseEntity<UserResponse> responseEntity = restTemplate.exchange(uri + "/users", HttpMethod.POST, httpEntity, UserResponse.class);
        if (responseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
            System.out.println("User created-> Userid=" + responseEntity.getBody().getId());
        } else
            System.out.println("User not created !" + responseEntity.getStatusCode());
    }

    private UserRequest generateUser() throws NoSuchAlgorithmException {
        return UserRequest
                .builder()
                .userName(randomString(new Random().nextInt(5, 30)))
                .password(MD5Util.encode("admin"))
                .enabled(new Random().nextBoolean())
                .locked(new Random().nextBoolean())
                .mail(randomString(new Random().nextInt(1, 50)).toLowerCase() + "@gmail.com")
                .passwordNeedsChanged(true)
                .userTypeId(SetupDataLoader.userTypeGercekKisi.getId())
                .build();
    }


    private String randomString(int i) {
        String s = "";
        for (int k = 0; k < i; k++)
            s += upper.toCharArray()[new Random().nextInt(upper.length())];
        return s;
    }
}
