package com.chriniko.classifieds.service.service;

import com.chriniko.classifieds.service.domain.ClassifiedWordsCounterResult;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
public class ClassifiedWordsCounter {

    // ---
    private final boolean debugEnabled;

    private String alternation = "|";

    private String supportedAlphaNums = "\\(\\)\"0-9α-ωΑ-ΩύάέίήώόΆΈΊΉΏΌϊΪa-zA-Z'-\\.\\/";

    private String word = "([" + supportedAlphaNums + "]{2,})";

    private String telephone = "([0-9()]{2,})";

    private String number = "([0-9]{2,})";

    private String email = "([a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+)";

    // Note: from more specific to less specific.
    private Pattern pattern = Pattern.compile(email
            + alternation + word
            + alternation + telephone
            + alternation + number
    );

    private Pattern euroAmountRegex = Pattern.compile("([0-9]+(.[0-9]+)?(€| ευρώ))");

    private final MetricsProvider metricsProvider;

    @Autowired
    public ClassifiedWordsCounter(@Value("${classified-words-counter.debug-enabled}") boolean debugEnabled, MetricsProvider metricsProvider) {
        this.debugEnabled = debugEnabled;
        this.metricsProvider = metricsProvider;
    }

    // ---

    public ClassifiedWordsCounterResult processHtml(String classifiedInsideHtml) {
        return metricsProvider.measure(c -> {

            Document doc = Jsoup.parse(classifiedInsideHtml);
            String strippedClassified = doc.text();
            log.trace("stripped classified: " + strippedClassified);
            return process(strippedClassified);

        }, classifiedInsideHtml, "processHtml");
    }

    public ClassifiedWordsCounterResult process(String classified) {
        return metricsProvider.measure(c -> {

            Map<String, Integer> matches = new LinkedHashMap<>();
            Matcher matcher = pattern.matcher(c);

            int count = 0;
            while (matcher.find()) {
                count++;
                String group = matcher.group();

                log.trace("found: " + count + " : " + matcher.start() + " - " + matcher.end() + " --- match: " + group);

                matches.compute(group, (k, v) -> {
                    if (v == null) {
                        return 1;
                    }
                    return v + 1;
                });
            }
            if (debugEnabled) {
                debug(c, matches);
            }

            int words = matches.values().stream().reduce(0, Integer::sum);
            return extractEuroAmount(c)
                    .map(euroAmount -> new ClassifiedWordsCounterResult(words, euroAmount))
                    .orElseGet(() -> new ClassifiedWordsCounterResult(words, null));

        }, classified, "process");
    }

    private void debug(String input, Map<String, Integer> matches) {

        List<String> notMatches = new LinkedList<>();

        for (String inputWord : input.split(" ")) {

            String w = inputWord.trim()
                    .replace("\uFEFF", "")
                    .replace(",", "")
                    .replace(":", "");

            if (matches.get(w) == null) {
                notMatches.add(inputWord);
            }

        }

        log.debug("input: " + input);
        log.debug("matches result: " + matches.entrySet());
        log.debug("notMatches founds: " + notMatches);
    }

    private Optional<BigDecimal> extractEuroAmount(String classified) {
        Matcher euroAmountMatcher = euroAmountRegex.matcher(classified);
        if(euroAmountMatcher.find()) {
            String euroAmount = euroAmountMatcher.group();

            String cleanedEuroAmount = euroAmount
                    .replace("€", "")
                    .replace("ευρώ", "")
                    .trim();

            return Optional.of(new BigDecimal(cleanedEuroAmount));
        }
        return Optional.empty();
    }


}
