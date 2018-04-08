package com.example.gaofeng.tulingdemo.view.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.example.gaofeng.tulingdemo.R;
import com.example.gaofeng.tulingdemo.adapter.RobotChatAdapter;
import com.example.gaofeng.tulingdemo.control.InitConfig;
import com.example.gaofeng.tulingdemo.control.MyRecognizer;
import com.example.gaofeng.tulingdemo.control.MySyntherizer;
import com.example.gaofeng.tulingdemo.control.NonBlockSyntherizer;
import com.example.gaofeng.tulingdemo.listener.UiMessageListener;
import com.example.gaofeng.tulingdemo.model.bean.ContactInfo;
import com.example.gaofeng.tulingdemo.model.bean.DishBean;
import com.example.gaofeng.tulingdemo.model.bean.NewsBean;
import com.example.gaofeng.tulingdemo.model.bean.RobotChatBean;
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
import com.example.gaofeng.tulingdemo.util.Logger;
import com.example.gaofeng.tulingdemo.util.NetWorkUtil;
import com.example.gaofeng.tulingdemo.util.OfflineResource;
import com.example.gaofeng.tulingdemo.util.StringUtil;
import com.example.gaofeng.tulingdemo.view.fragment.TextFragment;
import com.example.gaofeng.tulingdemo.view.ui.BaiduASRDigitalDialog;
import com.example.gaofeng.tulingdemo.view.ui.DigitalDialogInput;
import com.example.gaofeng.tulingdemo.view.ui.SimpleTransApplication;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    @BindView(R.id.recycle_view_main)
    RecyclerView recycle_view_main;
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

    // ================== 初始化参数设置开始 ==========================
    /**
     * 发布时请替换成自己申请的appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
     * 本demo的包名是com.baidu.tts.sample，定义在build.gradle中。
     */
    protected String appId = "11034666";

    protected String appKey = "HqhwIAVMaO753SC0of9q9Gqt";

    protected String secretKey = "jTfb3xWWtcIGs1z20U0Ec6xUf16aOY2v";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected TtsMode ttsMode = TtsMode.MIX;

    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat为离线男声模型；
    // assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat为离线女声模型
    protected String offlineVoice = OfflineResource.VOICE_FEMALE;

    // ===============初始化参数设置完毕，更多合成参数请至getParams()方法中设置 =================

    // 主控制类，所有合成控制方法从这个类开始
    protected MySyntherizer synthesizer;

    private RobotChatBean robotChatBean;
    private List<RobotChatBean> list = new ArrayList<>();
    private RobotChatAdapter adapter;

    //语音识别handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != msg.obj) {
                if (msg.obj.toString().contains("final_result")) {
                    String str = msg.obj.toString();
                    String json = str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1);
                    SpeechKnownBean speechKnownBean = new Gson().fromJson(json, SpeechKnownBean.class);
                    robotChatBean = new RobotChatBean();
                    robotChatBean.setTextType(1);
                    robotChatBean.setChat_text(speechKnownBean.getBest_result());
                    list.add(robotChatBean);
                    adapter.addChat(list);
                    info = speechKnownBean.getBest_result();

                    //如果是发送的短信内容，必须写在最前面，防止短信内容里面出现关键词
                    if (isMessage) {
                        sendMessages(info);
                        isMessage = false;
                        return;
                    }

                    //关键词"打开"
                    if (info.contains("打开")) {
                        String appName = info.substring(info.indexOf("开") + 1);
                        openApp(appName);
                        return;
                    }

                    //关键词"打电话"
                    if (info.contains("打电话")) {
                        String tel = StringUtil.getPhoneNum(info);
                        if (StringUtil.isEmpty(tel)) {
                            call("");
                        } else {
                            call(tel);
                        }
                        return;
                    }

                    //关键词"发短信"
                    if (info.contains("发短信")) {
                        getSendMsgContactInfo();
                        return;
                    }

                    //关键词"查找"
                    if (info.contains("查找")) {
                        String searchContent = info.substring(info.indexOf("找") + 1);

                        surfTheInternet(searchContent);
                        return;
                    }

                    getResult();
                }
            }
        }
    };
    //语音合成handler
    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("msg", String.valueOf(msg.obj));
        }
    };

    private boolean isMessage = false;//判断是否是短信内容的标志
    private String msg_number;//发短信时的联系人电话
    private String msg_name;//发短信时的联系人名字


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        robotChatBean = new RobotChatBean();
        robotChatBean.setChat_text("请问有什么能帮到您？");
        robotChatBean.setTextType(0);
        list.add(robotChatBean);
        adapter = new RobotChatAdapter(this, list, recycle_view_main);
        recycle_view_main.setLayoutManager(new LinearLayoutManager(this));
        recycle_view_main.setAdapter(adapter);
        initPermission();
        initRecog();
        initialTts();
    }

    //网上查找
    private void surfTheInternet(String searchContent) {
        refresh("已经为您上网查找" + "\"" + searchContent + "\"");

        // 指定intent的action是ACTION_WEB_SEARCH就能调用浏览器
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        // 指定搜索关键字是选中的文本
        intent.putExtra(SearchManager.QUERY, searchContent);
        startActivity(intent);
    }

    //发送短信的内容
    private void sendMessages(String content) {
        if (msg_number == null) {
            return;
        }

        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> list = manager.divideMessage(content);  //因为一条短信有字数限制，因此要将长短信拆分
        for (String text : list) {
            manager.sendTextMessage(msg_number, null, text, null, null);
        }

        refresh("已经发送" + "\"" + content + "\"" + "给" + msg_name);

    }

    //发送短信的联系人信息
    private void getSendMsgContactInfo() {
        List<ContactInfo> contactLists = getContactLists(this);
        if (contactLists.isEmpty()) {
            refresh("通讯录为空");
            return;
        }
        for (ContactInfo contactInfo : contactLists) {
            if (info.contains(contactInfo.getName())) {
                msg_name = contactInfo.getName();
                msg_number = contactInfo.getNumber();
                refresh("请问您要发送什么给" + msg_name);
                isMessage = true;
                return;
            }
        }

        if (StringUtil.isEmpty(msg_name) && !StringUtil.isEmpty(StringUtil.getPhoneNum(info))) {
            msg_name = StringUtil.getPhoneNum(info);
            msg_number = StringUtil.getPhoneNum(info);
            refresh("请问您要发送什么给" + msg_name);
            isMessage = true;
            return;
        }
        refresh("通讯录中没有此人");
    }


    //获取通信录中所有的联系人
    private List<ContactInfo> getContactLists(Context context) {
        List<ContactInfo> lists = new ArrayList<ContactInfo>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        //moveToNext方法返回的是一个boolean类型的数据
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            ContactInfo contactInfo = new ContactInfo(name, number);
            lists.add(contactInfo);
        }
        return lists;
    }


    //打电话
    private void call(String tel) {
        List<ContactInfo> contactLists = getContactLists(this);
        if (contactLists.isEmpty()) {
            refresh("通讯录为空");
            return;
        }
        for (ContactInfo contactInfo : contactLists) {
            if (!StringUtil.isEmpty(tel)) {

                refresh("正在拨打" + tel);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.CALL");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("tel:" + tel));
                startActivity(intent);
                return;
            }

            if (info.contains(contactInfo.getName())) {
                refresh("已经为您拨通" + contactInfo.getName() + "的电话");
                String number = contactInfo.getNumber();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.CALL");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
                return;
            }
        }

        refresh("通讯录中没有此人");

    }


    //打开应用
    private void openApp(String appName) {
        PackageManager packageManager = MainActivity.this.getPackageManager();
        // 获取手机里的应用列表
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pInfo.size(); i++) {
            PackageInfo p = pInfo.get(i);
            // 获取相关包的<application>中的label信息，也就是-->应用程序的名字
            String label = packageManager.getApplicationLabel(p.applicationInfo).toString();
            Log.d("tag", label);
            if (label.equals(appName)) { //比较label
                refresh(appName + "已经为您打开");
                String pName = p.packageName; //获取包名
                //获取intent
                Intent intent = packageManager.getLaunchIntentForPackage(pName);
                startActivity(intent);
                return;
            }
        }
        refresh("您没有安装该应用");
    }

    //刷新页面
    private void refresh(String content) {
        robotChatBean = new RobotChatBean();
        robotChatBean.setTextType(0);
        robotChatBean.setChat_text(content);
        list.add(robotChatBean);
        adapter.addChat(list);
        speak(content);
    }


    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }


    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    protected void initialTts() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);

        Map<String, String> params = getParams();
        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
    }


    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
        }
        return offlineResource;
    }


    /**
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    private void speak(String text) {
        synthesizer.speak(text);
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


        intent.putExtra(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_ORANGE_DEEPBG);

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
    protected void onDestroy() {
        super.onDestroy();
        synthesizer.release();
        myRecognizer.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        synthesizer.stop();
        myRecognizer.stop();
    }


    @OnClick(R.id.speak_button)
    public void speak(View view) {
        synthesizer.stop();
        myRecognizer.stop();
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
                                robotChatBean = new RobotChatBean();
                                robotChatBean.setTextType(0);
                                robotChatBean.setChat_text(textBean.getText());
                                list.add(robotChatBean);
                                adapter.addChat(list);
                                speak(textBean.getText());

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
                                robotChatBean = new RobotChatBean();
                                robotChatBean.setTextType(0);
                                robotChatBean.setChat_text(urlBean.getText());
                                list.add(robotChatBean);
                                adapter.addChat(list);
                                speak(urlBean.getText());
                                EventBus.getDefault().postSticky(new UrlMsg(urlBean.getUrl(), urlBean.getText(), ""));
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
