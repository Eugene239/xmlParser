package ru.epavlov.xmlParser.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import rx.Subscription;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private DirectoryChooser directoryChooser =new DirectoryChooser();
    private Subscription subscription;
    private ThreadParser thread;
    @FXML
    TextField area;
    @FXML
    ListView listview;
    @FXML
    Button dirChooser;
    @FXML
    Button start;
    @FXML
    Label processText;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/scene.fxml"));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        area.setOnAction(event -> System.out.println(area.getText()));
        listview.setCellFactory(param -> {
            Item item =new Item();
            return  item;
        });

        Model.getInstance().getRxFiles().subscribe(files -> {
            ArrayList<String> s = new ArrayList<>();
            files.forEach(f -> s.add(f.getName()));
            ObservableList<String> fileNames = FXCollections.observableArrayList(s);
            listview.setItems(fileNames);
        });

        dirChooser.setOnMouseClicked(l -> {
            File dir = directoryChooser.showDialog(primaryStage);
            if (dir!=null && dir.exists()) {
                Model.getInstance().addFiles(dir);
            }

        });
        start.setOnMouseClicked(l->{
           parse();
        });

        primaryStage.setTitle("xmlParser");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void setStatus(String name,String status){
        Model.getInstance().get(name).getStatus().onNext(status);
    }
    private void parse(){
        try {
            float f = Float.parseFloat(area.getText());
            Model.getInstance().setArea(f);
            if (thread==null || !thread.isAlive()) {
                thread = new ThreadParser();
                if (subscription!=null) subscription.unsubscribe();
                subscription= thread.getStatus().subscribe(s->{
                    Platform.runLater(()->{
                        processText.setText(s);
                    });
                });
                thread.start();
            }
        } catch (Exception e){
           e.printStackTrace();
            processText.setText("Неверная площадь дома");
            System.out.println(area.getText()+" "+area.getText().length());
        }
    }

}
