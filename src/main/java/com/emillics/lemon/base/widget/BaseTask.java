package com.emillics.lemon.base.widget;

import com.emillics.lemon.base.controller.Action;
import com.emillics.lemon.model.Result;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;

public class BaseTask {
    protected ImageView loadingIcon;
    protected RotateTransition rotateTransition;
    protected Alert waitingDialog;

    public BaseTask() {
        loadingIcon = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/loading_dark.png"))));
        rotateTransition = new RotateTransition(Duration.seconds(1.0), loadingIcon);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.setAutoReverse(false);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
    }

    protected void showLoading() {
        rotateTransition.play();
    }

    protected void hideLoading() {
        rotateTransition.stop();
        if (waitingDialog != null && waitingDialog.isShowing()) {
            waitingDialog.close();
        }
    }

    protected <T> void doTask(Action<T> action) {
        doTask(action, true);
    }

    protected <T> void doTask(Action<T> action, boolean showLoading) {
        Single.create((SingleOnSubscribe<T>) emitter -> {
            if (showLoading) Platform.runLater(this::showLoading);
            Result<T> result = action.run();
            if (result.getData() == null) {
                emitter.onError(new Exception(result.getError()));
            } else {
                emitter.onSuccess(result.getData());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
                .subscribe(new SingleObserver<T>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Platform.runLater(() -> {
                            action.onSubscribe(d);
                        });
                    }

                    @Override
                    public void onSuccess(@NonNull T data) {
                        Platform.runLater(() -> {
                            if (showLoading) hideLoading();
                            action.onSuccess(data);
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Platform.runLater(() -> {
                            if (showLoading) hideLoading();
                            action.onError(e.getMessage());
                        });
                    }
                });
    }

}
