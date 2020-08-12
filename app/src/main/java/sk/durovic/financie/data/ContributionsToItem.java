package sk.durovic.financie.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class ContributionsToItem {
    @PrimaryKey (autoGenerate = true)
    public int ContributionsId;

    @ColumnInfo(name = "ItemIdOwner")
    public int ItemIdOwner;

    @ColumnInfo (name = "contribution")
    public double contribution;

    @ColumnInfo (name = "addDate")
    public String addDate;

    public ContributionsToItem(double newcontribution, String setDate, int ItemOwner) {
        contribution = newcontribution;
        addDate = setDate;
        ItemIdOwner = ItemOwner;
    }

    public ContributionsToItem() {
    }

    public int getContributionsId() {
        return ContributionsId;
    }

    public int getItemIdOwnder() {
        return this.ItemIdOwner;
    }
}
