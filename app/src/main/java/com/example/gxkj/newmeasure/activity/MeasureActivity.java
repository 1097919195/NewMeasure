package com.example.gxkj.newmeasure.activity;

import com.example.gxkj.newmeasure.Contract.MeasureContract;
import com.example.gxkj.newmeasure.Model.MeasureModel;
import com.example.gxkj.newmeasure.Presenter.MeasurePresenter;
import com.example.gxkj.newmeasure.R;
import com.jaydenxiao.common.base.BaseActivity;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class MeasureActivity extends BaseActivity<MeasurePresenter, MeasureModel> implements MeasureContract.View{
    @Override
    public int getLayoutId() {
        return R.layout.act_measure;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {

    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }
}
