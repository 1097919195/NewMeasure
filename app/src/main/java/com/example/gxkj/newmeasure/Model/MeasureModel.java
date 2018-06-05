package com.example.gxkj.newmeasure.Model;

import com.example.gxkj.newmeasure.Contract.MeasureContract;
import com.example.gxkj.newmeasure.app.AppApplication;
import com.example.gxkj.newmeasure.app.AppConstant;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;

import java.util.UUID;

import io.reactivex.Observable;

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
}
