package com.fekrety.fekretyonline.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fekrety.fekretyonline.Adapters.ListItemAdapter;
import com.fekrety.fekretyonline.Dialogs.DialogFeedback;
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

public class Fragment2 extends Fragment {

    private ListItemAdapter itemAdapter;
    private ProgressBar progressBar ;
    private int postion=0;
    private LinearLayoutManager manager;
    private   List<item> pendingitems = new ArrayList<>();
    private   List<item> allitems;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         RecyclerView RV_list_items ;

        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        RV_list_items = view.findViewById(R.id.Recycle_view_items);
        progressBar = view.findViewById(R.id.prograssBar);

        manager = new LinearLayoutManager(getActivity());
        RV_list_items.setLayoutManager(manager);
        RV_list_items.setHasFixedSize(true);

        itemAdapter = new ListItemAdapter(getActivity());

        RV_list_items.setAdapter(itemAdapter);

        GetItem();

        RV_list_items.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();
                if(isScrolling && (currentItems + scrollOutItems == totalItems))
                {
                    isScrolling = false;
                    if(pendingitems.size()!=allitems.size()&&postion<=allitems.size())
                      FetchData();    // fetch data if not all item loaded in adapter
                }
            }
        });

        itemAdapter.setOnItemClickListener(new ListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(item child) {
                DialogFeedback dialogaboutprogram = new DialogFeedback(child.getId(),getActivity());       //dialog feedback obkect
                dialogaboutprogram.show(getActivity().getSupportFragmentManager(), "About Application");
            }
        });
        return view;
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
                   allitems =response.body();
                    if(allitems.isEmpty()){
                        Toast.makeText(getActivity(), "No Data Found from Server", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // for loop to handle scrolling performance, take first 30 item only to adapter
                    for (int i = 0; i <20 ; i++) {
                        pendingitems.add(allitems.get(i));
                        postion++;
                    }
                    itemAdapter.setItem(pendingitems);
                    progressBar.setVisibility(View.INVISIBLE);   // after finsh disable loading
                }
            }
            @Override
            public void onFailure(Call<List<item>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), t.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FetchData(){
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (postion < allitems.size()) {
                    // for loop to load 30 item more to adapter
                    for (int i = 1; i < 30; i++) {
                        if(postion+i < allitems.size())
                            pendingitems.add(allitems.get(postion + i));
                    }
                    if(postion<=allitems.size())
                       postion = postion + 29;
                    itemAdapter.setItem(pendingitems);   // set new item to adapter
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        },2000);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
