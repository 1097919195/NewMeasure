package com.jaydenxiao.common.commonwidget;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Administrator on 2018/6/20 0020.
 */

public class MaterialDialogWithProgress {

    private static MaterialDialog CircleProgress;

    public static MaterialDialog showDialog(Activity context, String msg) {
        CircleProgress = new MaterialDialog.Builder(context)
                .progress(true, 100)
                .content(msg)
                .show();
        return CircleProgress;
    }

    public static void dissmissDialog() {
        if (CircleProgress != null) {
            CircleProgress.dismiss();
        }
    }
}
