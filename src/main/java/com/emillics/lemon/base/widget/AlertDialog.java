package com.emillics.lemon.base.widget;

import com.emillics.lemon.utils.FXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetroStyleClass;

import java.util.Objects;
import java.util.Optional;

public class AlertDialog {
    public static void info(String title, String headerText, String contentText) {
        Alert alert = getDialog(Alert.AlertType.INFORMATION, title, headerText, contentText, true);
        alert.showAndWait();
    }

    public static void error(String title, String headerText, String contentText) {
        Alert alert = getDialog(Alert.AlertType.ERROR, title, headerText, contentText, true);
        alert.showAndWait();
    }

    public static boolean warning(String title, String headerText, String contentText) {
        Alert alert = getDialog(Alert.AlertType.WARNING, title, headerText, contentText, true, ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && (result.get() == ButtonType.OK);
    }

    public static Alert waiting(String title, String headerText, String contentText, boolean cancelable) {
        Alert alert = getDialog(Alert.AlertType.INFORMATION, title, headerText, contentText, cancelable);
        alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
        alert.show();
        return alert;
    }

    public static ButtonType saving(String title, String headerText, String contentText, boolean cancelable) {
        Alert alert = getDialog(Alert.AlertType.WARNING, title, headerText, contentText, cancelable, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(ButtonType.CANCEL);
    }

    public static Alert about(String title, String headerText, String contentText, String iconName, boolean cancelable) {
        Alert alert = getDialog(iconName, title, headerText, contentText, cancelable);
        alert.show();
        return alert;
    }

    private static Alert getDialog(Alert.AlertType type, String title, String headerText, String contentText, boolean cancelable, ButtonType... buttons) {
        Alert alert = new Alert(type, contentText, buttons);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.getDialogPane().getStyleClass().add(JMetroStyleClass.BACKGROUND);
        FXUtils.setThemeFor(alert.getDialogPane().getScene());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(AlertDialog.class.getResourceAsStream("/images/app.png"))));
        stage.setOnCloseRequest(event -> {
            if (!cancelable) {
                event.consume();
            }
        });
        stage.initStyle(StageStyle.UTILITY);
        return alert;
    }

    private static Alert getDialog(String resourceName, String title, String headerText, String contentText, boolean cancelable, ButtonType... buttons) {
        Alert alert = new Alert(Alert.AlertType.NONE, contentText, buttons);
        Image icon = new Image(Objects.requireNonNull(AlertDialog.class.getResourceAsStream(resourceName)), 50, 50, true, true);
        alert.setGraphic(new ImageView(icon));
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.getDialogPane().getStyleClass().add(JMetroStyleClass.BACKGROUND);
        FXUtils.setThemeFor(alert.getDialogPane().getScene());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(event -> {
            if (!cancelable) {
                event.consume();
            }
        });
        stage.initStyle(StageStyle.UTILITY);
        return alert;
    }
}
