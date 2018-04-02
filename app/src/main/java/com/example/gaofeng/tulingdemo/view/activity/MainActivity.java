package com.example.gaofeng.tulingdemo.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaofeng.tulingdemo.R;
import com.example.gaofeng.tulingdemo.control.MyRecognizer;
import com.example.gaofeng.tulingdemo.model.bean.DishBean;
import com.example.gaofeng.tulingdemo.model.bean.NewsBean;
import com.example.gaofeng.tulingdemo.model.bean.SpeechKnownBean;
import com.example.gaofeng.tulingdemo.model.bean.TextBean;
import com.example.gaofeng.tulingdemo.model.bean.UrlBean;
import com.example.gaofeng.tulingdemo.model.eventmsg.TextMsg;
import com.example.gaofeng.tulingdemo.model.eventmsg.UrlMsg;
import com.example.gaofeng.tulingdemo.network.NetWorkService;
import com.example.gaofeng.tulingdemo.recognization.ChainRecogListener;
import com.example.gaofeng.tulingdemo.recognization.CommonRecogParams;
import com.example.gaofeng.tulingdemo.recognization.MessageStatusRecogListener;
import com.example.gaofeng.tulingdemo.recognization.offline.OfflineRecogParams;
import com.example.gaofeng.tulingdemo.recognization.online.OnlineRecogParams;
import com.example.gaofeng.tulingdemo.util.LocationUtil;
import com.example.gaofeng.tulingdemo.util.Logger;
import com.example.gaofeng.tulingdemo.util.NetWorkUtil;
import com.example.gaofeng.tulingdemo.util.StringUtil;
import com.example.gaofeng.tulingdemo.view.fragment.TextFragment;
import com.example.gaofeng.tulingdemo.view.ui.BaiduASRDigitalDialog;
import com.example.gaofeng.tulingdemo.view.ui.DigitalDialogInput;
import com.example.gaofeng.tulingdemo.view.ui.SimpleTransApplication;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.gaofeng.tulingdemo.recognization.IStatus.STATUS_NONE;

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
    private Call<ResponseBody> call;
    private NetWorkService netWorkService = retrofit.create(NetWorkService.class);
    private String info;
    /**
     * 对话框界面的输入参数
     */
    private DigitalDialogInput input;
    /**
     * 有2个listner，一个是用户自己的业务逻辑，如MessageStatusRecogListener。另一个是UI对话框的。
     * 使用这个ChainRecogListener把两个listener和并在一起
     */
    private ChainRecogListener listener;

    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;

    /*
     * Api的参数类，仅仅用于生成调用START的json字符串，本身与SDK的调用无关
     */
    protected CommonRecogParams apiParams;
    /**
     * 控制UI按钮的状态
     */
    protected int status;
    protected boolean enableOffline = false;
    protected boolean running = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (et_user_info != null && msg.obj != null) {
                if (msg.obj.toString().contains("final_result")) {
                    String str = msg.obj.toString();
                    String json = str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1);
                    SpeechKnownBean speechKnownBean = new Gson().fromJson(json, SpeechKnownBean.class);
                    et_user_info.setText(speechKnownBean.getBest_result());
                    searchHomeLife(null);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRecog();
    }

    protected void initRecog() {
        listener = new ChainRecogListener();
        // DigitalDialogInput 输入 ，MessageStatusRecogListener可替换为用户自己业务逻辑的listener
        listener.addListener(new MessageStatusRecogListener(handler));
        myRecognizer = new MyRecognizer(this, listener); // DigitalDialogInput 输入
        apiParams = new OnlineRecogParams(this);
        status = STATUS_NONE;
        if (enableOffline) {
            myRecognizer.loadOfflineEngine(OfflineRecogParams.fetchOfflineParams());
        }
    }


    /**
     * 开始录音，点击“开始”按钮后调用。
     */
    protected void start() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, Object> params = apiParams.fetch(sp);  // params可以手动填入

        // BaiduASRDigitalDialog的输入参数
        input = new DigitalDialogInput(myRecognizer, listener, params);

        Intent intent = new Intent(this, BaiduASRDigitalDialog.class);
        // 在BaiduASRDialog中读取, 因此需要用 SimpleTransApplication传递input参数
        ((SimpleTransApplication) getApplicationContext()).setDigitalDialogInput(input);

        // 修改对话框样式
        // intent.putExtra(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_ORANGE_DEEPBG);

        running = true;
        startActivityForResult(intent, 2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        running = false;
        if (requestCode == 2) {
            String message = null;
            if (resultCode == RESULT_OK) {
                ArrayList results = data.getStringArrayListExtra("results");
                if (results != null && results.size() > 0) {
                    message = results.get(0).toString();
                }
            } else {
                message += "没有结果";
            }
            Logger.info(message);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!running) {
            myRecognizer.release();
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.btn_search)
    public void searchHomeLife(View view) {

        this.info = et_user_info.getText().toString();
        if (null != info) {
            getResult();
        }

    }

    @OnClick(R.id.speak_button)
    public void speak(View view) {
        start();
    }

    private void getResult() {


        if (!StringUtil.isEmpty(info)) {

            call = netWorkService.getTextInfo(key, info);
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
                                EventBus.getDefault().postSticky(new UrlMsg(urlBean.getUrl()));
                                startActivity(new Intent(MainActivity.this, NewsWebViewActivity.class));
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


}
