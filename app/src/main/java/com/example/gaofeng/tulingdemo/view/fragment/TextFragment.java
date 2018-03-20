package com.example.gaofeng.tulingdemo.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gaofeng.tulingdemo.R;
import com.example.gaofeng.tulingdemo.model.eventmsg.TextMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gaofeng on 2018/3/19.
 */

public class TextFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.image_close)
    ImageView close;
    @BindView(R.id.text_content)
    TextView text_content;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_dialog_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        close.setOnClickListener(this);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getTextContent(TextMsg textMsg) {
        text_content.setText(textMsg.getText());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_close:
                getActivity().getSupportFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss();
                break;
        }
    }
}
