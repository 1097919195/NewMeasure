package com.example.gxkj.newmeasure.Presenter;

import com.example.gxkj.newmeasure.Contract.LoginContract;
import com.example.gxkj.newmeasure.bean.LoginTokenData;
import com.jaydenxiao.common.baserx.RxSubscriber;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class LoginPresenter extends LoginContract.Presenter {
    @Override
    public void getTokenRequset(String username, String password) {
        mRxManage.add(mModel.getToken(username,password).subscribeWith(new RxSubscriber<LoginTokenData>(mContext, true) {
            @Override
            protected void _onNext(LoginTokenData tokenData) {
                mView.returnGetToken(tokenData);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip("账号或者密码错误");
            }
        }));
    }
}
