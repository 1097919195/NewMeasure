package com.example.gxkj.newmeasure.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.baidu.location.BDLocation;
import com.example.gxkj.newmeasure.Contract.MainContract;
import com.example.gxkj.newmeasure.Model.MainModel;
import com.example.gxkj.newmeasure.Presenter.MainPresenter;
import com.example.gxkj.newmeasure.R;
import com.example.gxkj.newmeasure.app.AppApplication;
import com.example.gxkj.newmeasure.app.AppConstant;
import com.example.gxkj.newmeasure.bean.BleDevice;
import com.example.gxkj.newmeasure.bean.ContractNumWithPartsData;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.example.gxkj.newmeasure.bean.MeasureCustomer;
import com.example.gxkj.newmeasure.bean.MeasureWeChat;
import com.example.gxkj.newmeasure.bean.UserData;
import com.example.gxkj.newmeasure.camera.CaptureActivity;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baseapp.AppConfig;
import com.jaydenxiao.common.baseapp.AppManager;
import com.jaydenxiao.common.baserx.RxBus2;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity<MainPresenter, MainModel> implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.scan_img)
    ImageView scan_img;

    private TextView currTimesView, totalTimesView, userNameView;
    private ImageView logoView;

    /*********************************** BLE *********************************/
    private List<BleDevice> bleDeviceList = new ArrayList<>();
    private CommonRecycleViewAdapter<BleDevice> bleDeviceAdapter;
    private MaterialDialog scanResultDialog, cirProgressBarWithScan, cirProgressBarWithChoose;
    private List<String> rxBleDeviceAddressList = new ArrayList<>();

    /*********************************** UI *********************************/
    private String macAddress, deviceName;
    public static final int REQUEST_CODE_CONTRACT = 1201;
    public static final int REQUEST_CODE_MEASURE = 1202;
    private static final int SCAN_HINT = 1001;
    private static final int CODE_HINT = 1002;
    private ArrayList<ContractNumWithPartsData.Parts> partsArrayList = new ArrayList<>();
    private String contractID;
    private BDLocation location;


    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGO.onResume(this);
        mPresenter.getUserDataRequset();
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        navView.getMenu().removeItem(R.id.nav_device);
        toolbar.setBackgroundColor(getResources().getColor(R.color.main_color));

        initListener();
        initDrawerMenuContent();
        initRxBus2FindBle();

        //此处填上在http://fir.im/注册账号后获得的API_TOKEN以及APP的应用ID
        UpdateKey.API_TOKEN = AppConfig.API_FIRE_TOKEN;
        UpdateKey.APP_ID = AppConfig.APP_FIRE_ID;
        //如果你想通过Dialog来进行下载，可以如下设置
        UpdateKey.DialogOrNotification = UpdateKey.WITH_DIALOG;
        UpdateFunGO.init(this);//Android的自动更新库
    }

    private void initListener() {
        scan_img.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_MEASURE);
        });

        location = AppApplication.getmLocationClient().getLastKnownLocation();//放在start后面，防止还没有获取到BDLocation的情况
        if (location != null) {
            AppConstant.LOCATION_ADDRESS = location.getAddrStr();
            if (AppConstant.LOCATION_ADDRESS == null) {
                AppConstant.LOCATION_ADDRESS = "定位失败";
            } else {
                LogUtils.loge("用户登陆的位置信息：" + AppConstant.LOCATION_ADDRESS);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == REQUEST_CODE_CONTRACT) {
                Bundle bundle = data.getExtras();
                String result = bundle.getString("result");
                LogUtils.loge("二维码解析====" + result);
                switch (resultCode) {
                    case SCAN_HINT:
                        if (result != null) {
                            //修改合同号请求判断
                            mPresenter.changeContractNumRequest(result);
                        } else {
                            ToastUtil.showShort(getString(R.string.scan_qrcode_failed));
                        }
                        break;
                    default:
                        break;
                }
            } else if (requestCode == REQUEST_CODE_MEASURE) {
                Bundle bundle = data.getExtras();
                String result = bundle.getString("result");
                LogUtils.loge("二维码解析====" + result);
                switch (resultCode) {
                    case SCAN_HINT:
                        if (result != null) {
                            //微信用户没有HTTP开头,合同成员是HTTP开头的，区分不传合同号即可
                            if (result.contains("http")) {

                                int tidIndex = result.indexOf("tid") + "tid".length() + 1;
                                String s = result.substring(tidIndex);
                                String tid = s.substring(s.indexOf("?") + 1, s.length());
                                LogUtils.loge(tid);
                                mPresenter.MeasureCustomerDataRequest(tid);
                            } else {
                                //微信用户
                                mPresenter.MeasureWeChatDataRequest(result);
                            }
                        } else {
                            ToastUtil.showShort(getString(R.string.scan_qrcode_failed));
                        }
                        break;
                    case CODE_HINT:
                        break;
                    default:
                        break;
                }
            }
        }

    }

    private void initRxBus2FindBle() {
        //监听是否发现附近蓝牙
        mRxManager.on(AppConstant.NO_BLE_FIND, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isChecked) throws Exception {
                if (isChecked) {
                    ToastUtil.showShort("附近没有可见设备！请重试");
                }
            }
        });
    }

    private void initDrawerMenuContent() {
        initBleState();
        configureBleList();
        //初始化加载合同号(合同号也需要重网络获取，包括默认的)
        if (!SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.CONTRACT_NUM).isEmpty()) {
            mPresenter.changeContractNumRequest(SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.CONTRACT_NUM));
        } else {
            mPresenter.changeContractNumRequest("default");
        }
        View headerView = navView.getHeaderView(0);
        currTimesView = headerView.findViewById(R.id.curr_times);
        totalTimesView = headerView.findViewById(R.id.total_times);
        userNameView = headerView.findViewById(R.id.user_name);
        logoView = headerView.findViewById(R.id.logo);
    }

    private void scanAndConnectBle() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        //先判断蓝牙是否打开
        if (!defaultAdapter.isEnabled()) {
            new MaterialDialog.Builder(this)
                    .content("是否打开蓝牙")
                    .positiveText("打开")
                    .negativeText("取消")
                    .backgroundColor(getResources().getColor(R.color.white))
                    .contentColor(getResources().getColor(R.color.primary))
                    .onPositive((dialog, which) -> defaultAdapter.enable())
                    .show();
        } else {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.requestEach(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(permission -> { // will emit 2 Permission objects
                        if (permission.granted) {

                            if (isLocationEnabled()) {
                                cirProgressBarWithScan.show();
                                Timer timer = new Timer();
                                timer = new Timer(true);
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        if (cirProgressBarWithScan.isShowing()) {
                                            cirProgressBarWithScan.dismiss();
                                            RxBus2.getInstance().post(AppConstant.NO_BLE_FIND, true);
                                        }
                                    }
                                }, 6000);
                                rxBleDeviceAddressList.clear();
                                bleDeviceList.clear();
                                mPresenter.getBleDeviceDataRequest();
                            } else {
                                ToastUtil.showShort("需要开启位置信息才能支持蓝牙搜索");
                            }


                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            ToastUtil.showShort("未授予位置信息权限,请手动开启");
                        }
                    });
        }
    }

    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    private void configureBleList() {
        bleDeviceAdapter = new CommonRecycleViewAdapter<BleDevice>(this, R.layout.item_bledevice, bleDeviceList) {
            @Override
            public void convert(ViewHolderHelper helper, BleDevice bleDevice) {
                TextView text_name = helper.getView(R.id.text_name);
                TextView text_mac = helper.getView(R.id.text_mac);
                TextView text_rssi = helper.getView(R.id.text_rssi);
                text_name.setText(bleDevice.getName());
                text_mac.setText(bleDevice.getAddress());
                text_rssi.setText(String.valueOf(bleDevice.getRssi()));

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //连接蓝牙
                        mPresenter.chooseDeviceConnectRequest(text_mac.getText().toString());
                        if (scanResultDialog != null) {
                            scanResultDialog.dismiss();
                        }
                    }
                });

            }
        };

        scanResultDialog = new MaterialDialog.Builder(this)
                .title(R.string.choose_device_prompt)
                .content("已检测到的蓝牙设备...")
                .backgroundColor(getResources().getColor(R.color.white))
                .titleColor(getResources().getColor(R.color.scan_result_list_title))
                .adapter(bleDeviceAdapter, null)
                .dividerColor(getResources().getColor(R.color.white))
                .build();

        cirProgressBarWithScan = new MaterialDialog.Builder(this)
                .progress(true, 100)
                .content("扫描附近蓝牙...")
                .backgroundColor(getResources().getColor(R.color.white))
                .build();

        cirProgressBarWithChoose = new MaterialDialog.Builder(this)
                .progress(true, 100)
                .content("配对中...")
                .backgroundColor(getResources().getColor(R.color.white))
                .build();
    }

    private void initBleState() {
        macAddress = SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.MAC_ADDRESS);
        if (TextUtils.isEmpty(macAddress)) {
            navView.getMenu().add(R.id.device, R.id.nav_device, 0, "连接智能尺").setIcon(R.drawable.ic_blueteeth_unconnected);
        } else {
            deviceName = SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.MAC_DEVICE_NAME);
            updateBlueToothState();
