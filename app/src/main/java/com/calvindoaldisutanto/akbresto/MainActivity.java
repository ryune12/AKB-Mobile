package com.calvindoaldisutanto.akbresto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.calvindoaldisutanto.akbresto.API.ApiClient;
import com.calvindoaldisutanto.akbresto.API.ApiInterface;
import com.calvindoaldisutanto.akbresto.API.Response.OrderResponse;
import com.calvindoaldisutanto.akbresto.Fragment.CartFragment;
import com.calvindoaldisutanto.akbresto.Fragment.HomeFragment;
import com.calvindoaldisutanto.akbresto.Fragment.SummaryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.calvindoaldisutanto.akbresto.Adapter.MainCourseAdapter.QR_CODE;
import static com.calvindoaldisutanto.akbresto.Adapter.MainCourseAdapter.SHARED_PREFS;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QR_SCAN = 101;
    public static final String ID_ORDER = "id_order";
    private int index;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected = new HomeFragment();

                    switch (item.getItemId()){
                        //memilih homeFragment
                        case R.id.action_home:
                            selected = new HomeFragment();
                            index = 0;
                            break;
//                        //memilih favFragment
                        case R.id.action_cart:
                            selected = new CartFragment();
                            index = 1;
                            break;
//                        //memilih profilFragment
                        case R.id.action_summary:
                            selected = new SummaryFragment();
                            index = 2;
                            break;
                    }
                    //mengubah fragment sesuai fragment yang di pilih
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                            selected).commit();
                    return true;
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set default layout dengan homeFragment

        if(savedInstanceState != null) {
            int currentTab = savedInstanceState.getInt("CurrentTab", 0);
            /* Set currently selected tab */
        }else {
            Fragment default_frag = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                    default_frag).commit();
        }
        //memanggil fungsi bottom navigation ke dalam layout
        BottomNavigationView bottomNavigation = findViewById(R.id.navigation_view);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.d("TAG", "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;
        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            String reservasi_id;
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d("TAG", "Have scan result in your app activity :" + result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                isOrderEmpty(jsonObject.getString("id"), jsonObject.getString("tanggal_reservasi"), jsonObject.getString("sesi"));

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Scan result");
                alertDialog.setMessage(result);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void isOrderEmpty(String id,  String tanggal, String sesi){
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");

        try {
            int sesi_now = 0;
            DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH-mm-ss");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime start = LocalDateTime.parse(df.format(now)+"T17:00:00");
            if (now.isBefore(start)) {
                sesi_now = 1;
            }else
                sesi_now = 2;
            if (tanggal.equals(df.format(now))){
                if (sesi_now == Integer.parseInt(sesi)){
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<OrderResponse> call = apiInterface.getOrder(id);

                    call.enqueue(new Callback<OrderResponse>() {
                        @Override
                        public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                            Log.i("TAG", "Mesej :" + response);
                            if(response.body().getOrder().size() == 0){
                                save(id);
                                Toast.makeText(MainActivity.this,"Order ID berhasil di bentuk", Toast.LENGTH_SHORT).show();
                            }else{
                                if (response.body().getOrder().get(0).getStatus().equals("Completed"))
                                    Toast.makeText(MainActivity.this,"Sesi Telah Berakhir, Silahkan membuat reservasi baru", Toast.LENGTH_SHORT).show();
                                else{
                                    Toast.makeText(MainActivity.this,"Order ID sudah terbentuk", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt(ID_ORDER, response.body().getOrder().get(0).id);
                                    editor.apply();
                                    editor.putString(QR_CODE, id);
                                    editor.apply();
                                }

                                Log.i("TAF", "status : " + (response.body().getOrder().get(0).getStatus().equals("Completed")));
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderResponse> call, Throwable t) {
                            Toast.makeText(MainActivity.this,"Kesalahan Jaringan : "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(MainActivity.this,"Sesi Reservasi tidak sesuai", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(MainActivity.this,"Tanggal Reservasi tidak sesuai", Toast.LENGTH_SHORT).show();
            }
        } catch (DateTimeException e) {
            Toast.makeText(MainActivity.this,"Invalid : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void save(String id){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OrderResponse> add = apiInterface.createOrder(id);

        add.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(ID_ORDER, response.body().getOrder().get(0).id);
                editor.apply();
                editor.putString(QR_CODE, id);
                editor.apply();
                Log.i("Quda : ", "ID_ORDER ," + sharedPreferences.getInt(ID_ORDER, 0)+", QRCODE: "+ sharedPreferences.getString(QR_CODE, ""));
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}