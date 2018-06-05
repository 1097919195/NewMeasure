package com.example.gxkj.newmeasure.Contract;

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
    }

    interface View extends BaseView {
        void returnStartMeasure(Float length, Float angle, int battery);

        void returnCheckBleConnectState(RxBleConnection.RxBleConnectionState connectionState);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void startMeasureRequest(UUID characteristicUUID);

        public abstract void checkBleConnectStateRequest();
    }
}
