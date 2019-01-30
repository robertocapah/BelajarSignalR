package net.robertocapah.belajarsignalr;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.robertocapah.belajarsignalr.BL.SignalRBL;
import net.robertocapah.belajarsignalr.addson.ConnectivityReceiver;
import net.robertocapah.belajarsignalr.services.WMSMobileService;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static junit.framework.Assert.assertEquals;

public class MainActivity extends clsMainActivity implements View.OnClickListener, View.OnKeyListener, ConnectivityReceiver.ConnectivityReceiverListener, WMSMobileService.mHubConnectionSevice, WMSMobileService.mHubConnectionSlow {
    private ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    ConnectivityManager conMan;
    Button btnGas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        btnGas = (Button) findViewById(R.id.btnAction);
        Intent service = new Intent(MainActivity.this, WMSMobileService.class);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading... Please Wait");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        startService(service);

        btnGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendRequestLogin();
                requestCheckVersion("AND.2018.002");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setHubConnection(this);
        HubConnectionSlow(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        boolean checkPermission = checkPermission();
    }
    public void setHubConnection(WMSMobileService.mHubConnectionSevice hubConnection) {
        WMSMobileService.mHubConnectionSevice = hubConnection;
    }

    public void HubConnectionSlow(WMSMobileService.mHubConnectionSlow mHubConnectionSlow) {
        WMSMobileService.mHubConnectionSlow = mHubConnectionSlow;
    }
    private void requestCheckVersion(String versionName) {
        boolean status;
        if (versionName != null) {
            progressDialog.show();
            status = new WMSMobileService().getDataLastVersion(versionName);
            if (!status) {
                new clsMainActivity().checkConnection(coordinatorLayout, conMan);
                progressDialog.dismiss();
//                btnCheckVersion.setVisibility(View.VISIBLE);
//                llContentWarning.setVisibility(View.VISIBLE);
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void checkConnection(CoordinatorLayout coordinatorLayout, ConnectivityManager connectivityManager){

        boolean status = false;
        //mobile
        NetworkInfo.State mobile = connectivityManager.getNetworkInfo(0).getState();
        //wifi
        NetworkInfo.State wifi = connectivityManager.getNetworkInfo(1).getState();

        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING)
        {
//            snackbarWithNoAction(coordinatorLayout, "connecting...", status, "");
            AsyncTestConnection task = new AsyncTestConnection(coordinatorLayout);
            task.execute();
//            status = new SignalRBL().buildingConnection();
//            if(!status){
//                snackbarWithActionRetry(coordinatorLayout,"Connecting Failed", status, "RETRY");
//            } else {
//                snackbarWithNoAction(coordinatorLayout,"Connected", status, "");
//            }
        }
        else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING)
        {
//            snackbarWithNoAction(coordinatorLayout, "connecting...", status, "");
            AsyncTestConnection task = new AsyncTestConnection(coordinatorLayout);
            task.execute();
//            status = new SignalRBL().buildingConnection();
//            if(!status){
//                snackbarWithActionRetry(coordinatorLayout,"Connecting Failed", status, "RETRY");
//            } else {
//                snackbarProgress(coordinatorLayout,"Connected", status, "");
//            }
        }
        else {
            snackbarWithNoAction(coordinatorLayout,"No Connectivity", status, "");
        }
    }


    private void sendRequestLogin() {
//        String nameRole = selectedRole;
        boolean status = false;
        String txtPass = "WQQWEQWE";
        if (txtPass.length() > 0) {
            progressDialog.show();
//            new clsMainActivity().timerDelayRemoveDialog(time, progressDialog);
            try {
                status = new WMSMobileService().login("", "","","","");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!status) {
                new clsMainActivity().checkConnection(coordinatorLayout, conMan);
                progressDialog.dismiss();
                boolean report = new SignalRBL().buildingConnection();
//                etTxtPass.requestFocus();
//                Toast.makeText(Login.this, String.valueOf(report), Toast.LENGTH_SHORT).show();
            }
        } else {
//            etTxtPass.requestFocus();
//            new clsMainActivity().showSnackbar(coordinatorLayout, "Password cannot empty", false, "");
        }
    }
    private boolean checkPermission() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("You need to allow access. . .");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && !ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        &&!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        &&!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.CAMERA)){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    dialog.dismiss();

                }
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
    int a = 1;
    }

    @Override
    public void onReceiveMessageHub(JSONObject jsonObject) {
    int a= 1;
    }

    @Override
    public void onConnectionSlow(boolean info) {
    int a=2 ;
    }
}
