package com.calvindoaldisutanto.akbresto.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.calvindoaldisutanto.akbresto.models.Cart;
import com.calvindoaldisutanto.akbresto.databinding.CardviewCartBinding;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import static com.calvindoaldisutanto.akbresto.API.ApiClient.BASE_URL_PUBLIC;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private int id;
    private List<Cart> cart;
    private Context mContext;
    private int tambah;

    public CartAdapter(Context context, List<Cart> mData) {
        this.mContext = context;
        this.cart = mData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CardviewCartBinding cardviewCartBinding = CardviewCartBinding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(cardviewCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {
        final Cart data = cart.get(position);
        holder.cardviewCartBinding.setCart(data);
        holder.cardviewCartBinding.executePendingBindings();

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        double harga = data.getHarga() * data.getKuantitas();
        String harga_string = kursIndonesia.format(harga);

        int dstok = 0;
        String url;
        url = BASE_URL_PUBLIC + "/" + data.getImage();


        holder.cardviewCartBinding.hargaCart.setText(harga_string);
        holder.cardviewCartBinding.kuantitas.setText(String.valueOf(data.getKuantitas()));
        

        Glide.with(mContext)
                .load(url)
                .apply(new RequestOptions().override(100, 150))
                .into(holder.cardviewCartBinding.imgMenu);
    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public final CardviewCartBinding cardviewCartBinding;

        Button btnMinus, btnPlus;
        TextView nama_menu,cost, stok;
        Button btnSubmit;
        ImageView gambar;
        int jml;
        String snama, salamat, sharga, slongitude, slatitude, sgambar, sid;
        LinearLayout mParent;

        public MyViewHolder(CardviewCartBinding cardviewCartBinding) {
            super(cardviewCartBinding.getRoot());
            this.cardviewCartBinding = cardviewCartBinding;

        }

        public void deleteCart(Cart cart){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    DatabaseClient.getInstance(mContext).getDatabase()
//                            .cartDAO().delete(cart);
//                }
//            }).start();
        }

        public void updateQty(int jml, String id_menu){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    DatabaseClient.getInstance(mContext).getDatabase()
//                            .cartDAO().updateJumlah(jml, id_menu);
//                }
//            }).start();
        }

        public void insertDetail(){

        }

        public void loadKost(String sid){
//            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//            Call<MenuResponse> call = apiInterface.getOneMenu(sid);
//
//            call.enqueue(new Callback<MenuResponse>() {
//                @Override
//                public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
////                    Log.i("TAG", "response : "+ response.body().getKost().get(0).getJumlah());
//                    int dstok = Integer.parseInt(response.body().getKost().get(0).getJumlah()) /
//                            Integer.parseInt(response.body().getKost().get(0).getServing_size());
//                    if (jml < dstok){
//                        jml++;
//                        updateQty(jml, sid);
//                    }else {
//                        Toast.makeText(mContext,"Stok tidak mencukupi", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<MenuResponse> call, Throwable t) {
//                    Toast.makeText(mContext,"Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }
}
