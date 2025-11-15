package com.emillics.lemon.base.widget;

import com.emillics.lemon.utils.FXUtils;
import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import jfxtras.styles.jmetro.MDL2IconFont;

public class IconButton extends StackPane {
    private Button button;
    private MDL2IconFont icon;
    private int defaultIconSize = 15;
    private int padding = 5;

    public IconButton(@NamedArg("iconId") String iconId) {
        super();
        init(iconId, null, null);
    }

    public IconButton(@NamedArg("iconId") String iconId, @NamedArg("size") Integer size) {
        super();
        init(iconId, size, null);
    }

    public IconButton(@NamedArg("iconId") String iconId, @NamedArg("tooltip") String toolTip) {
        super();
        init(iconId, null, toolTip);
    }

    public IconButton(@NamedArg("iconId") String iconId, @NamedArg("size") Integer size, @NamedArg("tooltip") String toolTip) {
        super();
        init(iconId, size, toolTip);
    }

    public IconButton(@NamedArg("iconId") String iconId, Node... children) {
        super(children);
        init(iconId, null, null);
    }

    private void init(String iconId, Integer size, String toolTip) {
        setAlignment(Pos.CENTER);

        button = new Button();
        button.setStyle("-fx-background-color:transparent");
        getChildren().add(button);

        icon = new MDL2IconFont();
        icon.setMouseTransparent(true);
        icon.setText(iconId);
        getChildren().add(icon);

        if (size != null) defaultIconSize = size;
        setDefaultIconSize(defaultIconSize);
        setPadding(padding);

        if (toolTip != null && !toolTip.isEmpty()) {
            Tooltip tooltip = new Tooltip(toolTip);
            Tooltip.install(button, tooltip);
            FXUtils.setToolTipTime(tooltip, 1000);
        }
    }

    public void setIcon(String iconId) {
        icon.setText(iconId);
    }

    public String getIcon() {
        return icon.getText();
    }

    public void setDefaultIconSize(int defaultIconSize) {
        this.defaultIconSize = defaultIconSize;
        icon.setSize(defaultIconSize);
        double size = defaultIconSize + padding * 2;
        button.setPrefSize(size, size);
        button.setMinSize(size, size);
        button.setMaxSize(size, size);
        setPrefSize(size, size);
        setMinSize(size, size);
        setMaxSize(size, size);
    }

    public void setPadding(int padding) {
        this.padding = padding;
        double size = defaultIconSize + padding * 2;
        button.setPrefSize(size, size);
        button.setMinSize(size, size);
        button.setMaxSize(size, size);
        setPrefSize(size, size);
        setMinSize(size, size);
        setMaxSize(size, size);
    }

    public void setActionHandler(EventHandler<ActionEvent> actionHandler) {
        button.setOnAction(actionHandler);
    }
}
