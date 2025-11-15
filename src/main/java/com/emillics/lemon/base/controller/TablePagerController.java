package com.emillics.lemon.base.controller;

import com.emillics.lemon.base.widget.TableViewIDCell;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TablePagerController<T> extends BaseController {
    @FXML
    protected TableView<T> tbvData;
    protected List<T> data = new ArrayList<T>();
    protected TableViewIDCell idCell;
    protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void onInit() {
        super.onInit();

        idCell = new TableViewIDCell(0);
        tbvData.getColumns().get(0).setCellFactory(idCell);
        tbvData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            onTableItemSelected(oldValue, newValue);
        });
        tbvData.setItems(FXCollections.observableList(data));
    }

    protected void onTableItemSelected(T oldItem, T newItem) {

    }
}
