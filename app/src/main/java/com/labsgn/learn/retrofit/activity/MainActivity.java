package com.labsgn.learn.retrofit.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.labsgn.learn.retrofit.R;
import com.labsgn.learn.retrofit.model.GitModel;
import com.labsgn.learn.retrofit.utils.Constants;
import com.labsgn.learn.retrofit.utils.GitApi;
import com.labsgn.learn.retrofit.utils.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private AppCompatEditText mainTxtUser;
    private AppCompatTextView mainLblDetail;
    private AppCompatButton mainBtnUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();

        mainBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMainBtnUser();
            }
        });
    }

    private void initComponent() {
        mainTxtUser = (AppCompatEditText) findViewById(R.id.mainTxtUser);
        mainLblDetail = (AppCompatTextView) findViewById(R.id.mainLblDetail);
        mainBtnUser = (AppCompatButton) findViewById(R.id.mainBtnUser);
    }

    private void onClickMainBtnUser() {
        if (mainTxtUser.getText()!= null){
            hideKeyboard();
            final ProgressDialog progBar = ProgressDialog.show(this, "Fetcing Data", "Please wait...",false, false);

            String user = mainTxtUser.getText().toString().trim();

            //Need to create a RestAdapter object with baseUrl(API) and then buid()
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL_BASE_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //Create new GitAPI
            GitApi gitApi = retrofit.create(GitApi.class);

            //insert what kind of model that GitAPI going to use
            Call<GitModel> currentUser = gitApi.getUser(user);

            //Get the json data
            currentUser.enqueue(new Callback<GitModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<GitModel> call, Response<GitModel> response) {
                    int statusCode = response.code();
                    Logger.log_i("MainActivity","status code : "+""+statusCode);

                    //Avoid wrong github username input
                    try{
                        GitModel knownUser = response.body();

                        mainLblDetail.setText
                                (
                                        "Github Name : " + knownUser.getName()+"\n"+
                                                "\nEmail : "+ knownUser.getEmail()+"\n"+
                                                "\nBio : "+knownUser.getBio()+"\n"+
                                                "\nWebsite : "+knownUser.getBlog()
                                );

                        progBar.dismiss();
                    }catch (Throwable t){
                        Logger.toast(getApplicationContext(),"Unknown github user");
                        progBar.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<GitModel> call, Throwable t) {
                    Logger.toast(getApplicationContext(),t.toString());
                    progBar.dismiss();
                }
            });
        }
    }

    public void hideKeyboard() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Hide the virtual keyboard
                if (MainActivity.this.getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
    }
}
