package com.example.gxkj.newmeasure.activity;

import android.widget.Button;
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

public class AccountActivity extends BaseActivity<LoginPresenter,LoginModel> implements LoginContract.View {
    @BindView(R.id.input_name)
    EditText input_name;
    @BindView(R.id.input_password)
    EditText input_password;
    @BindView(R.id.input_eye)
    ImageView input_eye;
    @BindView(R.id.action_sign_in)
    Button action_signin;

    @Override
    public int getLayoutId() {
        return R.layout.act_account;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this,mModel);
    }

    @Override
    public void initView() {

        initListener();
    }

    private void initListener() {
        action_signin.setOnClickListener(v -> {
            mPresenter.getTokenRequset("18888780080","000000");
        });
    }

    @Override
    public void returnGetToken(LoginTokenData tokenData) {
        SPUtils.setSharedStringData(AppApplication.getAppContext(),AppConstant.LOGIN_TOKEN,tokenData.getToken_type()+tokenData.getAccess_token());
        LogUtils.loge(tokenData.getAccess_token());
        ToastUtil.showShort("登录成功！");
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
