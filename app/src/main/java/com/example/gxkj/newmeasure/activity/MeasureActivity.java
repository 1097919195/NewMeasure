package com.example.gxkj.newmeasure.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.aspsine.irecyclerview.universaladapter.recyclerview.OnItemClickListener;
import com.example.gxkj.newmeasure.BuildConfig;
import com.example.gxkj.newmeasure.Contract.MeasureContract;
import com.example.gxkj.newmeasure.Model.MeasureModel;
import com.example.gxkj.newmeasure.Presenter.MeasurePresenter;
import com.example.gxkj.newmeasure.R;
import com.example.gxkj.newmeasure.app.AppApplication;
import com.example.gxkj.newmeasure.app.AppConstant;
import com.example.gxkj.newmeasure.bean.ContractNumWithPartsData;
import com.example.gxkj.newmeasure.bean.HttpResponse;
import com.example.gxkj.newmeasure.bean.MultipartBeanWithUserData;
import com.example.gxkj.newmeasure.utils.BitmapUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baserx.RxBus2;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class MeasureActivity extends BaseActivity<MeasurePresenter, MeasureModel> implements MeasureContract.View {
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
    @BindView(R.id.camera_add)
    RelativeLayout camera_add;
    @BindView(R.id.save_measure_result)
    Button save_measure_result;
    @BindView(R.id.unmeasured_item_hint)
    TextView unmeasured_item_hint;

    @BindView(R.id.frame_1)
    FrameLayout frame1;
    @BindView(R.id.frame_2)
    FrameLayout frame2;
    @BindView(R.id.frame_3)
    FrameLayout frame3;
    @BindView(R.id.img_1)
    ImageView img1;
    @BindView(R.id.img_2)
    ImageView img2;
    @BindView(R.id.img_3)
    ImageView img3;

    ArrayList<ContractNumWithPartsData.Parts> partsArrayList = new ArrayList<>();
    CommonRecycleViewAdapter<ContractNumWithPartsData.Parts> adapter;
    public static final String FEMALE = "女";
    public static final String MALE = "男";

    public SpeechSynthesizer speechSynthesizer;//提供对已安装的语音合成引擎的功能的访问
    String mac = SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.MAC_ADDRESS);
    String uuidString = SPUtils.getSharedStringData(AppApplication.getAppContext(), AppConstant.UUID);
    UUID characteristicUUID = null;

    int unMeasuredCounts = 0;
    int measuredCounts = 0;
    int itemPostion = 0;
    int itemPostionAgo = 0;
    boolean remuasure = false;
    List<Integer> canRemeasureData = new ArrayList<>();
    private List<FrameLayout> unVisibleView = new ArrayList<>();
    public static final int BATTERY_LOW = 30;
    public static final int BATTERY_HIGH = 80;
    public static final int TAKE_PHOTO = 1222;
    // 保存图片的文件
    private File imageFile;
    private Uri imageUri;
    public static final File PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    public static final String JPG_SUFFIX = ".jpg";
    private String picName;
    MenuItem batteryItem;


    public static void startAction(Activity activity, ArrayList<ContractNumWithPartsData.Parts> partsArrayList, String UserPhoto, String UserName, int gender, int signTidOrOpenID, String tidOrOpenID, String contractID) {
        Intent intent = new Intent(activity, MeasureActivity.class);
        intent.putExtra(AppConstant.PARTS_CONTRACT_NUM, partsArrayList);
        intent.putExtra(AppConstant.PARTS_USER_PHOTO, UserPhoto);
        intent.putExtra(AppConstant.PARTS_USER_NAME, UserName);
        intent.putExtra(AppConstant.PARTS_USER_GENDER, gender);
        intent.putExtra(AppConstant.SIGN_TID_OR_OPENID, signTidOrOpenID);//tid为1,openID为2
        intent.putExtra(AppConstant.TID_OR_OPENID, tidOrOpenID);
        intent.putExtra(AppConstant.CONTRACT_ID, contractID);
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
        toolbar.inflateMenu(R.menu.toolbar_menu_battery);
        batteryItem = toolbar.getMenu().getItem(0);
        initRxBus();
        initSpeech();
        initDisplayWithUserInfo();
        initRcycleAdapter();
        initMeasure();
        initListener();
        itemClickRemeasure();
    }

    private void initRxBus() {
        mRxManager.on(AppConstant.DISPLAY_BIG_PART, (Consumer<Integer>) integer -> {
                    unmeasured_item_hint.setText(partsArrayList.get(integer).getName());
                }
        );
    }

    private void itemClickRemeasure() {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                if (canRemeasureData.size() > position) {
                    itemPostion = position;
                    remuasure = true;
                    partsArrayList.get(itemPostionAgo).setSelected(false);
                    if (unMeasuredCounts != 0) {
                        partsArrayList.get(measuredCounts - unMeasuredCounts).setSelected(false);
                    }
                    partsArrayList.get(itemPostion).setSelected(true);
                    RxBus2.getInstance().post(AppConstant.DISPLAY_BIG_PART, itemPostion);
                    adapter.notifyDataSetChanged();
                    itemPostionAgo = itemPostion;
                } else if (canRemeasureData.size() == position) {
                    remuasure = false;
                    itemPostion = position;
                    partsArrayList.get(itemPostionAgo).setSelected(false);
                    if (unMeasuredCounts != 0) {
                        partsArrayList.get(measuredCounts - unMeasuredCounts).setSelected(false);
                    }
                    partsArrayList.get(itemPostion).setSelected(true);
                    RxBus2.getInstance().post(AppConstant.DISPLAY_BIG_PART, itemPostion);
                    adapter.notifyDataSetChanged();
                    itemPostionAgo = itemPostion;
                } else {
                    ToastUtil.showShort("请先按顺序完成第一次测量");
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    @OnClick({R.id.del_1, R.id.del_2, R.id.del_3})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.del_1:
            case R.id.del_2:
            case R.id.del_3:
                delPic((ImageView) v);
                break;
            default:
                break;
        }
    }

    private void initListener() {
        genderLine.setOnClickListener(v -> {
            switchGender();
        });

        camera_add.setOnClickListener(v -> {
            if (unVisibleView.size() > 0) {
                capturePic();
            } else {
                ToastUtil.showShort("已拍照三张特体图片");
            }
        });

        RxView.clicks(save_measure_result)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        String gender = userGender.getText().toString();
                        int sex = 0;
                        if (FEMALE.equals(gender)) {
                            sex = 2;
                        } else if (MALE.equals(gender)) {
                            sex = 1;
                        }
                        if (sex != 0) {
                            if (unMeasuredCounts == 0) {
                                String contractID = getIntent().getStringExtra(AppConstant.CONTRACT_ID);
                                String tidOrOpenID = getIntent().getStringExtra(AppConstant.TID_OR_OPENID);
                                //user_data[]----sex AND tidOrOpenID
                                String tid = tidOrOpenID;
                                String openID = tidOrOpenID;
                                if (getIntent().getIntExtra(AppConstant.SIGN_TID_OR_OPENID, 1) == 1) {
                                    openID = "";
                                } else {
                                    tid = "";
                                }
                                //images
                                MultipartBody.Part[] images = new MultipartBody.Part[3];
                                if (img1.getDrawable() != null) {
                                    images[0] = getSpecialBodyTypePic((String) img1.getTag());
                                }
                                if (img2.getDrawable() != null) {
                                    images[1] = getSpecialBodyTypePic((String) img2.getTag());
                                }
                                if (img3.getDrawable() != null) {
                                    images[2] = getSpecialBodyTypePic((String) img3.getTag());
                                }
//                                partsData
                                MultipartBeanWithUserData multipartData = new MultipartBeanWithUserData(partsArrayList);
//                                List<String> parts = new ArrayList<>();
//                                List<Float> measureValue = new ArrayList<>();
//                                for (ContractNumWithPartsData.Parts partsData : partsArrayList) {
//                                    parts.add(partsData.getName());
//                                }
//                                for (ContractNumWithPartsData.Parts partsData : partsArrayList) {
//                                    measureValue.add(partsData.getValue());
//                                }
//                                String[] stringsParts = (String[]) parts.toArray(new String[parts.size()]);
//                                Float[] stringsValue = (Float[]) measureValue.toArray(new Float[measureValue.size()]);
//                                Object[][] data = {stringsParts, stringsValue};
//
//                                Map<List<String>, List<Float>> dataMap = new HashMap<List<String>, List<Float>>();
//                                dataMap.put(parts, measureValue);


                                //发送保存测量结果请求
                                mPresenter.upLoadMeasureResultRequset(tid, openID, sex, images, multipartData, contractID);
                            } else {
                                ToastUtil.showShort("请先测完所有部位在进行保存！");
                            }
                        } else {
                            ToastUtil.showShort("请先选择性别！");
                        }

                    }
                });
    }

    private void delPic(ImageView view) {
        FrameLayout parent = (FrameLayout) view.getParent();
        ImageView img = (ImageView) parent.getChildAt(0);
        img.setImageDrawable(null);
        view.setVisibility(View.INVISIBLE);
        unVisibleView.add(0, parent);
    }

    /**
     * 读取特体大图
     *
     * @param filename
     * @return
     */
    private MultipartBody.Part getSpecialBodyTypePic(String filename) {
        File f = new File(PATH + File.separator + AppConstant.FILE_PROVIDER_NAME + File.separator + filename + JPG_SUFFIX);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData("images[]", filename, requestFile);
    }

    private void capturePic() {
        Date date = new Date(System.nanoTime());
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(date.toString().getBytes());
            picName = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            picName = date.toString();
        }
        File storageFile = new File(PATH.getAbsoluteFile() + File.separator + AppConstant.FILE_PROVIDER_NAME);
        if (!storageFile.isDirectory()) {
            storageFile.mkdirs();
        }
        File outputImage = new File(storageFile, picName + JPG_SUFFIX);
        imageFile = outputImage;
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            LogUtils.loge(e.toString());
        }
        //将File对象转换为Uri并启动照相程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        // 系统版本大于N的统一用FileProvider处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 将文件转换成content://Uri的形式
            imageUri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".fileprovider", outputImage);
            // 申请临时访问权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            imageUri = Uri.fromFile(outputImage);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                //广播刷新相册
                Intent intentBc1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intentBc1.setData(imageUri);
                this.sendBroadcast(intentBc1);

                Bitmap bitmap = BitmapUtils.decodeUri(this, imageUri, 800, 800);
