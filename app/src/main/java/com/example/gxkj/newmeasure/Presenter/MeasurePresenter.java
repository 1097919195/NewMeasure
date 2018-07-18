package com.example.gxkj.newmeasure.Presenter;

import com.example.gxkj.newmeasure.Contract.MeasureContract;
import com.example.gxkj.newmeasure.app.AppConstant;
import com.example.gxkj.newmeasure.bean.ContractNumWithPartsData;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.example.gxkj.newmeasure.utils.HexString;
import com.example.gxkj.newmeasure.bean.MultipartBeanWithUserData;
import com.jaydenxiao.common.baserx.RxSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class MeasurePresenter extends MeasureContract.Presenter{
    private static final int MEASURE_DURATION = 400;

    @Override
    public void startMeasureRequest(UUID characteristicUUID) {
        mRxManage.add(mModel.startMeasure(characteristicUUID)
                .throttleFirst(MEASURE_DURATION, TimeUnit.MILLISECONDS)
                .subscribeWith(new RxSubscriber<byte[]>(mContext,false) {
                    @Override
                    protected void _onNext(byte[] bytes) {
                        String s = HexString.bytesToHex(bytes);
                        if (s.length() == AppConstant.STANDARD_LENGTH) {
                            int code = Integer.parseInt("8D6A", 16);
                            int length = Integer.parseInt(s.substring(0, 4), 16);
                            int angle = Integer.parseInt(s.substring(4, 8), 16);
                            int battery = Integer.parseInt(s.substring(8, 12), 16);
                            int a1 = length ^ code;
                            int a2 = angle ^ code;
                            int a3 = battery ^ code;
                            a1 += AppConstant.ADJUST_VALUE;
                            mView.returnStartMeasure(Float.valueOf(a1) / 10, Float.valueOf(a2) / 10, a3);
                        }

                    }

                    @Override
                    protected void _onError(String message) {
//                        mView.showErrorTip("连接通讯失败！");
                    }
                }));
    }

    @Override
    public void checkBleConnectStateRequest() {
        mRxManage.add(mModel.checkBleConnectState()
                .subscribe(
                        connectedState->mView.returnCheckBleConnectState(connectedState)
                ));
    }

//    @Override
//    public void upLoadMeasureResultRequset(MultipartBeanWithUserData user_data, String[] images, Object[][] data, String contract_id) {
//        mRxManage.add(mModel.upLoadMeasureResult(user_data, images, data, contract_id).subscribeWith(new RxSubscriber<HttpResponse>(mContext, true) {
//            @Override
//            protected void _onNext(HttpResponse httpResponse) {
//                mView.returnUpLoadMeasureResult(httpResponse);
//            }
//
//            @Override
//            protected void _onError(String message) {
//                mView.showErrorTip(message);
//            }
//        }));
//    }

    @Override
    public void upLoadMeasureResultRequset(String tid, String openID, int sex, MultipartBody.Part[] images, List<ContractNumWithPartsData.Parts> data, String contract_id, String address) {
        mRxManage.add(mModel.upLoadMeasureResult(tid, openID, sex, images, data, contract_id, address).subscribeWith(new RxSubscriber<HttpResponse>(mContext, true) {
            @Override
            protected void _onNext(HttpResponse httpResponse) {
                mView.returnUpLoadMeasureResult(httpResponse);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }


}
