package com.calvindoaldisutanto.akbresto.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.calvindoaldisutanto.akbresto.API.ApiClient;
import com.calvindoaldisutanto.akbresto.API.ApiInterface;
import com.calvindoaldisutanto.akbresto.API.ClientAccess.MenuClientAccess;
import com.calvindoaldisutanto.akbresto.API.Response.MenuResponse;
import com.calvindoaldisutanto.akbresto.Database.DatabaseClient;
import com.calvindoaldisutanto.akbresto.models.Cart;
import com.calvindoaldisutanto.akbresto.R;
import com.calvindoaldisutanto.akbresto.databinding.FragmentMenuBinding;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.calvindoaldisutanto.akbresto.API.ApiClient.BASE_URL_PUBLIC;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    private FragmentMenuBinding fragmentMenuBinding;
    private View view;

    // Data from API;
    private List<MenuClientAccess> menuClientAccess;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ID_MENU = "id_menu";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String id_menu;

    String sid_menu, snama_menu, sharga, skategori, simage, sjumlah, sserving_size, sdeskripsi;

    private int tambah = 0, kurang = 0;
    int dstok;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
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
        // Assign variable data binding
        fragmentMenuBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_menu, container,
                false);
        view = fragmentMenuBinding.getRoot();

        // Load SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id_menu = sharedPreferences.getString(ID_MENU, "id_menu");

        fragmentMenuBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });

//        // Load Data menu
        getMenu(id_menu);

        // Inflate the layout for this fragment
        return view;
    }



    public void getMenu(String id_menu){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MenuResponse> call = apiService.getMenuById(id_menu);

        call.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                menuClientAccess = response.body().getMenu();
//                format Currency
                DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                formatRp.setCurrencySymbol("Rp. ");
                formatRp.setMonetaryDecimalSeparator(',');
                formatRp.setGroupingSeparator('.');

                kursIndonesia.setDecimalFormatSymbols(formatRp);
                double harga_menu = Double.parseDouble(menuClientAccess.get(0).getHarga());
                String harga = kursIndonesia.format(harga_menu);

                fragmentMenuBinding.namaMenuModel.setText(menuClientAccess.get(0).getNama_menu());
                fragmentMenuBinding.hargaMenuModel.setText(harga);
                fragmentMenuBinding.deskripsiMenuModel.setText(menuClientAccess.get(0).getDeskripsi());
                fragmentMenuBinding.kuantitasMenuModel.setText("1");
                tambah = 1;

                fragmentMenuBinding.subtotalMenuModel.setText(kursIndonesia.format(tambah * harga_menu));
                String url;
                url = BASE_URL_PUBLIC + "/" + menuClientAccess.get(0).getImage();
                Glide.with(getContext())
                        .load(url)
                        .apply(new RequestOptions().override(100, 150))
                        .into(fragmentMenuBinding.imageMenuModel);

                try{
                    dstok = Integer.parseInt(menuClientAccess.get(0).getJumlah()) / Integer.parseInt(menuClientAccess.get(0).getServing_size());
                }catch (Exception e){
                    dstok = 0;
                }
                fragmentMenuBinding.btnAddModel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tambah < dstok){
                            tambah++;
                            fragmentMenuBinding.kuantitasMenuModel.setText(String.valueOf(tambah));
                            fragmentMenuBinding.subtotalMenuModel.setText(kursIndonesia.format(tambah * harga_menu));
                        }else
                            Toast.makeText(getContext(), "Stok tidak cukup", Toast.LENGTH_SHORT).show();
                    }
                });

                fragmentMenuBinding.btnRemoveModel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tambah > 1){
                            tambah--;
                            fragmentMenuBinding.kuantitasMenuModel.setText(String.valueOf(tambah));
                            fragmentMenuBinding.subtotalMenuModel.setText(kursIndonesia.format(tambah * harga_menu));
                        }
                    }
                });
                if (dstok == 0){
                    fragmentMenuBinding.buttonAddToCart.setVisibility(View.INVISIBLE);
                    fragmentMenuBinding.btnAddModel.setVisibility(View.INVISIBLE);
                    fragmentMenuBinding.btnRemoveModel.setVisibility(View.INVISIBLE);
                    fragmentMenuBinding.subtotalMenuModel.setVisibility(View.INVISIBLE);
                    fragmentMenuBinding.kuantitasMenuModel.setVisibility(View.INVISIBLE);
                }else {
                    fragmentMenuBinding.chip.setVisibility(View.INVISIBLE);
                }
                sid_menu = menuClientAccess.get(0).getId();
                snama_menu = menuClientAccess.get(0).getNama_menu();
                sharga  = menuClientAccess.get(0).getHarga();
                skategori = menuClientAccess.get(0).getKategori();
                simage = menuClientAccess.get(0).getImage();
                sjumlah = menuClientAccess.get(0).getJumlah();
                sserving_size = menuClientAccess.get(0).getServing_size();
                sdeskripsi = menuClientAccess.get(0).getDeskripsi();

                fragmentMenuBinding.buttonAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getOneMenu(sid_menu);

                    }
                });
            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                Log.i("ERRO API", "Mesej : "+ t.getMessage());
                Toast.makeText(getContext(), "Kesalahan jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getOneMenu(String id_menu){
        class GetCart extends AsyncTask<Void,Void,Cart> {

            @Override
            protected Cart doInBackground(Void... voids) {
                Cart cart = DatabaseClient
                        .getInstance(getContext())
                        .getDatabase().cartDAO().getcart(Integer.parseInt(id_menu));
                return cart;
            }
            @Override
            protected void onPostExecute(Cart cart) {
                super.onPostExecute(cart);
                if (cart == null){
                    insertToCart(sid_menu, snama_menu, Double.parseDouble(sharga), skategori,
                            simage, Integer.parseInt(sjumlah),Integer.parseInt(sserving_size), sdeskripsi, tambah);
                }else {
                    int kuantitas = tambah + cart.getKuantitas();
                    if (kuantitas <= dstok){
                        updateToCart(kuantitas);
                    }else {
                        Toast.makeText(getContext(),"Stok tidak cukup", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        GetCart get = new GetCart();
        get.execute();
    }

    public void updateToCart(int kuantitas){

        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.getInstance(getContext()).getDatabase()
                        .cartDAO().updateJumlah(kuantitas, id_menu);
            }
        }).start();
    }

    public void insertToCart(String id, String nama, double harga, String kategori, String image,
                             int jumlah, int serving, String deskripsi, int kuantitas){
        class InsertCart extends AsyncTask<Void,Void,Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Cart cart = new Cart(id, nama, harga, kategori, image, jumlah, serving, deskripsi, kuantitas);

                DatabaseClient.getInstance(getContext()).getDatabase()
                        .cartDAO().insert(cart);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getContext(),"Berhasil", Toast.LENGTH_SHORT).show();
            }
        }

        InsertCart add = new InsertCart();
        add.execute();
    }

}