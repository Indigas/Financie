package sk.durovic.financie.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

import sk.durovic.financie.GoalsListViewModel;
import sk.durovic.financie.data.ContributionsToItem;
import sk.durovic.financie.data.ItemContributions;

public class ItemController {
    private Item currentItem;
    private GoalsListViewModel mViewModel;
    private boolean fixError = false;
    private Context context;

    public ItemController(Item currentItem, GoalsListViewModel mViewModel) {
        this.currentItem = currentItem;
        this.mViewModel = mViewModel;
    }

    public ItemController(Context context){
        this.context = context;
    }

    public void changeOnDeleteItem(ContributionsToItem cont){
        mViewModel.getDatabaseAccess().deleteContributions(cont);
        currentItem.inBank -= cont.contribution;
        mViewModel.getDatabaseAccess().updateItem(currentItem);
        mViewModel.setCurrentItem(currentItem);
    }



    public Item parseItem(List<String> a){
        Item ab = new Item();
        ab.name = checkName(a.get(0));
        ab.description = checkDescription(a.get(1));
        ab.overall = checkGoal(a.get(2));
        ab.inBank = checkSaved(a.get(3));
        ab.endDate = checkDate(a.get(4));
        ab.savingFrequency = checkFrequency(a.get(5));


        if(fixError)
            return null;


        return ab;
    }

    String checkDescription(String a){
        if(a.length()>25) {
            Toast.makeText(context, "Description has to be less than 25 chars", Toast.LENGTH_SHORT).show();
            fixError = true;
            return null;
        }

        return a;
    }

    String checkName(String a){
        if(a.length()==0 || a.length()>15){
            Toast.makeText(context, "Name has to be between 1 - 15 chars", Toast.LENGTH_SHORT).show();
            fixError=true;
            return null;
        }

        return a;
    }

    double checkSaved(String a){
        return a.equals("") ? 0 : Math.round(Double.parseDouble(a)*100)/100.00;
    }

    double checkGoal(String a){
        if(a.equals("")) {
            Toast.makeText(context, "You have to enter goal.", Toast.LENGTH_SHORT).show();
            fixError = true;
            return 0;
        }

        return Math.round(Double.parseDouble(a)*100)/100.00;
    }

    int checkFrequency(String a){
        return Integer.parseInt(a);
    }

    String checkDate(String dateString){
        if(dateString.equals("Click here to add date") || dateString.equals("")) {
            return "";
        }

        String[] datum = dateString.split("/");

        LocalDate today = LocalDate.now();
        LocalDate dueDate = LocalDate.of(Integer.parseInt(datum[2]), Integer.parseInt(datum[0]), Integer.parseInt(datum[1]));


        if(ChronoUnit.DAYS.between(today, dueDate)<=0) {
            Toast.makeText(context, "Can't add date before/or today.", Toast.LENGTH_SHORT).show();
            fixError = true;
            return null;
        }

        return dateString;
    }
}
