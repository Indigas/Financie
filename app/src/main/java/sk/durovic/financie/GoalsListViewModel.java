package sk.durovic.financie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import sk.durovic.financie.controller.Item;
import sk.durovic.financie.data.AppDatabase;
import sk.durovic.financie.data.DatabaseAccess;
import sk.durovic.financie.data.ItemContributions;

public class GoalsListViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    // IMPLEMENTACIA DO FUNKCII ABY SA ODTIALTO PRISTUPOVALO K DATABAZE
    // NIEKTORE FUNKCIE ANI NEPOTREBUJEM
    // UROBIT SI REVIZIU KDE A AKE UDAJE POTREBUJEM
    // AKE UDAJE BUDEM POSIELAT DO TEJTO TRIEDY A CO DALEJ S NIMI?
    private MutableLiveData<List<Item>> listOfItems = new MutableLiveData<>();
    private MutableLiveData<Item> currentItemLive = new MutableLiveData<>();
    private MutableLiveData<Item> newItemLiveData = new MutableLiveData<>();
    private MutableLiveData<AppDatabase> myDatabase = new MutableLiveData<>();
    private Item itemForEdit;
    private AppDatabase database;
    private DatabaseAccess databaseAccess;
    private List<ItemContributions> listOfAll = new LinkedList<>();

    public LiveData<List<Item>> getListOfItems() {
        return listOfItems;
    }
    public void setListOfItems(List<Item> list) {
        listOfItems.setValue(list);
    }
    public void setCurrentItem(Item currentItem){
        currentItemLive.setValue(currentItem);
        itemForEdit = currentItem;
    }
    public LiveData<Item> getCurrentItem() { return currentItemLive; }
    public Item getItemForEdit() { return itemForEdit; }



    public void setNewItem(Item newItem) {newItemLiveData.setValue(newItem);}
    public LiveData<Item> getNewItem() { return newItemLiveData; }

    public List<ItemContributions> getListOfAll() {
        return listOfAll;
    }
    public void setListOfAll(List<ItemContributions> listOfAll) {
        this.listOfAll = listOfAll;
    }

    // NASTAVENIE DATABAZ
    public AppDatabase getDatabase() {
        return database;
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    public DatabaseAccess getDatabaseAccess() {
        return databaseAccess;
    }

    public void setDatabaseAccess(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }



}
