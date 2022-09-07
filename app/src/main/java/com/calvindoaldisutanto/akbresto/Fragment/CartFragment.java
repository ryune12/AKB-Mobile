package com.calvindoaldisutanto.akbresto.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.calvindoaldisutanto.akbresto.API.ApiClient;
import com.calvindoaldisutanto.akbresto.API.ApiInterface;
import com.calvindoaldisutanto.akbresto.API.Response.OrderResponse;
import com.calvindoaldisutanto.akbresto.Adapter.CartAdapter;
import com.calvindoaldisutanto.akbresto.Database.DatabaseClient;
import com.calvindoaldisutanto.akbresto.databinding.FragmentHomeBinding;
import com.calvindoaldisutanto.akbresto.models.Cart;
import com.calvindoaldisutanto.akbresto.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.calvindoaldisutanto.akbresto.Adapter.MainCourseAdapter.SHARED_PREFS;
import static com.calvindoaldisutanto.akbresto.MainActivity.ID_ORDER;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CartAdapter adapter;
    RecyclerView recyclerView;
    Button buttonConfirm;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        FragmentHomeBinding fragmentHomeBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_cart);
        getCart();
        buttonConfirm = v.findViewById(R.id.btnConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDetail();
            }
        });

        return v;
    }
    public void getCart(){
        class GetCart extends AsyncTask<Void,Void, List<Cart>> {

            @Override
            protected List<Cart> doInBackground(Void... voids) {
                List<Cart> cart = DatabaseClient
                        .getInstance(getContext())
                        .getDatabase().cartDAO().getAll();
                return cart;
            }
            @Override
            protected void onPostExecute(List<Cart> cart) {
                super.onPostExecute(cart);
                if (cart == null){
                    Toast.makeText(getContext(), "Cart Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    generateList(cart);
                }
            }
        }

        GetCart get = new GetCart();
        get.execute();
    }

    public void insertDetail(){
        class GetCart extends AsyncTask<Void,Void,List<Cart>> {

            @Override
            protected List<Cart> doInBackground(Void... voids) {
                List<Cart> cart = DatabaseClient
                        .getInstance(getContext())
                        .getDatabase().cartDAO().getAll();
                return cart;
            }
            @Override
            protected void onPostExecute(List<Cart> cart) {
                super.onPostExecute(cart);
                getCart();
                int  id_order;
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                id_order = sharedPreferences.getInt(ID_ORDER, 0);
                for ( Cart cartModel : cart) {
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<OrderResponse> add = apiInterface.createDetail(
                            String.valueOf(id_order),
                            cartModel.getId_menu(),
                            String.valueOf(cartModel.getKuantitas()),
                            String.valueOf(cartModel.getHarga()*cartModel.getKuantitas()));

                    add.enqueue(new Callback<OrderResponse>() {
                        @Override
                        public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                            Log.i("Quda : ", "Masuk RESPONSE ," + response.code());
                        }

                        @Override
                        public void onFailure(Call<OrderResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "Gagal Enqueue", Toast.LENGTH_SHORT).show();
                            Log.i("Quda : ", "Masuk FAILURE ," + t.getMessage());
                        }
                    });
                }
                destroyRoom();
            }
        }
        GetCart get = new GetCart();
        get.execute();
    }

    public void destroyRoom(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.getInstance(getContext())
                        .getDatabase().cartDAO().nukeTable();
            }
        }).start();
    }

    public void generateList(List<Cart> cartList){
        adapter = new CartAdapter(getContext(), cartList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}