//            updateBlueToothState(deviceName);
        }
    }

    public void updateBlueToothState() {
        //先清除蓝牙设备信息菜单项
        navView.getMenu().removeItem(R.id.nav_device);
        navView.getMenu()
                .add(R.id.device, R.id.nav_device, 0, "连接智能尺(已绑定)")
                .setIcon(R.drawable.ic_blueteeth_connected);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_logout:
                new MaterialDialog.Builder(this)
                        .title("确定要退出登录？")
                        .titleColor(getResources().getColor(R.color.ff5001))
                        .positiveText(R.string.sure)
                        .negativeText(R.string.cancel)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .onPositive((dialog, which) -> {
                            drawerLayout.closeDrawers();
                            dialog.dismiss();
                            SPUtils.setSharedStringData(AppApplication.getAppContext(), AppConstant.LOGIN_TOKEN, "");
                            AppManager.getAppManager().finishAllActivity();
                            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                            startActivity(intent);
                        })
                        .show();
                break;
            case R.id.nav_instruction:
                ToastUtil.showShort("说明文档开发中");
                break;
            case R.id.nav_device:
                String macAddress = SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.MAC_ADDRESS);
                if (TextUtils.isEmpty(macAddress)) {
                    scanAndConnectBle();
                } else {
                    new MaterialDialog.Builder(this)
                            .title("已绑定智能尺: " + macAddress + "，需要连接新智能尺？")
                            .titleColor(getResources().getColor(R.color.ff5001))
                            .positiveText(R.string.sure)
                            .negativeText(R.string.cancel)
                            .backgroundColor(getResources().getColor(R.color.white))
                            .onPositive((dialog, which) -> {
                                scanAndConnectBle();
                            })
                            .show();
                }
                break;
            case R.id.nav_offset_setting:
