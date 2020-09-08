package com.example.examscanner.authentication;

import android.content.Intent;

public interface UIAuthenticationHandler<T> {
    public Intent generateAuthenticationIntent();
    public T getOnResultContent();
}
