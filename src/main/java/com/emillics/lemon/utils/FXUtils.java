package com.emillics.lemon.utils;

import com.emillics.lemon.base.controller.BaseController;
import com.emillics.lemon.base.widget.AlertDialog;
import com.emillics.lemon.base.widget.IconButton;
import com.emillics.lemon.model.Result;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowContainer;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.reactivex.rxjava3.annotations.NonNull;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.prefs.Preferences;

public class FXUtils {

    public static void setThemeFor(@NonNull Scene scene) {
        JMetro jMetro = (new JMetro(Preferences.systemRoot()
                .getBoolean("DarkTheme", true) ? Style.DARK : Style.LIGHT));
        scene.getProperties().put("theme", jMetro);
        jMetro.setScene(scene);
    }

    public static void switchTheme(@NonNull Scene scene) {
        JMetro jMetro = (JMetro) scene.getProperties().get("theme");
        if (jMetro != null) {
            boolean isDark = Preferences.systemRoot().getBoolean("DarkTheme", true);
            Preferences.systemRoot().putBoolean("DarkTheme", !isDark);
            jMetro.setStyle(!isDark ? Style.DARK : Style.LIGHT);
        }
    }

    public static Boolean isDarkTheme() {
        return Preferences.systemRoot().getBoolean("DarkTheme", true);
    }

