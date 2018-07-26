package com.example.gxkj.newmeasure.Presenter;

import com.example.gxkj.newmeasure.Contract.MainContract;
import com.example.gxkj.newmeasure.bean.ContractNumWithPartsData;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.example.gxkj.newmeasure.bean.MeasureCustomer;
import com.example.gxkj.newmeasure.bean.MeasureWeChat;
import com.example.gxkj.newmeasure.bean.UserData;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.polidea.rxandroidble2.scan.ScanResult;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class MainPresenter extends MainContract.Presenter {
    @Override
    public void getUserDataRequset() {
        mRxManage.add(mModel.getUserData().subscribeWith(new RxSubscriber<UserData>(mContext, false) {
            @Override
            protected void _onNext(UserData userData) {
                mView.returnGetUserData(userData);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }

    @Override
    public void getBleDeviceDataRequest() {
        mRxManage.add(mModel.getBleDeviceData()
                .subscribeWith(new RxSubscriber<ScanResult>(mContext, false) {
                    @Override
                    protected void _onNext(ScanResult scanResult) {
                        mView.returnGetBleDeviceData(scanResult);
                    }

                    @Override
                    protected void _onError(String message) {
//                mView.showErrorTip(message);
                    }
                }));

    }

    @Override
    public void chooseDeviceConnectRequest(String macAddress) {
        mRxManage.add(mModel.chooseDeviceConnect(macAddress)
                .doOnSubscribe(disposable->
                        mView.showLoading("chooseConnect"))
                .subscribe(deviceServices -> {
                    mView.returnChooseDeviceConnectWithSetUuidAndMacAddress(deviceServices,macAddress);
                },e -> {mView.showErrorTip("connectFail");
                    LogUtils.loge(e.getCause().toString());}));

    }

    @Override
    public void changeContractNumRequest(String id) {
        mRxManage.add(mModel.changeContractNum(id).subscribeWith(new RxSubscriber<ContractNumWithPartsData>(mContext, false) {
            @Override
            protected void _onNext(ContractNumWithPartsData contractNumWithPartsData) {
                mView.returnChangeContractNum(contractNumWithPartsData,id);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }

    @Override
    public void MeasureCustomerDataRequest(String tid) {
        mRxManage.add(mModel.MeasureCustomerData(tid).subscribeWith(new RxSubscriber<MeasureCustomer>(mContext, true) {
            @Override
            protected void _onNext(MeasureCustomer measureCustomer) {
                mView.returnMeasureCustomerData(measureCustomer,tid);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }

    @Override
    public void MeasureWeChatDataRequest(String openID) {
        mRxManage.add(mModel.MeasureWeChatData(openID).subscribeWith(new RxSubscriber<MeasureWeChat>(mContext, true) {
            @Override
            protected void _onNext(MeasureWeChat measureWeChat) {
                mView.returnMeasureWeChatData(measureWeChat);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
