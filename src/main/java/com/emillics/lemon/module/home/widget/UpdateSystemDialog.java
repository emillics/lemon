package com.emillics.lemon.module.home.widget;

import com.emillics.lemon.base.config.Constants;
import com.emillics.lemon.base.controller.Action;
import com.emillics.lemon.base.widget.AlertDialog;
import com.emillics.lemon.base.widget.BaseTask;
import com.emillics.lemon.model.Result;
import com.emillics.lemon.model.Version;
import com.emillics.lemon.utils.FileUtils;
import com.emillics.lemon.utils.ZipUtils;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class UpdateSystemDialog extends BaseTask {
    private Dialog<Boolean> dialog;
    private Stage stage;
    private Button btnUpdate;
    private ProgressBar pbUpdate;
    private Version latestVersion;
    private String updateFile;

    public UpdateSystemDialog(Stage stage, @NonNull Version latestVersion)
            throws IOException {
        super();
        this.stage = stage;
        this.latestVersion = latestVersion;
        updateFile = Constants.UPDATE_DIRECTORY + File.separator + latestVersion.getVersion() + ".zip";
        init();
    }

    private void init() throws IOException {
        dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initOwner(stage);

        dialog.setTitle(Constants.APP_NAME);
        dialog.setHeaderText("发现软件的新版本：");
        ButtonType submitButtonType = new ButtonType("开始升级", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType);
        btnUpdate = (Button) dialog.getDialogPane().lookupButton(submitButtonType);

        Parent content = new FXMLLoader().load(Objects.requireNonNull(getClass().getResource("/fxml/update_system_dialog.fxml")));
        dialog.getDialogPane().setContent(content);

        ((Text) content.lookup("#tvCurrentVersion")).setText(String.valueOf(Constants.APP_VERSION));
        ((Text) content.lookup("#tvLatestVersion")).setText(String.valueOf(latestVersion.getVersion()));
        ProgressBar pbDownload = (ProgressBar) content.lookup("#pbDownload");
        btnUpdate.addEventFilter(ActionEvent.ACTION, event -> {
            event.consume();
            pbDownload.setVisible(true);
            doTask(new Action<Boolean>() {
                @Override
                public Result<Boolean> run() {
                    System.out.println(latestVersion.getUrl());
                    btnUpdate.setDisable(true);

                    File updateDir = new File(Constants.UPDATE_DIRECTORY);
                    if (!updateDir.exists()) {
                        updateDir.mkdirs();
                    }
                    if (updateDir.exists()) {
                        System.out.println(updateFile);

                        return FileUtils.downloadFile(latestVersion.getUrl(), updateFile, (url, localFile, total, downloaded) -> {
                            Platform.runLater(() -> {
                                pbDownload.setProgress(1.0d * downloaded / total);
                            });
                        });
                    } else {
                        return new Result<>(null, "无法访问升级文件所在目录");
                    }
                }

                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onSuccess(Boolean data) {
                    try {
                        ZipUtils.unzip(updateFile, System.getProperty("user.dir"));
                        AlertDialog.info(Constants.APP_NAME, "升级成功", "软件升级完成，即将重新启动程序");
                        dialog.setResultConverter(param -> true);
                        dialog.close();
                        Constants.EXIT_FOR_RESTART = true;
                        Platform.exit();
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertDialog.error("智汇矫", "升级失败", e.getMessage());
                        dialog.setResultConverter(null);
                        dialog.close();
                    }
                }

                @Override
                public void onError(String error) {
                    AlertDialog.error("智汇矫", "升级失败", error);
                    dialog.setResultConverter(null);
                    dialog.close();
                }
            }, false);
        });
    }

    public void show() {
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(Event::consume);
        dialog.getDialogPane().getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                event.consume();
            }
        });
        dialog.showAndWait();
    }

}
