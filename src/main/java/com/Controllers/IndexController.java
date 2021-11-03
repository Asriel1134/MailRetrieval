package com.Controllers;

import com.Search.BooleanSearch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Asriel
 */
public class IndexController {
    public VBox mainBox;
    public Label searchButton;
    public TextField searchText;
    public AnchorPane title;
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

    public void init(){
        booleanSearch = new BooleanSearch();

        searchText.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                search();
            }
        });
    }

    public void search(){
        if (!("").equals(searchText.getText())){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/SearchResult.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SearchResultController controller = loader.getController();

            double timeS = System.currentTimeMillis();
            String[] result = booleanSearch.search(searchText.getText());
            double timeE = System.currentTimeMillis();
            controller.init(result.length, timeE-timeS, searchText.getText() ,booleanSearch);
            controller.setResult(result);

            Stage stage = (Stage) mainBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.getScene().setFill(null);
        }
    }
}
