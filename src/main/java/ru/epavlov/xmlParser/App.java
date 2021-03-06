//package ru.epavlov.xmlParser;
//
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListView;
//import javafx.scene.control.TextField;
//import javafx.stage.DirectoryChooser;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//import org.apache.log4j.Logger;
//import ru.epavlov.xmlParser.gui.Item;
//import ru.epavlov.xmlParser.gui.Model;
//import ru.epavlov.xmlParser.gui.ThreadParser;
//import ru.epavlov.xmlParser.gui.ThreadSaver;
//import ru.epavlov.xmlParser.logic.Messages;
//import ru.epavlov.xmlParser.logic.Parser;
//import rx.Subscription;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class App extends Application {
//    private static final String TAG = "["+ App.class.getSimpleName()+"]: ";
//    private static final Logger log = Logger.getLogger(App.class);
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//    private DirectoryChooser directoryChooser =new DirectoryChooser();
//
//    private Subscription subscription;
//    private ThreadParser thread;
//    private ThreadSaver saver;
//    @FXML
//    TextField area;
//    @FXML
//    ListView listview;
//    @FXML
//    Button dirChooser;
//    @FXML
//    Button start;
//    @FXML
//    Button fileChooser;
//    @FXML
//    Label processText;
//    @FXML
//    Button clear;
//    @FXML
//    Button saveBtn;
//    @Override
//    public void start(Stage primaryStage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/scene.fxml"));
//        fxmlLoader.setController(this);
//        Parent root = fxmlLoader.load();
//        Scene scene = new Scene(root);
//        area.setOnAction(event -> System.out.println(area.getText()));
//        listview.setCellFactory(param -> {
//            Item item =new Item();
//            return  item;
//        });
//
//        Model.getInstance().getRxFiles().subscribe(files -> {
//            ArrayList<String> s = new ArrayList<>();
//            files.forEach(f -> s.add(f.getName()));
//            ObservableList<String> fileNames = FXCollections.observableArrayList(s);
//            listview.setItems(fileNames);
//        });
//
//        dirChooser.setOnMouseClicked(l -> {
//            File dir = directoryChooser.showDialog(primaryStage);
//            if (dir!=null && dir.exists()) {
//                Model.getInstance().addFiles(dir);
//            }
//
//        });
//        start.setOnMouseClicked(l->{
//            parse();
//        });
//        fileChooser.setOnMouseClicked(l->{
//            FileChooser fileChooser = new FileChooser();
//            List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
//            if (list!=null) Model.getInstance().addFiles(list);
//        });
//        clear.setOnMouseClicked(l->{
//            Model.getInstance().clear();
//        });
//        saveBtn.setOnMouseClicked(l->{
//            if ( (saver==null || !saver.isAlive()) && thread!=null && thread.isDone()) {
//                FileChooser fileChooser1 = new FileChooser();
//                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLS файл", "*.xls");
//                fileChooser1.getExtensionFilters().add(extFilter);
//                File file = fileChooser1.showSaveDialog(primaryStage);
//                if (file != null) {
//                    Parser.OUTPUT_FILE = file.getAbsolutePath();
//                    save();
//                }
//            }
//        });
//        primaryStage.setTitle("xmlParser");
//        primaryStage.setResizable(false);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//
//    private void setStatus(String name,String status){
//        Model.getInstance().get(name).getStatus().onNext(status);
//    }
//    private void parse(){
//        try {
//            String text = area.getText().replace(",",".");
//            float f = Float.parseFloat(text);
//            Model.getInstance().setArea(f);
//            if (thread==null || !thread.isAlive()) {
//                thread = new ThreadParser();
//                if (subscription!=null) subscription.unsubscribe();
//                subscription= thread.getStatus().subscribe(s->{
//                    Platform.runLater(()->{
//                        processText.setText(s);
//                    });
//                });
//                thread.start();
//            }
//        } catch (Exception e){
//            //e.printStackTrace();
//            processText.setText(Messages.AREA_ERROR);
//            log.error(TAG+e.toString());
//            System.err.println(area.getText()+" "+area.getText().length());
//        }
//    }
//    public void save(){
//            saver = new ThreadSaver();
//            if (subscription!=null) subscription.unsubscribe();
//            subscription= saver.getStatus().subscribe(s->{
//                Platform.runLater(()->{
//                    processText.setText(s);
//                });
//            });
//            saver.start();
//
//    }
//
//}