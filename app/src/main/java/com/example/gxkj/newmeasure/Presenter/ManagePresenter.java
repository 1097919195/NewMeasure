package com.example.gxkj.newmeasure.Presenter;

import com.example.gxkj.newmeasure.Contract.ManageContract;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.baserx.RxSubscriber2;

/**
 * Created by Administrator on 2018/6/7 0007.
 */

public class ManagePresenter extends ManageContract.Presenter {
    @Override
    public void ChangePasswordRequest(String old_password, String new_password) {
        mRxManage.add(mModel.ChangePassword(old_password,new_password).subscribeWith(new RxSubscriber2<HttpResponse>(mContext, true) {
            @Override
            protected void _onNext(HttpResponse s) {
                mView.returnChangePassword(s, new_password);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
