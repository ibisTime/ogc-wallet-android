package com.cdkj.token.views;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cdkj.token.R;
import com.cdkj.token.databinding.LayoutMyPsswordViewBinding;

/**
 *
 */

public class MyPasswordView extends LinearLayout {

    private LayoutMyPsswordViewBinding mBinding;

    public MyPasswordView(Context context) {
        this(context, null);
    }

    public MyPasswordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        this(context, attrs, 0);
        //        //可以自定义  样式
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.sign_edit_clear_layout);
//        hintText = ta.getString(R.styleable.sign_edit_clear_layout_hint_text);

        init();
    }

    private void init() {

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_my_pssword_view, this, true);
        mBinding.etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = mBinding.etText.getText().toString();
                if (text.length() >= 6) {
                    return;
                }
                switch (text.length()) {
                    case 1:
                        mBinding.tv1.setText(charSequence);
                        //主线程睡觉也是  不得已 没想到更好的办法
                        break;
                    case 2:
                        mBinding.tv2.setText(charSequence);
                        break;
                    case 3:
                        mBinding.tv3.setText(charSequence);
                        break;
                    case 4:
                        mBinding.tv4.setText(charSequence);
                        break;
                    case 5:
                        mBinding.tv5.setText(charSequence);
                        break;
                    case 6:
                        mBinding.tv6.setText(charSequence);
                        break;
                    default:
                        mBinding.tv1.setText("");
                        mBinding.tv2.setText("");
                        mBinding.tv3.setText("");
                        mBinding.tv4.setText("");
                        mBinding.tv5.setText("");
                        mBinding.tv6.setText("");
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                switch (mBinding.etText.getText().toString().length()) {
                    case 0:
                        mBinding.tv1.setText("");
                        mBinding.tv2.setText("");
                        mBinding.tv3.setText("");
                        mBinding.tv4.setText("");
                        mBinding.tv5.setText("");
                        mBinding.tv6.setText("");

                        break;
                    case 1:
                        mBinding.tv1.setText("●");
                        mBinding.tv2.setText("");
                        mBinding.tv3.setText("");
                        mBinding.tv4.setText("");
                        mBinding.tv5.setText("");
                        mBinding.tv6.setText("");
                        break;
                    case 2:
                        mBinding.tv1.setText("●");
                        mBinding.tv2.setText("●");
                        mBinding.tv3.setText("");
                        mBinding.tv4.setText("");
                        mBinding.tv5.setText("");
                        mBinding.tv6.setText("");
                        break;
                    case 3:
                        mBinding.tv1.setText("●");
                        mBinding.tv2.setText("●");
                        mBinding.tv3.setText("●");
                        mBinding.tv4.setText("");
                        mBinding.tv5.setText("");
                        mBinding.tv6.setText("");
                        break;
                    case 4:
                        mBinding.tv1.setText("●");
                        mBinding.tv2.setText("●");
                        mBinding.tv3.setText("●");
                        mBinding.tv4.setText("●");
                        mBinding.tv5.setText("");
                        mBinding.tv6.setText("");
                        break;
                    case 5:
                        mBinding.tv1.setText("●");
                        mBinding.tv2.setText("●");
                        mBinding.tv3.setText("●");
                        mBinding.tv4.setText("●");
                        mBinding.tv5.setText("●");
                        mBinding.tv6.setText("");
                        break;
                    case 6:
                        mBinding.tv1.setText("●");
                        mBinding.tv2.setText("●");
                        mBinding.tv3.setText("●");
                        mBinding.tv4.setText("●");
                        mBinding.tv5.setText("●");
                        mBinding.tv6.setText("●");
                        break;
                    default:
                        mBinding.tv1.setText("●");
                        mBinding.tv2.setText("●");
                        mBinding.tv3.setText("●");
                        mBinding.tv4.setText("●");
                        mBinding.tv5.setText("●");
                        mBinding.tv6.setText("●");
                }
            }
        });
    }

    public String getText() {
        if (mBinding == null || mBinding.etText == null || TextUtils.isEmpty(mBinding.etText.getText().toString())) {
            return "";
        }
        return mBinding.etText.getText().toString().trim();
    }

    public void clean() {
        mBinding.etText.setText("");
    }
}
