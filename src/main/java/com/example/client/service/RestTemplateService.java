package com.example.client.service;


import com.example.client.dto.UserRequest;
import com.example.client.dto.UserResponse;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class RestTemplateService {
    //http://localhost/api/server/hello
    //response
    // URICOmponenteBuilder
    public UserResponse hello() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/hello") //?name="steve"&age =10
                .queryParam("name", "steve") // 쿼리파람으로 받는다
                .queryParam("age", 25)
                .encode()
                .build()
                .toUri();

        System.out.println(uri.toString());
        //이게 원래 서버에서 나오는 uri를 toString으로 출력
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);// 우리가 지정한 타입으로 받을수 있음
        ResponseEntity<String> resultGetEntity = restTemplate.getForEntity(uri, String.class);
        // 그냥 json을 스트링으로 변환해준다
        System.out.println(resultGetEntity.getStatusCode()); //200 ok
        System.out.println(resultGetEntity.getBody());

        ResponseEntity<UserResponse> resultJson = restTemplate.getForEntity(uri, UserResponse.class);
        // 헤더 정보를 받으려면 ResponseEntity로 받는게 좋다
        System.out.println("Json 받을 꺼야 이번엔");
        System.out.println(resultJson.getStatusCode());
        System.out.println(resultJson.getBody());
        return resultJson.getBody();
    }

    //loombok은 아닌 dto 게더 세더
    public UserResponse post() {
        // http://localhost:9090/api/server/(post)user/(pathvairble){userId}/{userName}
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100,"steve")
                .toUri();
        System.out.println(uri);

        //http body -> object -> object mapper -> json -> rest template -> http body json
        UserRequest userRequest = new UserRequest();
        userRequest.setName("steve");
        userRequest.setAge(10);
        RestTemplate restTemplate = new RestTemplate();
        //레스트 탬플레이트 생성
        //string 으로 받으면 그냥 문자열을 받는거고
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(uri, userRequest, UserResponse.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        return response.getBody();
    }

    public UserResponse exchane(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100,"steve")
                .toUri();
        System.out.println(uri);

        //http body -> object -> object mapper -> json -> rest template -> http body json
        UserRequest userRequest = new UserRequest();
        userRequest.setName("steve");
        userRequest.setAge(10);
        //리퀘스트 엔티티만들기
        RequestEntity<UserRequest> requestEntity = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-authorization", "abcd")
                .header("custom-header", "fffff")
                .body(userRequest);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.exchange(requestEntity, UserResponse.class);
        return response.getBody();
    }

}
