package com.fekrety.fekretyonline.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.fekrety.fekretyonline.Model.JasonPlaceHolderApi;
import com.fekrety.fekretyonline.Model.item;
import com.fekrety.fekretyonline.PicassoClient;
import com.fekrety.fekretyonline.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DialogFeedback extends AppCompatDialogFragment {

    private int id ;
    private TextView name,descrpition;
    private Button btn_send,btn_cancel;
    private EditText et_feedback;
    private ProgressBar progressBar;
    private de.hdodenhof.circleimageview.CircleImageView circleImageView;
    private Activity activity;

    public DialogFeedback(int id, Activity activity){
        this.id=id;           // id to get item from server
        this.activity=activity;
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.layout_dialog, null);
         name = view.findViewById(R.id.txt_name_Dialog);
         descrpition = view.findViewById(R.id.txt_desc_Dialog);
         btn_send = view.findViewById(R.id.btn_send_Dialog);
         btn_cancel = view.findViewById(R.id.btn_cancel_Dialog);
         et_feedback=view.findViewById(R.id.et_feedback);
         circleImageView=view.findViewById(R.id.img_profile_image_Dialog);
        progressBar= view.findViewById(R.id.prograsbar_Dialog);
            progressBar.setVisibility(View.VISIBLE);
        alertDialog.setView(view).setTitle("");
        GetItem();
        final AlertDialog dialog = alertDialog.create();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "You are great, Thank you for your Feedback", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    private  void  GetItem(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.npoint.io/dc681578b8837eb6eaca/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JasonPlaceHolderApi jasonPlaceHolderApi =retrofit.create(JasonPlaceHolderApi.class);

        final Call<item> itemCall = jasonPlaceHolderApi.GetitemWithID(id);
        itemCall.enqueue(new Callback<item>() {
            @Override
            public void onResponse(Call<item> call, Response<item> response) {
                if(response.isSuccessful()){
                    //retrive data according to id and show it to user
                    item item = response.body();
                   name.setText(item.getName());
                    descrpition.setText(item.getDescrpition());
                    try {
                        PicassoClient.dowloadImage(getActivity().getApplicationContext(), item.getPhoto(), circleImageView);
                    }catch (NullPointerException e){
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<item> call, Throwable t) {
                Toast.makeText(activity, "Fail to connect to the server ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