//                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//                img1.setImageBitmap(bitmap);
                FrameLayout frameLayout = unVisibleView.get(0);
                ImageView imageView = (ImageView) frameLayout.getChildAt(0);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    frameLayout.getChildAt(1).setVisibility(View.VISIBLE);
                    unVisibleView.remove(0);
                    if (!TextUtils.isEmpty(picName)) {
                        imageView.setTag(picName);
                    }
                } else {
                    ToastUtil.showShort("拍照失败！");
                }
                break;
            default:
                break;

        }
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
                .itemsCallbackSingleChoice(index - 1, (dialog, itemView, which, text) -> {
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
        measuredCounts = partsArrayList.size();
        unMeasuredCounts = measuredCounts;
        if (measuredCounts > 0) {
            partsArrayList.get(0).setSelected(true);
            unmeasured_item_hint.setText(partsArrayList.get(0).getName());
        }
        if (mac != "" && uuidString != "") {
            characteristicUUID = UUID.fromString(uuidString);
            mPresenter.startMeasureRequest(characteristicUUID);
            mPresenter.checkBleConnectStateRequest();
        } else {
            ToastUtil.showShort("请先配对蓝牙设备！");
        }

        unVisibleView.clear();
        unVisibleView.add(frame1);
        unVisibleView.add(frame2);
        unVisibleView.add(frame3);
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
        int gender = getIntent().getIntExtra(AppConstant.PARTS_USER_GENDER, 1);
        ImageLoaderUtils.displayBigPhoto(this, wechat_icon, imgUrl);
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
        adapter = new CommonRecycleViewAdapter<ContractNumWithPartsData.Parts>(mContext, R.layout.item_measure_parts, partsArrayList) {
            @Override
            public void convert(ViewHolderHelper helper, ContractNumWithPartsData.Parts parts) {
                TextView part = helper.getView(R.id.parts);
                TextView value = helper.getView(R.id.measureValue);

                part.setText(parts.getName());
                value.setText(String.valueOf(parts.getValue()));

                value.setTextColor(getResources().getColor(R.color.battery_color));
                if (parts.isSelected()) {
                    //选中的样式
                    helper.setBackgroundColor(R.id.parts, getResources().getColor(R.color.item_selector));
                    helper.setBackgroundColor(R.id.measureValue, getResources().getColor(R.color.item_selector));
                } else {
                    //未选中的样式
                    helper.setBackgroundColor(R.id.parts, getResources().getColor(R.color.white));
                    helper.setBackgroundColor(R.id.measureValue, getResources().getColor(R.color.white));
                }

                if (value.getText().toString() == "0.0") {
                    value.setTextColor(getResources().getColor(R.color.c333));
                }
            }
        };

        rcy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//默认分割线
        rcy.setAdapter(adapter);
//        irc.setLayoutManager(new LinearLayoutManager(this));//默认
        rcy.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void returnStartMeasure(Float length, Float angle, int battery) {
        if (battery < BATTERY_LOW) {
            batteryItem.setIcon(R.mipmap.battery_low);
        }
        if (battery >= BATTERY_LOW && battery < BATTERY_HIGH) {
            batteryItem.setIcon(R.mipmap.battery_mid);
        }
        if (battery >= BATTERY_HIGH) {
            batteryItem.setIcon(R.mipmap.battery_high);
        }
        if (remuasure) {
            partsArrayList.get(itemPostion).setValue(length);
            partsArrayList.get(itemPostion).setSelected(false);

            if (unMeasuredCounts != 0) {
                partsArrayList.get(measuredCounts - unMeasuredCounts).setSelected(true);
                RxBus2.getInstance().post(AppConstant.DISPLAY_BIG_PART, measuredCounts - unMeasuredCounts);
                remuasure = false;
            } else {
                remuasure = false;
            }
            adapter.notifyDataSetChanged();
        } else {
            if (unMeasuredCounts != 0) {
                assignValue(length, angle);
            } else {
                ToastUtil.showShort(getString(R.string.measure_completed));
            }
        }
    }

    private void assignValue(Float length, Float angle) {
        try {
            if (unMeasuredCounts != 1) {//这个操作只有蓝牙按下后才会触发，所以unMeasuredCounts不能为1
                partsArrayList.get(measuredCounts + 1 - unMeasuredCounts).setSelected(true);
                RxBus2.getInstance().post(AppConstant.DISPLAY_BIG_PART, measuredCounts + 1 - unMeasuredCounts);
            }
            partsArrayList.get(measuredCounts - unMeasuredCounts).setSelected(false);
            partsArrayList.get(measuredCounts - unMeasuredCounts).setValue(length);
            canRemeasureData.add(measuredCounts - unMeasuredCounts);
            if (unMeasuredCounts != 0) {
                unMeasuredCounts = unMeasuredCounts - 1;
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {

        }

    }

    //蓝牙状态监听提示
    @Override
    public void returnCheckBleConnectState(RxBleConnection.RxBleConnectionState connectionState) {
        RxBleClient rxBleClient = AppApplication.getRxBleClient(this);
        RxBleDevice rxBleDevice = rxBleClient.getBleDevice(mac);
        RxBleConnection.RxBleConnectionState bleState = rxBleDevice.getConnectionState();
        if (bleState == connectionState.DISCONNECTED) {
            ToastUtil.showShort("连接断开");
            mPresenter.startMeasureRequest(characteristicUUID);//自动连接
        }
        if (bleState == connectionState.CONNECTED) {
            ToastUtil.showShort("蓝牙通信成功，请开始测量");
        }
    }

    //上传数据返回提示
    @Override
    public void returnUpLoadMeasureResult(HttpResponse httpResponse) {
        if (httpResponse.getData() != null) {
            ToastUtil.showShort("保存成功！");
            finish();
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
