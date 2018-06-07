package com.example.gxkj.newmeasure.Model;

import android.text.TextUtils;

import com.example.gxkj.newmeasure.Contract.MainContract;
import com.example.gxkj.newmeasure.api.Api;
import com.example.gxkj.newmeasure.api.HostType;
import com.example.gxkj.newmeasure.app.AppApplication;
import com.example.gxkj.newmeasure.bean.ContractNumWithPartsData;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.example.gxkj.newmeasure.bean.MeasureCustomer;
import com.example.gxkj.newmeasure.bean.MeasureWeChat;
import com.example.gxkj.newmeasure.bean.UserData;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class MainModel implements MainContract.Model {
    private RxBleClient rxBleClient = AppApplication.getRxBleClient(AppApplication.getAppContext());

    @Override
    public Observable<UserData> getUserData() {
        return Api.getDefault(HostType.QUALITY_DATA)
                .getUser()
                .map(new Api.HttpResponseFunc<>())
                .compose(RxSchedulers.io_main());
    }

    @Override
    public Observable<ScanResult> getBleDeviceData() {
        return rxBleClient.scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)//此段代码会导致部分设备找不打对应的RxBleDeviceServices,模式一定要对
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                        .build(),
                new ScanFilter.Builder().build())
                .filter(s -> !TextUtils.isEmpty(s.getBleDevice().getName()))
                .compose(RxSchedulers.<ScanResult>io_main());
    }

    @Override
    public Maybe<RxBleDeviceServices> chooseDeviceConnect(String macAddress) {
        return rxBleClient.getBleDevice(macAddress)
                .establishConnection(false) //autoConnect flag布尔值：是否直接连接到远程设备（false）或在远程设备变为可用时立即自动连接
                .flatMapSingle(RxBleConnection::discoverServices)
                .firstElement() // Disconnect automatically after discovery
                .compose(RxSchedulers.io_main_maybe());
    }

    @Override
    public Observable<ContractNumWithPartsData> changeContractNum(String id) {
        return Api.getDefault(HostType.QUALITY_DATA)
                .changeContractNum(id)
                .map(new Api.HttpResponseFunc<>())
                .compose(RxSchedulers.io_main());
    }

    @Override
    public Observable<MeasureCustomer> MeasureCustomerData(String tid) {
        return Api.getDefault(HostType.QUALITY_DATA)
                .MeasureCustomer(tid)
                .map(new Api.HttpResponseFunc<>())
                .compose(RxSchedulers.io_main());
    }

    @Override
    public Observable<MeasureWeChat> MeasureWeChatData(String openID) {
        return Api.getDefault(HostType.QUALITY_DATA)
                .MeasureWeChat(openID)
                .map(new Api.HttpResponseFunc<>())
                .compose(RxSchedulers.io_main());
    }
}
