package com.example.guesstheday;


import java.io.Serializable;

public class Date implements Serializable {
    private int DD;
    private int MM;
    private int YY;
    public Date(int a,int b,int c){
        DD=a;
        MM=b;
        YY=c;
    }
    public int getDD(){return DD;}
    public int getMM(){return MM;}
    public int getYY(){return YY;}
    public String getDay(){
        int CenturyOffset=getCenturyOffset();
        int MonthOffset=getMonthOffset();
        int YearOffset=getYearOffset();
        int day=(CenturyOffset+MonthOffset+YearOffset+DD)%7;
        String[] Day=new String[]{"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
        return Day[day];
    }
    public int getCenturyOffset(){
        int num=(YY-YY%100)/100;
        int offset=0;
        offset = (4-(num%4)-1)*2;

        return offset;

    }
    public int getYearOffset(){
        int offset=0;
        if(isLeapYear(YY)&&(MM==1||MM==2))
        {
            offset=(YY%100+(YY%100)/4)%7-1;
        }
        else offset=(YY%100+(YY%100)/4)%7;
        return offset;
    }
    public static Boolean isLeapYear(int YY){
        boolean leapYear=false;
        if((YY%4==0 && YY%100!=0)||(YY%400==0))
            leapYear=true;
        return leapYear;
    }
    public int getMonthOffset(){
        int[] monthOffset=new int[]{0,0,3,3,6,1,4,6,2,5,0,3,5};
        return monthOffset[MM];
    }

}
