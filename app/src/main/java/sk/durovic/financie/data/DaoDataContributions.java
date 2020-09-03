package sk.durovic.financie.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sk.durovic.financie.controller.Item;

@Dao
public interface DaoDataContributions {
    @Query("SELECT * FROM contributionstoitem WHERE ItemIdOwner LIKE :owner")
    List<ContributionsToItem> getAllContributions(int owner);

    @Query("SELECT * FROM contributionstoitem WHERE ItemIdOwner LIKE :owner ORDER BY addDate DESC")
    LiveData<List<ContributionsToItem>> getAllContributionsLive(int owner);

    @Insert
    void insertContributions(ContributionsToItem daoItemContributions);

    @Delete
    void deleteContributions(ContributionsToItem daoItemContributions);

    @Update
    void updateContributions(ContributionsToItem daoItemContributions);
}
