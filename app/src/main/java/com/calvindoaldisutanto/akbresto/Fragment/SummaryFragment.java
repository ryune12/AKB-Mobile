package com.calvindoaldisutanto.akbresto.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.calvindoaldisutanto.akbresto.API.ApiClient;
import com.calvindoaldisutanto.akbresto.API.ApiInterface;
import com.calvindoaldisutanto.akbresto.API.ClientAccess.DetailClientAccess;
import com.calvindoaldisutanto.akbresto.API.ClientAccess.MenuClientAccess;
import com.calvindoaldisutanto.akbresto.API.Response.DetailResponse;
import com.calvindoaldisutanto.akbresto.API.Response.OrderResponse;
import com.calvindoaldisutanto.akbresto.Adapter.CartAdapter;
import com.calvindoaldisutanto.akbresto.Adapter.MainCourseAdapter;
import com.calvindoaldisutanto.akbresto.Adapter.SummaryAdapter;
import com.calvindoaldisutanto.akbresto.R;
import com.calvindoaldisutanto.akbresto.databinding.CardviewCartBinding;
import com.calvindoaldisutanto.akbresto.databinding.FragmentHomeBinding;
import com.calvindoaldisutanto.akbresto.databinding.FragmentSummaryBinding;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.calvindoaldisutanto.akbresto.Adapter.MainCourseAdapter.QR_CODE;
import static com.calvindoaldisutanto.akbresto.Adapter.MainCourseAdapter.SHARED_PREFS;
import static com.calvindoaldisutanto.akbresto.MainActivity.ID_ORDER;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SummaryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int id_order;
    private RecyclerView myRecyclerView;
    private SummaryAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
//    FragmentSummaryBinding fragmentSummaryBinding;

    public SummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SummaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SummaryFragment newInstance(String param1, String param2) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_summary, container, false);

        FragmentSummaryBinding fragmentSummaryBinding = FragmentSummaryBinding.inflate(getLayoutInflater());
        myRecyclerView = (RecyclerView) v.findViewById(R.id.rv_summary);
        getSummary(fragmentSummaryBinding);

        Button btnEnd = v.findViewById(R.id.btnEnd);
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endPesanan();
            }
        });

        return v;
    }

    public void endPesanan(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String id_reservasi = sharedPreferences.getString(QR_CODE, "");
//        Log.i("Quda : ", "QRCODE : "+ id_reservasi);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OrderResponse> call = apiInterface.end(id_reservasi);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.clear().apply();
                Toast.makeText(getContext(), "Terima Kasih! Silahkan menuju kasir untuk pembayaran", Toast.LENGTH_LONG);
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {

            }
        });
    }

    public void getSummary(FragmentSummaryBinding fragmentSummaryBinding){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id_order = sharedPreferences.getInt(ID_ORDER, 0);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DetailResponse> call = apiInterface.getDetail(String.valueOf(id_order));
        call.enqueue(new Callback<DetailResponse>() {
            @Override
            public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                generateSummary(response.body().getDetail());
                TextView tv_subtotal, tv_pajak, tv_sevice, tv_total;
                double subtotal = 0;
                for (DetailClientAccess data: response.body().getDetail()
                     ) {
                    subtotal += Double.parseDouble(data.getSubtotal());
                }
                DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                formatRp.setCurrencySymbol("Rp. ");
                formatRp.setMonetaryDecimalSeparator(',');
                formatRp.setGroupingSeparator('.');

                kursIndonesia.setDecimalFormatSymbols(formatRp);
                String subtotal_format = kursIndonesia.format(subtotal);
                String pajak_format = kursIndonesia.format(subtotal*0.1);
                String service_format = kursIndonesia.format(subtotal*0.05);
                String total_format = kursIndonesia.format(subtotal + (subtotal * 0.1) + (subtotal * 0.05));
                tv_subtotal = getActivity().findViewById(R.id.subtotal);
                tv_pajak = getActivity().findViewById(R.id.pajak);
                tv_sevice = getActivity().findViewById(R.id.service);
                tv_total = getActivity().findViewById(R.id.total);

                tv_subtotal.setText(subtotal_format);
                tv_pajak.setText(pajak_format);
                tv_sevice.setText(service_format);
                tv_total.setText(total_format);
            }

            @Override
            public void onFailure(Call<DetailResponse> call, Throwable t) {

            }
        });
    }
    public void generateSummary(List<DetailClientAccess> menuList){
        adapter = new SummaryAdapter(getContext(), menuList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setAdapter(adapter);
    }
}