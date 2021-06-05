package com.example.guesstheday;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.example.guesstheday.Date.isLeapYear;

public class Data {
    private int yy=0,mm=0,dd=0;
    private Random random=new Random();
    public void AssignDate(){
        yy=random.nextInt(201)+1900;
        mm=random.nextInt(12)+1;
        if(mm==2){
            if(isLeapYear(yy))
            dd=random.nextInt(29)+1;
            else dd=random.nextInt(28)+1;
        }
        else if((mm%2==1&&mm<8)||(mm%2==0&&mm>7))
            dd=random.nextInt(31)+1;
        else dd= random.nextInt(30)+1;
    }
    public Date getDate(){
        Date date=new Date(dd,mm,yy);
        return date;
    }
    private ArrayList<String> optionList=new ArrayList<String>();
    public ArrayList<String> getoptionList(){
        ArrayList<String> Day=new ArrayList<String>();
        Day.add("SUNDAY");
        Day.add("MONDAY");
        Day.add("TUESDAY");
        Day.add("WEDNESDAY");
        Day.add("THURSDAY");
        Day.add("FRIDAY");
        Day.add("SATURDAY");
        Date date=new Date(dd,mm,yy);
        optionList.add(date.getDay());
        Day.remove(date.getDay());
        Collections.shuffle(Day);
        for(int i=0;i<3;i++){
            optionList.add(Day.get(i));
        }
        return optionList;
    }
}
