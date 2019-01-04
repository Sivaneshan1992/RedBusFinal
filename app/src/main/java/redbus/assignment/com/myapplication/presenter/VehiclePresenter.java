package redbus.assignment.com.myapplication.presenter;

import java.util.List;

import redbus.assignment.com.myapplication.VehicleInterface;
import redbus.assignment.com.myapplication.itemclass.InventoryModel;

/**
 * Created by guna on 04-01-2019.
 */

public class VehiclePresenter implements VehicleInterface.presenter, VehicleInterface.GetNoticeIntractor.OnFinishedListener {

    private VehicleInterface.MainView mainView;
    private VehicleInterface.GetNoticeIntractor getNoticeIntractor;

    public VehiclePresenter(VehicleInterface.MainView mainView, VehicleInterface.GetNoticeIntractor getNoticeIntractor) {
        this.mainView = mainView;
        this.getNoticeIntractor = getNoticeIntractor;
    }

    @Override
    public void onDestroy() {

        mainView = null;

    }


    @Override
    public void requestDataFromServer() {
        getNoticeIntractor.getNoticeArrayList(this);
    }


    @Override
    public void onProgressStart() {
        if (mainView != null) {
            mainView.showProgress();
        }
    }

    @Override
    public void onFinished(List<InventoryModel.Inventory> noticeArrayList, InventoryModel inventoryModel) {
        if (mainView != null) {
            mainView.setDataToRecyclerView(noticeArrayList, inventoryModel);
            mainView.hideProgress();
        }
    }


    @Override
    public void onFailure(String t) {
        if (mainView != null) {
            mainView.onResponseFailure(t);
            mainView.hideProgress();
        }
    }
}
