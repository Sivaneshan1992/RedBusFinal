package redbus.assignment.com.myapplication.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import redbus.assignment.com.myapplication.R;
import redbus.assignment.com.myapplication.Utils.SortDuration;
import redbus.assignment.com.myapplication.Utils.SortFare;
import redbus.assignment.com.myapplication.VehicleInterface;
import redbus.assignment.com.myapplication.adapter.VehicleListAdapter;
import redbus.assignment.com.myapplication.itemclass.InventoryModel;
import redbus.assignment.com.myapplication.presenter.VehiclePresenter;

public class MainActivity extends AppCompatActivity implements VehicleInterface.MainView {


    private ProgressBar progressBar;
    private VehicleInterface.presenter presenter;

    private RecyclerView recyclerView;
    private VehicleListAdapter vehicleListAdapter;
    private List<InventoryModel.Inventory> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadToolbar();
        initializeUIComponents();
        initProgressBar();

        /*Fetching details from server*/
        if (isOnline(this)) {
            presenter = new VehiclePresenter(this, new GetVehicleListImpl());
            presenter.requestDataFromServer();
        } else
            Toast.makeText(MainActivity.this, getResources().getString(R.string.no_network), Toast.LENGTH_LONG).show();

    }

    private void loadToolbar() {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(getResources().getString(R.string.toolbar_title));

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initializeUIComponents() {

        recyclerView = findViewById(R.id.listview_vehicle);

        /*Initially it wont visible, visible only after data loaded*/
        recyclerView.setVisibility(View.GONE);
    }

    private void initProgressBar() {
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        this.addContentView(relativeLayout, params);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {


            case R.id.action_filter:

                if (isOnline(this)) {
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View dialogView = inflater.inflate(R.layout.sort_alert_dialog, null);
                    dialogBuilder.setView(dialogView);


                    final AlertDialog dialog = dialogBuilder.create();

                    final RadioGroup rgrp = dialogView.findViewById(R.id.rgrp_sort);
                    Button btn_ok = dialogView.findViewById(R.id.btn_ok);
                    Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);

                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            int rgrpid = rgrp.getCheckedRadioButtonId();

                            RadioButton radioButton = dialogView.findViewById(rgrpid);

                        /*Sorting based on selected value*/

                            if (Integer.parseInt(radioButton.getTag().toString()) == 1)
                                Collections.sort(datalist, new SortFare());
                            else
                                Collections.sort(datalist, new SortDuration());

                            if (vehicleListAdapter != null)
                                vehicleListAdapter.refreshdata(datalist);

                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                } else
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.no_network), Toast.LENGTH_LONG).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setDataToRecyclerView(List<InventoryModel.Inventory> datalist, InventoryModel inventoryModel) {

        recyclerView.setVisibility(View.VISIBLE);
        this.datalist = datalist;
        vehicleListAdapter = new VehicleListAdapter(MainActivity.this, datalist, inventoryModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(vehicleListAdapter);
    }


    @Override
    public void onResponseFailure(String throwable) {
        Toast.makeText(MainActivity.this,
                throwable,
                Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
