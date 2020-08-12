package sk.durovic.financie.data;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import java.util.LinkedList;
import java.util.List;

import sk.durovic.financie.GoalsListViewModel;
import sk.durovic.financie.controller.Item;

public class DatabaseAccess implements Runnable {

    private AppDatabase itemDatabase;
    private GoalsListViewModel mViewModel;
    private Context context;
    private Item item;
    private ContributionsToItem contributions;
    private DatabaseAction action;
    private List<ContributionsToItem> listOf = new LinkedList<>();

    private enum DatabaseAction {
        INSERT,
        UPDATE,
        DELETE,
        ADD_CONTRIBUTION,
        UPDATE_CONTRIBUTION,
        DELETE_CONTRIBUTION;
    }

    public DatabaseAccess(Context context) {
        this.context = context;
        mViewModel = new ViewModelProvider((FragmentActivity)context).get(GoalsListViewModel.class);
        mViewModel.setDatabase(Room.databaseBuilder(context, AppDatabase.class, "DaoItem").build());
        itemDatabase = mViewModel.getDatabase();
    }

    public void inputItem(Item item){
        Thread a = new Thread(this);
        this.item = item;
        this.action = DatabaseAction.INSERT;
        a.start();
        try{
            a.join();
        }catch (Exception ignored){}
    }

    public void inputContributions(ContributionsToItem contributionsToItem){
        this.item = item;
        this.contributions = contributionsToItem;
        this.action = DatabaseAction.ADD_CONTRIBUTION;
        Thread a = new Thread(this);
        a.start();
        try{
            a.join();
        }catch (Exception ignored){}
    }

    public void deleteItem(Item item){
        this.item = item;
        this.action = DatabaseAction.DELETE;
        Thread a = new Thread(this);
        a.start();
        try{
            a.join();
        }catch (Exception ignored){}
    }

    public void updateItem(Item item) {
        this.item = item;
        this.action = DatabaseAction.UPDATE;
        Thread a = new Thread(this);
        a.start();
        try{
            a.join();
        }catch (Exception ignored){}
    }

    public void updateItemContributions(ContributionsToItem contributionsToItem) {
        this.contributions = contributionsToItem;
        this.action = DatabaseAction.UPDATE_CONTRIBUTION;
        Thread a = new Thread(this);
        a.start();
        try{
            a.join();
        }catch (Exception ignored){}
    }

    public void deleteContributions(ContributionsToItem contributionsToItem){
        this.contributions = contributionsToItem;
        this.action = DatabaseAction.DELETE_CONTRIBUTION;
        Thread a = new Thread(this);
        a.start();
        try{
            a.join();
        }catch (Exception ignored){}
    }

    public LiveData<List<ItemContributions>> getAll() {
        return mViewModel.getDatabase().daoItem().getAll();
    }

    public LiveData<List<ContributionsToItem>> getAllContributions(int owner){
        return mViewModel.getDatabase().daoContributions().getAllContributionsLive(owner);
    }

    public List<ContributionsToItem> getAllContributionsOfItem(final int ownerID){
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
               listOf= mViewModel.getDatabase().daoContributions().getAllContributions(ownerID);
            }
        });
        a.start();

        try{
        a.join();
        }catch (Exception ignored){}
        return listOf;
    }

    @Override
    public void run() {

        switch (action){
            case INSERT: itemDatabase.daoItem().insertItem(item);
                break;
            case UPDATE: itemDatabase.daoItem().updateItem(item);
                //Log.i("DatabaseAccess", String.valueOf(item.ItemId));
                break;
            case DELETE: itemDatabase.daoItem().deleteItem(item);
                break;
            case ADD_CONTRIBUTION: itemDatabase.daoContributions().insertContributions(contributions);
                break;
            case UPDATE_CONTRIBUTION: itemDatabase.daoContributions().updateContributions(contributions);
                break;
            case DELETE_CONTRIBUTION: itemDatabase.daoContributions().deleteContributions(contributions);
                break;
        }

    }
}
