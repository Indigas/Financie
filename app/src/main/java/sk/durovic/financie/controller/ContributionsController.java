package sk.durovic.financie.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import java.util.Calendar;

import sk.durovic.financie.GoalsListViewModel;
import sk.durovic.financie.data.ContributionsToItem;
import sk.durovic.financie.data.ItemContributions;

public class ContributionsController {
    private Context context;
    private GoalsListViewModel mViewModel;
    private boolean fixError=false;
    private Item itemOwner;

    public ContributionsController(Context context, ViewModel vm, Item item) {
        this.context = context;
        this.mViewModel = (GoalsListViewModel)vm;
        this.itemOwner = item;
    }

    public ContributionsToItem parseContribution(String save, String endDate){
        ContributionsToItem cTi = new ContributionsToItem();
        cTi.ItemIdOwner = itemOwner.ItemId;
        cTi.contribution = checkSave(save);
        cTi.addDate = checkDate(endDate);

        if(fixError)
            cTi = null;

        return cTi;
    }

     private double checkSave(String save){
        if(save.equals("")){
            Toast.makeText(context, "Please enter number", Toast.LENGTH_SHORT).show();
            fixError=true;
            return 0;
        }

        return Math.round(Double.parseDouble(save)*100)/100.00;
    }

    private String checkDate(String datte){
        if(datte.equals("") || datte.equals("Click here to add date")) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DATE);
            String endDate = (month+1) + "/" + day + "/" + year;
            return endDate;
        }

        return datte;
    }

    public void changeOnUpdateContribution(ContributionsToItem before, ContributionsToItem after){
        after.ContributionsId = before.ContributionsId;
        double sum = after.contribution - before.contribution;
        itemOwner.inBank += sum;
        mViewModel.getDatabaseAccess().updateItem(itemOwner);
        mViewModel.setCurrentItem(itemOwner);
    }

    public void changeOnAddContribution(ContributionsToItem cTi){
        itemOwner.inBank += cTi.contribution;
        mViewModel.getDatabaseAccess().updateItem(itemOwner);
        mViewModel.setCurrentItem(itemOwner);
    }
}
