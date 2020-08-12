package sk.durovic.financie.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import sk.durovic.financie.controller.Item;

public class ItemContributions {
    @Embedded public Item daoItem;
    @Relation(
            parentColumn = "ItemId",
            entityColumn = "ItemIdOwner"
    )
    public List<ContributionsToItem> daoItemContributions;
}
