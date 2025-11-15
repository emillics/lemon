package com.emillics.lemon.base.config;

import java.io.File;

public class Constants {
    public static final String APP_NAME = "柠檬";
    public static final float APP_VERSION = 1.0f;
    public static final int TABLE_PAGE_SIZE = 20;
    public static final int TENCENT_TASK_REFRESH_INTERVAL = 30000;
    public static final String UPDATE_DIRECTORY = System.getProperty("user.dir") + File.separator + "update";
    public static final String VIDEO_DIRECTORY = System.getProperty("user.dir") + File.separator + "video";
    public static boolean EXIT_FOR_RESTART = false;
    public static Boolean SYSTEM_WINDOWS = null;

    public static String SOURCE_DOUYIN = "douyin";
    public static String SOURCE_BILIBILI = "bilibili";

    public static String TYPE_NORMAL= "normal";
    public static String TYPE_SHORT= "short";


    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("windows")) {//Win
            SYSTEM_WINDOWS = true;
        } else {//MacOS
            SYSTEM_WINDOWS = false;
        }
    }
}
