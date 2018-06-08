package com.example.gxkj.newmeasure.Model;

import com.example.gxkj.newmeasure.Contract.ManageContract;
import com.example.gxkj.newmeasure.api.Api;
import com.example.gxkj.newmeasure.api.HostType;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.jaydenxiao.common.baserx.RxSchedulers;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/6/7 0007.
 */

public class ManageModel implements ManageContract.Model {
    @Override
    public Observable<HttpResponse> ChangePassword(String old_password, String new_password) {
        return Api.getDefault(HostType.QUALITY_DATA_TEST)
                .changePassword(old_password,new_password)
                .compose(RxSchedulers.io_main());
    }
}
