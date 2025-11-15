package com.emillics.lemon.base.widget;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class TableViewTextAlignmentCell<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
    private String alignment;

    public TableViewTextAlignmentCell(TextAlignment textAlignment) {
        switch (textAlignment) {
            case RIGHT:
                alignment = "CENTER-RIGHT";
                break;
            case CENTER:
                alignment = "CENTER";
                break;
            default:
                alignment = "CENTER-LEFT";
        }
    }

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {
        return new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);
                if (!empty && item != null) {
                    this.setText(String.valueOf(item));
                    this.setStyle("-fx-alignment: " + alignment);
                }
            }
        };
    }
}
