package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.adapter.UserBankCardAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.UserBankCardModel;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class UserBackCardActivity extends AbsRefreshListActivity {

    private boolean mIsselect;//用户打开类型是否是选择银行卡
    /**
     * @param context
     */
    public static void open(Context context, boolean isSelect) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserBackCardActivity.class);
        intent.putExtra("isSelect", isSelect);
        context.startActivity(intent);
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initRefreshHelper(RefreshHelper.LIMITE);
        init();
        initOnClickListener();
    }

    private void initOnClickListener() {
        mBaseBinding.titleView.setRightFraClickListener(view -> {
            AddBackCardActivity.open(this);
        });
    }

    private void init() {
        mBaseBinding.titleView.setMidTitle("我的银行卡");
        mBaseBinding.titleView.setRightTitle("绑定");
        if (getIntent() != null) {
            mIsselect = getIntent().getBooleanExtra("isSelect", true);
        }
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        UserBankCardAdapter mAdapter = new UserBankCardAdapter(listData);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mIsselect) {//如果是选择银行卡
                EventBus.getDefault().post(listData.get(position));
                finish();
            } else {
                //此处可修改银行卡 但是没有改功能
//                UpdateBackCardActivity.open(mContext, bankCardModel);
            }
        });

        //条目按钮点击事件
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            UserBankCardModel.ListBean currentItem = (UserBankCardModel.ListBean) adapter.getData().get(position);

            switch (view.getId()) {
                case R.id.cb_default:
                    //设置默认一银行卡
                    CommonDialog defaultDialog = new CommonDialog(this).builder()
                            .setTitle(getString(com.cdkj.baselibrary.R.string.activity_base_tip)).setContentMsg("确定设为默认银行卡")
                            .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), view1 -> {
                                setDefaultOrUntyingBankCard("0", currentItem.getCode());
                            })
                            .setNegativeBtn(getString(com.cdkj.baselibrary.R.string.activity_base_cancel), view1 -> {
                                adapter.notifyDataSetChanged();
                            }, false);

                    defaultDialog.show();
                    break;
                case R.id.tv_untying:
                    //解绑银行卡
                    CommonDialog untyingDialog = new CommonDialog(this).builder()
                            .setTitle(getString(com.cdkj.baselibrary.R.string.activity_base_tip)).setContentMsg("确定解绑银行卡")
                            .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), view1 -> {
                                setDefaultOrUntyingBankCard("1", currentItem.getCode());
                            })
                            .setNegativeBtn(getString(com.cdkj.baselibrary.R.string.activity_base_cancel), null, false);
                    untyingDialog.show();
                    break;
            }
        });
        return mAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("limit", limit + "");
        map.put("start", pageindex + "");
        map.put("status", "0");//0是可用的   1是 已解绑的
        map.put("type", "0");//0银行卡  1支付宝
        map.put("userId", SPUtilHelper.getUserId());
        Call<BaseResponseModel<UserBankCardModel>> bankData = RetrofitUtils.createApi(MyApi.class).getBankData("802030", StringUtils.getRequestJsonString(map));
        addCall(bankData);
        showLoadingDialog();
        bankData.enqueue(new BaseResponseModelCallBack<UserBankCardModel>(this) {
            @Override
            protected void onSuccess(UserBankCardModel data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.bank_list_empty), 0);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

    /**
     * 设置默认银行卡  或者解绑一行卡
     *
     * @param type     0是设置默认一行卡  1是解绑银行卡
     * @param bankCode
     */
    private void setDefaultOrUntyingBankCard(String type, String bankCode) {

        Map<String, String> map = new HashMap<>();
        String urlCode;
        if (type.equals("0")) {
            urlCode = "802026";
        } else {
            urlCode = "802025";
        }
        map.put("code", bankCode);
        map.put("userId", SPUtilHelper.getUserId());
        Call<BaseResponseModel<IsSuccessModes>> setDefaultBankCard = RetrofitUtils.createApi(MyApi.class).setDefaultOrUntyingBankCard(urlCode, StringUtils.getRequestJsonString(map));
        addCall(setDefaultBankCard);
        showLoadingDialog();
        setDefaultBankCard.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (type.equals("0")) {
                    ToastUtil.show(UserBackCardActivity.this, "设置成功");
                } else {
                    ToastUtil.show(UserBackCardActivity.this, "解绑成功");
                }
                mRefreshHelper.onDefaluteMRefresh(true);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshHelper.onDefaluteMRefresh(true);
    }
}
