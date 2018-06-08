package com.example.gxkj.newmeasure.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.example.gxkj.newmeasure.R;
import com.example.gxkj.newmeasure.app.AppApplication;
import com.example.gxkj.newmeasure.app.AppConstant;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.SPUtils;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class SplashActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.act_splash;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        String token = SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.LOGIN_TOKEN);
        Handler handler = new Handler();
        if (!TextUtils.isEmpty(token)) {
            handler.postDelayed(this::goToMain, 500);
        } else {
            handler.postDelayed(this::goToSignIn, 500);
        }
    }

    private void goToMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void goToSignIn() {
        startActivity(new Intent(SplashActivity.this, AccountActivity.class));
        finish();
    }
}
