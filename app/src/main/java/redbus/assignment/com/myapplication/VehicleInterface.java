package redbus.assignment.com.myapplication;

import java.util.List;

import redbus.assignment.com.myapplication.itemclass.InventoryModel;

/**
 * Created by guna on 04-01-2019.
 */

public interface VehicleInterface {

    /**
     * Call when user interact with the view and other when view OnDestroy()
     */
    interface presenter {

        void onDestroy();

        void requestDataFromServer();


    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setDataToRecyclerView and onResponseFailure is fetched from the GetNoticeInteractorImpl class
     **/
    interface MainView {

        void showProgress();

        void hideProgress();

        void setDataToRecyclerView(List<InventoryModel.Inventory> datalist, InventoryModel inventoryModel);


        void onResponseFailure(String throwable);

    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface GetNoticeIntractor {

        interface OnFinishedListener {

            void onProgressStart();

            void onFinished(List<InventoryModel.Inventory> datalist, InventoryModel inventoryModel);

            void onFailure(String t);
        }

        void getNoticeArrayList(OnFinishedListener onFinishedListener);

    }
}
