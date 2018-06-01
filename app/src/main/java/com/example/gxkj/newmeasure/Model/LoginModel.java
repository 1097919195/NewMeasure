package com.example.gxkj.newmeasure.Model;

import com.example.gxkj.newmeasure.Contract.LoginContract;
import com.example.gxkj.newmeasure.api.Api;
import com.example.gxkj.newmeasure.api.HostType;
import com.example.gxkj.newmeasure.bean.LoginTokenData;
import com.jaydenxiao.common.baserx.RxSchedulers;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class LoginModel implements LoginContract.Model{
    @Override
    public Observable<LoginTokenData> getToken(String username, String password) {
        return Api.getDefault(HostType.QUALITY_DATA)
                .getTokenWithSignIn(username,password)
                .compose(RxSchedulers.io_main());
    }
}
