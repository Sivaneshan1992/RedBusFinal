package redbus.assignment.com.myapplication.activity;

import redbus.assignment.com.myapplication.VehicleInterface;
import redbus.assignment.com.myapplication.itemclass.InventoryModel;
import redbus.assignment.com.myapplication.retrofit.APIClient;
import redbus.assignment.com.myapplication.retrofit.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by guna on 04-01-2019.
 */

public class GetVehicleListImpl implements VehicleInterface.GetNoticeIntractor {

    @Override
    public void getNoticeArrayList(final OnFinishedListener onFinishedListener) {

        onFinishedListener.onProgressStart();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<InventoryModel> call1 = apiInterface.getVehicleDetails();
        call1.enqueue(new Callback<InventoryModel>() {
            @Override
            public void onResponse(Call<InventoryModel> call, Response<InventoryModel> response) {

                if (response.body() != null) {

                    try {

                        InventoryModel inventoryModel = response.body();

                        if (inventoryModel != null && inventoryModel.getInventory() != null) {

                            onFinishedListener.onFinished(inventoryModel.getInventory(), inventoryModel);
                        } else {
                            onFinishedListener.onFailure("Server error");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<InventoryModel> call, Throwable t) {

                call.cancel();
                onFinishedListener.onFailure(t.getMessage());

            }
        });

    }

}