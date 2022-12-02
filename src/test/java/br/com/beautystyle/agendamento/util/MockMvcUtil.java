package br.com.beautystyle.agendamento.util;

import br.com.beautystyle.agendamento.controller.form.LoginForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Service
public class MockMvcUtil {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;

    public MockMvcUtil(MockMvc mockMvc, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
    }

    public ResultActions DELETE(String uri, String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .delete(uri)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public MvcResult GET(String uri, String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    public ResultActions POST(String uri, Object jsonObject,
                              String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(mapper.writeValueAsString(jsonObject))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions PUT(String uri, Object jsonObject, String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .put(uri)
                .content(mapper.writeValueAsString(jsonObject))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions POST(String uri, LoginForm loginForm) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(mapper.writeValueAsString(loginForm))
                .contentType(MediaType.APPLICATION_JSON));
    }
}
