package com.example.gxkj.newmeasure.Presenter;

import com.example.gxkj.newmeasure.Contract.RefreshContract;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.example.gxkj.newmeasure.bean.LoginTokenData;
import com.jaydenxiao.common.baserx.RxSubscriber2;

/**
 * Created by Administrator on 2018/11/8 0008.
 */

public class RefreshPresenter extends RefreshContract.Presenter {
    @Override
    public void getRefreshToken() {
        mRxManage.add(mModel.refreshToken().subscribeWith(new RxSubscriber2<LoginTokenData>(mContext, false) {
            @Override
            protected void _onNext(LoginTokenData loginTokenData) {
                mView.returnRefreshToken(loginTokenData);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
