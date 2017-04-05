package ru.epavlov.xmlParser.main;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 05.04.2017, 17:47
 */
public class PersonInfo {
    public static float houseArea;
    private String street;
    private String house;
    private String corpus;
    private String liter;
    private String room;
    private String regNumber;
    private String regDate;
    private float roomArea;
    private String spart;
    private float fpart;



    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public void setLiter(String liter) {
        this.liter = liter;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public void setRoomArea(float roomArea) {
        this.roomArea = roomArea;
    }

    public void setSpart(String spart) {
        this.spart = spart;
    }

    public void setFpart(float fpart) {
        this.fpart = fpart;
    }

    public String getStreet() {
        return street;
    }

    public String getHouse() {
        return house;
    }

    public String getCorpus() {
        return corpus;
    }

    public String getLiter() {
        return liter;
    }

    public String getRoom() {
        return room;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public String getRegDate() {
        return regDate;
    }

    public float getRoomArea() {
        return roomArea;
    }

    public String getSpart() {
        return spart;
    }

    public float getFpart() {
        return fpart;
    }
}
