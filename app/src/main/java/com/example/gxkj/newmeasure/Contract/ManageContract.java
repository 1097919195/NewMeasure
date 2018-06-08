package com.example.gxkj.newmeasure.Contract;

import com.example.gxkj.newmeasure.bean.BleDevice;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/6/7 0007.
 */

public interface ManageContract {
    interface Model extends BaseModel {
        Observable<HttpResponse> ChangePassword(String old_password, String new_password);
    }

    interface View extends BaseView {
        void returnChangePassword(HttpResponse s, String password);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void ChangePasswordRequest(String old_password,String new_password);
    }
}
