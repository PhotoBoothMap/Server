package com.photoboothmap.backend.home;

import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.BaseResponse;
import com.photoboothmap.backend.util.config.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class HomeController {

    @GetMapping
    public String healthCheck(){
        return "Hello World!";
    }

    @GetMapping("/responseTest")
    public ResponseEntity<BaseResponse> responseTest() {
        System.out.println("HomeController.responseTest");

        try {
            // 서비스단에서 실행할 무언가
            List<TestDto> list = doSomething();
            // 성공시 자동 200 반환
            return new BaseResponse<>(list).convert();
        } catch (BaseException e){
            // 실패시 custom한 status로 code 헤더 설정, body로 메세지 반환
            return new BaseResponse<>(e.getStatus()).convert();
        }
    }

    public List<TestDto> doSomething() throws BaseException { // 서비스단 가정
        TestDto test = TestDto.builder().id(1).name("테스트").build();
        TestDto test2 = TestDto.builder().id(2).name("테스트2").build();
        List<TestDto> list = new ArrayList<>(Arrays.asList(test, test2));

        if (false) { // 에러가 발생했을 때 예외 status 명시
            throw new BaseException(ResponseStatus.TEST_STATUS);
        } else {
            return list;
        }
    }
}
