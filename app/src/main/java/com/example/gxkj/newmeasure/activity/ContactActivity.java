package com.example.gxkj.newmeasure.activity;

import android.os.Build;
import android.support.v7.widget.Toolbar;

import com.example.gxkj.newmeasure.R;
import com.jaydenxiao.common.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/11 0011.
 */

public class ContactActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    public int getLayoutId() {
        return R.layout.act_contact;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        toolbar.setNavigationOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            }else {
                finish();
            }
        });
    }
}
