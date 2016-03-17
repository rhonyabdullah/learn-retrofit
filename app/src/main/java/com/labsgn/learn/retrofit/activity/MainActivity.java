package com.labsgn.learn.retrofit.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.labsgn.learn.retrofit.R;
import com.labsgn.learn.retrofit.model.GitModel;
import com.labsgn.learn.retrofit.utils.Constants;
import com.labsgn.learn.retrofit.utils.GitApi;
import com.labsgn.learn.retrofit.utils.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private EditText mainTxtUser;
    private TextView mainLblDetail;
    private ProgressBar mainProgressBar;
    private Button mainBtnUser;

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
        mainTxtUser = (EditText) findViewById(R.id.mainTxtUser);
        mainLblDetail = (TextView) findViewById(R.id.mainLblDetail);
        mainProgressBar = (ProgressBar) findViewById(R.id.mainProgressBar);
        mainBtnUser = (Button) findViewById(R.id.mainBtnUser);
    }

    private void onClickMainBtnUser() {
        if (mainTxtUser.getText()!= null){
            mainProgressBar.setVisibility(View.VISIBLE);
            String user = mainTxtUser.getText().toString().trim();

            //Need to create a RestAdapter object with baseUrl(API) and then buid()
            Retrofit restAdapter = new Retrofit.Builder().baseUrl(Constants.URL_BASE_API).build();

            GitApi gitApi = restAdapter.create(GitApi.class);

            //Get the JSON string then put on the pojo (GitModel)
            final GitModel gitModel = GitModel.getInstance();

            gitApi.getUserDetail(user, new Callback<GitModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<GitModel> call, Response<GitModel> response) {
                    mainLblDetail.setText("");

                    mainLblDetail.setText
                            (
                                    "Github Name : " + gitModel.getName()+
                                    "\nEmail : "+ gitModel.getEmail()+
                                    "\nBio : "+gitModel.getBio()+
                                    "\nWebsite : "+gitModel.getBlog()
                            );

                    mainProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<GitModel> call, Throwable t) {
                    Logger.toast(getApplicationContext(), t.toString());
                }
            });
        }
    }
}
