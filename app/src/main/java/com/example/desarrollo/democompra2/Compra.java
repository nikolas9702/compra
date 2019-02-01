package com.example.desarrollo.democompra2;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.android.vending.billing.IInAppBillingService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Compra {
    IInAppBillingService mService;
    Context context;
    public Compra (final Context context) {
        this.context = context;
        load ();
    }

    public void load() {
        Log.e("Error dilan" , " dilan 0 ");

        BillingClient mBillingClient = BillingClient.newBuilder(context)
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
                        Log.e("Error dilan" , " dilan 1 ");
                        if (responseCode == BillingClient.BillingResponse.OK
                                && purchases != null) {
                            for (Purchase purchase : purchases) {
                                // handlePurchase(purchase);
                                Log.e("Error dilan" , "dilan 1.1 ");
                            }
                        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
                            // Handle an error caused by a user canceling the purchase flow.
                            Log.e("Error dilan" , "dilan 2 ");
                        } else {
                            // Handle any other error codes.
                            Log.e("Error dilan" , "dilan 3 ");

                        }
                    }
                })
                .build();



        List<String> skuList = new ArrayList<> ();
        skuList.add("premiumUpgrade");
        skuList.add("gas");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        mBillingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                Log.e("Error dilan" , "dilan 4 ");
                Log.e("Error dilan" , "dilan 4.1 "+new Gson().toJson(skuDetailsList));
            }
        });

/*
        Log.e("Error dilan" , "dilan inicia ");
        ServiceConnection mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
                Log.e("Error dilan" , "dilan inicia 1");
                Toast.makeText(context, "  No se conecto esta vuelta ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
                Log.e("Error dilan" , "dilan inicia 2");
                Toast.makeText(context, " Se conecto bien ", Toast.LENGTH_SHORT).show();
                procces();
            }

            @Override
            public void onBindingDied(ComponentName name) {
                Log.e("Error dilan" , "dilan inicia 3");
                Toast.makeText(context, "  Esta muerto ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNullBinding(ComponentName name) {
                Log.e("Error dilan" , "dilan inicia 4");
                Toast.makeText(context, "  se devolvio un NULL ", Toast.LENGTH_SHORT).show();
            }

        };*/
    }

    public void procces (){
        Log.e("Error dilan" , "dilan inicia 2.1");
        List<String> skuList = new ArrayList<>();
        skuList.add("suscripcion_mes");
        skuList.add("suscripcion_mes_2");
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", (ArrayList<String>) skuList);

        Bundle skuDetails = null;
        try {
            skuDetails = mService.getSkuDetails(3, "com.dilan.desarrollo.democompra2", "inapp", querySkus);
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Alert detail ");
            alertDialog.setMessage(" se murio " + new Gson().toJson(skuDetails));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Alert error ");
            alertDialog.setMessage(" se murio " + e.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }
}
