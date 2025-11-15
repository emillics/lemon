package com.emillics.lemon.base.widget;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class TableViewIDCell<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
    private int page, size;

    public TableViewIDCell(int size) {
        this.size = size;
    }

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {
        TableCell<S, T> cell = new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);
                if (!empty) {
                    int rowIndex = this.getIndex() + 1 + page * size;
                    this.setText(String.valueOf(rowIndex));
                }
            }
        };
        return cell;
    }

    public void setPageAndSize(int page, int size) {
        this.page = page;
        this.size = size;
    }
}
