package com.example.gxkj.newmeasure.Model;

import com.example.gxkj.newmeasure.Contract.UserContract;
import com.example.gxkj.newmeasure.api.Api;
import com.example.gxkj.newmeasure.api.HostType;
import com.example.gxkj.newmeasure.bean.UserData;
import com.jaydenxiao.common.baserx.RxSchedulers;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class UserModel implements UserContract.Model {
    @Override
    public Observable<UserData> getUserData() {
        return Api.getDefault(HostType.QUALITY_DATA)
                .getUser()
                .map(new Api.HttpResponseFunc<>())
                .compose(RxSchedulers.io_main());
    }
}
