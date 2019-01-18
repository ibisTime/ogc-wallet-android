package com.cdkj.token.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogPasswInputBinding;

/**
 * @updateDts 2019/1/15
 */
public class PasswordInputDialog extends Dialog {
    private static PasswordInputDialog dialog = null;

    private final DialogPasswInputBinding mBinding;
    private Activity mActivity;

    private PasswordInputDialog(Activity context) {
        super(context);
        this.mActivity = context;
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_passw_input, null, false);
        init();
    }


    private void init() {
    }

    /**
     * 单利模式
     * <p>
     * 双重检查线程安全的  懒汉式单利
     *
     * @param context
     * @return
     */
    public static PasswordInputDialog build(Activity context) {
        if (dialog == null) {
            synchronized (PasswordInputDialog.class) {
                if (dialog == null) {
                    dialog = new PasswordInputDialog(context);
                }
            }
        }
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
//        int screenWidth = DisplayHelper.getScreenWidth(mActivity);
//        int screenHeight = DisplayHelper.getScreenHeight(mActivity);
        setContentView(mBinding.getRoot());
//        getWindow().setLayout((int) (screenWidth * 0.9f), (int) (screenHeight * 0.6));
//        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    /**
     * 此方法需在show之后调用方生效
     *
     * @param title1
     * @param title2
     * @return
     */
    public PasswordInputDialog setView(String title1, String title2) {
        mBinding.tvTitle.setText(title1);
        mBinding.tvNumber.setText(title2);
        return this;
    }

    @Override
    public void show() {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        mBinding.myPVPsw.clean();
        super.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    /**
     * 消极的 取消事件
     *
     * @param cancel
     */
    public PasswordInputDialog setOnNegativeListener(View.OnClickListener cancel) {

        if (cancel == null) {
            mBinding.tvCancel.setOnClickListener(view -> dismiss());
        } else {
            mBinding.tvCancel.setOnClickListener(cancel);
        }
        return this;
    }

    /**
     * 积极的  确定事件
     *
     * @param cancel
     */
    public PasswordInputDialog setOnPositiveListener(PositiveListener cancel) {
        if (cancel == null) {
            mBinding.tvConfirm.setOnClickListener(view -> dismiss());
        } else {
            mBinding.tvConfirm.setOnClickListener(view -> {
                cancel.onSuccer(mBinding.tvConfirm, mBinding.myPVPsw.getText());
            });
        }
        return this;
    }

    public interface PositiveListener {
        void onSuccer(View view, String psaaword);
    }
}