    public static void setToolTipTime(Tooltip tooltip, int time) {
        try {
            Class tipClass = tooltip.getClass();
            Field f = tipClass.getDeclaredField("BEHAVIOR");
            f.setAccessible(true);
            Class behavior = Class.forName("javafx.scene.control.Tooltip$TooltipBehavior");
            Constructor constructor = behavior.getDeclaredConstructor(Duration.class, Duration.class, Duration.class, boolean.class);
            constructor.setAccessible(true);
            f.set(behavior, constructor.newInstance(new Duration(300), new Duration(time), new Duration(300), false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Image getImageFromClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable cc = clipboard.getContents(null);
        if (cc == null)
            return null;
        else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                return (Image) cc.getTransferData(DataFlavor.imageFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Result<byte[]> imageToBytes(Image src) {
        byte[] data = null;
        String error = null;
        //将设置好的图片追加到BufferedImage中
        BufferedImage bufferedImage = new BufferedImage(src.getWidth(null), src.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(src, null, null);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut;
        try {
            imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bufferedImage, "png", imOut);
            data = bs.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            data = null;
            error = e.getMessage();
        }
        return new Result<>(data, error);
    }

    public static String getMD5(String source) {
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(source.getBytes());
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //将加密后的数据转换为16进制数字
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code.insert(0, "0");
        }
        return md5code.toString();
    }

    /**
     * 将字符串中的中文转化为拼音,英文字符不变
     *
     * @param inputString 患者姓名
     * @return
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        String output = "";
        if (inputString != null && inputString.length() > 0
                && !"null".equals(inputString)) {
            String substring = diffPro(inputString);
            char[] input = substring.trim().toCharArray();
            try {
                for (int i = 0; i < input.length; i++) {
                    if (Character.toString(input[i]).matches(
                            "[\\u4E00-\\u9FA5]+")) {
                        String[] temp = PinyinHelper.toHanyuPinyinStringArray(
                                input[i], format);
                        output += temp[0] + " ";
                    } else {
                        output += Character.toString(input[i]);
                    }
                    output = output;
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        } else {
            return "";
        }
        return output;
    }

    /**
     * 与pinyin.properties中多音字的姓名对比
     *
     * @param inputString 中文姓名
     * @return 百家姓的拼音读法
     */
    private static String diffPro(String inputString) {
        Properties properties = new Properties();
        try {
            InputStreamReader reader = new InputStreamReader(FXUtils.class.getResourceAsStream("/pinyin.list"), "utf-8");
            properties.load(reader);
            reader.close();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Set keyValue = properties.keySet();
        for (Iterator it = keyValue.iterator(); it.hasNext(); ) {
            String key = (String) it.next();

            if (inputString.substring(0, key.length()).equals(key)) {
                String value = inputString.substring(key.length(), inputString.length());
                return properties.getProperty(key) + " " + value;
            }
        }
        return inputString;
    }

    public static void playMedia(String title, String uri) {
        final double[] endTime = new double[1];
        Media media = new Media(uri);
        final MediaPlayer mplayer = new MediaPlayer(media);
        MediaView mView = new MediaView(mplayer);
        Label lbCurrentTime = new Label();
        lbCurrentTime.setText("00:00:00/00:00:00");
        Slider slTime = new Slider(); // 时间轴
        slTime.setPrefWidth(200);
        BorderPane pane = new BorderPane();
        pane.setStyle(Preferences.systemRoot()
                .getBoolean("DarkTheme", true) ? "-fx-background-color:black" : "-fx-background-color:white");
        pane.setPadding(new Insets(30));

        mView.fitWidthProperty().bind(pane.widthProperty());
        mView.fitHeightProperty().bind(pane.heightProperty().subtract(100));
        IconButton btnPlay = new IconButton("\uedb5");
//        javafx.scene.control.Button btnPlay = new javafx.scene.control.Button("播放");
        btnPlay.setActionHandler(event -> {
            if (btnPlay.getIcon().equals("\uedb5")) {
                btnPlay.setIcon("\uedb4");
                mplayer.play();
            } else {
                btnPlay.setIcon("\uedb5");
                mplayer.pause();
            }
        });
        mplayer.setOnEndOfMedia(() -> { // 为初始存在的奇葩
            mplayer.stop();
            btnPlay.setIcon("\uedb5");
        });

        Slider slVolume = new Slider(); // 音量
        slVolume.setPrefWidth(150);
        slVolume.setValue(50);
//        slVolume.setShowTickLabels(true);
//        slVolume.setShowTickMarks(true);

        HBox paneCtl = new HBox(15);
        paneCtl.setAlignment(Pos.CENTER);
        paneCtl.getChildren().addAll(lbCurrentTime, slTime, btnPlay, new Label("音量"), slVolume);

        pane.setCenter(mView);
        pane.setBottom(paneCtl);

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(pane, 650, 800));
        FXUtils.setThemeFor(stage.getScene());
        stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(AlertDialog.class.getResourceAsStream("/images/app.png"))));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setOnCloseRequest(event -> {
            mplayer.stop();
            mplayer.dispose();
        });
        stage.show();

        btnPlay.setIcon("\uedb4");
        mView.setMediaPlayer(mplayer);
        mplayer.setOnReady(() -> {
            endTime[0] = mplayer.getStopTime().toSeconds();

        }); // 媒体准备好时获得信息
        mplayer.setOnEndOfMedia(() -> {
            mplayer.stop();
            mplayer.seek(Duration.ZERO);
            btnPlay.setIcon("\uedb5");
        });
        mplayer.currentTimeProperty().addListener(ov -> {
            if (!slTime.isValueChanging()) {
                double currentTime = mplayer.getCurrentTime().toSeconds();
                lbCurrentTime.setText(Seconds2Str(currentTime) + "/" + Seconds2Str(endTime[0]));
                slTime.setValue(currentTime / endTime[0] * 100);
            }
        });
        slTime.valueProperty().addListener(ov -> {
            if (slTime.isValueChanging()) {
                mplayer.seek(mplayer.getTotalDuration().multiply(slTime.getValue() / 100));
            }
        });
        mplayer.volumeProperty().bind(slVolume.valueProperty().divide(100)); // 音量调节
        mplayer.play();
    }

    public static String Seconds2Str(Double seconds) {
        Integer count = seconds.intValue();
        Integer Hours = count / 3600;
        count = count % 3600;
        Integer Minutes = count / 60;
        count = count % 60;
        String str = String.format("%s%d:%s%d:%s%d",
                Hours < 10 ? "0" : "", Hours, Minutes < 10 ? "0" : "", Minutes, count < 10 ? "0" : "", count);
        return str;
    }

    public static Node startNodeFlow(ViewFlowContext viewFlowContext, Class<? extends BaseController> controllerClass) throws FlowException {
        Flow flow = new Flow(controllerClass);
        FlowHandler flowHandler = flow.createHandler(viewFlowContext);
        FlowContainer<StackPane> flowContainer = new DefaultFlowContainer();
        return flowHandler.start(flowContainer);
    }
}
