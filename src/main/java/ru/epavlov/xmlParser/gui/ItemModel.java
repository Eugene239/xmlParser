package ru.epavlov.xmlParser.gui;

import rx.subjects.BehaviorSubject;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 21.04.2017, 15:24
 */
public class ItemModel {
    private String name;
    private BehaviorSubject<String> status = BehaviorSubject.create();

    public ItemModel(String name) {
        this.name = name;
        status.onNext("Загружен");
    }

    public String getName() {
        return name;
    }

    public BehaviorSubject<String> getStatus() {
        return status;
    }
}
