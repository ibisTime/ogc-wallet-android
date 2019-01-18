package com.cdkj.token.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityBindBankCardBinding;
import com.cdkj.token.model.BankTypeModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


public class AddBackCardActivity extends AbsLoadActivity {

    private ActivityBindBankCardBinding mBinding;

    private String[] mBankNames;
    private String[] mBankCodes;
    private String mSelectCardId;//选择的银行卡ID


    /**
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, AddBackCardActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_bind_bank_card, null, false);
        return mBinding.getRoot();

    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("绑定银行卡");

        //添加银行类型
        mBinding.txtBankName.setOnClickListener(v -> {
            getBankBrand();
        });
//如果已经实名认证则不能更改持卡人
//        if (!TextUtils.isEmpty(SPUtilHelpr.getUserName())) {
//            mBinding.editName.setText(SPUtilHelpr.getUserName());
//            mBinding.editName.setEnabled(false);
//        } else {
//            mBinding.editName.setEnabled(true);
//        }

        //添加银行卡
        mBinding.txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mBinding.editName.getText().toString())) {
                    showToast("请输入开户名");
                    return;
                }
                if (TextUtils.isEmpty(mSelectCardId)) {
                    showToast("请选择开户行");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtCardZH.getText().toString().trim())) {
                    showToast("请填写开户支行");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtCardId.getText().toString())) {
                    showToast("请输入银行卡号");
                    return;
                }

                if (mBinding.edtCardId.getText().toString().length() < 16) {
                    showToast("银行卡号最低为16位数字");
                    return;
                }
                addCard();
            }
        });
    }


    //绑定银行卡
    public void addCard() {

        Map<String, String> object = new HashMap<>();
        object.put("bankCode", mSelectCardId);
        object.put("bankName", mBinding.txtBankName.getText().toString().trim());
        object.put("bankcardNumber", mBinding.edtCardId.getText().toString().trim());
        object.put("realName", mBinding.editName.getText().toString().trim());
        object.put("subbranch", mBinding.edtCardZH.getText().toString().trim());
        object.put("type", "0");//0 银行卡 1支付宝
        object.put("userId", SPUtilHelper.getUserId());
//        object.put("systemCode", AppConfig.SYSTEMCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).addBankCard("802024", StringUtils.getRequestJsonString(object));
        addCall(call);
        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack(this) {
            @Override
            protected void onSuccess(Object data, String SucMessage) {
                showToast("银行卡添加成功");
                finish();
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }


    /**
     * 获取银行卡渠道
     */
    private void getBankBrand() {

        Call<BaseResponseListModel<BankTypeModel>> backTypeList = RetrofitUtils.createApi(MyApi.class).getBackTypeList("802116", "{}");
        addCall(backTypeList);
        showLoadingDialog();
        backTypeList.enqueue(new BaseResponseListCallBack<BankTypeModel>(this) {
            @Override
            protected void onSuccess(List<BankTypeModel> data, String SucMessage) {
                mBankNames = new String[data.size()];
                mBankCodes = new String[data.size()];
                int i = 0;
                for (BankTypeModel b : data) {
                    mBankNames[i] = b.getBankName();
                    mBankCodes[i] = b.getBankCode();
                    LogUtil.E("银行卡code" + b.getBankCode());
                    i++;
                }
                if (mBankNames.length != 0 && mBankNames.length == mBankCodes.length) {
                    chooseBankCard();
                }
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    /**
     * 选择银行卡  弹窗
     */
    private void chooseBankCard() {
        new AlertDialog.Builder(this).setTitle("请选择银行").setSingleChoiceItems(
                mBankNames, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        txtBankCard.setText(list.get(which).getBankName());
                        mBinding.txtBankName.setText(mBankNames[which]);
                        mSelectCardId = mBankCodes[which];
                        LogUtil.E("选择银行卡code" + mSelectCardId);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
    }
}
