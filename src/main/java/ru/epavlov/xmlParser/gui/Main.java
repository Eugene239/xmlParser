package ru.epavlov.xmlParser.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import ru.epavlov.xmlParser.logic.MacroRunner;
import ru.epavlov.xmlParser.logic.Parser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private File dir;
    private final Label label = new Label("0/0");
    private final Button start  = new Button("Старт");
    private final Label error = new Label("");
    private final Label labelArea = new Label("Площадь дома");
    private final TextField area = new TextField();
    @Override
    public void start(Stage primaryStage) throws Exception{

      //  System.out.println(getClass().getResource("gui.fxml").toURI());
        //Parent root = FXMLLoader.load(");
        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList ();
//        ObservableList<String> items = FXCollections.observableArrayList();
//        File f = new File("C:\\Users\\epavlov\\Desktop\\xmlEugene");
//        for (File f_:f.listFiles()) {
//            items.add(f_.getName());
//        }
        list.setItems(items);
//        list.setMinWidth(600);
//        list.setMinHeight(475);
        error.setTextFill(Color.RED);
        start.setMinWidth(50);
        label.setMinWidth(70);
        label.setPadding(new Insets(10,10,10,10));

        DirectoryChooser directoryChooser =new DirectoryChooser();

        Button b  = new Button("Выберете папку");
        b.setMinWidth(150);
        b.setOnMouseClicked(l->{
            dir= directoryChooser.showDialog(primaryStage);
            if (dir!=null && dir.exists()) label.setText("0/"+size(dir));
           //  System.out.println(dir.getAbsolutePath());
        });
        final ProgressBar pb = new ProgressBar(0);
        pb.setMinWidth(520);
        pb.setPadding(new Insets(20,0,20,0));


        labelArea.setMinWidth(230);
        labelArea.setPadding(new Insets(5,0,0,120));
        HBox hb = new HBox();
        hb.getChildren().addAll(b,start, labelArea, area);
        hb.setSpacing(10);

        hb.setPadding(new Insets(0,0,0,0));
        GridPane root = new GridPane();
        root.addRow(5,pb,label);
        root.addRow(6,list);
        root.setPadding(new Insets(10,10,10,10));
        primaryStage.setTitle("XMLParser");



        root.add(hb,0,1);
//        root.add(b,0,1);
//        root.add(start,1,1);
//        ListView<> listView = new ListView()
//        root.add();
       // primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 600, 175));
        primaryStage.show();
        start.setOnMouseClicked(l->{
            if (!area.getText().equals("")) {
                new Thread(() -> {
                    if (dir != null && dir.exists()) {
                        int x = 0;
                        for (File f : dir.listFiles()) {
                            try {
                                if (f.getName().contains(".xml")) {
                                    Parser.getInstance().parseFile(f, Float.parseFloat(area.getText()));
                                }
                                x++;
                                int finalX = x;
                                Platform.runLater(() -> {
                                    label.setText(finalX + "/" + size(dir));
                                    pb.setProgress((float) finalX / (float) size(dir));
                                });

                            } catch (Exception e) {
                                Platform.runLater(() -> {
                                    items.add(e.getMessage());
                                });
                                continue;
                            }
                        }

                        try {
                            Parser.getInstance().save(new File(Parser.OUTPUT_FILE)); //запуск сохранения
                        } catch (IOException | SAXException | ParserConfigurationException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Thread done");
                    try {
                        MacroRunner.start(new File(Parser.OUTPUT_FILE));
                    } catch (IOException e) {
                        Platform.runLater(() -> {
                            items.add(e.getMessage());
                        });
                        System.err.println("MacroRunner:: "+e.toString());
                        //e.printStackTrace();
                    }
                }).start();
            }
        });

    }

    private int size(File f){
        int x=0;
        for(File f_:f.listFiles()){
            if (f_.getName().contains(".xml")) x++;
        }
        return x;
    }
    public static void main(String[] args) {
        launch(args);
    }
}