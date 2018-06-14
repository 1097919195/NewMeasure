package com.example.gxkj.newmeasure.Model;

import com.example.gxkj.newmeasure.Contract.MeasureContract;
import com.example.gxkj.newmeasure.api.Api;
import com.example.gxkj.newmeasure.api.HostType;
import com.example.gxkj.newmeasure.app.AppApplication;
import com.example.gxkj.newmeasure.app.AppConstant;
import com.example.gxkj.newmeasure.bean.ContractNumWithPartsData;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.example.gxkj.newmeasure.bean.MultipartBeanWithUserData;
import com.google.gson.Gson;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class MeasureModel implements MeasureContract.Model {
    private RxBleClient rxBleClient = AppApplication.getRxBleClient(AppApplication.getAppContext());
    String macAddress = SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.MAC_ADDRESS);
    @Override
    public Observable<byte[]> startMeasure(UUID characteristicUUID) {
        return rxBleClient.getBleDevice(macAddress)
                .establishConnection(false)
                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(characteristicUUID))
                .flatMap(notificationObservable -> notificationObservable)
                .compose(RxSchedulers.<byte[]>io_main());
    }

    @Override
    public Observable<RxBleConnection.RxBleConnectionState> checkBleConnectState() {
        return rxBleClient.getBleDevice(macAddress)
                .observeConnectionStateChanges()
                .compose(RxSchedulers.io_main());
    }

//    @Override
//    public Observable<HttpResponse> upLoadMeasureResult(MultipartBeanWithUserData user_data, String[] images, Object[][] data, String contract_id) {
//        String s = (new Gson()).toJson(user_data);
//        return Api.getDefault(HostType.QUALITY_DATA)
//                .upLoadMeasureResult(s, images, data, contract_id)
//                .compose(RxSchedulers.io_main());
//    }

    @Override
    public Observable<HttpResponse> upLoadMeasureResult(String tid, String openID, int sex, MultipartBody.Part[] images, MultipartBeanWithUserData data, String contract_id, String address) {
        String s = (new Gson()).toJson(data);
        return Api.getDefault(HostType.QUALITY_DATA)
                .upLoadMeasureResult(tid, openID, sex, images, s, contract_id, address)
                .compose(RxSchedulers.io_main());
    }


}
