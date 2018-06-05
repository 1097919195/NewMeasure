package com.example.gxkj.newmeasure.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.example.gxkj.newmeasure.Contract.MeasureContract;
import com.example.gxkj.newmeasure.Model.MeasureModel;
import com.example.gxkj.newmeasure.Presenter.MeasurePresenter;
import com.example.gxkj.newmeasure.R;
import com.example.gxkj.newmeasure.app.AppApplication;
import com.example.gxkj.newmeasure.app.AppConstant;
import com.example.gxkj.newmeasure.bean.ContractNumWithPartsData;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class MeasureActivity extends BaseActivity<MeasurePresenter, MeasureModel> implements MeasureContract.View{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rcy_measureData)
    RecyclerView rcy;
    @BindView(R.id.wechat_icon)
    ImageView wechat_icon;
    @BindView(R.id.wechat_nickname)
    TextView wechat_nickname;
    @BindView(R.id.wechat_gender_edit)
    LinearLayout genderLine;
    @BindView(R.id.wechat_gender)
    TextView userGender;

    ArrayList<ContractNumWithPartsData.Parts> partsArrayList = new ArrayList<>();
    CommonRecycleViewAdapter<ContractNumWithPartsData.Parts> adapter;
    public static final String FEMALE = "女";
    public static final String MALE = "男";

    public SpeechSynthesizer speechSynthesizer;//提供对已安装的语音合成引擎的功能的访问
    String mac = SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.MAC_ADDRESS);
    String uuidString = SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.UUID);
    UUID characteristicUUID = null;


    public static void startAction(Activity activity, ArrayList<ContractNumWithPartsData.Parts> partsArrayList, String UserPhoto, String UserName, int gender) {
        Intent intent = new Intent(activity, MeasureActivity.class);
        intent.putExtra(AppConstant.PARTS_CONTRACT_NUM, partsArrayList);
        intent.putExtra(AppConstant.PARTS_USER_PHOTO, UserPhoto);
        intent.putExtra(AppConstant.PARTS_USER_NAME, UserName);
        intent.putExtra(AppConstant.PARTS_USER_GENDER, gender);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_measure;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        toolbar.setNavigationOnClickListener(view -> showHandleBackPress());

        initSpeech();
        initDisplayWithUserInfo();
        initRcycleAdapter();
        initMeasure();
        initListener();
    }

    private void initListener() {
        genderLine.setOnClickListener(v -> {
            switchGender();
        });
    }

    private void switchGender() {
        String gender = userGender.getText().toString();
        int index = 1;
        if (FEMALE.equals(gender)) {
            index = 2;
        }
        //颜色状态列表
        ColorStateList sl = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}},
                new int[]{getResources().getColor(R.color.c252527), getResources().getColor(R.color.primary)});
        new MaterialDialog.Builder(this)
                .title("更正性别")
                .choiceWidgetColor(sl)
                .titleColor(getResources().getColor(R.color.c252527))
                .items(R.array.genders)
                .contentColor(getResources().getColor(R.color.c252527))
                .itemsCallbackSingleChoice(index-1, (dialog, itemView, which, text) -> {
                    dialog.setSelectedIndex(which);
                    userGender.setText(text);
                    dialog.dismiss();
                    return true;//false没有选中样式
                })
                .alwaysCallSingleChoiceCallback()
                .backgroundColor(getResources().getColor(R.color.white))
                .show();
    }

    private void initMeasure() {
        if (mac != "" && uuidString != "") {
            characteristicUUID = UUID.fromString(uuidString);
            mPresenter.startMeasureRequest(characteristicUUID);
            mPresenter.checkBleConnectStateRequest();
        } else {
            ToastUtil.showShort("请先配对蓝牙设备！");
        }
    }

    private void initSpeech() {
        try {
            if (speechSynthesizer == null) {
                speechSynthesizer = new SpeechSynthesizer(this, AppConstant.SPEECH_APP_KEY, AppConstant.SPEECH_APP_SECRET);
            }
            speechSynthesizer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_NET);
            speechSynthesizer.setOption(SpeechConstants.TTS_KEY_VOICE_SPEED, 70);
            speechSynthesizer.init(null);
        } catch (Exception e) {
            LogUtils.loge(e.getMessage());
            ToastUtil.showShort("语音播报出现异常");
        }
    }

    private void initDisplayWithUserInfo() {
        String imgUrl = getIntent().getStringExtra(AppConstant.PARTS_USER_PHOTO);
        String userName = getIntent().getStringExtra(AppConstant.PARTS_USER_NAME);
        int gender = getIntent().getIntExtra(AppConstant.PARTS_USER_GENDER,1);
        ImageLoaderUtils.displayBigPhoto(this,wechat_icon,imgUrl);
        wechat_nickname.setText(userName);
        if (gender == 1) {
            userGender.setText("男");
        } else if (gender == 2) {
            userGender.setText("女");
        } else {
            userGender.setText("N/A");
        }

    }

    private void initRcycleAdapter() {
        partsArrayList = getIntent().getParcelableArrayListExtra(AppConstant.PARTS_CONTRACT_NUM);
        adapter = new CommonRecycleViewAdapter<ContractNumWithPartsData.Parts>(mContext,R.layout.item_measure_parts,partsArrayList) {
            @Override
            public void convert(ViewHolderHelper helper, ContractNumWithPartsData.Parts parts) {
                TextView part = helper.getView(R.id.parts);

                part.setText(parts.getName());
            }
        };

        rcy.setAdapter(adapter);
//        irc.setLayoutManager(new LinearLayoutManager(this));//默认
        rcy.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void returnStartMeasure(Float length, Float angle, int battery) {
        ToastUtil.showShort(String.valueOf(length));
    }

    @Override
    public void returnCheckBleConnectState(RxBleConnection.RxBleConnectionState connectionState) {
        RxBleClient rxBleClient = AppApplication.getRxBleClient(this);
        RxBleDevice rxBleDevice = rxBleClient.getBleDevice(mac);
        RxBleConnection.RxBleConnectionState bleState = rxBleDevice.getConnectionState();
        if (bleState==connectionState.DISCONNECTED) {
            ToastUtil.showShort("连接断开");
            mPresenter.startMeasureRequest(characteristicUUID);//自动连接
        }
        if (bleState==connectionState.CONNECTED) {
            ToastUtil.showShort("蓝牙通信成功，请开始测量");
        }
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        ToastUtil.showShort(msg);
    }

    @Override
    public void onBackPressed() {
        showHandleBackPress();
    }

    private void showHandleBackPress() {
        new MaterialDialog.Builder(this)
                .title("确定要离开当前量体界面?")
                .onPositive((d, i) -> {
                    finish();
                })
                .positiveText(getResources().getString(R.string.sure))
                .negativeColor(getResources().getColor(R.color.ff0000))
                .negativeText("点错了")
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechSynthesizer != null) {
            speechSynthesizer = null;
        }
    }
}
