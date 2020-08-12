package sk.durovic.financie.controller;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int ItemId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo (name = "description")
    public String description;

    @ColumnInfo (name = "overall")
    public double overall;

    @ColumnInfo (name = "inBank")
    public double inBank;

    @ColumnInfo (name = "savingFrequency")
    public int savingFrequency;

    @ColumnInfo (name = "endDate")
    public String endDate;


    public Item(){}

    public Item(String name, String info, double overall, double inBank, int savingFrequency, Date endDate) {
        this.name = name;
        this.description = info;
        this.overall = overall;
        this.inBank = inBank;
        this.endDate = String.valueOf(endDate);
        this.savingFrequency = savingFrequency;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public void setContribution(double value, Date setDate){
    }

    public int getIdItem() {
        return ItemId;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return description;
    }

    public double getOverall() {
        return overall;
    }

    public double getInBank() {
        return inBank;
    }

    public int getSavingFrequency() {
        return savingFrequency;
    }

    public String getEndDate() {
        return endDate;
    }
}
