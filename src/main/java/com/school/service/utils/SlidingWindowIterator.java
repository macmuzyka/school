package com.school.service.utils;

import java.util.Iterator;
import java.util.List;

public class SlidingWindowIterator<T> implements Iterator<T> {
    private final List<T> list;
    private int index = 0;

    public SlidingWindowIterator(final List<T> list) {
        this.list = list;
    }

    public T peekPrevious() {
        return (index > 1) ? list.get(index - 1) : null;
    }

    public T current() {
        return (index < 0 || index <= list.size()) ? list.get(index) : null;
    }

    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    @Override
    public T next() {
        return (index < list.size()) ? list.get(index++) : null;
    }

    public T peekNext() {
        return (index < list.size()) ? list.get(index + 1) : null;
    }

    public int size() {
        return list.size();
    }
}
