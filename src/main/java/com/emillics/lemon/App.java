package com.emillics.lemon;

import com.emillics.lemon.base.config.Constants;
import com.emillics.lemon.base.listener.WindowEventListener;
import com.emillics.lemon.module.home.controller.HomeController;
import com.emillics.lemon.utils.FXUtils;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetroStyleClass;
import org.teasoft.honey.util.StringUtils;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        final ViewFlowContext flowContext = new ViewFlowContext();
        flowContext.register("WindowEventListeners", new ArrayList<WindowEventListener>());
        primaryStage.setScene(new Scene((Parent) FXUtils.startNodeFlow(flowContext, HomeController.class)));
        Scene scene = primaryStage.getScene();
        scene.getRoot().getStyleClass().add(JMetroStyleClass.BACKGROUND);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/global.css")).toExternalForm());
        FXUtils.setThemeFor(scene);

//        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/app.png"))));
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(832);
        primaryStage.setOnCloseRequest(event -> {
            ArrayList<WindowEventListener> listeners =
                    (ArrayList<WindowEventListener>) flowContext.getRegisteredObject("WindowEventListeners");
            for (WindowEventListener listener : listeners) {
                if (listener != null) {
                    if (listener.onWindowClose()) {
                        event.consume();
                        break;
                    }
                }
            }
        });
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (Constants.EXIT_FOR_RESTART) {
            if (Constants.SYSTEM_WINDOWS) {//Win
                String jarDir = System.getProperty("user.dir");
                startProgram(jarDir.substring(0, jarDir.lastIndexOf('\\') + 1) + "Lemon.exe");
            } else {//MacOS
                Runtime.getRuntime().exec(new String[]{"/usr/bin/open", "-a", "Lemon.app"});
            }
        }
    }

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        System.setProperty("jxbrowser.license.key", "6P835FT5HAPTB03TPIEFPGU5ECGJN8GMGDD79MD7Y52NVP0K0IV6FHYZVQI25H0MLGI2");
        launch(args);
    }

    /**
     * 启动应用程序
     *
     * @param programPath
     * @return
     */
    public void startProgram(String programPath) {
        if (StringUtils.isNotBlank(programPath)) {
            try {
                Desktop.getDesktop().open(new File(programPath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
