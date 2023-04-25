package com.alsvietnam.utils;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Duc_Huy
 * Date: 6/9/2022
 * Time: 1:10 AM
 */

@UtilityClass
public class Extensions {

    public boolean isBlankOrNull(String str) {
        return str == null || str.isEmpty();
    }

    public <T> boolean isNullOrEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public <T> Stream<T> toStream(Collection<T> collection) {
        return Optional.ofNullable(collection).stream().flatMap(Collection::stream);
    }

    public <T> List<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toList());
    }

    public <T> List<T> toList(Iterable<T> iterator) {
        List<T> result = new ArrayList<>();
        iterator.forEach(result::add);
        return result;
    }

    public <T> Map<String, T> toMap(Stream<T> stream, Function<? super T, ? extends String> keyMapper) {
        return stream.collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    public <T> Collection<T> merge(Collection<T> from, Collection<T> to) {
        if (from == null) {
            from = new ArrayList<>(to);
        } else {
            from.removeIf(t -> !to.contains(t));
        }
        return from;
    }

    public <T> Optional<T> toOptional(T value) {
        return Optional.ofNullable(value);
    }

    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String getType(String mime) {
        String[] splits = mime.split("/");
        return splits[0];
    }

    private Set<String> baseSortFields;

    public Set<String> getBaseSortField() {
        if (baseSortFields == null) {
            baseSortFields = new HashSet<>();
            baseSortFields.add("createdBy");
            baseSortFields.add("createdAt");
            baseSortFields.add("updatedBy");
            baseSortFields.add("updatedAt");
        }
        return baseSortFields;
    }

}
