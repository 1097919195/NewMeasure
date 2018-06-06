package com.example.gxkj.newmeasure.Contract;

import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.example.gxkj.newmeasure.bean.MultipartBeanWithUserData;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;
import com.polidea.rxandroidble2.RxBleConnection;

import java.util.UUID;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public interface MeasureContract {
    interface Model extends BaseModel {
        Observable<byte[]> startMeasure(UUID characteristicUUID);

        Observable<RxBleConnection.RxBleConnectionState> checkBleConnectState();

//        Observable<HttpResponse> upLoadMeasureResult(MultipartBeanWithUserData user_data, String[] images, Object[][] data, String contract_id);
        Observable<HttpResponse> upLoadMeasureResult(String tid, String openID, int sex, String[] images, Object[][] data, String contract_id);
    }

    interface View extends BaseView {
        void returnStartMeasure(Float length, Float angle, int battery);

        void returnCheckBleConnectState(RxBleConnection.RxBleConnectionState connectionState);

        void returnUpLoadMeasureResult(HttpResponse httpResponse);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void startMeasureRequest(UUID characteristicUUID);

        public abstract void checkBleConnectStateRequest();

//        public abstract void upLoadMeasureResultRequset(MultipartBeanWithUserData user_data, String[] images, Object[][] data, String contract_id);
        public abstract void upLoadMeasureResultRequset(String tid, String openID, int sex, String[] images, Object[][] data, String contract_id);
    }
}
