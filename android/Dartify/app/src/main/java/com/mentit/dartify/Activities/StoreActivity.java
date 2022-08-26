package com.mentit.dartify.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.google.common.collect.ImmutableList;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.Tienda.PutPurchaseTask;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.List;

public class StoreActivity extends AppCompatActivity implements PutPurchaseTask.OnTaskCompleted {
    CarouselView c;
    int TOTAL_PAGES = 4;
    long userid;
    Purchase comprap;
    Button buttonCancelarCompra;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_store);

        c = findViewById(R.id.carouselView);
        c.setPageCount(TOTAL_PAGES);
        c.setViewListener(vListener);

        buttonCancelarCompra = findViewById(R.id.buttonCancelarCompra);
        buttonCancelarCompra.setOnClickListener(cancelarCompra);

        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(purchasesUpdatedListener)
                .build();

        connectToGooglePlayBilling();
    }

    ViewListener vListener = position -> {
        View v = getLayoutInflater().inflate(R.layout.storeitem_layout, null);

        String title = "";
        String text = "";

        LinearLayout ll = v.findViewById(R.id.storeitem_slide);
        TextView titleview = v.findViewById(R.id.storeitem_title);
        TextView textview = v.findViewById(R.id.storeitem_text);
        ImageView imgview = v.findViewById(R.id.storeitem_image);

        userid = SharedPreferenceUtils.getInstance(this).getLongValue(this.getString(R.string.user_id), 0);

        int img = 0;
        int col = 0;

        switch (position) {
            case 0: {
                title = "¡Justo ahora!";
                text = "Te mostramos a quienes están en a tu alcance vibracional. ¿Quién, justo ahora, se siente como tú?";
                img = R.drawable.store_justnow;
                col = R.color.store1;
                break;
            }
            case 1: {
                title = "Define tu Química";
                text = "Te mostraremos los potenciales matches compatibles. Conecta profundamente.";
                img = R.drawable.store_user;
                col = R.color.store2;
                break;
            }
            case 2: {
                title = "Sin anuncios";
                text = "Pasa el tiempo que quieras dentro de la aplicación sin molestos anuncios";
                img = R.drawable.store_cross;
                col = R.color.store3;
                break;
            }
            case 3: {
                title = "Guardar a tus match en tu lista de favoritos";
                text = "Guarda tus match favoritos sin preocuparte de que desaparezcan cada 24 horas";
                img = R.drawable.store_star;
                col = R.color.store4;
                break;
            }
        }

        imgview.setImageResource(img);
        titleview.setText(title);
        textview.setText(text);
        ll.setBackgroundResource(col);

        return v;
    };

    private BillingClient billingClient;

    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            // To be implemented in a later section.
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (Purchase p : purchases) {
                    if (p.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !p.isAcknowledged()) {
                        Log.d("onPurchasesUpdated", p.getPurchaseToken());
                        Log.d("onPurchasesUpdated", p.getOrderId());
                        Log.d("onPurchasesUpdated", p.getPurchaseTime() + "");

                        Log.d("onPurchasesUpdated", "");
                        String sku = "";
                        for (String s : p.getSkus()) {
                            sku += s;
                        }
                        comprap = p;
                        new PutPurchaseTask(StoreActivity.this, userid, p.getPurchaseToken(), p.getOrderId(), p.getPurchaseTime(), sku).execute("");
                    }
                }
            }
        }
    };

    private void connectToGooglePlayBilling() {
        billingClient.startConnection(
                new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(BillingResult billingResult) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            // The BillingClient is ready. You can query purchases here.
                            getSuscriptions();
                            Log.d("onBillingSetupFinished", "");
                        }
                    }

                    @Override
                    public void onBillingServiceDisconnected() {
                        connectToGooglePlayBilling();
                        Log.d("onBillingServiceDisco", "");
                    }
                });
    }

    private void getSuscriptions() {
        Activity activity = this;

        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("suscripcion_001")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("suscripcion_002")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("suscripcion_003")
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build());

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(params, (billingResult, list) -> {
            ProductDetails sk1 = list.get(0);
            ProductDetails sk2 = list.get(1);
            ProductDetails sk3 = list.get(2);

            TextView tvprecio1 = findViewById(R.id.tvprecio1);
            TextView tvprecio2 = findViewById(R.id.tvprecio2);
            TextView tvprecio3 = findViewById(R.id.tvprecio3);

            tvprecio1.setText(sk1.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice());
            tvprecio2.setText(sk2.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice());
            tvprecio3.setText(sk3.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice());

            Button btn1 = findViewById(R.id.buttonComprar1);
            Button btn2 = findViewById(R.id.buttonComprar2);
            Button btn3 = findViewById(R.id.buttonComprar3);

            btn1.setOnClickListener(v -> {
                String offerToken = sk1.getSubscriptionOfferDetails().get(0).getOfferToken();

                ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(sk1)
                                        .setOfferToken(offerToken)
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                billingClient.launchBillingFlow(activity, billingFlowParams);
            });

            btn2.setOnClickListener(v -> {
                String offerToken = sk2.getSubscriptionOfferDetails().get(0).getOfferToken();

                ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(sk2)
                                        .setOfferToken(offerToken)
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                billingClient.launchBillingFlow(activity, billingFlowParams);
            });

            btn3.setOnClickListener(v -> {
                String offerToken = sk3.getSubscriptionOfferDetails().get(0).getOfferToken();

                ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(sk3)
                                        .setOfferToken(offerToken)
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                billingClient.launchBillingFlow(activity, billingFlowParams);
            });

            /*
            sk1.getPrice();
            sk1.getTitle();
            sk1.getDescription();
            sk1.getSubscriptionPeriod();
            sk1.getPriceCurrencyCode();
            */

        });
    }

    @Override
    public void OnTaskCompletedPutCompra(Boolean valid) {
        Log.d("put purchase", valid + "");
        Activity act = this;

        if (valid) {
            AcknowledgePurchaseParams ackp = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(comprap.getPurchaseToken())
                    .build();

            billingClient.acknowledgePurchase(ackp,
                    new AcknowledgePurchaseResponseListener() {
                        @Override
                        public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                //Toasty.success(act, "ACK", Toast.LENGTH_LONG, true).show();
                            }
                        }
                    }
            );

            SharedPreferenceUtils.getInstance(context).setValue("membresia", 3);

            Intent i = new Intent(StoreActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    private View.OnClickListener cancelarCompra = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(StoreActivity.this, MainActivity.class);
            startActivity(i);
        }
    };
}
