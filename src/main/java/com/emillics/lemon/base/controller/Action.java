package com.emillics.lemon.base.controller;

import com.emillics.lemon.model.Result;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;

public interface Action<T> {
    Result<T> run();

    void onSubscribe(@NonNull Disposable d);

    void onSuccess(@NonNull T data);

    void onError(@NonNull String error);
}
