package com.emillics.lemon.base.widget;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class TableViewPhotoThumbCell<S, T> implements Callback<TableColumn<S, String>, TableCell<S, String>> {
    private final int width;
    private final int height;
    private boolean clickable;

    public TableViewPhotoThumbCell(int width, int height, boolean clickable) {
        this.width = width;
        this.height = height;
        this.clickable = clickable;
    }

    public TableViewPhotoThumbCell(int width, int height) {
        this(width, height, true);
    }

    @Override
    public TableCell<S, String> call(TableColumn<S, String> param) {
        return new TableCell<S, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    HBox container = (HBox) this.getGraphic();
                    if (container == null) {
                        container = new HBox();
                        container.setAlignment(Pos.CENTER);
                        container.setMinWidth(width);
                        container.setMaxWidth(width);
                        container.setMinHeight(height);
                        container.setMaxHeight(height);
//                        container.setStyle("-fx-background-color: #808080");
                        this.setContentDisplay(ContentDisplay.CENTER);
                        this.setGraphic(container);
                    }
                    ImageView iv = (ImageView) (container.getChildren().size() > 0 ? container.getChildren().get(0) : null);
                    if (item != null && item.trim().length() > 0) {
                        Image image = new Image(item, width, height, true, true, true);
                        if (iv == null) {
                            iv = new ImageView(image);
                            container.getChildren().add(iv);
                        } else {
                            iv.setImage(image);
                        }
                        this.setText(null);
                    } else {
                        if (iv != null) iv.setImage(null);
                        this.setText("<未获取>");
                    }
                    if (clickable) {
                        HBox finalContainer = container;
                        setOnMouseEntered(event -> {
                            DropShadow ds = new DropShadow();
                            ds.setColor(Color.valueOf("#1e1e1e"));
                            ds.setRadius(20);
                            ds.setBlurType(BlurType.GAUSSIAN);
                            finalContainer.setEffect(ds);
                        });
                        setOnMouseExited(event -> {
                            finalContainer.setEffect(null);
                        });
                        setOnMousePressed(event -> {
                            finalContainer.setTranslateX(5);
                            finalContainer.setTranslateY(5);
                        });
                        setOnMouseReleased(event -> {
                            finalContainer.setTranslateX(0);
                            finalContainer.setTranslateY(0);
                        });
                        ImageView finalIv = iv;
                        setOnMouseClicked(event -> {

                        });
                    }
                } else {
                    this.setText(null);
                    this.setGraphic(null);
                    if (clickable) {
                        setOnMouseEntered(null);
                        setOnMouseExited(null);
                        setOnMousePressed(null);
                        setOnMouseReleased(null);
                        setOnMouseClicked(null);
                    }
                }
            }
        };
    }
}
