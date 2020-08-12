package sk.durovic.financie.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import sk.durovic.financie.controller.Item;

@Database(entities = {Item.class, ContributionsToItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoDataItem daoItem();
    public abstract DaoDataContributions daoContributions();
}
