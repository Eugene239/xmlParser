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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import ru.epavlov.xmlParser.logic.Parser;
import rx.Subscription;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Eugene on 28.04.2017.
 */
public class Main extends Application {
    private static final String TAG = "["+ Main.class.getSimpleName()+"]: ";
    private static final Logger log = Logger.getLogger(Main.class);
    private DirectoryChooser directoryChooser =new DirectoryChooser();
    private Subscription subscription;
    private ThreadParser thread;
    private ThreadSaver saver;

    @FXML
    ListView listview;
    @FXML
    private Button dirBtn,filesBtn,parseBtn,saveBtn,clearBtn;
    @FXML
    TextField area;

    @FXML
    Label processText;
    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/main.view.fxml"));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("XMLtoXLSConverter");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);


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
        dirBtn.setOnMouseClicked(l -> {
            File dir = directoryChooser.showDialog(primaryStage);
            if (dir!=null && dir.exists()) {
                Model.getInstance().addFiles(dir);
            }

        });
        parseBtn.setOnMouseClicked(l->{
            parse();
        });
        filesBtn.setOnMouseClicked(l->{
            FileChooser fileChooser = new FileChooser();
            java.util.List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
            if (list!=null) Model.getInstance().addFiles(list);
        });
        clearBtn.setOnMouseClicked(l->{
            Model.getInstance().clear();
        });
        saveBtn.setOnMouseClicked(l->{
            if ( (saver==null || !saver.isAlive()) && thread!=null && thread.isDone()) {
                FileChooser fileChooser1 = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLS файл", "*.xls");
                fileChooser1.getExtensionFilters().add(extFilter);
                File file = fileChooser1.showSaveDialog(primaryStage);
                if (file != null) {
                    Parser.OUTPUT_FILE = file.getAbsolutePath();
                    save();
                }
            }
        });
        primaryStage.show();
    }

    private void parse(){
        try {
            String text = area.getText().replace(",",".");
            float f = Float.parseFloat(text);
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
            //e.printStackTrace();
            processText.setText("Неверная площадь дома");
            log.error(TAG+e.toString());
            System.err.println(area.getText()+" "+area.getText().length());
        }
    }
    public void save(){
        saver = new ThreadSaver();
        if (subscription!=null) subscription.unsubscribe();
        subscription= saver.getStatus().subscribe(s->{
            Platform.runLater(()->{
                processText.setText(s);
            });
        });
        saver.start();

    }
}
