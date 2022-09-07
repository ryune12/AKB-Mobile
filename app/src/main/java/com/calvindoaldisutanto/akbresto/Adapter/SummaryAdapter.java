package com.calvindoaldisutanto.akbresto.Adapter;

import android.content.Context;
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
import com.calvindoaldisutanto.akbresto.API.ClientAccess.DetailClientAccess;
import com.calvindoaldisutanto.akbresto.databinding.CardviewCartBinding;
import com.calvindoaldisutanto.akbresto.databinding.CartviewSummaryBinding;
import com.calvindoaldisutanto.akbresto.models.Cart;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import static com.calvindoaldisutanto.akbresto.API.ApiClient.BASE_URL_PUBLIC;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MyViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private int id;
    private List<DetailClientAccess> detail;
    private Context mContext;
    private int tambah;

    public SummaryAdapter(Context context, List<DetailClientAccess> mData) {
        this.mContext = context;
        this.detail = mData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CartviewSummaryBinding cartviewSummaryBinding = CartviewSummaryBinding.inflate(layoutInflater, parent, false);
        return new SummaryAdapter.MyViewHolder(cartviewSummaryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapter.MyViewHolder holder, int position) {
        final DetailClientAccess data = detail.get(position);
        int kuantitas = Integer.parseInt(data.getKuantitas());
        double subtotal = Double.parseDouble(data.getSubtotal());

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        double harga = subtotal/kuantitas;
        String harga_string = kursIndonesia.format(harga);

        String subtotal_string = kursIndonesia.format(subtotal);
        holder.cartviewSummaryBinding.executePendingBindings();
        holder.cartviewSummaryBinding.hargaCart.setText(harga_string);
        holder.cartviewSummaryBinding.kuantitas.setText('x'+data.getKuantitas());
        holder.cartviewSummaryBinding.subtotal.setText(subtotal_string);
        holder.cartviewSummaryBinding.namaMenuCart.setText(data.getNama_menu());

    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public final CartviewSummaryBinding cartviewSummaryBinding;

        Button btnMinus, btnPlus;
        TextView nama_menu,cost, stok;
        Button btnSubmit;
        ImageView gambar;
        int jml;
        String snama, salamat, sharga, slongitude, slatitude, sgambar, sid;
        LinearLayout mParent;

        public MyViewHolder(CartviewSummaryBinding cartviewSummaryBinding) {
            super(cartviewSummaryBinding.getRoot());
            this.cartviewSummaryBinding = cartviewSummaryBinding;

        }

    }
}
