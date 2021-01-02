package main.java.impls.utils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class StreamUtils {
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return distinctByKey(keyExtractor, seen);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor, Set<Object> seen) {
        return t -> seen.add(keyExtractor.apply(t));
    }
}
