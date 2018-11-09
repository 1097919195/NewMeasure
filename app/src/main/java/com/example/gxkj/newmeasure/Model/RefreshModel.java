package com.example.gxkj.newmeasure.Model;

import com.example.gxkj.newmeasure.Contract.RefreshContract;
import com.example.gxkj.newmeasure.api.Api;
import com.example.gxkj.newmeasure.api.HostType;
import com.example.gxkj.newmeasure.bean.LoginTokenData;
import com.jaydenxiao.common.baserx.RxSchedulers;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/11/8 0008.
 */

public class RefreshModel implements RefreshContract.Model {
    @Override
    public Observable<LoginTokenData> refreshToken() {
        return Api.getDefault(HostType.QUALITY_DATA)
                .refreshToken()
                .map(new Api.HttpResponseFunc<>())
                .compose(RxSchedulers.io_main());
    }
}
