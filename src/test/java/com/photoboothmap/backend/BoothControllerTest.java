package com.photoboothmap.backend;

import com.photoboothmap.backend.util.config.ResponseStatus;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class BoothControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    String allBrand = "포토이즘,하루필름,포토시그니처,인생네컷,셀픽스,기타";
    String etcBrand = "인생네컷,포토시그니처,기타";
    String noEtcBrand = "포토이즘,하루필름,셀픽스";

    String wrongBrand = "인생세컷,기타";
    ResponseStatus rangeError = ResponseStatus.WRONG_LATLNG_RANGE;
    ResponseStatus nameError = ResponseStatus.WRONG_BRAND_NAME;
    ResponseStatus keywordError = ResponseStatus.EMPTY_KEYWORD;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /* /map test start ------------------------------------------------------------------------------------------------------------------------------------- */
    private String mapUrl = "/map?curx={v1}&cury={v2}&nex={v3}&ney={v4}&filter={v5}";

    @Test
    @DisplayName("[GET-OK] booth pin")
    public void booth_pin_성공() throws Exception {

        // given

        // when
        MvcResult result = mvc.perform(get(mapUrl,
                        126.5709308145358, 33.452739313807456, 126.5809308145358, 33.552739313807456, allBrand))

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
    @DisplayName("[GET-OK] booth pin (filter)")
    public void booth_pin_성공_필터() throws Exception {

        // given

        /**
         * test 1: all
         */
        // when
        MvcResult result = mvc.perform(get(mapUrl,
                        127.068308, 37.543405, 127.073633, 37.546472, allBrand))

        // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.result.boothList").isArray())
                .andExpect(jsonPath("$.result.boothList.length()").value(11))
                .andReturn();

        String jsonRes = "{\"success\":true," +
                "\"result\":{\"boothList\":" +
                "[" +
                "{\"boothIdx\":2048,\"brand\":\"하루필름\",\"latitude\":37.541799,\"longitude\":127.06862826}," +
                "{\"boothIdx\":2050,\"brand\":\"포토이즘\",\"latitude\":37.54130192,\"longitude\":127.06814577}," +
                "{\"boothIdx\":2051,\"brand\":\"인생네컷\",\"latitude\":37.54094932,\"longitude\":127.07022291}," +
                "{\"boothIdx\":2054,\"brand\":\"포토이즘\",\"latitude\":37.54176854,\"longitude\":127.06673745}," +
                "{\"boothIdx\":2057,\"brand\":\"포토시그니처\",\"latitude\":37.54108222,\"longitude\":127.06944795}," +
                "{\"boothIdx\":2058,\"brand\":\"인생네컷\",\"latitude\":37.54092679,\"longitude\":127.07022176}," +
                "{\"boothIdx\":2069,\"brand\":\"인싸포토\",\"latitude\":37.54243248,\"longitude\":127.07153123}," +
                "{\"boothIdx\":2071,\"brand\":\"포토랩플러스\",\"latitude\":37.54210979,\"longitude\":127.06872246}," +
                "{\"boothIdx\":2823,\"brand\":\"플레이인더박스\",\"latitude\":37.54120584,\"longitude\":127.06757879}," +
                "{\"boothIdx\":3224,\"brand\":\"셀픽스\",\"latitude\":37.54242257,\"longitude\":127.07002401}," +
                "{\"boothIdx\":21096,\"brand\":\"모노맨션\",\"latitude\":37.54144301,\"longitude\":127.06878521}" +
                "]" +
                "}}";
        Assertions.assertEquals(result.getResponse().getContentAsString(), jsonRes);

        /**
         * test 2: 인생네컷,포토시그니처,기타
         */
        // when
        MvcResult result2 = mvc.perform(get(mapUrl,
                        127.068308, 37.543405, 127.073633, 37.546472, etcBrand))

        // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.result.boothList").isArray())
                .andExpect(jsonPath("$.result.boothList.length()").value(7))
                .andExpect(content().string(not(containsString("포토이즘"))))
                .andExpect(content().string(not(containsString("하루필름"))))
                .andExpect(content().string(not(containsString("셀픽스"))))
                .andReturn();

        String jsonRes2 = "{\"success\":true," +
                "\"result\":{\"boothList\":" +
                "[" +
                "{\"boothIdx\":2051,\"brand\":\"인생네컷\",\"latitude\":37.54094932,\"longitude\":127.07022291}," +
                "{\"boothIdx\":2057,\"brand\":\"포토시그니처\",\"latitude\":37.54108222,\"longitude\":127.06944795}," +
                "{\"boothIdx\":2058,\"brand\":\"인생네컷\",\"latitude\":37.54092679,\"longitude\":127.07022176}," +
                "{\"boothIdx\":2069,\"brand\":\"인싸포토\",\"latitude\":37.54243248,\"longitude\":127.07153123}," +
                "{\"boothIdx\":2071,\"brand\":\"포토랩플러스\",\"latitude\":37.54210979,\"longitude\":127.06872246}," +
                "{\"boothIdx\":2823,\"brand\":\"플레이인더박스\",\"latitude\":37.54120584,\"longitude\":127.06757879}," +
                "{\"boothIdx\":21096,\"brand\":\"모노맨션\",\"latitude\":37.54144301,\"longitude\":127.06878521}" +
                "]" +
                "}}";
        Assertions.assertEquals(result2.getResponse().getContentAsString(), jsonRes2);

        /**
         * test 3: 포토이즘,하루필름,셀픽스
         */
        // when
        MvcResult result3 = mvc.perform(get(mapUrl,
                        127.068308, 37.543405, 127.073633, 37.546472, noEtcBrand))

        // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.result.boothList").isArray())
                .andExpect(jsonPath("$.result.boothList.length()").value(4))
                .andExpect(content().string(not(containsString("인생네컷"))))
                .andExpect(content().string(not(containsString("포토시그니처"))))
                .andReturn();

        String jsonRes3 = "{\"success\":true," +
                "\"result\":{\"boothList\":" +
                "[" +
                "{\"boothIdx\":2048,\"brand\":\"하루필름\",\"latitude\":37.541799,\"longitude\":127.06862826}," +
                "{\"boothIdx\":2050,\"brand\":\"포토이즘\",\"latitude\":37.54130192,\"longitude\":127.06814577}," +
                "{\"boothIdx\":2054,\"brand\":\"포토이즘\",\"latitude\":37.54176854,\"longitude\":127.06673745}," +
                "{\"boothIdx\":3224,\"brand\":\"셀픽스\",\"latitude\":37.54242257,\"longitude\":127.07002401}]" +
                "}}";
        Assertions.assertEquals(result3.getResponse().getContentAsString(), jsonRes3);

        /* 예상 결과 (test1)
        {
            "success": true,
            "result": {
                "boothList": [
                    {
                        "boothIdx": 2048,
                        "brand": "하루필름",
                        "latitude": 37.541799,
                        "longitude": 127.06862826
                    },
                    {
                        "boothIdx": 2050,
                        "brand": "포토이즘",
                        "latitude": 37.54130192,
                        "longitude": 127.06814577
                    },
                    {
                        "boothIdx": 2051,
                        "brand": "인생네컷",
                        "latitude": 37.54094932,
                        "longitude": 127.07022291
                    },
                    {
                        "boothIdx": 2054,
                        "brand": "포토이즘",
                        "latitude": 37.54176854,
                        "longitude": 127.06673745
                    },
                    {
                        "boothIdx": 2057,
                        "brand": "포토시그니처",
                        "latitude": 37.54108222,
                        "longitude": 127.06944795
                    },
                    {
                        "boothIdx": 2058,
                        "brand": "인생네컷",
                        "latitude": 37.54092679,
                        "longitude": 127.07022176
                    },
                    {
                        "boothIdx": 2069,
                        "brand": "인싸포토",
                        "latitude": 37.54243248,
                        "longitude": 127.07153123
                    },
                    {
                        "boothIdx": 2071,
                        "brand": "포토랩플러스",
                        "latitude": 37.54210979,
                        "longitude": 127.06872246
                    },
                    {
                        "boothIdx": 2823,
                        "brand": "플레이인더박스",
                        "latitude": 37.54120584,
                        "longitude": 127.06757879
                    },
                    {
                        "boothIdx": 3224,
                        "brand": "셀픽스",
                        "latitude": 37.54242257,
                        "longitude": 127.07002401
                    },
                    {
                        "boothIdx": 21096,
                        "brand": "모노맨션",
                        "latitude": 37.54144301,
                        "longitude": 127.06878521
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
        mvc.perform(get(mapUrl,
                        0, 0, 20, 20, allBrand))

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
    @DisplayName("[GET-OK] booth pin (no filter)")
    public void booth_pin_성공_빈필터() throws Exception {

        // given

        // when
        mvc.perform(get(mapUrl,
                        126.5709308145358, 33.452739313807456, 126.5809308145358, 33.552739313807456, ""))

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

    @Test
    @DisplayName("[GET-fail] booth pin - wrong brand name")
    public void booth_map_실패_브랜드명() throws Exception {

        // given

        // when
        mvc.perform(get(mapUrl,
                        126.5709308145358, 33.452739313807456, 126.5809308145358, 33.552739313807456, wrongBrand))

        // then
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(status().is(nameError.getCode()))
                .andExpect(jsonPath("$.success").value(nameError.isSuccess()))
                .andExpect(jsonPath("$.message").value(nameError.getMessage()));

        /* 예상 결과
        {
            "success": false,
            "message": "wrong latitude/longitude range"
        }
        */

    }

    /* /map test end --------------------------------------------------------------------------------------------------------------------------------------- */

    /* /map/list test start -------------------------------------------------------------------------------------------------------------------------------- */
    private String mapListUrl = "/map/list?curx={v1}&cury={v2}&count={v3}&filter={v4}";

    @Test
    @DisplayName("[GET-OK] booth list")
    public void booth_list_성공() throws Exception {
        // given

        // when
        MvcResult result = mvc.perform(get(mapListUrl,
                        126.5709308145358, 33.452739313807456, 0, allBrand))

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
    @DisplayName("[GET-OK] booth list (filter)")
    public void booth_list_성공_필터() throws Exception {

        // given

        /**
         * test 1: all
         */
        // when
        MvcResult result = mvc.perform(get(mapListUrl,
                        127.068308, 37.543405, 0, allBrand))

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
                "{\"boothIdx\":2071,\"brand\":\"포토랩플러스\",\"name\":\"포토랩플러스\",\"address\":\"서울 광진구 화양동 9-43\",\"distance\":149,\"score\":null,\"reviewNum\":0,\"latitude\":37.54210979,\"longitude\":127.06872246}," +
                "{\"boothIdx\":2048,\"brand\":\"하루필름\",\"name\":\"하루필름 건대점\",\"address\":\"서울 광진구 화양동 9-25 1층\",\"distance\":181,\"score\":null,\"reviewNum\":0,\"latitude\":37.541799,\"longitude\":127.06862826}," +
                "{\"boothIdx\":3224,\"brand\":\"셀픽스\",\"name\":\"셀픽스 건대점\",\"address\":\"서울 광진구 화양동 8-57 1층\",\"distance\":187,\"score\":null,\"reviewNum\":0,\"latitude\":37.54242257,\"longitude\":127.07002401}," +
                "{\"boothIdx\":21096,\"brand\":\"모노맨션\",\"name\":\"모노맨션 건대점\",\"address\":\"서울 광진구 화양동 9-91 1층 101, 104호\",\"distance\":222,\"score\":null,\"reviewNum\":0,\"latitude\":37.54144301,\"longitude\":127.06878521}," +
                "{\"boothIdx\":2054,\"brand\":\"포토이즘\",\"name\":\"포토이즘컬러드 건대점\",\"address\":\"서울 광진구 화양동 48-23 1층\",\"distance\":229,\"score\":null,\"reviewNum\":0,\"latitude\":37.54176854,\"longitude\":127.06673745}," +
                "{\"boothIdx\":2050,\"brand\":\"포토이즘\",\"name\":\"포토이즘박스 건대점\",\"address\":\"서울 광진구 화양동 48-25 1층\",\"distance\":234,\"score\":null,\"reviewNum\":0,\"latitude\":37.54130192,\"longitude\":127.06814577}," +
                "{\"boothIdx\":2823,\"brand\":\"플레이인더박스\",\"name\":\"플레이인더박스 건대점\",\"address\":\"서울 광진구 화양동 48-17 1층\",\"distance\":253,\"score\":null,\"reviewNum\":0,\"latitude\":37.54120584,\"longitude\":127.06757879}," +
                "{\"boothIdx\":2057,\"brand\":\"포토시그니처\",\"name\":\"포토시그니처 서울건대점\",\"address\":\"서울 광진구 화양동 8-100\",\"distance\":277,\"score\":null,\"reviewNum\":0,\"latitude\":37.54108222,\"longitude\":127.06944795}," +
                "{\"boothIdx\":2069,\"brand\":\"인싸포토\",\"name\":\"인싸포토 건대점\",\"address\":\"서울 광진구 화양동 3-41\",\"distance\":304,\"score\":null,\"reviewNum\":0,\"latitude\":37.54243248,\"longitude\":127.07153123}," +
                "{\"boothIdx\":2051,\"brand\":\"인생네컷\",\"name\":\"인생네컷 건대점\",\"address\":\"서울 광진구 화양동 5-72\",\"distance\":321,\"score\":null,\"reviewNum\":0,\"latitude\":37.54094932,\"longitude\":127.07022291}" +
                "]" +
                "}}";
        Assertions.assertEquals(result.getResponse().getContentAsString(), jsonRes);

        /**
         * test 2: 인생네컷,포토시그니처,기타
         */
        // when
        MvcResult result2 = mvc.perform(get(mapListUrl,
                        127.068308, 37.543405, 0, etcBrand))

        // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.result.boothList").isArray())
                .andExpect(jsonPath("$.result.boothList.length()").value(10))
                .andExpect(content().string(not(containsString("포토이즘"))))
                .andExpect(content().string(not(containsString("하루필름"))))
                .andExpect(content().string(not(containsString("셀픽스"))))
                .andReturn();

        String jsonRes2 = "{\"success\":true," +
                "\"result\":{\"boothList\":" +
                "[" +
                "{\"boothIdx\":2071,\"brand\":\"포토랩플러스\",\"name\":\"포토랩플러스\",\"address\":\"서울 광진구 화양동 9-43\",\"distance\":149,\"score\":null,\"reviewNum\":0,\"latitude\":37.54210979,\"longitude\":127.06872246}," +
                "{\"boothIdx\":21096,\"brand\":\"모노맨션\",\"name\":\"모노맨션 건대점\",\"address\":\"서울 광진구 화양동 9-91 1층 101, 104호\",\"distance\":222,\"score\":null,\"reviewNum\":0,\"latitude\":37.54144301,\"longitude\":127.06878521}," +
                "{\"boothIdx\":2823,\"brand\":\"플레이인더박스\",\"name\":\"플레이인더박스 건대점\",\"address\":\"서울 광진구 화양동 48-17 1층\",\"distance\":253,\"score\":null,\"reviewNum\":0,\"latitude\":37.54120584,\"longitude\":127.06757879}," +
                "{\"boothIdx\":2057,\"brand\":\"포토시그니처\",\"name\":\"포토시그니처 서울건대점\",\"address\":\"서울 광진구 화양동 8-100\",\"distance\":277,\"score\":null,\"reviewNum\":0,\"latitude\":37.54108222,\"longitude\":127.06944795}," +
                "{\"boothIdx\":2069,\"brand\":\"인싸포토\",\"name\":\"인싸포토 건대점\",\"address\":\"서울 광진구 화양동 3-41\",\"distance\":304,\"score\":null,\"reviewNum\":0,\"latitude\":37.54243248,\"longitude\":127.07153123}," +
                "{\"boothIdx\":2051,\"brand\":\"인생네컷\",\"name\":\"인생네컷 건대점\",\"address\":\"서울 광진구 화양동 5-72\",\"distance\":321,\"score\":null,\"reviewNum\":0,\"latitude\":37.54094932,\"longitude\":127.07022291}," +
                "{\"boothIdx\":2058,\"brand\":\"인생네컷\",\"name\":\"인생네컷 서울건대점\",\"address\":\"서울 광진구 화양동 5-72 1층\",\"distance\":323,\"score\":null,\"reviewNum\":0,\"latitude\":37.54092679,\"longitude\":127.07022176}," +
                "{\"boothIdx\":2063,\"brand\":\"시현하다프레임\",\"name\":\"시현하다프레임 그린라임점\",\"address\":\"서울 광진구 자양동 7-8 1층\",\"distance\":386,\"score\":null,\"reviewNum\":0,\"latitude\":37.53995636,\"longitude\":127.06878384}," +
                "{\"boothIdx\":2049,\"brand\":\"인생네컷\",\"name\":\"인생네컷 서울건대CGV로드점\",\"address\":\"서울 광진구 자양동 9-4 1층\",\"distance\":424,\"score\":null,\"reviewNum\":0,\"latitude\":37.53976103,\"longitude\":127.06688954}," +
                "{\"boothIdx\":2081,\"brand\":\"1퍼센트\",\"name\":\"1퍼센트\",\"address\":\"서울 광진구 자양동 1-1\",\"distance\":441,\"score\":null,\"reviewNum\":0,\"latitude\":37.53973028,\"longitude\":127.07020027}]}}";
        Assertions.assertEquals(result2.getResponse().getContentAsString(), jsonRes2);

        /**
         * test 3: 포토이즘,하루필름,셀픽스
         */
        // when
        MvcResult result3 = mvc.perform(get(mapListUrl,
                        127.068308, 37.543405, 0, noEtcBrand))

        // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.result.boothList").isArray())
                .andExpect(jsonPath("$.result.boothList.length()").value(10))
                .andExpect(content().string(not(containsString("인생네컷"))))
                .andExpect(content().string(not(containsString("포토시그니처"))))
                .andReturn();

        String jsonRes3 = "{\"success\":true," +
                "\"result\":{\"boothList\":" +
                "[" +
                "{\"boothIdx\":2048,\"brand\":\"하루필름\",\"name\":\"하루필름 건대점\",\"address\":\"서울 광진구 화양동 9-25 1층\",\"distance\":181,\"score\":null,\"reviewNum\":0,\"latitude\":37.541799,\"longitude\":127.06862826}," +
                "{\"boothIdx\":3224,\"brand\":\"셀픽스\",\"name\":\"셀픽스 건대점\",\"address\":\"서울 광진구 화양동 8-57 1층\",\"distance\":187,\"score\":null,\"reviewNum\":0,\"latitude\":37.54242257,\"longitude\":127.07002401}," +
                "{\"boothIdx\":2054,\"brand\":\"포토이즘\",\"name\":\"포토이즘컬러드 건대점\",\"address\":\"서울 광진구 화양동 48-23 1층\",\"distance\":229,\"score\":null,\"reviewNum\":0,\"latitude\":37.54176854,\"longitude\":127.06673745}," +
                "{\"boothIdx\":2050,\"brand\":\"포토이즘\",\"name\":\"포토이즘박스 건대점\",\"address\":\"서울 광진구 화양동 48-25 1층\",\"distance\":234,\"score\":null,\"reviewNum\":0,\"latitude\":37.54130192,\"longitude\":127.06814577}," +
                "{\"boothIdx\":2059,\"brand\":\"포토이즘\",\"name\":\"포토이즘박스 세종대점\",\"address\":\"서울 광진구 화양동 18-26 1층\",\"distance\":493,\"score\":null,\"reviewNum\":0,\"latitude\":37.54756896,\"longitude\":127.07021667}," +
                "{\"boothIdx\":2848,\"brand\":\"포토이즘\",\"name\":\"포토이즘박스 성수카페거리점\",\"address\":\"서울 성동구 성수동2가 316-44 1층\",\"distance\":1293,\"score\":null,\"reviewNum\":0,\"latitude\":37.54308167,\"longitude\":127.05364662}," +
                "{\"boothIdx\":2044,\"brand\":\"하루필름\",\"name\":\"하루필름 성수점\",\"address\":\"서울 성동구 성수동2가 302-36 1층\",\"distance\":1486,\"score\":null,\"reviewNum\":0,\"latitude\":37.54373587,\"longitude\":127.05145414}," +
                "{\"boothIdx\":2046,\"brand\":\"하루필름\",\"name\":\"하루필름 서울숲점\",\"address\":\"서울 성동구 성수동1가 668-49 1층\",\"distance\":2204,\"score\":null,\"reviewNum\":0,\"latitude\":37.54644653,\"longitude\":127.04360045}," +
                "{\"boothIdx\":2056,\"brand\":\"포토이즘\",\"name\":\"포토이즘박스 성수점\",\"address\":\"서울 성동구 성수동1가 685-423 지하 1층\",\"distance\":2467,\"score\":null,\"reviewNum\":0,\"latitude\":37.54717736,\"longitude\":127.04073791}," +
                "{\"boothIdx\":2072,\"brand\":\"하루필름\",\"name\":\"하루필름 왕십리점\",\"address\":\"서울 성동구 행당동 3-7 1층\",\"distance\":3166,\"score\":null,\"reviewNum\":0,\"latitude\":37.56071067,\"longitude\":127.03978323}" +
                "]" +
                "}}";
        Assertions.assertEquals(result3.getResponse().getContentAsString(), jsonRes3);

        /* 예상 결과 (test1)
        {
            "success": true,
            "result": {
                "boothList": [
                    {
                        "boothIdx": 2071,
                        "brand": "포토랩플러스",
                        "name": "포토랩플러스",
                        "address": "서울 광진구 화양동 9-43",
                        "distance": 149,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 37.54210979,
                        "longitude": 127.06872246
                    },
                    {
                        "boothIdx": 2048,
                        "brand": "하루필름",
                        "name": "하루필름 건대점",
                        "address": "서울 광진구 화양동 9-25 1층",
                        "distance": 181,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 37.541799,
                        "longitude": 127.06862826
                    },
                    {
                        "boothIdx": 3224,
                        "brand": "셀픽스",
                        "name": "셀픽스 건대점",
                        "address": "서울 광진구 화양동 8-57 1층",
                        "distance": 187,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 37.54242257,
                        "longitude": 127.07002401
                    },
                    {
                        "boothIdx": 21096,
                        "brand": "모노맨션",
                        "name": "모노맨션 건대점",
                        "address": "서울 광진구 화양동 9-91 1층 101, 104호",
                        "distance": 222,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 37.54144301,
                        "longitude": 127.06878521
                    },
                    {
                        "boothIdx": 2054,
                        "brand": "포토이즘",
                        "name": "포토이즘컬러드 건대점",
                        "address": "서울 광진구 화양동 48-23 1층",
                        "distance": 229,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 37.54176854,
                        "longitude": 127.06673745
                    },
                    {
                        "boothIdx": 2050,
                        "brand": "포토이즘",
                        "name": "포토이즘박스 건대점",
                        "address": "서울 광진구 화양동 48-25 1층",
                        "distance": 234,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 37.54130192,
                        "longitude": 127.06814577
                    },
                    {
                        "boothIdx": 2823,
                        "brand": "플레이인더박스",
                        "name": "플레이인더박스 건대점",
                        "address": "서울 광진구 화양동 48-17 1층",
                        "distance": 253,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 37.54120584,
                        "longitude": 127.06757879
                    },
                    {
                        "boothIdx": 2057,
                        "brand": "포토시그니처",
                        "name": "포토시그니처 서울건대점",
                        "address": "서울 광진구 화양동 8-100",
                        "distance": 277,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 37.54108222,
                        "longitude": 127.06944795
                    },
                    {
                        "boothIdx": 2069,
                        "brand": "인싸포토",
                        "name": "인싸포토 건대점",
                        "address": "서울 광진구 화양동 3-41",
                        "distance": 304,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 37.54243248,
                        "longitude": 127.07153123
                    },
                    {
                        "boothIdx": 2051,
                        "brand": "인생네컷",
                        "name": "인생네컷 건대점",
                        "address": "서울 광진구 화양동 5-72",
                        "distance": 321,
                        "score": null,
                        "reviewNum": 0,
                        "latitude": 37.54094932,
                        "longitude": 127.07022291
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
        mvc.perform(get(mapListUrl,
                        126.5709308145358, 33.452739313807456, 1750, allBrand))

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

    @Test
    @DisplayName("[GET-OK] booth list (no filter)")
    public void booth_list_성공_빈필터() throws Exception {

        // given

        // when
        mvc.perform(get(mapListUrl,
                        126.5709308145358, 33.452739313807456, 0, ""))

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

    @Test
    @DisplayName("[GET-fail] booth list - wrong lat/lng range")
    public void booth_list_실패_좌표범위() throws Exception {
        // given

        /**
         * test 1: wrong latitude
         * proper range : -90.0 ~ 90.0
         */
        // when
        mvc.perform(get(mapListUrl,
                        126, 133, 1, allBrand))

        // then
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(status().is(rangeError.getCode()))
                .andExpect(jsonPath("$.success").value(rangeError.isSuccess()))
                .andExpect(jsonPath("$.message").value(rangeError.getMessage()));

        /**
         * test 2: wrong longitude
         * proper range : -180.0 ~ 180.0
         */
        // when
        mvc.perform(get(mapListUrl,
                        1126, 33, 1, allBrand))

        // then
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(status().is(rangeError.getCode()))
                .andExpect(jsonPath("$.success").value(rangeError.isSuccess()))
                .andExpect(jsonPath("$.message").value(rangeError.getMessage()));

        /* 예상 결과
        {
            "success": false,
            "message": "wrong latitude/longitude range"
        }
        */

    }

    @Test
    @DisplayName("[GET-fail] booth list - wrong brand name")
    public void booth_list_실패_브랜드명() throws Exception {
        // given

        // when
        mvc.perform(get(mapListUrl,
                        126, 33, 1, wrongBrand))

        // then
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(status().is(nameError.getCode()))
                .andExpect(jsonPath("$.success").value(nameError.isSuccess()))
                .andExpect(jsonPath("$.message").value(nameError.getMessage()));

        /* 예상 결과
        {
            "success": false,
            "message": "wrong latitude/longitude range"
        }
        */

    }

    /* /map/list test end ---------------------------------------------------------------------------------------------------------------------------------- */

    /* /map/search test start ------------------------------------------------------------------------------------------------------------------------------ */
    private String mapSearchUrl = "/map/search?keyword={v1}&filter={v2}";

    @Test
    @DisplayName("[GET-OK] booth search")
    public void booth_search_성공() throws Exception {

        // given

        // when
        MvcResult result = mvc.perform(get(mapSearchUrl,
                        "강남역", allBrand))

        // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.result.boothList").isArray())
                .andExpect(jsonPath("$.result.boothList.length()").value(5))
                .andReturn();

        String jsonRes = "{\"success\":true," +
                "\"result\":{\"boothList\":" +
                "[" +
                "{\"boothIdx\":2087,\"brand\":\"인생네컷\",\"latitude\":37.5029765,\"longitude\":127.02623783}," +
                "{\"boothIdx\":2088,\"brand\":\"하루필름\",\"latitude\":37.50308788,\"longitude\":127.02774881}," +
                "{\"boothIdx\":2095,\"brand\":\"포토시그니처\",\"latitude\":37.50242142,\"longitude\":127.02652263}," +
                "{\"boothIdx\":2129,\"brand\":\"모노맨션\",\"latitude\":37.50182937,\"longitude\":127.02690468}," +
                "{\"boothIdx\":5996,\"brand\":\"그믐달셀프스튜디오\",\"latitude\":37.50253651,\"longitude\":127.02756087}" +
                "]" +
                "}}";
        Assertions.assertEquals(result.getResponse().getContentAsString(), jsonRes);

        /* 예상 결과
        {
            "success": true,
            "result": {
                "boothList": [
                    {
                        "boothIdx": 2087,
                        "brand": "인생네컷",
                        "latitude": 37.5029765,
                        "longitude": 127.02623783
                    },
                    {
                        "boothIdx": 2088,
                        "brand": "하루필름",
                        "latitude": 37.50308788,
                        "longitude": 127.02774881
                    },
                    {
                        "boothIdx": 2095,
                        "brand": "포토시그니처",
                        "latitude": 37.50242142,
                        "longitude": 127.02652263
                    },
                    {
                        "boothIdx": 2129,
                        "brand": "모노맨션",
                        "latitude": 37.50182937,
                        "longitude": 127.02690468
                    },
                    {
                        "boothIdx": 5996,
                        "brand": "그믐달셀프스튜디오",
                        "latitude": 37.50253651,
                        "longitude": 127.02756087
                    }
                ]
            }
        }
        */

    }

    @Test
    @DisplayName("[GET-OK] booth search (empty)")
    public void booth_search_성공_빈리스트() throws Exception {
        // given

        // when
        mvc.perform(get(mapSearchUrl,
                        "이런이름가진건없을껄", allBrand))

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

    @Test
    @DisplayName("[GET-fail] booth search - empty keyword")
    public void booth_search_실패_빈검색어() throws Exception {
        // given

        // when
        mvc.perform(get(mapSearchUrl,
                        " ", allBrand))

        // then
                .andExpect(content().contentType("application/json;charset=utf8"))
                .andExpect(status().is(keywordError.getCode()))
                .andExpect(jsonPath("$.success").value(keywordError.isSuccess()))
                .andExpect(jsonPath("$.message").value(keywordError.getMessage()));

        /* 예상 결과
        {
            "success": false,
            "message": "empty keyword"
        }
        */

    }

    /* /map/search test end -------------------------------------------------------------------------------------------------------------------------------- */

}
