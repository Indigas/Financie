package sk.durovic.financie.ui.main;

import android.util.Log;

import androidx.annotation.NonNull;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.time.*;

import sk.durovic.financie.controller.Item;

public class SavingFrequency {
    private Item item;

    public SavingFrequency(Item item) {
        this.item = item;
    }

    public static String getLeft(@NonNull Item item){
        StringBuilder sb = new StringBuilder();
        if(item.getEndDate()==null || item.getEndDate().equals(""))
            return "";

        String[] dueDateText = item.endDate.split("/");

        LocalDate today = LocalDate.now();
        LocalDate dueDate = LocalDate.of(Integer.parseInt(dueDateText[2]), Integer.parseInt(dueDateText[0]), Integer.parseInt(dueDateText[1]));

        if(ChronoUnit.DAYS.between(today, dueDate)<=0)
            return "";

        Period p = Period.between(today, dueDate);
        if(p.getYears()>0 && p.getYears()<2)
            sb.append(p.getYears() + " year ");
        else if (p.getYears()>=2)
            sb.append(p.getYears() + " years ");

        if(p.getMonths()!=0 && p.getMonths()<2)
            sb.append(p.getMonths() + " month ");
        else if(p.getMonths()>=2)
            sb.append(p.getMonths() + " months ");

        if(p.getDays()!=0 && p.getDays()<2)
            sb.append(p.getDays() + " day");
        else if(p.getDays() >= 2)
            sb.append(p.getDays() + " days");


        return sb.toString();
    }

    public static String getToSave(Item item){
        if(item.getEndDate()==null || item.getEndDate().equals(""))
            return "";

        int savingFrequency = item.savingFrequency;
        StringBuilder sb = new StringBuilder();
        long between=0;
        String[] dueDateText = item.endDate.split("/");

        LocalDate today = LocalDate.now();
        LocalDate dueDate = LocalDate.of(Integer.parseInt(dueDateText[2]), Integer.parseInt(dueDateText[0]), Integer.parseInt(dueDateText[1]));

        //check if dueDate is not before or today
        long afterGoal = ChronoUnit.DAYS.between(today, dueDate);
        if(afterGoal==0)
            return "Ends today";
        else if(afterGoal<0)
            return "After end date";

        switch (savingFrequency) {
            case 0:
                return "";
            case 1: sb.append(" EUR /daily");
                between = ChronoUnit.DAYS.between(today, dueDate);
                break;
            case 2: sb.append(" EUR /weekly");
                between = ChronoUnit.WEEKS.between(today, dueDate);
                break;
            case 3: sb.append(" EUR /monthly");
                between = ChronoUnit.MONTHS.between(today, dueDate);
                break;
        }


        if(between<1)
            between=1;

        double toSave = Math.round((item.overall - item.inBank) / between *100) /100.00 ;
        sb.insert(0, toSave);


        return sb.toString();
    }
}
