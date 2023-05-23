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

}
