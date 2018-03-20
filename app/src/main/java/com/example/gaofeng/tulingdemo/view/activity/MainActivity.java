package com.example.gaofeng.tulingdemo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaofeng.tulingdemo.R;
import com.example.gaofeng.tulingdemo.model.bean.DishBean;
import com.example.gaofeng.tulingdemo.model.bean.NewsBean;
import com.example.gaofeng.tulingdemo.model.bean.TextBean;
import com.example.gaofeng.tulingdemo.model.bean.UrlBean;
import com.example.gaofeng.tulingdemo.model.eventmsg.TextMsg;
import com.example.gaofeng.tulingdemo.network.NetWorkService;
import com.example.gaofeng.tulingdemo.util.NetWorkUtil;
import com.example.gaofeng.tulingdemo.view.fragment.TextFragment;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    //API key :  daedd0780ee042a49d1cf67342201081
    @BindView(R.id.et_user_info)
    EditText et_user_info;
    @BindView(R.id.tv_homelife)
    TextView tv_homelife;
    private List<String> list = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private Retrofit retrofit = NetWorkUtil.getInstance().getRetrofit();
    private String key = NetWorkUtil.getInstance().getKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.btn_search)
    public void searchHomeLife() {

        String info = et_user_info.getText().toString();
        if (info != null) {
            getResult(info);
        }

    }


    private void getResult(String info) {

        NetWorkService netWorkService = retrofit.create(NetWorkService.class);
        Call<ResponseBody> call = netWorkService.getTextInfo(key, info);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject json = null;

                try {
                    json = new JSONObject(response.body().string());
                    String text = json.toString();
                    int code = json.getInt("code");
                    Gson gson = new Gson();
                    switch (code) {
                        case 100000:
                            //文本类解析
                            TextBean textBean = gson.fromJson(text, TextBean.class);
//                            tv_homelife.setText(textBean.getText());
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction transaction = fm.beginTransaction();
                            TextFragment tf = new TextFragment();
                            transaction.replace(R.id.container, tf);
                            transaction.commitAllowingStateLoss();
                            EventBus.getDefault().postSticky(new TextMsg(textBean.getText()));

                            break;
                        case 308000:
                            //菜品类解析
                            DishBean dishBean = gson.fromJson(text, DishBean.class);
//                            tv_homelife.setText(dishBean.getList().get(0).getInfo());

                            EventBus.getDefault().postSticky(dishBean);
                            startActivity(new Intent(MainActivity.this, NewsActivity.class));

                            break;
                        case 302000:
                            //新闻类解析
                            NewsBean newsBean = gson.fromJson(text, NewsBean.class);
//                            tv_homelife.setText(newsBean.getList().get(0).getArticle());
                            EventBus.getDefault().postSticky(newsBean);
                            startActivity(new Intent(MainActivity.this, NewsActivity.class));

                            break;
                        case 200000:
                            //链接类解析
                            UrlBean urlBean = gson.fromJson(text, UrlBean.class);
                            tv_homelife.setText(urlBean.getUrl());
                            break;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


}
