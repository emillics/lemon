package com.emillics.lemon.module.spider.widget;

import com.emillics.lemon.base.config.Constants;
import com.emillics.lemon.base.listener.SpiderEventListener;
import com.emillics.lemon.model.JavaObjectForJs;
import com.emillics.lemon.model.Video;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.callback.InjectJsCallback;
import com.teamdev.jxbrowser.browser.event.ConsoleMessageReceived;
import com.teamdev.jxbrowser.dom.Element;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.ProprietaryFeature;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.js.JsObject;
import com.teamdev.jxbrowser.navigation.event.FrameLoadFinished;
import com.teamdev.jxbrowser.net.UrlRequest;
import com.teamdev.jxbrowser.net.UrlRequestStatus;
import com.teamdev.jxbrowser.net.event.RequestCompleted;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.teasoft.honey.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DYWebView extends StackPane {
    private Browser browser;
    private Engine engine;
    private String url;
    private boolean running;
    private Element targetElement;
    private String jsScript;
    private List<SpiderEventListener> spiderEventListenerList;
    private Timeline taskTimeline;
    private List<String> urlRequestList;

    public DYWebView(@NamedArg("url") String url) {
        this.url = url;
        init();
    }

    public DYWebView(@NamedArg("url") String url, Node... children) {
        super(children);
        this.url = url;
        init();
    }

    private void init() {
        jsScript = "var MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;\n" +
                "        const container = document.querySelector('.kkbhJDRa.MvotIm_G');\n" +
                "        const options = {\n" +
                "            childList: true,\n" +
                "        };\n" +
                "        // 创建MutationObserver实例，返回一个观察者对象\n" +
                "        const mutation = new MutationObserver(function(mutationRecords, observer) {\n" +
                "            console.log('======js======');\n" +
                "            console.log(mutationRecords);\n" +
                "        });\n" +
                "        // 对观察者添加需要观察的元素，并设置需要观察元素的哪些方面\n" +
                "        mutation.observe(container, options);";

    }

    public void start() {
        engine = Engine.newInstance(EngineOptions.newBuilder(RenderingMode.OFF_SCREEN)
                .enableProprietaryFeature(ProprietaryFeature.AAC)
                .enableProprietaryFeature(ProprietaryFeature.H_264)
                .build());
        browser = engine.newBrowser();
        browser.navigation().on(FrameLoadFinished.class, frameLoadFinished -> {
            if (frameLoadFinished.frame().equals(browser.mainFrame().orElse(null))) {
                browser.mainFrame().flatMap(Frame::document)
                        .flatMap(document -> document.findElementByCssSelector(".kkbhJDRa.MvotIm_G"))
                        .ifPresent(element -> {
                            targetElement = element;
                            running = true;
                            runTask(5);
//                            browser.mainFrame().get().executeJavaScript(jsScript);
                        });
            }
        });
        browser.set(InjectJsCallback.class, params -> {
            JsObject window = params.frame().executeJavaScript("window");
            Objects.requireNonNull(window).putProperty("java", new JavaObjectForJs());
            return InjectJsCallback.Response.proceed();
        });
        browser.on(ConsoleMessageReceived.class, event -> {
            if (spiderEventListenerList != null) {
                for (SpiderEventListener eventListener : spiderEventListenerList) {
                    if (eventListener != null) {
                        Platform.runLater(() -> eventListener.onWebLog(event.consoleMessage().toString()));
                    }
                }
            }
        });
        browser.profile().network().on(RequestCompleted.class, requestCompleted -> {
            if (requestCompleted.status() == UrlRequestStatus.URL_REQUEST_STATUS_SUCCESS &&
                    requestCompleted.responseCode() == 200) {
                UrlRequest request = requestCompleted.urlRequest();
                if (request.url().contains("vid=")) {
                    if (urlRequestList == null) urlRequestList = new ArrayList<>();
                    urlRequestList.add(request.url());
                }
            }
        });
//        browser.userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1");
        browser.audio().mute();

        if (StringUtils.isNotBlank(url)) browser.navigation().loadUrl(url);
        getChildren().add(BrowserView.newInstance(browser));
        setVisible(true);
    }

    private void runTask(Integer delay) {
        taskTimeline = new Timeline(new KeyFrame(Duration.seconds(delay != null ? delay : 2), event -> {
            int total = 0;
            com.teamdev.jxbrowser.dom.Node playingElement = null;
            for (int i = 0; i < targetElement.children().size(); i++) {
                com.teamdev.jxbrowser.dom.Node element = targetElement.children().get(i);
                if (element.children().size() > 0) {
                    total++;
                    if (total == 1) playingElement = element;
                }
                if (total == 3) {
                    playingElement = targetElement.children().get(i - 1);
                    break;
                }
            }
            if (playingElement != null) {
                // 视频id是否有效
                Element feedVideo = (Element) playingElement.children().get(0);
                if (feedVideo.attributeValue("data-e2e") == null ||
                        !feedVideo.attributeValue("data-e2e").equals("feed-active-video")) {
                    switchNext();
                    return;
                }
                Video video = new Video();
                video.setId(feedVideo.attributeValue("data-e2e-vid"));
                // 账号id
                playingElement.findElementByCssSelector(".uz1VJwFY.Xp5KTxmx").ifPresent(element -> {
                    String href = element.attributeValue("href");
                    video.setUserId(href.substring(href.lastIndexOf('/') + 1));
                });
                // 账号名称
                playingElement.findElementByCssSelector(".arnSiSbK.ypGAC_xH.ONzzdL2F").ifPresent(element -> {
                    video.setUserName(element.innerText().replace("@", ""));
                });
                // 头像
                playingElement.findElementByCssSelector(".fiWP27dC").ifPresent(element -> {
                    video.setUserAvatar("https:" + element.attributeValue("src"));
                });
                // 视频标题和话题标签
                playingElement.findElementByCssSelector(".arnSiSbK.hT34TYMB.ONzzdL2F").ifPresent(element -> {
                    String[] titleAndTags = element.innerText().split(" #");
                    String firstTag = "";
                    if (titleAndTags[0].contains("#")) {
                        String[] temp = titleAndTags[0].split("#");
                        video.setTitle(temp[0]);
                        if (temp.length > 1) firstTag = " #" + temp[1];
                    } else {
                        video.setTitle(titleAndTags[0]);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(firstTag);
                    for (int i = 1; i < titleAndTags.length; i++) {
                        sb.append(" #").append(titleAndTags[i]);
                    }
                    String tags = sb.toString();
                    video.setTags(StringUtils.isBlank(tags) ? null : tags);
                });
                // 点赞量
                playingElement.findElementByCssSelector(".KV_gO8oI.uwkzJlBF.myn2Itp_").ifPresent(element -> {
                    video.setLikeCount(parseCount(element.innerText()));
                });
                // 评论量
                playingElement.findElementByCssSelector(".X_wB9MpJ.RfKJW3Qx").ifPresent(element -> {
                    video.setCommentCount(parseCount(element.innerText()));
                });
                // 收藏量
                playingElement.findElementByCssSelector(".OjAuUiYV.GHGs1foJ").ifPresent(element -> {
                    video.setFavouriteCount(parseCount(element.innerText()));
                });
                // 转发量
                playingElement.findElementByCssSelector(".hzIYk71v").ifPresent(element -> {
                    video.setShareCount(parseCount(element.innerText()));
                });
                // 发布时间
                playingElement.findElementByCssSelector(".time").ifPresent(element -> {
                    String time = element.innerText().replace("· ", "");
                    if (time.contains("年")) {
                        try {
                            video.setPublishTime(new SimpleDateFormat("yyyy年M月d日").parse(time).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else if (time.contains("周前")) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(time.replace("周前", "")) * 7);
                        video.setPublishTime(calendar.getTime().getTime());
                    } else if (time.contains("天前")) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(time.replace("天前", "")));
                        video.setPublishTime(calendar.getTime().getTime());
                    } else if (time.contains("小时前")) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.HOUR_OF_DAY, -Integer.parseInt(time.replace("小时前", "")));
                        video.setPublishTime(calendar.getTime().getTime());
                    } else if (time.contains("月")) {
                        String date = Calendar.getInstance().get(Calendar.YEAR) + "年" + time;
                        try {
                            video.setPublishTime(new SimpleDateFormat("yyyy年M月d日").parse(date).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // 平台来源
                video.setSource(Constants.SOURCE_DOUYIN);
                // 视频类型
                video.setType(Constants.TYPE_SHORT);
                // 采集时间
                video.setCreateTime(new Date().getTime());
                // 视频时长
                playingElement.findElementByCssSelector(".time-duration").ifPresent(element -> {
                    String duration = element.innerText().replaceFirst(":", "H");
                    if (duration.contains(":")) {
                        duration = duration.replace(":", "M");
                    } else {
                        duration = duration.replace("H", "M");
                    }
                    video.setDuration(java.time.Duration.parse("PT" + duration + "S")
                            .get(ChronoUnit.SECONDS));
                });
                // 筛选条件
                // 时长 <= 1分10秒
                //     x 发布时间距当前7天内
                //     x 点赞量 >= 10000
                if (video.getDuration() > 90 ||
                        video.getLikeCount() < 100_000 ||
                        ChronoUnit.DAYS.between(new Date(video.getPublishTime()).toInstant(), new Date().toInstant()) > 1) {
                    switchNext();
                    return;
                }

                // 解析url
                boolean hasUrl = false;
                for (String requestUrl : urlRequestList) {
                    if (requestUrl.contains("vid=" + video.getId())) {
                        video.setUrl(requestUrl);
                        urlRequestList.removeIf(s -> s.equals(requestUrl));
                        hasUrl = true;
                        break;
                    }
                }
                if (!hasUrl) {
                    switchNext();
                    return;
                }

                // 选中视频
                if (spiderEventListenerList != null) {
                    for (SpiderEventListener eventListener : spiderEventListenerList) {
                        if (eventListener != null) {
                            eventListener.onVideoSelected(video);
                        }
                    }
                }
                switchNext();
            }
        }));
        taskTimeline.play();
    }

    public void stop() {
        setVisible(false);
        getChildren().removeAll();
        destroy();
        running = false;
    }

    public void addEventListener(SpiderEventListener eventListener) {
        if (spiderEventListenerList == null) spiderEventListenerList = new ArrayList<>();
        if (!spiderEventListenerList.contains(eventListener)) spiderEventListenerList.add(eventListener);
    }

    public void removeEventListener(SpiderEventListener eventListener) {
        if (eventListener != null && spiderEventListenerList != null) {
            spiderEventListenerList.remove(eventListener);
        }
    }

    private long parseCount(String count) {
        if (count.contains("万")) {
            return (long) (Double.parseDouble(count.replace("万", "")) * 10000);
        } else {
            try {
                return Long.parseLong(count);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }

    private void switchNext() {
        browser.mainFrame().flatMap(Frame::document)
                .flatMap(document -> document.findElementByCssSelector(".xgplayer-playswitch-next"))
                .ifPresent(element -> {
//                    if (urlRequestList != null) urlRequestList.clear();
                    element.click();
                    runTask(2);
                });
    }

    public void destroy() {
        if (taskTimeline != null) {
            taskTimeline.stop();
            taskTimeline = null;
        }
        if (browser != null && !browser.isClosed()) {
            browser.profile().httpCache().clear();
            browser.close();
            browser = null;
        }
        if (engine != null && !engine.isClosed()) {
            engine.close();
            engine = null;
        }
    }
}
