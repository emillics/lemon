package com.emillics.lemon.module.spider.controller;

import com.emillics.lemon.base.config.Constants;
import com.emillics.lemon.base.controller.Action;
import com.emillics.lemon.base.controller.TablePagerController;
import com.emillics.lemon.base.listener.SpiderEventListener;
import com.emillics.lemon.base.widget.TableViewPhotoThumbCell;
import com.emillics.lemon.base.widget.TableViewTextAlignmentCell;
import com.emillics.lemon.base.widget.TableViewTimestampCell;
import com.emillics.lemon.base.widget.TableViewVideoFileCell;
import com.emillics.lemon.model.Result;
import com.emillics.lemon.model.TimeFormat;
import com.emillics.lemon.model.Video;
import com.emillics.lemon.module.spider.widget.DYWebView;
import com.emillics.lemon.utils.FileUtils;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ViewController("/fxml/spider_douyin.fxml")
public class DYTaskController extends TablePagerController<Video> implements SpiderEventListener {
    @FXML
    @ActionTrigger("enableSpider")
    private Button btnSpider;
    @FXML
    private DYWebView dyWebView;
    @FXML
    private TextArea webLog;
    private boolean working;
    private List<Disposable> taskList;

    @Override
    public void onInit() {
        super.onInit();
        dyWebView.addEventListener(this);
        tbvData.getColumns().get(3).setCellFactory(new TableViewPhotoThumbCell(32, 32, false));
        tbvData.getColumns().get(7).setCellFactory(new TableViewTimestampCell(TimeFormat.DATE));
        tbvData.getColumns().get(8).setCellFactory(new TableViewTextAlignmentCell(TextAlignment.RIGHT));
        tbvData.getColumns().get(9).setCellFactory(new TableViewTextAlignmentCell(TextAlignment.RIGHT));
        tbvData.getColumns().get(10).setCellFactory(new TableViewTextAlignmentCell(TextAlignment.RIGHT));
        tbvData.getColumns().get(11).setCellFactory(new TableViewTextAlignmentCell(TextAlignment.RIGHT));
        tbvData.getColumns().get(12).setCellFactory(new TableViewTimestampCell(TimeFormat.DATETIME));
        tbvData.getColumns().get(13).setCellFactory(new TableViewTimestampCell(TimeFormat.DURATION));
        ((TableColumn<Video, String>) tbvData.getColumns().get(6)).setCellFactory(new Callback<TableColumn<Video, String>, TableCell<Video, String>>() {
            @Override
            public TableCell<Video, String> call(TableColumn<Video, String> param) {
                return new TableCell<Video, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        if (!empty && item != null) {
                            this.setText(item.equals("normal") ? "长视频" : "短视频");
                        }
                    }
                };
            }
        });
        tbvData.getColumns().get(14).setCellFactory(new TableViewVideoFileCell() {
            @Override
            public void onRetryDownload(Video video) {
                super.onRetryDownload(video);
                videoDownloadTask(video);
            }
        });
        ((TableColumn<Video, Long>) tbvData.getColumns().get(16)).setCellFactory(new Callback<TableColumn<Video, Long>, TableCell<Video, Long>>() {
            @Override
            public TableCell<Video, Long> call(TableColumn<Video, Long> param) {
                return new TableCell<Video, Long>() {
                    @Override
                    protected void updateItem(Long item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        if (!empty && item != null) {
                            this.setText(FileUtils.parseFileSize(item));
                        }
                    }
                };
            }
        });
    }

    @Override
    public boolean onWindowClose() {
        if (dyWebView != null) {
            dyWebView.removeEventListener(this);
            dyWebView.destroy();
        }
        if (taskList != null) {
            for (Disposable d : taskList) d.dispose();
        }
        return super.onWindowClose();
    }

    @ActionMethod("enableSpider")
    public void enableSpider() {
        if (!working) {
            working = true;
            ImageView iv = new ImageView(new Image("/images/stop.png"));
            iv.setFitWidth(14);
            iv.setFitHeight(14);
            btnSpider.setGraphic(iv);
            btnSpider.setTooltip(new Tooltip("停止"));
            dyWebView.start();
        } else {
            ImageView iv = new ImageView(new Image("/images/start.png"));
            iv.setFitWidth(12);
            iv.setFitHeight(12);
            btnSpider.setGraphic(iv);
            btnSpider.setTooltip(new Tooltip("开始"));
            dyWebView.stop();
            working = false;
        }
    }


    @Override
    protected void showLoading() {
        super.showLoading();
//        loginButton.setDisable(true);
//        loginButton.setGraphic(loadingIcon);
//        loginButton.setText("登录中");
//        loadingIcon.setFitWidth(loginButton.getHeight() / 2);
//        loadingIcon.setFitHeight(loginButton.getHeight() / 2);
//        tfAccount.setDisable(true);
//        tfPasswd.setDisable(true);
    }

    @Override
    protected void hideLoading() {
        super.hideLoading();
//        loginButton.setGraphic(null);
//        loginButton.setText("登录");
//        loginButton.setDisable(false);
//        tfAccount.setDisable(false);
//        tfPasswd.setDisable(false);
    }

    @Override
    public void onWebLog(String message) {
        webLog.appendText(message);
    }

    @Override
    public void onVideoSelected(Video video) {
        for (Video v : tbvData.getItems()) {
            if (v.getId().equals(video.getId())) return;
        }
        tbvData.getItems().add(video);
        videoDownloadTask(video);
    }

    private void videoDownloadTask(Video video) {
        if (taskList == null) taskList = new ArrayList<>();
        doTask(new Action<Boolean>() {
            private String videoFile;
            private Disposable d;

            @Override
            public Result<Boolean> run() {
                File videoDir = new File(Constants.VIDEO_DIRECTORY);
                if (!videoDir.exists()) {
                    videoDir.mkdirs();
                }
                if (videoDir.exists()) {
                    videoFile = videoDir + File.separator + video.getSource() + "_" + video.getId() + ".mp4";
                    return FileUtils.downloadFile(video.getUrl(), videoFile, (url, localFile, total, downloaded) -> {
                        Platform.runLater(() -> {
                            video.setPath(String.valueOf(1.0d * downloaded / total));
                            tbvData.refresh();
                        });
                    });
                } else {
                    return new Result<>(null, "无法访问视频文件所在目录");
                }
            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                this.d = d;
                taskList.add(d);
            }

            @Override
            public void onSuccess(Boolean data) {
                taskList.remove(this.d);
                video.setPath(videoFile);
                FileUtils.getVideoMetaData(video);
                tbvData.refresh();
            }

            @Override
            public void onError(String error) {
                taskList.remove(this.d);
                video.setPath("error");
                tbvData.refresh();
                System.out.println("下载视频失败：" + error);
            }
        }, false);
    }
}
