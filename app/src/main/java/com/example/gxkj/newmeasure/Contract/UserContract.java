package com.example.gxkj.newmeasure.Contract;

import com.example.gxkj.newmeasure.bean.UserData;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public interface UserContract {
    interface Model extends BaseModel{
        Observable<UserData> getUserData();
    }

    interface View extends BaseView {
        void returnGetUserData(UserData userData);
    }

    abstract class Presenter extends BasePresenter<View,Model>{
        public abstract void getUserDataRequset();
    }
}
