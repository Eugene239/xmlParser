package ru.epavlov.xmlParser.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(getClass().getResource("gui.fxml").toURI());
        //Parent root = FXMLLoader.load(");
        primaryStage.setTitle("Hello World");
       // primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}