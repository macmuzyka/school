package com.school.service.utils;

import java.util.Optional;
import java.util.function.Function;

public class EntityFetcher {
    public static <T, ID> T getByIdOrThrow(Function<ID, Optional<T>> finder, ID id, String entity) {
        return finder.apply(id).orElseThrow(() -> new IllegalArgumentException("Could not find " + entity + " with id: " + id));
    }
}
