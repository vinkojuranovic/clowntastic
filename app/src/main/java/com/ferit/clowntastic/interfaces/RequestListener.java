package com.ferit.clowntastic.interfaces;

public interface RequestListener {

    void failed(String message);

    void finished(String message);
}
