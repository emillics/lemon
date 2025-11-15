package com.emillics.lemon.model;

import com.teamdev.jxbrowser.js.JsAccessible;
import com.teamdev.jxbrowser.js.JsObject;

public class JavaObjectForJs {
    @JsAccessible
    public void log(JsObject jsObject) {
        System.out.println(jsObject);
    }
}
