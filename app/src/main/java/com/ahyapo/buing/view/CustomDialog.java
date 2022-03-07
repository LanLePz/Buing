package com.ahyapo.buing.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahyapo.buing.R;

/**
 * 自定义弹窗
 */
public class CustomDialog extends Dialog {

    TextView tvLoadingTx;
    ImageView ivLoading;

    public CustomDialog(Context context) {
        this(context, R.style.Comment_Dialog_Style, "正在上传...");

    }

    public CustomDialog(Context context, String string) {
        this(context, R.style.Comment_Dialog_Style, string);
    }

    protected CustomDialog(Context context, int theme, String string) {
        super(context, theme);
        setCanceledOnTouchOutside(true);//点击其他区域时 true 关闭弹窗 false 不关闭弹窗
        setContentView(R.layout.dialog_loading);//加载布局
        tvLoadingTx = findViewById(R.id.tv_loading_tx);
        tvLoadingTx.setText(string);
        ivLoading = findViewById(R.id.loading);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_rotate);
        // 使用ImageView显示动画
        ivLoading.startAnimation(hyperspaceJumpAnimation);
        getWindow().getAttributes().gravity = Gravity.CENTER;//居中显示
        getWindow().getAttributes().dimAmount = 0.5f;//背景透明度 取值范围 0 ~ 1
    }

    //关闭弹窗
    @Override
    public void dismiss() {
        super.dismiss();
    }
}
