package redbus.assignment.com.myapplication.Utils;

import java.util.Comparator;

import redbus.assignment.com.myapplication.itemclass.InventoryModel;

/**
 * Created by guna on 03-01-2019.
 */

public class SortDuration implements Comparator<InventoryModel.Inventory> {

    @Override
    public int compare(InventoryModel.Inventory o1, InventoryModel.Inventory o2) {

        if(o1.getDuration() >  o2.getDuration() )
            return 1;
        else if(o1.getDuration()  <  o2.getDuration())
            return  -1;

        return 0;
    }
}
