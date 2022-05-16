package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.form.JobForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.net.URI;


@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest // carrega todas as nossas classes
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void insert()  throws Exception {
        URI uri = new URI("/service");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JobForm jobForm = new JobForm();
        jobForm.setName("Rafael");
        jobForm.setValueOfJob(new BigDecimal("25.0"));
       // jobForm.setDurationTime(LocalTime.of(10,10,10));
        String s = mapper.writeValueAsString(jobForm);
        String json = "{\"name\":\"rafael\",\"valueOfService\":\"25.0\",\"durationTime\":\"10:10:10\"}";

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(s)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201));
    }
}