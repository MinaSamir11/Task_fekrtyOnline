package com.fekrety.fekretyonline.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.fekrety.fekretyonline.Adapters.FilterAdapter;
import com.fekrety.fekretyonline.Model.JasonPlaceHolderApi;
import com.fekrety.fekretyonline.R;
import com.fekrety.fekretyonline.Model.item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Fragment1 extends Fragment {
    //vars
   private FilterAdapter filterAdapter;
   public static List<item>items = new ArrayList<>();
   public static ListView lstview;
   private SearchView  searchView;
   private ProgressBar progressBar;
   private Button btn_search;
   private String newtextsearch = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        lstview = view.findViewById(R.id.lst_view);
        btn_search = view.findViewById(R.id.btn_search);
        searchView = view.findViewById(R.id.search_view);
        progressBar = view.findViewById(R.id.progressBar);

        try {
            filterAdapter = new FilterAdapter(getActivity().getApplicationContext(), items,getActivity().getSupportFragmentManager(),getActivity());
        }catch (NullPointerException e){

        }
        lstview.setAdapter(filterAdapter);

        searchViewQuery();
        GetItem();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterAdapter.getFilter().filter(newtextsearch);  // search on according to text input in edit text
            }
        });
        return view;
    }

    private void searchViewQuery(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() >0){     // check if text in search bigger than 0 to make button active
                    btn_search.setBackground(getActivity().getResources().getDrawable(R.drawable.custombutton));
                    btn_search.setEnabled(true);
                    newtextsearch = newText;

                }else{     // disable button if no char in input field
                    btn_search.setBackground(getActivity().getResources().getDrawable(R.drawable.custombuttonii));
                    btn_search.setEnabled(false);
                    filterAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }

    private  void  GetItem(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/MinaSamir11/Test/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JasonPlaceHolderApi jasonPlaceHolderApi =retrofit.create(JasonPlaceHolderApi.class);
        Call<List<item>> call = jasonPlaceHolderApi.getitems();

        call.enqueue(new Callback<List<item>>() {
            @Override
            public void onResponse(Call<List<item>> call, Response<List<item>> response) {
                if(response.isSuccessful()) {
                   items =response.body();
                    if(items.isEmpty()){
                        Toast.makeText(getActivity(), "No Data Found from Server", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        filterAdapter = new FilterAdapter(getActivity().getApplicationContext(),
                                items,getActivity().getSupportFragmentManager(),getActivity());
                    }catch (NullPointerException e){

                    }
                    lstview.setAdapter(filterAdapter);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<item>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

                }catch (NullPointerException e){

                }
            }
        });
    }

}
