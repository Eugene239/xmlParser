package ru.epavlov.xmlParser.logic;

import java.util.ArrayList;
import java.util.HashMap;


public class RoomInfo {
    private HashMap<String,ArrayList<String>> map = new HashMap<>();

    public int maxSize(){
       return  map.computeIfAbsent("ФИО",k->new ArrayList<>()).size();
    }
    public void add(String s,String value){
        if (s.equals("Доля_собственника") && !value.equals("1") ){
           // System.err.println("доля");
            if (value.trim().equals("весь объект")) value="1/1";
            int x = Integer.parseInt(value.split("/")[0]);
            int y = Integer.parseInt(value.split("/")[1]);
            double f = (double) x/(double)y;
            //System.out.println(x+"/"+y+"="+ f);
            add("Доля_собственника_дробь",f+"");
            double d = Double.parseDouble(getValue("Площадь_квартиры").get(0));
            add("Площадь_собственника", f*d+"");
        }
        map.computeIfAbsent(s, k -> new ArrayList<>());
       if (s.equals("Номер_свидетельства")|| s.equals("Дата_свидетельства")  )  {
           if(map.get(s).size()<maxSize()) map.get(s).add(value);
       } else  map.get(s).add(value);
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
//        ArrayList<String> s= getValue("Номер_свидетельства");
//        ArrayList<Integer> index = new ArrayList<>();
//        for (int i = 0; i <s.size()-1 ; i++) {
//            for (int j = i+1; j <s.size() ; j++) {
//                if (s.get(i).equals(s.get(j))) {
//                    index.add(i);
//                }
//            }
//        }
    //    remove(index);
        map.forEach((s,l)->{ //дублируем данные
            if (l.size()==1){
                for (int i = 0; i <maxSize()-1 ; i++) {
                    l.add(l.get(0));
                }
            }
        });
    }
    private void remove(ArrayList<Integer > list){
        for (int i= list.size()-1; i>=0; i--) {
         //   System.out.println("Deleting:"+list.size());
            getValue("Номер_свидетельства").remove(list.get(i));
            getValue("Дата_свидетельства").remove(list.get(i));
        }
        map.forEach((s,l)->{
            if (l.size()==1){
                for (int i = 0; i <maxSize()-1 ; i++) {
                    l.add(l.get(0));
                }
            }
        });
    }
}
