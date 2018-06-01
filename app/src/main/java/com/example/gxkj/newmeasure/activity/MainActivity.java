package com.example.gxkj.newmeasure.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.gxkj.newmeasure.Contract.UserContract;
import com.example.gxkj.newmeasure.Model.UserModel;
import com.example.gxkj.newmeasure.Presenter.UserPresenter;
import com.example.gxkj.newmeasure.R;
import com.example.gxkj.newmeasure.bean.UserData;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ToastUtil;

import butterknife.BindView;

public class MainActivity extends BaseActivity<UserPresenter,UserModel> implements UserContract.View , NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
    @Override
    public int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        navView.getMenu().removeItem(R.id.nav_device);
        toolbar.setBackgroundColor(getResources().getColor(R.color.main_color));

        mPresenter.getUserDataRequset();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void returnGetUserData(UserData userData) {
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
