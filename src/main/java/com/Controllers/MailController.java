package com.Controllers;

import com.Search.BooleanSearch;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author Asriel
 */
public class MailController {
    public VBox mainBox;
    public AnchorPane title;
    public String[] info;
    public Label titleName;
    public AnchorPane resultArea;
    public Label text;
    public Label project;
    public Label from;
    public Label to;
    public Label date;
    public Label messageId;

    public void winClose(){
        Stage stage = (Stage) mainBox.getScene().getWindow();
        stage.close();
    }

    public void winMinimize(){
        Stage stage = (Stage) mainBox.getScene().getWindow();
        stage.setIconified(true);
    }

    private double xOffset;
    private double yOffset;

    public void setDragXY(){
        Scene scene = mainBox.getScene();
        scene.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
    }

    public void dragWindow(){
        Stage stage = (Stage) mainBox.getScene().getWindow();
        title.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
    }

    public void init(String[] info){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setSpread(0.1);
        mainBox.setEffect(dropShadow);

        this.info = info;
        titleName.setText(info[4]);
        text.setText(info[17]);
        project.setText(info[4]);
        from.setText("From: " + info[2]);
        to.setText("To: " + Arrays.toString(info[3].split(",")));
        date.setText("Date: " + info[1]);
        messageId.setText("Message-ID: " + info[0]);
    }
}
