package ru.epavlov.xmlParser.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.io.IOException;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 21.04.2017, 11:39
 */
public class Item extends ListCell<String> {
    @FXML
    private Label name;
    @FXML
    private Label status;
    @FXML
    private Button delete;
    private Parent scene;
    String lastItem;
    public Item() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/item.fxml"));
        fxmlLoader.setController(this);
        scene= fxmlLoader.load();
    }
    @Override
    public void updateItem(String string, boolean empty)
    {
        super.updateItem(string,empty);
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = string;
            name.setText(string);
            status.setText("Загружено");
            setGraphic(scene.lookup("HBox"));
        }
    }


    static class XCell extends ListCell<String> {
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Button button = new Button("(>)");
        String lastItem;

        public XCell() {
            super();
            hbox.getChildren().addAll(label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println(lastItem + " : " + event);
                }
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);  // No text in label of super class
            if (empty) {
                lastItem = null;
                setGraphic(null);
            } else {
                lastItem = item;
                label.setText(item!=null ? item : "<null>");
                setGraphic(hbox);
            }
        }
    }
}
