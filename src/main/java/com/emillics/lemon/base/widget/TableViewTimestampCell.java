package com.emillics.lemon.base.widget;

import com.emillics.lemon.model.TimeFormat;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class TableViewTimestampCell<S, T> implements Callback<TableColumn<S, Long>, TableCell<S, Long>> {
    private SimpleDateFormat dateFormat, dateTimeFormat;
    private TimeFormat timeFormat;

    public TableViewTimestampCell(TimeFormat timeFormat) {
        this.timeFormat = timeFormat;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public TableCell<S, Long> call(TableColumn<S, Long> param) {
        return new TableCell<S, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);
                if (!empty && item != null) {
                    switch (timeFormat) {
                        case DATE:
                            this.setText(dateFormat.format(item));
                            break;
                        case DATETIME:
                            this.setText(dateTimeFormat.format(item));
                            break;
                        case DURATION:
                            Duration duration = Duration.of(item, ChronoUnit.SECONDS);
                            this.setText(String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutes() % 60, (duration.toMillis() / 1000) % 60));
                            break;
                    }
                }
            }
        };
    }
}
