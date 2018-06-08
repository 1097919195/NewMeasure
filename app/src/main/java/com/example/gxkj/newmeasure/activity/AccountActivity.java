package com.example.gxkj.newmeasure.activity;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.gxkj.newmeasure.Contract.LoginContract;
import com.example.gxkj.newmeasure.Model.LoginModel;
import com.example.gxkj.newmeasure.Presenter.LoginPresenter;
import com.example.gxkj.newmeasure.R;
import com.example.gxkj.newmeasure.app.AppApplication;
import com.example.gxkj.newmeasure.app.AppConstant;
import com.example.gxkj.newmeasure.bean.LoginTokenData;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;

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

    String username = "";
    String password = "";
    private boolean diaplayPassword = false;


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
        initUserInfo();
        initListener();
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
