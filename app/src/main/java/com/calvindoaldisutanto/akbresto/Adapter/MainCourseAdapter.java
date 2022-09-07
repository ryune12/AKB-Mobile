package com.calvindoaldisutanto.akbresto.Adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.calvindoaldisutanto.akbresto.API.ApiClient;
import com.calvindoaldisutanto.akbresto.API.ApiInterface;
import com.calvindoaldisutanto.akbresto.API.ClientAccess.MenuClientAccess;
import com.calvindoaldisutanto.akbresto.API.Response.MenuResponse;
import com.calvindoaldisutanto.akbresto.Fragment.MenuFragment;
import com.calvindoaldisutanto.akbresto.MainActivity;
import com.calvindoaldisutanto.akbresto.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.calvindoaldisutanto.akbresto.API.ApiClient.BASE_URL_PUBLIC;

public class MainCourseAdapter extends RecyclerView.Adapter<MainCourseAdapter.MyViewHolder> {
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final String TAG = "RecyclerViewAdapter";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ID_MENU = "id_menu";
    public static final String QR_CODE = "qr_code";


    List<MenuClientAccess> mData;
    List<MenuClientAccess> filteredDataList;
    private Context mContext;
    private String qr_code;


    public MainCourseAdapter(Context context, List<MenuClientAccess> mData) {
        this.mContext = context;
        this.mData = mData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_maincourse, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final MenuClientAccess menu = mData.get(position);

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        double harga_sewa = Double.parseDouble(menu.getHarga());
        String harga = kursIndonesia.format(harga_sewa);
        int dstok;
        try{
            dstok = Integer.parseInt(menu.getJumlah()) / Integer.parseInt(menu.getServing_size());
        }catch (Exception e){
            dstok = 0;
        }
        String stok = Integer.toString(dstok);
        String url;
        url = BASE_URL_PUBLIC + "/" + menu.getImage();
        Log.i("TAG", "GAMBAR : "+ menu.getImage());
        holder.nama_menu.setText(menu.getNama_menu());
        holder.harga.setText(harga);
        holder.stok.setText(stok);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                qr_code = sharedPreferences.getString(QR_CODE, "");
                if (qr_code == ""){
                    AlertDialog alertDialog = new AlertDialog.Builder((MainActivity)mContext).create();
                    alertDialog.setTitle("Anda Belum melakukan Scan QR Code");
                    alertDialog.setMessage("Ingin Scan QR code?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Dexter.withContext(mContext)
                                    .withPermission(Manifest.permission.CAMERA)
                                    .withListener(new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                            Intent i = new Intent(mContext, QrCodeActivity.class);
                                            ((MainActivity)mContext).startActivityForResult( i,REQUEST_CODE_QR_SCAN);
                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                            permissionDeniedResponse.getRequestedPermission();
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                        }
                                    }).check();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }else {
                    save_data(menu.getId());
                    ((FragmentActivity) view.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new MenuFragment()).addToBackStack("tag").commit();
                }
//
            }
        });

        Glide.with(mContext)
                .load(url)
                .apply(new RequestOptions().override(100, 150))
                .into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nama_menu,stok,harga;
        ImageView gambar;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_menu = itemView.findViewById(R.id.nama_menu);
            stok = itemView.findViewById(R.id.stok);
            harga = itemView.findViewById(R.id.harga);
            gambar = itemView.findViewById(R.id.imgMenu);

            cardView = itemView.findViewById(R.id.item_menu);
        }
    }

    public void save_data(String id){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ID_MENU, id);
        editor.apply();
    }

}
