package com.example.gxkj.newmeasure.activity;

import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.example.gxkj.newmeasure.Contract.ManageContract;
import com.example.gxkj.newmeasure.Model.ManageModel;
import com.example.gxkj.newmeasure.Presenter.ManagePresenter;
import com.example.gxkj.newmeasure.R;
import com.example.gxkj.newmeasure.bean.HttpResponse;

import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;


import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/7 0007.
 */

public class ManagePassWordActicity extends BaseActivity<ManagePresenter, ManageModel> implements ManageContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.input_old_pwd)
    EditText input_old_pwd;
    @BindView(R.id.input_new_pwd1)
    EditText input_new_pwd1;
    @BindView(R.id.input_new_pwd2)
    EditText input_new_pwd2;
    @BindView(R.id.action_submit)
    Button action_submit;

    @Override
    public int getLayoutId() {
        return R.layout.act_manage_password;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        toolbar.setNavigationOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
        });

        action_submit.setOnClickListener(v -> {
            if (valid()) {
                if (!input_new_pwd1.getEditableText().toString().equals(input_new_pwd2.getEditableText().toString())) {
                    LogUtils.loge(input_new_pwd1.getText().toString() + "    " + input_new_pwd2.getText().toString());
                    ToastUtil.showShort("二次密码输入不相同！");
                } else {
                    mPresenter.ChangePasswordRequest(input_old_pwd.getText().toString(), input_new_pwd2.getText().toString());
                }
            } else {
                ToastUtil.showShort("请先确认输入内容是否为空");
            }

        });

    }

    private boolean valid() {
        return input_old_pwd.getText().length() > 0
                && input_new_pwd1.getText().length() > 0 && input_new_pwd2.getText().length() > 0;
    }

    //修改密码成功
    @Override
    public void returnChangePassword(HttpResponse s) {
        ToastUtil.showShort("修改密码成功,请重新登录");
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
