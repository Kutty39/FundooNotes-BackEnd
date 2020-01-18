package com.blbz.fundoonotebackend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@WebMvcTest
class FrontControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    /*    @Value("classpath:NagTestCasesRegiter.json")
        Resource resourceFile;*/
    @Autowired
    MockMvc mockMvc;

    @Test
    void registerNagativeTestCase() throws Exception {
        JsonNode jsonNode = objectMapper.readTree(ResourceUtils.getFile("TestCase/NagTestCasesRegiter.json"));
        jsonNode.forEach(node -> {
            MvcResult result;
            try {
                result = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(String.valueOf(node)))
                        .andReturn();
                assertNotEquals(200, result.getResponse().getStatus());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }

        });
    }

    @Test
    void registerestCase() throws Exception {
        JsonNode jsonNode = objectMapper.readTree(ResourceUtils.getFile("TestCase/PasTestCasesRegiter.json"));

        jsonNode.forEach(node -> {
            MvcResult result;
            try {
                result = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(String.valueOf(node)))
                        .andReturn();
                assertEquals(200, result.getResponse().getStatus());
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        });

    }
}