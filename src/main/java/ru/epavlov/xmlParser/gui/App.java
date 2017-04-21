package ru.epavlov.xmlParser.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @FXML
    TextField area;
    @FXML
    ListView listview;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/scene.fxml"));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        Scene scene =new Scene(root);
        area.setOnAction(event -> System.out.println(area.getText()));
        ObservableList<String> list = FXCollections.observableArrayList(
                "Item 1", "Item 2", "Item 3", "Item 4");
        listview.setItems(list);
        listview.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                try {
                    return new Item();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        primaryStage.setTitle("xmlParser");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
//        fxmlLoader.load();
//
    }
}
