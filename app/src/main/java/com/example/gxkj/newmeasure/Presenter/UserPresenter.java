package com.example.gxkj.newmeasure.Presenter;

import com.example.gxkj.newmeasure.Contract.UserContract;
import com.example.gxkj.newmeasure.bean.LoginTokenData;
import com.example.gxkj.newmeasure.bean.UserData;
import com.jaydenxiao.common.baserx.RxSubscriber;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class UserPresenter extends UserContract.Presenter {
    @Override
    public void getUserDataRequset() {
        mRxManage.add(mModel.getUserData().subscribeWith(new RxSubscriber<UserData>(mContext, true) {
            @Override
            protected void _onNext(UserData userData) {
                mView.returnGetUserData(userData);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
