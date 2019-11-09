package com.chriniko.classifieds.service.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@Log4j2
public class MetricsProviderImpl implements MetricsProvider {

    @Override
    public <T, R> R measure(Function<T, R> f, T t, String methodName) {
        long startTime = System.nanoTime();
        try {
            return f.apply(t);
        } finally {
            long totalTimeInNs = System.nanoTime() - startTime;
            long totalTimeInMs = TimeUnit.MILLISECONDS.convert(totalTimeInNs, TimeUnit.NANOSECONDS);
            log.debug("method: {} execution time in {} ns and in {} ms", methodName, totalTimeInNs, totalTimeInMs);
        }
    }
}
