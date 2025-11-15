package com.emillics.lemon.base.controller;

import com.emillics.lemon.base.listener.WindowEventListener;
import com.emillics.lemon.base.widget.BaseTask;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowContainer;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.scene.Node;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

public abstract class BaseController extends BaseTask implements WindowEventListener {
    @FXMLViewFlowContext
    protected ViewFlowContext viewFlowContext;
    protected FlowContainer<? extends Node> flowContainer;

    @PostConstruct
    public void onInit() {
        flowContainer = (FlowContainer<? extends Node>) viewFlowContext.getRegisteredObject("container");
        ArrayList<WindowEventListener> windowEventListeners =
                (ArrayList<WindowEventListener>) viewFlowContext.getRegisteredObject("WindowEventListeners");
        if (!windowEventListeners.contains(this)) windowEventListeners.add(this);
    }

    protected void navigate(Class<?> controllerClass) throws FlowException {
        Flow flow = new Flow(controllerClass);
        FlowHandler flowHandler = flow.createHandler(viewFlowContext);
        flowHandler.start(flowContainer);
    }

    @Override
    public boolean onWindowClose() {
        return false;
    }
}
