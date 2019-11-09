package com.chriniko.classifieds.service.service;

import com.chriniko.classifieds.service.domain.ClassifiedWordsCounterResult;
import com.chriniko.classifieds.service.test.infra.FileSupport;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

public class ClassifiedWordsCounterTest {

    private ClassifiedWordsCounter classifiedWordsCounter;

    @Before
    public void setup() {
        classifiedWordsCounter = new ClassifiedWordsCounter(true, new MetricsProvider() {
            @Override
            public <T, R> R measure(Function<T, R> f, T t, String methodName) {
                return f.apply(t);
            }
        });
    }

    @Test
    public void process_works_as_expected_case_1() {

        // given
        String classified = FileSupport.readResource("data/ad1.txt");

        // when
        ClassifiedWordsCounterResult result = classifiedWordsCounter.process(classified);

        // then
        assertEquals(45, result.getWords());
        assertEquals("400", result.getEuroAmount().toString());

    }

    @Test
    public void process_works_as_expected_case_2() {
        // given
        String classified = FileSupport.readResource("data/ad2.txt");

        // when
        ClassifiedWordsCounterResult result = classifiedWordsCounter.process(classified);

        // then
        assertEquals(41, result.getWords());
        assertEquals("800", result.getEuroAmount().toString());

    }

    @Test
    public void process_works_as_expected_case_3() {

        // given
        String classified = FileSupport.readResource("data/ad3.txt");

        // when
        ClassifiedWordsCounterResult result = classifiedWordsCounter.process(classified);

        // then
        assertEquals(59, result.getWords());
        assertNull(result.getEuroAmount());

    }

    @Test
    public void process_works_as_expected_case_4() {

        // given
        String classified = FileSupport.readResource("data/ad4.txt");

        // when
        ClassifiedWordsCounterResult result = classifiedWordsCounter.process(classified);

        // then
        assertEquals(11, result.getWords());
        assertNull(result.getEuroAmount());

    }

    @Test
    public void process_works_as_expected_case_5() {

        // given
        String classified = FileSupport.readResource("data/ad5.txt");

        // when
        ClassifiedWordsCounterResult result = classifiedWordsCounter.process(classified);

        // then
        assertEquals(22, result.getWords());
        assertEquals("10.000", result.getEuroAmount().toString());
    }

    @Test
    public void process_works_as_expected_case_6() {

        // given
        String classified = FileSupport.readResource("data/ad6.txt");

        // when
        ClassifiedWordsCounterResult result = classifiedWordsCounter.process(classified);

        // then
        assertEquals(51, result.getWords());
        assertEquals("570.000", result.getEuroAmount().toString());
    }


    @Test
    public void strip_html_tags_from_classified() {

        // given
        String classified = "<font size=\"5\"><p>some text</p>\n<p>another text</p></font>";

        // when
        ClassifiedWordsCounterResult result = classifiedWordsCounter.processHtml(classified);

        // then
        assertEquals(4, result.getWords());
        assertNull(result.getEuroAmount());

    }
}