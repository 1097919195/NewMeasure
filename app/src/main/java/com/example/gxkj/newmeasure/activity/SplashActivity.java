package com.example.gxkj.newmeasure.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.gxkj.newmeasure.R;
import com.example.gxkj.newmeasure.app.AppApplication;
import com.example.gxkj.newmeasure.app.AppConstant;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

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
            checkPremissionLocation();//获取当前位置信息
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

    private void checkPremissionLocation() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        AppApplication.getmLocationClient().start();//获取当前位置信息
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again
                        ToastUtil.showShort("您拒绝了位置信息权限，会导致定位上传失败");
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings
                        ToastUtil.showShort("未授予位置信息权限,请手动开启");
//                        new MaterialDialog.Builder(this)
//                                .content("未授予位置信息权限,去手动开启")
//                                .positiveText("确认")
//                                .negativeText("取消")
//                                .backgroundColor(getResources().getColor(R.color.white))
//                                .contentColor(getResources().getColor(R.color.primary))
//                                .onPositive((dialog, which) -> {
//                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
//                                    intent.setData(uri);
//                                    startActivity(intent);
//                                })
//                                .show();
                    }
                });
    }
}