//                new MaterialDialog.Builder(this)
//                        .content(R.string.input_offset)
//                        .inputType(InputType.TYPE_CLASS_NUMBER)
//                        .input(R.string.input_offset_hint, R.string.default_value, (dialog, offset) -> {
//                            dialog.dismiss();
//                            String s = offset.toString();
//                            float v = 0.0f;
//                            try {
//                                v = !TextUtils.isEmpty(s) ? Float.parseFloat(s) : 0.0f;
//                            } catch (NumberFormatException e) {
//                                v = 0.0f;
//                            }
//                            instance.setMeasureOffset(v);
//                        }).show();
                ToastUtil.showShort("已经设置默认偏移量1.4cm");
                break;
            case R.id.nav_contract:
                new MaterialDialog.Builder(this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .title(R.string.contract_setting)
                        .content(R.string.contract_instruction)
                        .positiveText(R.string.new_contract)
                        .negativeText(R.string.default_contract)
                        .onPositive((dialog, action) -> {
                                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                                    startActivityForResult(intent, REQUEST_CODE_CONTRACT);
                                }
                        )
                        .onNegative((dialog, action) -> {
                                    mPresenter.changeContractNumRequest("default");
                                }
                        )
                        .show();
                break;
            case R.id.nav_account:
                Intent pwdIntent = new Intent(this, ManagePassWordActicity.class);
                startActivity(pwdIntent);
                break;
            case R.id.nav_feedback:
                ToastUtil.showShort("暂不支持反馈");
                break;
            case R.id.nav_link:
                Intent contactIntent = new Intent(this, ContactActivity.class);
                startActivity(contactIntent);
                break;
            case R.id.nav_version:
                drawerLayout.closeDrawers();
                UpdateFunGO.manualStart(this);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void returnGetUserData(UserData userData) {
        userNameView.setText(userData.getName());
        currTimesView.setText(String.valueOf(userData.getMonthCount()));
        totalTimesView.setText(String.valueOf(userData.getTotalCount()));
    }

    //获取附近的蓝牙设备
    @Override
    public void returnGetBleDeviceData(ScanResult scanResult) {
        if (scanResult != null) {
            RxBleDevice device = scanResult.getBleDevice();
            if (!rxBleDeviceAddressList.contains(device.getMacAddress())) {//避免重复添加设备
                rxBleDeviceAddressList.add(device.getMacAddress());
                bleDeviceList.add(new BleDevice(device.getName(), device.getMacAddress(), scanResult.getRssi()));
                bleDeviceAdapter.notifyDataSetChanged();
            }

            if (rxBleDeviceAddressList.size() != 0 && cirProgressBarWithScan.isShowing()) {
                cirProgressBarWithScan.dismiss();
                scanResultDialog.show();
            }
        }
    }

    //设置对应蓝牙的UUID和MAC
    @Override
    public void returnChooseDeviceConnectWithSetUuidAndMacAddress(RxBleDeviceServices deviceServices, String macAddress) {
        SPUtils.setSharedStringData(AppApplication.getAppContext(), AppConstant.MAC_ADDRESS, macAddress);
        updateBlueToothState();
        for (BluetoothGattService service : deviceServices.getBluetoothGattServices()) {
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                if (isCharacteristicNotifiable(characteristic)) {
                    SPUtils.setSharedStringData(AppApplication.getAppContext(), AppConstant.UUID, characteristic.getUuid().toString());
                    cirProgressBarWithChoose.dismiss();
                    ToastUtil.showShort("蓝牙配对成功");
                    break;
                }
            }
        }
    }

    private boolean isCharacteristicNotifiable(BluetoothGattCharacteristic characteristic) {
        return (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
    }

    //修改合同号返回判断
    @Override
    public void returnChangeContractNum(ContractNumWithPartsData contractNumWithPartsData, String contract) {
        if (contractNumWithPartsData.getName() != null) {
            LogUtils.loge(contractNumWithPartsData.getName());
            navView.getMenu().removeItem(R.id.nav_contract);
            navView.getMenu()
                    .add(R.id.device, R.id.nav_contract, 1, "合同号(" + contractNumWithPartsData.getName() + ")")
                    .setIcon(R.drawable.ic_contract);
            SPUtils.setSharedStringData(AppApplication.getAppContext(), AppConstant.CONTRACT_ID_SP, contractNumWithPartsData.get_id());//设置合同id
        } else {
            navView.getMenu().removeItem(R.id.nav_contract);
            navView.getMenu()
                    .add(R.id.device, R.id.nav_contract, 1, "合同号(默认)")
                    .setIcon(R.drawable.ic_contract);
            SPUtils.setSharedStringData(AppApplication.getAppContext(), AppConstant.CONTRACT_ID_SP, "default");//设置合同id
        }
        SPUtils.setSharedStringData(AppApplication.getAppContext(), AppConstant.CONTRACT_NUM, contract);//设置合同号，下次初始化进入直接加载对应的合同号
        partsArrayList = contractNumWithPartsData.getParts();
        LogUtils.loge(String.valueOf(partsArrayList.size()));
    }

    //根据合同号获取的合同成员信息
    @Override
    public void returnMeasureCustomerData(MeasureCustomer measureCustomer, String tid) {
        if (partsArrayList != null && partsArrayList.size() != 0) {
            contractID = SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.CONTRACT_ID_SP);
            MeasureActivity.startAction(MainActivity.this, partsArrayList, measureCustomer.getAvatar(), measureCustomer.getName(), measureCustomer.getGender(), 1, tid, contractID, -1);
        } else {
            ToastUtil.showShort("请先设置合同号");
        }
    }

    //根据合同号获取的微信用户信息
    @Override
    public void returnMeasureWeChatData(MeasureWeChat measureWeChat) {
//        if (SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.CONTRACT_NUM).equals("default")) {
//
//        } else {
//            ToastUtil.showShort("当前为合同量体请先恢复为默认量体再进行微信用户的量体");
//        }
        if (partsArrayList != null && partsArrayList.size() != 0) {
            contractID = SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.CONTRACT_ID_SP);
            MeasureActivity.startAction(MainActivity.this, partsArrayList, measureWeChat.getAvatar(), measureWeChat.getNickname(), measureWeChat.getGender(), 2, measureWeChat.getOpenID(), contractID, measureWeChat.getMeasureTimes());
        } else {
            ToastUtil.showShort("请先设置合同号");
        }

    }

    @Override
    public void showLoading(String title) {
        if (title == "chooseConnect") {
            cirProgressBarWithChoose.show();
        }
    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        //蓝牙连接失败
        if (msg == "connectFail") {
            cirProgressBarWithChoose.dismiss();
        }
        //用户信息的token过期时
        if (msg == "token过期") {
            SPUtils.setSharedStringData(AppApplication.getAppContext(), AppConstant.LOGIN_TOKEN, "");
            AppManager.getAppManager().finishAllActivity();
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
            ToastUtil.showShort("用户信息已经过期,请重新登录");
            return;
        }
        ToastUtil.showShort(msg);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGO.onStop(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navView)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}
