package com.chriniko.classifieds.service.service;

import java.util.function.Function;

public interface MetricsProvider {

    <T, R> R measure(Function<T, R> f, T t, String methodName);

}
