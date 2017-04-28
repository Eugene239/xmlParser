package ru.epavlov.xmlParser.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.util.ArrayList;

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

    public static ArrayList<Item> list = new ArrayList<>();
    private ItemModel model;
    @FXML
    private Label name;
    @FXML
    private Label status;
    @FXML
    private Button delete;
    private Parent scene;
    String lastItem;

    public Label getStatus() {
        return status;
    }

    public Label getName() {
        return name;
    }

    public Item()  {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/item.fxml"));
        fxmlLoader.setController(this);
        try {
            scene= fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void updateItem(String string, boolean empty) {
        super.updateItem(string,empty);
        if (empty) {
            setGraphic(null);
        } else {
            model = Model.getInstance().get(string);
            name.setText(model.getName());
            model.getStatus().subscribe(s->{
                status.setText(s);
            });
            delete.setOnMouseClicked(l->{
                Model.getInstance().remove(string);
            });
            setGraphic(scene.lookup("HBox"));
        }
    }


}