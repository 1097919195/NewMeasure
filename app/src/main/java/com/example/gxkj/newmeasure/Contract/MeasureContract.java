package com.example.gxkj.newmeasure.Contract;

import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public interface MeasureContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<View, Model> {

    }
}
