package com.example.gxkj.newmeasure.Contract;

import com.example.gxkj.newmeasure.bean.ContractNumWithPartsData;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.example.gxkj.newmeasure.bean.LoginTokenData;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;
import com.polidea.rxandroidble2.RxBleConnection;

import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * Created by Administrator on 2018/11/8 0008.
 */

public interface RefreshContract {
    interface Model extends BaseModel {
        Observable<LoginTokenData> refreshToken();
    }

    interface View extends BaseView {
        void returnRefreshToken(LoginTokenData tokenData);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getRefreshToken();
    }
}
