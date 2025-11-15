package com.emillics.lemon.base.widget;

import com.emillics.lemon.model.Video;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.File;

public class TableViewVideoFileCell<S, T> implements Callback<TableColumn<Video, String>, TableCell<Video, String>> {

    @Override
    public TableCell<Video, String> call(TableColumn<Video, String> param) {
        return new TableCell<Video, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    Video video = getTableView().getItems().get(getIndex());
                    StackPane container = (StackPane) this.getGraphic();
                    if (container == null) {
                        container = new StackPane();
                        container.setAlignment(Pos.CENTER);
                        container.setMaxWidth(Double.POSITIVE_INFINITY);
                        container.setMaxHeight(Double.POSITIVE_INFINITY);
                        this.setContentDisplay(ContentDisplay.CENTER);
                        this.setGraphic(container);
                        Button button = new Button("重新下载");
                        container.getChildren().add(button);
                        ProgressBar pb = new ProgressBar(0.0);
                        pb.setMaxWidth(Double.POSITIVE_INFINITY);
                        pb.setMaxHeight(Double.POSITIVE_INFINITY);
                        container.getChildren().add(pb);
                        Label percent = new Label("0%");
                        container.getChildren().add(percent);
                    }
                    Button downloadBtn = (Button) container.getChildren().get(0);
                    ProgressBar downloadProgressBar = (ProgressBar) container.getChildren().get(1);
                    Label percent = (Label) container.getChildren().get(2);
                    if (item.equals("error")) {
                        this.setText(null);
                        downloadProgressBar.setVisible(false);
                        percent.setVisible(false);
                        downloadBtn.setVisible(true);
                        downloadBtn.setOnMouseClicked(event -> {
                            onRetryDownload(video);
                        });
                        container.setVisible(true);
                    } else if (item.contains(File.separator)) {
                        this.setText(item);
                        container.setVisible(false);
                    } else {
                        this.setText(null);
                        double progress = Double.parseDouble(item);
                        downloadProgressBar.setProgress(progress);
                        percent.setText((int) (progress * 100) + "%");
                        downloadProgressBar.setVisible(true);
                        percent.setVisible(true);
                        container.setVisible(true);
                    }
                } else {
                    this.setText(null);
                    this.setGraphic(null);
                }
            }
        };
    }

    public void onRetryDownload(Video video) {

    }
}
