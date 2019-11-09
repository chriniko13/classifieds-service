package com.chriniko.classifieds.service.it;


import com.chriniko.classifieds.service.Bootstrap;
import com.chriniko.classifieds.service.dto.ClassifiedWordsCounterReq;
import com.chriniko.classifieds.service.dto.ClassifiedWordsCounterResp;
import com.chriniko.classifieds.service.test.infra.FileSupport;
import com.chriniko.classifieds.service.test.infra.Specification;
import com.chriniko.classifieds.service.test.infra.TestInfraException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Bootstrap.class,
        properties = {"application.properties"}
)

@RunWith(SpringRunner.class)
public class ClassifiedWordsCounterSpecIT extends Specification {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void count_works_as_expected_normal_case() throws Exception {

        // given
        int clients = 150;

        ExecutorService clientsPool = Executors.newFixedThreadPool(clients);

        String requestAsString = FileSupport.readResource("data/ad1_req.json");
        ClassifiedWordsCounterReq request = objectMapper.readValue(requestAsString, ClassifiedWordsCounterReq.class);

        String url = getBaseUrl(port) + "/count";

        HttpEntity<ClassifiedWordsCounterReq> httpEntity = createHttpEntity(request);


        // when
        List<CompletableFuture<String>> clientsResponses = IntStream.rangeClosed(1, clients).boxed()
                .map(idx -> CompletableFuture
                        .supplyAsync(
                                () -> {
                                    try {
                                        ResponseEntity<ClassifiedWordsCounterResp> responseEntity
                                                = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ClassifiedWordsCounterResp.class);
                                        return objectMapper.writeValueAsString(responseEntity.getBody());
                                    } catch (JsonProcessingException e) {
                                        throw new TestInfraException(e);
                                    }
                                }, clientsPool
                        )
                )
                .collect(Collectors.toList());


        // then
        List<String> responses = clientsResponses.stream().map(cf -> {
            try {
                return cf.get(3, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new TestInfraException(e);
            }
        }).collect(Collectors.toList());

        String expectedResponse = FileSupport.readResource("data/ad1_resp.json");


        for (String response : responses) {
            JSONAssert.assertEquals(expectedResponse, response, true);
        }

        // cleanup
        clientsPool.shutdown();
    }

    @Test
    public void count_works_as_expected_validation_case() {

        // given
        ClassifiedWordsCounterReq request = new ClassifiedWordsCounterReq(null);

        String url = getBaseUrl(port) + "/count";

        HttpEntity<ClassifiedWordsCounterReq> httpEntity = createHttpEntity(request);

        // when

        try {
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, ClassifiedWordsCounterResp.class);
            fail();
        } catch (HttpClientErrorException error) { // then
            assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
            System.out.println(error.getResponseBodyAsString());
        }

    }

    @Test
    public void count_html_works_as_expected_normal_case() throws Exception {

        String text = "<font size=\"5\"><p>some text</p>\n<p>another text</p></font>";

        ClassifiedWordsCounterReq request = new ClassifiedWordsCounterReq(text);

        String url = getBaseUrl(port) + "/countHtml";

        HttpEntity<ClassifiedWordsCounterReq> httpEntity = createHttpEntity(request);

        // when
        ResponseEntity<ClassifiedWordsCounterResp> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ClassifiedWordsCounterResp.class);
        ClassifiedWordsCounterResp response = responseEntity.getBody();

        // then
        assertEquals(4, response.getWords());
        assertNull(response.getEuroAmount());
        assertEquals(text, response.getText());

    }
}
