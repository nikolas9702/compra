package com.example.desarrollo.democompra2;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.android.vending.billing.IInAppBillingService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private BillingClient mBillingClient;
    IInAppBillingService mService;
    ServiceConnection mServiceConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
                Log.e("mService : " , String.valueOf(mService));
                //run();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
                Log.e(" error mService : " , String.valueOf(mService));

            }
        };

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        Button btnProducto = findViewById(R.id.btnProducto);
        Button btnSuscripcion = findViewById(R.id.btnSuscripcion);

        btnProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product();
            }
        });

        btnSuscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suscripcion();
            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }


    public void suscripcion () {

        Log.e("err", String.valueOf(mService));

        ArrayList<String> skuList = new ArrayList<>();
        skuList.add("suscripcion_mes");
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        try {
            Bundle skuDetails = mService.getSkuDetails(3,
                    getPackageName(), "subs", querySkus);
            Log.e("run ", new Gson().toJson(skuDetails));
            int response = skuDetails.getInt("RESPONSE_CODE");
            Log.e("response ", new Gson().toJson(response));
            if (response == 0) {
                ArrayList<String> responseList
                        = skuDetails.getStringArrayList("DETAILS_LIST");
                Log.e("responseList ", new Gson().toJson(responseList));


                for (String thisResponse : responseList) {
                    JSONObject object = new JSONObject(thisResponse);
                    Log.e("object ", new Gson().toJson(object));
                    Log.e("sku ", object.getString("productId"));
                    Log.e("price ", object.getString("price"));

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Suscripcion ");
                    alertDialog.setMessage("sku :"+ object.getString("productId") + " price : "+object.getString("price"));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();


                    /*String sku = object.getString("productId");
                    String price = object.getString("price");

                    Toast.makeText(MainActivity.this,"sku : "+sku+ " price : "+price,Toast.LENGTH_LONG);*/
                }
            }
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this," No sjodimos ",Toast.LENGTH_LONG);
        }

        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                    "suscripcion_mes", "subs", "Hello world");
            Log.e("buyIntentBundle", String.valueOf(buyIntentBundle));
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            startIntentSenderForResult(pendingIntent.getIntentSender(),
                    1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(0));

        }
        catch (Exception e) {

            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG);

            Log.e("Error",e.getMessage());
        }
    }

    public void product () {

        Log.e("err", String.valueOf(mService));

        ArrayList<String> skuList = new ArrayList<>();
        skuList.add("suscripcion_mes_2");
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        try {
            Bundle skuDetails = mService.getSkuDetails(3,
                    getPackageName(), "inapp", querySkus);
            Log.e("run ", new Gson().toJson(skuDetails));
            int response = skuDetails.getInt("RESPONSE_CODE");
            Log.e("response ", new Gson().toJson(response));
            if (response == 0) {
                ArrayList<String> responseList
                        = skuDetails.getStringArrayList("DETAILS_LIST");
                Log.e("responseList ", new Gson().toJson(responseList));


                for (String thisResponse : responseList) {
                    JSONObject object = new JSONObject(thisResponse);
                    Log.e("object ", new Gson().toJson(object));
                    Log.e("sku ", object.getString("productId"));
                    Log.e("price ", object.getString("price"));

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Producto ");
                    alertDialog.setMessage("sku :"+ object.getString("productId") + " price : "+object.getString("price"));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();


                    /*String sku = object.getString("productId");
                    String price = object.getString("price");

                    Toast.makeText(MainActivity.this,"sku : "+sku+ " price : "+price,Toast.LENGTH_LONG);*/
                }
            }
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this," No sjodimos ",Toast.LENGTH_LONG);
        }

        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                    "suscripcion_mes_2", "inapp", "Hello world2");
            Log.e("buyIntentBundle", String.valueOf(buyIntentBundle));
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            startIntentSenderForResult(pendingIntent.getIntentSender(),
                    1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(0));

        }
        catch (Exception e) {

            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG);

            Log.e("Error",e.getMessage());
        }
    }


}
