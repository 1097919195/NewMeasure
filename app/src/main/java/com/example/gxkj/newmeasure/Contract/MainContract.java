package com.example.gxkj.newmeasure.Contract;

import com.example.gxkj.newmeasure.bean.ContractNumWithPartsData;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.example.gxkj.newmeasure.bean.LoginTokenData;
import com.example.gxkj.newmeasure.bean.MeasureCustomer;
import com.example.gxkj.newmeasure.bean.MeasureWeChat;
import com.example.gxkj.newmeasure.bean.UserData;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.scan.ScanResult;

import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public interface MainContract {
    interface Model extends BaseModel {
        Observable<UserData> getUserData();

        Observable<ScanResult> getBleDeviceData();

        Maybe<RxBleDeviceServices> chooseDeviceConnect(String macAddress);

        Observable<ContractNumWithPartsData> changeContractNum(String id);

        Observable<MeasureCustomer> MeasureCustomerData(String tid);

        Observable<MeasureWeChat> MeasureWeChatData(String openID);

        Observable<LoginTokenData> refreshToken();

    }

    interface View extends BaseView {
        void returnGetUserData(UserData userData);

        void returnGetBleDeviceData(ScanResult scanResult);

        void returnChooseDeviceConnectWithSetUuidAndMacAddress(RxBleDeviceServices deviceServices, String macAddress);

        void returnChangeContractNum(ContractNumWithPartsData contractNumWithPartsData, String contract);

        void returnMeasureCustomerData(MeasureCustomer measureCustomer, String tid);

        void returnMeasureWeChatData(MeasureWeChat measureWeChat);

        void returnRefreshToken(LoginTokenData tokenData);

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getUserDataRequset();

        public abstract void getBleDeviceDataRequest();

        public abstract void chooseDeviceConnectRequest(String macAddress);

        public abstract void changeContractNumRequest(String id);

        public abstract void MeasureCustomerDataRequest(String tid);

        public abstract void MeasureWeChatDataRequest(String openID);

        public abstract void getRefreshToken();

    }
}
