package com.emillics.lemon.base.widget;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.sql.Time;

public class TableViewTimeCell<S, T> implements Callback<TableColumn<S, Time>, TableCell<S, Time>> {

    @Override
    public TableCell<S, Time> call(TableColumn<S, Time> param) {
        return new TableCell<S, Time>() {
            @Override
            protected void updateItem(Time item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);
                if (!empty && item != null) {
                    this.setText(item.toString());
                }
            }
        };
    }
}
