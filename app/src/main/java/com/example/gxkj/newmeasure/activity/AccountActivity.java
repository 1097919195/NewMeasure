package com.example.gxkj.newmeasure.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.gxkj.newmeasure.Contract.LoginContract;
import com.example.gxkj.newmeasure.Model.LoginModel;
import com.example.gxkj.newmeasure.Presenter.LoginPresenter;
import com.example.gxkj.newmeasure.R;
import com.example.gxkj.newmeasure.app.AppApplication;
import com.example.gxkj.newmeasure.app.AppConstant;
import com.example.gxkj.newmeasure.bean.LoginTokenData;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.PermissionUtils;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class AccountActivity extends BaseActivity<LoginPresenter, LoginModel> implements LoginContract.View {
    @BindView(R.id.input_name)
    EditText input_name;
    @BindView(R.id.input_password)
    EditText input_password;
    @BindView(R.id.input_eye)
    ImageView input_eye;
    @BindView(R.id.action_sign_in)
    Button action_signin;
    @BindView(R.id.action_remember_pwd)
    CheckBox remainPassword;

    private String username = "";
    private String password = "";
    private boolean diaplayPassword = false;

    private boolean isFirstRun = SPUtils.getSharedBooleanData(AppApplication.getAppContext(),AppConstant.IS_FIRST_RUN);
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_PHONE_STATE};
    private static final int permissionCode = 1;


    @Override
    public int getLayoutId() {
        return R.layout.act_account;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        if (!isFirstRun) {
            initPremission();
            SPUtils.setSharedBooleanData(AppApplication.getAppContext(),AppConstant.IS_FIRST_RUN,true);
        }
        initUserInfo();
        initListener();
    }

    private void initPremission() {
        //请求多个权限
        PermissionUtils.checkAndRequestMorePermissions(this,permissions,permissionCode);
        LogUtils.loge("firstRun");
    }

    private void initUserInfo() {
        username = SPUtils.getSharedStringData(AppApplication.getAppContext(),AppConstant.USERINFO_NAME);
        password = SPUtils.getSharedStringData(AppApplication.getAppContext(),AppConstant.USERINFO_PASS);
        if (!"".equals(username)) {
            input_name.setText(username);
        }
        if (!"".equals(password)) {
            input_password.setText(password);
            remainPassword.setChecked(true);
        }
    }

    private void initListener() {
        checkPremissionLocation();
        
        action_signin.setOnClickListener(v -> {
            username = input_name.getEditableText().toString();
            password = input_password.getEditableText().toString();
            if (remainPassword.isChecked()) {
                SPUtils.setSharedStringData(AppApplication.getAppContext(), AppConstant.USERINFO_PASS, password);
            } else {
                SPUtils.setSharedStringData(AppApplication.getAppContext(), AppConstant.USERINFO_PASS, "");
            }
            SPUtils.setSharedStringData(AppApplication.getAppContext(), AppConstant.USERINFO_NAME, username);
//            mPresenter.getTokenRequset("18888780080", "000000");
            mPresenter.getTokenRequset(username, password);
        });

        input_eye.setOnClickListener(v -> {
            if (diaplayPassword) {
                input_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                diaplayPassword = false;
            } else {
                input_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                diaplayPassword = true;
            }
        });
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

    @Override
    public void returnGetToken(LoginTokenData tokenData) {
        SPUtils.setSharedStringData(AppApplication.getAppContext(), AppConstant.LOGIN_TOKEN, tokenData.getToken_type() + tokenData.getAccess_token());
        LogUtils.loge(tokenData.getAccess_token());
        ToastUtil.showShort("登录成功！");
        finish();
        MainActivity.startAction(AccountActivity.this);
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        ToastUtil.showShort(msg);
    }
}
