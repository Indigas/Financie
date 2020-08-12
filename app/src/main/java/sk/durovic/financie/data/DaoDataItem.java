package sk.durovic.financie.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import sk.durovic.financie.controller.Item;

@Dao
public interface DaoDataItem {
    @Transaction
    @Query("SELECT * FROM item")
    LiveData<List<ItemContributions>> getAll();

    @Insert
    void insertItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Update
    void updateItem(Item item);


}