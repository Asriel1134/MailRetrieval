package com.Controllers;

import com.Search.BooleanSearch;
import com.Search.ResultProcessing;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Arrays;

public class SearchResultController {
    public VBox mainBox;
    public Label searchButton;
    public TextField searchText;
    public AnchorPane title;
    public Label time;
    public Label sum;
    public AnchorPane resultArea;
    public BooleanSearch booleanSearch;

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

    public void search(){
        if (!("".equals(searchText.getText()))){
            double timeS = System.currentTimeMillis();
            String[] result = booleanSearch.search(searchText.getText());
            double timeE = System.currentTimeMillis();
            init(result.length, timeE-timeS, searchText.getText() ,booleanSearch);
            resultArea.getChildren().clear();
            setResult(result);
        }
    }

    public void init(int len, double time, String searchText, BooleanSearch booleanSearch){
        sum.setText("Found " + len + " related results");
        this.time.setText("Time cost " + time/1000 + " ms");
        this.searchText.setText(searchText);

        this.booleanSearch = booleanSearch;

        this.searchText.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                search();
            }
        });
    }

    public void setResult(String[] files) {
        resultArea.setPrefHeight(160*files.length+40);

        double layoutY = 20;
        for (String file:files){
            String[] info = ResultProcessing.getFileInfo(file);

            Label subject = new Label();
            subject.setText(info[4]);
            subject.setStyle("-fx-font-size: 16;-fx-text-fill: rgb(255,183,0);-fx-underline: true");
            subject.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Stage mail = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Mail.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MailController controller = loader.getController();
                    controller.init(info);

                    mail.setTitle("Mail");
                    mail.getIcons().add(new Image("/icon/logoMini.png"));
                    mail.setResizable(false);
                    mail.initModality(Modality.APPLICATION_MODAL);
                    mail.initStyle(StageStyle.TRANSPARENT);
                    mail.setScene(new Scene(root));
                    mail.getScene().setFill(null);
                    mail.show();
                }
            });
            subject.setCursor(Cursor.HAND);
            resultArea.getChildren().add(subject);
            subject.setLayoutX(40);
            subject.setLayoutY(layoutY);

            Label text = new Label();
            text.isWrapText();
            text.setText(info[17]);
            text.setStyle("-fx-font-size: 13");
            resultArea.getChildren().add(text);
            text.setPrefHeight(90);
            text.setPrefWidth(650);
            text.setLayoutX(40);
            text.setLayoutY(layoutY+30);

            Label from = new Label();
            from.setText("From: " + info[2]);
            from.setStyle("-fx-font-size: 11; -fx-text-fill: rgb(93,93,93)");
            from.setPrefWidth(300);
            from.setLayoutX(43);
            from.setLayoutY(layoutY+125);
            resultArea.getChildren().add(from);

            Label date = new Label();
            date.setText("Time: " + info[1]);
            date.setStyle("-fx-font-size: 11; -fx-text-fill: rgb(93,93,93)");
            date.setPrefWidth(300);
            date.setLayoutX(350);
            date.setLayoutY(layoutY+125);
            resultArea.getChildren().add(date);

            layoutY += 160;
        }
    }
}
