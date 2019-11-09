package com.chriniko.classifieds.service.resource;

import com.chriniko.classifieds.service.domain.ClassifiedWordsCounterResult;
import com.chriniko.classifieds.service.dto.ClassifiedWordsCounterReq;
import com.chriniko.classifieds.service.service.ClassifiedWordsCounter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest

public class ClassifiedWordsCounterResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ClassifiedWordsCounter classifiedWordsCounter;


    @Test
    public void count() throws Exception {

        // given
        String text = "this is a classified";

        ClassifiedWordsCounterReq req = new ClassifiedWordsCounterReq();
        req.setText(text);

        String payload = mapper.writeValueAsString(req);

        Mockito.when(classifiedWordsCounter.process(text)).thenReturn(new ClassifiedWordsCounterResult(4, null));


        // when - then
        mockMvc
                .perform(
                        post("/classified-words/count").contentType("application/json").content(payload)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"Text\":\"" + text + "\",\"Words\":4,\"Euro\":null}"));
    }

    @Test
    public void countHtml() throws Exception {

        // given
        String text = "<html> <p>this</p> <bold>is a classified</bold>";

        ClassifiedWordsCounterReq req = new ClassifiedWordsCounterReq();
        req.setText(text);

        String payload = mapper.writeValueAsString(req);

        Mockito.when(classifiedWordsCounter.processHtml(text)).thenReturn(new ClassifiedWordsCounterResult(4, null));


        // when - then
        mockMvc
                .perform(
                        post("/classified-words/countHtml").contentType("application/json").content(payload)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"Text\":\"" + text + "\",\"Words\":4,\"Euro\":null}"));


    }
}