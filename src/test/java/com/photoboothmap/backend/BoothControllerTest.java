package com.photoboothmap.backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class BoothControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("[GET-OK] booth pin")
    public void booth_pin_성공() throws Exception {

        // given

        // when
        MvcResult result = mvc.perform(get("/map?curx={v1}&cury={v2}&nex={v3}&ney={v4}",
                        126.5709308145358, 33.452739313807456, 126.5809308145358, 33.552739313807456))

        // then
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.result.boothList").isArray())
                .andExpect(jsonPath("$.result.boothList.length()").value(2))
                .andReturn();

        String jsonRes = "{\"success\":true," +
                "\"result\":{\"boothList\":" +
                "[" +
                "{\"boothIdx\":2812,\"brand\":\"인생네컷\",\"latitude\":33.51676492,\"longitude\":126.58029364}," +
                "{\"boothIdx\":21090,\"brand\":\"비룸스튜디오\",\"latitude\":33.52611009,\"longitude\":126.57742813}" +
                "]" +
                "}}";
        Assertions.assertEquals(result.getResponse().getContentAsString(), jsonRes);

        /* 예상 결과
        {
            "success": true,
            "result": {
                "boothList": [
                    {
                        "boothIdx": 2812,
                        "brand": "인생네컷",
                        "latitude": 33.51676492,
                        "longitude": 126.58029364
                    },
                    {
                        "boothIdx": 21090,
                        "brand": "비룸스튜디오",
                        "latitude": 33.52611009,
                        "longitude": 126.57742813
                    }
                ]
            }
        }
        */

    }

    @Test
    @DisplayName("[GET-OK] booth pin (empty)")
    public void booth_pin_성공_빈리스트() throws Exception {

        // given

        // when
        mvc.perform(get("/map?curx={v1}&cury={v2}&nex={v3}&ney={v4}",
                        0, 0, 20, 20))

        // then
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.result.boothList").isArray())
                .andExpect(jsonPath("$.result.boothList").isEmpty());

        /* 예상 결과
        {
            "success": true,
            "result": {
                "boothList": []
            }
        }
        */

    }

    @Test
    @DisplayName("[GET-OK] booth list")
    public void booth_list_성공() throws Exception {
        // given

        // when
        MvcResult result = mvc.perform(get("/map/list?curx={v1}&cury={v2}&count={v3}",
                        126.5709308145358, 33.452739313807456, 0))

        // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.result.boothList").isArray())
                .andExpect(jsonPath("$.result.boothList.length()").value(10))
                .andReturn();

        String jsonRes = "{\"success\":true," +
                "\"result\":{\"boothList\":" +
                "[" +
                "{\"boothIdx\":2811,\"brand\":\"인생네컷\",\"name\":\"인생네컷 제주제이원카트클럽점\",\"address\":\"제주특별자치도 제주시 도남동 411-6\",\"distance\":5099,\"score\":null,\"reviewNum\":0,\"latitude\":33.47981512,\"longitude\":126.52656919}," +
                "{\"boothIdx\":3215,\"brand\":\"포토시그니처\",\"name\":\"포토시그니처 제주시청점\",\"address\":\"제주특별자치도 제주시 이도이동 1768-20\",\"distance\":6331,\"score\":null,\"reviewNum\":0,\"latitude\":33.49843064,\"longitude\":126.53020421}," +
                "{\"boothIdx\":21116,\"brand\":\"모노맨션\",\"name\":\"모노맨션 제주시청점\",\"address\":\"제주특별자치도 제주시 이도이동 1768-21 1층\",\"distance\":6333,\"score\":null,\"reviewNum\":0,\"latitude\":33.49838333,\"longitude\":126.53009255}," +
                "{\"boothIdx\":2805,\"brand\":\"인생네컷\",\"name\":\"인생네컷 제주시청점\",\"address\":\"제주특별자치도 제주시 이도이동 1767-14\",\"distance\":6334,\"score\":null,\"reviewNum\":0,\"latitude\":33.49818005,\"longitude\":126.52974498}," +
                "{\"boothIdx\":3587,\"brand\":\"포토그레이\",\"name\":\"포토그레이 제주점\",\"address\":\"제주특별자치도 제주시 이도이동 1767-5 1층\",\"distance\":6341,\"score\":null,\"reviewNum\":0,\"latitude\":33.49831018,\"longitude\":126.52982391}," +
                "{\"boothIdx\":3082,\"brand\":\"하루필름\",\"name\":\"하루필름 제주시청점\",\"address\":\"제주특별자치도 제주시 이도이동 1768-30 1층\",\"distance\":6369,\"score\":null,\"reviewNum\":0,\"latitude\":33.49863487,\"longitude\":126.52985444}," +
                "{\"boothIdx\":2807,\"brand\":\"인생네컷\",\"name\":\"인생네컷 제주일도지구점\",\"address\":\"제주특별자치도 제주시 일도이동 392 1층\",\"distance\":6467,\"score\":null,\"reviewNum\":0,\"latitude\":33.50525091,\"longitude\":126.54095643}," +
                "{\"boothIdx\":3011,\"brand\":\"포토이즘\",\"name\":\"포토이즘박스 제주시청점\",\"address\":\"제주특별자치도 제주시 이도이동 1772-11\",\"distance\":6494,\"score\":null,\"reviewNum\":0,\"latitude\":33.49957694,\"longitude\":126.52911542}," +
                "{\"boothIdx\":3360,\"brand\":\"포토이즘\",\"name\":\"포토이즘컬러드 제주시청점\",\"address\":\"제주특별자치도 제주시 이도이동 1771-3\",\"distance\":6497,\"score\":null,\"reviewNum\":0,\"latitude\":33.49950772,\"longitude\":126.52893285}," +
                "{\"boothIdx\":3328,\"brand\":\"셀픽스\",\"name\":\"셀픽스 제주도점\",\"address\":\"제주특별자치도 제주시 이도이동 1771-1\",\"distance\":6530,\"score\":null,\"reviewNum\":0,\"latitude\":33.4997268,\"longitude\":126.52869491}" +
                "]" +
                "}}";
        Assertions.assertEquals(result.getResponse().getContentAsString(), jsonRes);

        /* 예상 결과
        {
            "success": true,
            "result": {
                "boothList": [
                    {
                        "boothIdx": 2811,
                        "brand": "인생네컷",
                        "name": "인생네컷 제주제이원카트클럽점",
                        "address": "제주특별자치도 제주시 도남동 411-6",
                        "distance": 5099,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 33.47981512,
                        "longitude": 126.52656919
                    },
                    {
                        "boothIdx": 3215,
                        "brand": "포토시그니처",
                        "name": "포토시그니처 제주시청점",
                        "address": "제주특별자치도 제주시 이도이동 1768-20",
                        "distance": 6331,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 33.49843064,
                        "longitude": 126.53020421
                    },
                    {
                        "boothIdx": 21116,
                        "brand": "모노맨션",
                        "name": "모노맨션 제주시청점",
                        "address": "제주특별자치도 제주시 이도이동 1768-21 1층",
                        "distance": 6333,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 33.49838333,
                        "longitude": 126.53009255
                    },
                    {
                        "boothIdx": 2805,
                        "brand": "인생네컷",
                        "name": "인생네컷 제주시청점",
                        "address": "제주특별자치도 제주시 이도이동 1767-14",
                        "distance": 6334,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 33.49818005,
                        "longitude": 126.52974498
                    },
                    {
                        "boothIdx": 3587,
                        "brand": "포토그레이",
                        "name": "포토그레이 제주점",
                        "address": "제주특별자치도 제주시 이도이동 1767-5 1층",
                        "distance": 6341,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 33.49831018,
                        "longitude": 126.52982391
                    },
                    {
                        "boothIdx": 3082,
                        "brand": "하루필름",
                        "name": "하루필름 제주시청점",
                        "address": "제주특별자치도 제주시 이도이동 1768-30 1층",
                        "distance": 6369,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 33.49863487,
                        "longitude": 126.52985444
                    },
                    {
                        "boothIdx": 2807,
                        "brand": "인생네컷",
                        "name": "인생네컷 제주일도지구점",
                        "address": "제주특별자치도 제주시 일도이동 392 1층",
                        "distance": 6467,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 33.50525091,
                        "longitude": 126.54095643
                    },
                    {
                        "boothIdx": 3011,
                        "brand": "포토이즘",
                        "name": "포토이즘박스 제주시청점",
                        "address": "제주특별자치도 제주시 이도이동 1772-11",
                        "distance": 6494,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 33.49957694,
                        "longitude": 126.52911542
                    },
                    {
                        "boothIdx": 3360,
                        "brand": "포토이즘",
                        "name": "포토이즘컬러드 제주시청점",
                        "address": "제주특별자치도 제주시 이도이동 1771-3",
                        "distance": 6497,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 33.49950772,
                        "longitude": 126.52893285
                    },
                    {
                        "boothIdx": 3328,
                        "brand": "셀픽스",
                        "name": "셀픽스 제주도점",
                        "address": "제주특별자치도 제주시 이도이동 1771-1",
                        "distance": 6530,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 33.4997268,
                        "longitude": 126.52869491
                    }
                ]
            }
        }
        */

    }

    @Test
    @DisplayName("[GET-OK] booth list (empty)")
    public void booth_list_성공_빈리스트() throws Exception {
        // given

        // when
        mvc.perform(get("/map/list?curx={v1}&cury={v2}&count={v3}",
                        126.5709308145358, 33.452739313807456, 1750))

        // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.result.boothList").isArray())
                .andExpect(jsonPath("$.result.boothList").isEmpty());

        /* 예상 결과
        {
            "success": true,
            "result": {
                "boothList": []
            }
        }
        */

    }

}
