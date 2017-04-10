package ru.epavlov.xmlParser.main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 05.04.2017, 17:47
 */
public class RoomInfo {
    private HashMap<String,ArrayList<String>> map = new HashMap<>();

    public int maxSize(){
        final int[] maxSize = {0};
        map.forEach((s,l)->{
            if (l.size()> maxSize[0]) maxSize[0] = l.size();
        });
        return maxSize[0];
    }
    public void add(String s,String value){
        if (s.equals("Доля_собственника")){
           // System.err.println("доля");
            int x = Integer.parseInt(value.split("/")[0]);
            int y = Integer.parseInt(value.split("/")[1]);
            double f = (double) x/(double)y;
            //System.out.println(x+"/"+y+"="+ f);
            add("Доля_собственника_дробь",f+"");
            double d = Double.parseDouble(getValue("Площадь_квартиры").get(0));
            add("Площадь_собственника", f*d+"");
        }
        map.computeIfAbsent(s, k -> new ArrayList<>());
        map.get(s).add(value);
    }
    public ArrayList<String> getValue(String s){
        map.computeIfAbsent(s, k -> new ArrayList<>());
        return map.get(s);
    }
    public ArrayList<String> cut(){
        ArrayList<String> list = new ArrayList<>();

        return list;
    }
    public void init(){
        if (getValue("Доля_собственника").size()==0){
            add("Доля_собственника","1");
            add("Доля_собственника_дробь","1");
            add("Площадь_собственника",getValue("Площадь_квартиры").get(0));
        }
        ArrayList<String> s= getValue("Номер_свидетельства");
        ArrayList<Integer> index = new ArrayList<>();
        for (int i = 0; i <s.size()-1 ; i++) {
            for (int j = i+1; j <s.size() ; j++) {
                if (s.get(i).equals(s.get(j))) {
                    index.add(i);
                }
            }
        }
        remove(index);
    }
    private void remove(ArrayList<Integer > list){
        System.out.println("Deleting:"+list.size());
        for (int i= list.size()-1; i>=0; i--) {
            getValue("Номер_свидетельства").remove(i);
            getValue("Дата_свидетельства").remove(i);
        }
        map.forEach((s,l)->{
            if (l.size()==1){
                for (int i = 0; i <maxSize()-1 ; i++) {
                    l.add(l.get(0));
                }
            }
        });
    }






//    public static float houseArea;
//    private String street;
//    private String house;
//    private String corpus;
//    private String liter;
//    private String room;
//    private ArrayList<String> fio = new ArrayList<>();
//    private ArrayList<String> regNumber = new ArrayList<>();
//    private ArrayList<String>  regDate = new ArrayList<>();
//    private float roomArea;
//    private ArrayList<String> spart =new ArrayList<>();
//    private ArrayList<Float> fpart = new ArrayList<>();
//
//
//
//    public void setStreet(String street) {
//        this.street = street;
//    }
//
//    public void setHouse(String house) {
//        this.house = house;
//    }
//
//    public void setCorpus(String corpus) {
//        this.corpus = corpus;
//    }
//
//    public void setLiter(String liter) {
//        this.liter = liter;
//    }
//
//    public void setRoom(String room) {
//        this.room = room;
//    }
//
//    public void setRoomArea(float roomArea) {
//        this.roomArea = roomArea;
//    }
//
//    public String getStreet() {
//        return street;
//    }
//
//    public String getHouse() {
//        return house;
//    }
//
//    public String getCorpus() {
//        return corpus;
//    }
//
//    public String getLiter() {
//        return liter;
//    }
//
//    public String getRoom() {
//        return room;
//    }
//
//    public ArrayList<String> getFio() {
//        return fio;
//    }
//
//    public ArrayList<String> getRegNumber() {
//        return regNumber;
//    }
//
//    public ArrayList<String> getRegDate() {
//        return regDate;
//    }
//
//    public float getRoomArea() {
//        return roomArea;
//    }
//
//    public ArrayList<String> getSpart() {
//        return spart;
//    }
//
//    public ArrayList<Float> getFpart() {
//        return fpart;
//    }


}
