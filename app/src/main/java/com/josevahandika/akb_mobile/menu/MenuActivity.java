package com.josevahandika.akb_mobile.menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.josevahandika.akb_mobile.R;
import com.josevahandika.akb_mobile.api.MenuAPI;
import com.josevahandika.akb_mobile.entity.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class MenuActivity extends AppCompatActivity {
    private List<Menu> menuList;
    private RecyclerView recyclerView;
    private AdapterMenu adapterMenu;
    SearchView searchView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setTitle("Menu");
        setContentView(R.layout.activity_menu);
        //get data laundry

        //recycler view
        menuList = new ArrayList<Menu>();
        recyclerView = findViewById(R.id.recycler_view_menu);
        searchView = findViewById(R.id.input_search);
        adapterMenu = new AdapterMenu(menuList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterMenu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Menu> menuSearch = new ArrayList<>();
                for(Menu menu : menuList)
                {
                    if(menu.getNama_menu().toLowerCase().contains(s.toLowerCase()))
                    {
                        menuSearch.add(menu);
                    }
                }
                adapterMenu.filter(menuSearch);
                return true;
            }
        });
//        recyclerView.setLayoutManager(mLayoutManager);
        getMenu();
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    public void getMenu() {
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());

//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(this.getApplicationContext());
//        progressDialog.setMessage("loading...");
//        progressDialog.setTitle("Mengambil semua data menu....");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, MenuAPI.URL_SELECT,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (!menuList.isEmpty())
                        menuList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id = jsonObject.optInt("id");
                        String nama_menu = jsonObject.optString("nama_menu");
                        String deskripsi = jsonObject.optString("deskripsi");
                        String takaran_saji = jsonObject.optString("takaran_saji");
                        String harga = jsonObject.optString("harga");
                        String kategori = jsonObject.optString("kategori");
                        String unit = jsonObject.optString("unit");
                        String id_bahan = jsonObject.optString("id_bahan");

                        Menu menu = new Menu(id, nama_menu, takaran_saji, harga,kategori, unit,deskripsi, id_bahan);

                        menuList.add(menu);
                    }
                    adapterMenu.setMenuList(menuList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, response.optString("message"), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}