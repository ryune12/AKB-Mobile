package com.calvindoaldisutanto.akbresto.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.calvindoaldisutanto.akbresto.API.ApiClient;
import com.calvindoaldisutanto.akbresto.API.ApiInterface;
import com.calvindoaldisutanto.akbresto.API.ClientAccess.MenuClientAccess;
import com.calvindoaldisutanto.akbresto.API.Response.MenuResponse;
import com.calvindoaldisutanto.akbresto.Adapter.MainCourseAdapter;
import com.calvindoaldisutanto.akbresto.models.Menu;
import com.calvindoaldisutanto.akbresto.R;
import com.calvindoaldisutanto.akbresto.databinding.FragmentHomeBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Menu> ListMenu;
    private RecyclerView myRecyclerView;
    private RecyclerView sideRecyclerView;
    private RecyclerView drinkRecyclerView;
    private MainCourseAdapter mainCourseAdapter;
    private MainCourseAdapter sideDishAdapter;
    private MainCourseAdapter drinkAdapter;
    private SwipeRefreshLayout refreshLayout;
    FragmentHomeBinding fragmentHomeBinding;
    private Button button_map;
    private SharedPreferences preferences;
    public static final int mode = Activity.MODE_PRIVATE;
    private ProgressBar progressBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_home, container, false);
        FragmentHomeBinding fragmentHomeBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        myRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_main);
        sideRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_side);
        drinkRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_minuman);
//        refreshLayout = v.findViewById(R.id.refresh);

//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshLayout.setRefreshing(false);
//            }
//        });

        progressBar = v.findViewById(R.id.progressBar);

        getMenu("maincourse");
        getMenu("sidedish");
        getMenu("drink");
        return v;
    }

    public void generateMainCourse(List<MenuClientAccess> menuList){
        mainCourseAdapter = new MainCourseAdapter(getContext(), menuList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setAdapter(mainCourseAdapter);
    }
    public void generateSideDish(List<MenuClientAccess> menuList){
        sideDishAdapter = new MainCourseAdapter(getContext(), menuList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        sideRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sideRecyclerView.setLayoutManager(layoutManager);
        sideRecyclerView.setAdapter(sideDishAdapter);
    }
    public void generateDrink(List<MenuClientAccess> menuList){
        mainCourseAdapter = new MainCourseAdapter(getContext(), menuList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        drinkRecyclerView.setItemAnimator(new DefaultItemAnimator());
        drinkRecyclerView.setLayoutManager(layoutManager);
        drinkRecyclerView.setAdapter(mainCourseAdapter);
    }

    public void getMenu(String kategori){
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MenuResponse> call = apiService.getMenuByKategori(kategori);

        call.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {

                if (kategori == "maincourse") generateMainCourse(response.body().getMenu());
                else if (kategori == "sidedish") generateSideDish(response.body().getMenu());
                else generateDrink(response.body().getMenu());
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                Log.i("ERRO API", "Mesej : "+ t.getMessage());
                Toast.makeText(getActivity(), "Kesalahan jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
