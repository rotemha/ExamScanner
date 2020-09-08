package com.example.examscanner.repositories;

import org.json.JSONException;

public interface Converter<V,T> {
    public T convert(V v);
}
