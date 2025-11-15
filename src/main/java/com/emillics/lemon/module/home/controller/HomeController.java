package com.emillics.lemon.module.home.controller;

import com.emillics.lemon.base.config.Constants;
import com.emillics.lemon.base.controller.BaseController;
import com.emillics.lemon.base.widget.AlertDialog;
import com.emillics.lemon.module.spider.controller.DYTaskController;
import com.emillics.lemon.utils.FXUtils;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

@ViewController("/fxml/home.fxml")
public class HomeController extends BaseController {
    @FXML
    private TabPane tabModule;
    @FXML
    private TabPane tabSpider;
    //    @FXML
//    private HBox toolBar;
//    @FXML
//    private IconButton btnTheme;
//    @FXML
//    private IconButton btnHelp;
    private List<String> dbList;

    @Override
    public void onInit() {
        super.onInit();
        try {
            loadModules();
        } catch (FlowException e) {
            e.printStackTrace();
        }
//        switchThemeBtn();
//        btnTheme.setActionHandler(event -> {
//            FXUtils.switchTheme(btnTheme.getScene());
//            switchThemeBtn();
//        });
//        ContextMenu helpMenu = new ContextMenu();
//        MenuItem updateMenuItem = new MenuItem("检查更新");
//        MenuItem aboutMenuItem = new MenuItem("关于...");
//        updateMenuItem.setOnAction((event) -> {
//            updateSystem();
//        });
//        aboutMenuItem.setOnAction((event) -> {
//            AlertDialog.about("关于", null, Constants.APP_NAME + "\n当前版本: " + Constants.APP_VERSION,
//                    "/images/app.png", true);
//        });
//        helpMenu.getItems().addAll(updateMenuItem, aboutMenuItem);
//        btnHelp.setActionHandler(event -> {
//            helpMenu.show(btnHelp, Side.BOTTOM, 0, 0);
//        });

        String dbs = Preferences.systemRoot().get("dbs" + Constants.APP_VERSION, "ds1");
        dbList = Arrays.stream(dbs.split(",")).collect(Collectors.toList());

//        showTab(Platforms.Hangzhou);
//        showTab(Platforms.Anhui);
//        showTab(Platforms.Shaoxing);
//        showTab(Platforms.Qiantang);
//        showTab(Platforms.Gongshu);
//        showTab(Platforms.Sanya);
//        showTab(Platforms.Gaochun);
////        showTab(Platforms.Hangzhou_20220804);
//        showTab(Platforms.Hangzhou_20231011);
    }

//    private void showTab(Platforms platform) throws FlowException {
//        if (dbList.contains(platform.getDS())) {
//            ViewFlowContext flowContext = new ViewFlowContext();
//            flowContext.register("platform", platform);
//            Flow flow = new Flow(LoginController.class);
//            FlowHandler flowHandler = flow.createHandler(flowContext);
//            FlowContainer<StackPane> flowContainer = new DefaultFlowContainer();
//            flowContext.register("container", flowContainer);
//            Tab tab = new Tab(platform.getName(),
//                    flowHandler.start(flowContainer));
//            tab.setClosable(false);
//            tab.setStyle("-fx-font-size:18");
//            tabPane.getTabs().add(tab);
//        }
//    }

//    private void switchThemeBtn() {
//        btnTheme.setIcon(FXUtils.isDarkTheme() ? "\uE706" : "\uE708");
//    }

    private void loadModules() throws FlowException {
        tabSpider.getTabs().get(1).setContent(FXUtils.startNodeFlow(viewFlowContext, DYTaskController.class));
    }

    @Override
    protected void showLoading() {
        super.showLoading();
        waitingDialog = AlertDialog.waiting(Constants.APP_NAME, "软件更新", "正在检查更新，请耐心等待...", false);
        waitingDialog.show();
    }

//    private void updateSystem() {
//        doTask(new Action<Version>() {
//            @Override
//            public Result<Version> run() {
//                return DBUtils.getSystemVersion();
//            }
//
//            @Override
//            public void onSuccess(@NonNull Version data) {
//                if (data.getVersion() <= Constants.APP_VERSION) {
//                    AlertDialog.info("智汇矫", "软件更新", "当前已经是最新版本");
//                } else {
//                    try {
//                        new UpdateSystemDialog((Stage) tabPane.getScene().getWindow(), data).show();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onError(@NonNull String error) {
//                System.out.println(error);
//            }
//        });
//    }
}
