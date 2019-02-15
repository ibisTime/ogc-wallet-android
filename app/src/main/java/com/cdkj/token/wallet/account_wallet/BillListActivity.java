package com.cdkj.token.wallet.account_wallet;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.BillListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityWalletBillBinding;
import com.cdkj.token.model.BillModel;
import com.cdkj.token.model.CoinAddressShowModel;
import com.cdkj.token.model.MoneyTransactionTypeModel;
import com.cdkj.token.model.WalletModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.wallet.private_wallet.WalletAddressShowActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 中心化钱包流水
 * Created by cdkj on 2018/5/25.
 */

public class BillListActivity extends AbsLoadActivity {

    private ActivityWalletBillBinding mBinding;

    private WalletModel.AccountListBean mAccountBean;
    private BillListAdapter mBillAdapter;

    private BaseRefreshCallBack refreshCallBackback;
    private RefreshHelper refreshHelper;

    private List<MoneyTransactionTypeModel> checkDate;
    private String filterType = "";
//    private String kind = "0";

//    private List<ScrollPicker.ScrollPickerData> filterTypeList; //筛选pop数据
//    private PickerPop filterPickerPop;


    public static void open(Context context, WalletModel.AccountListBean mAccountBean) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, BillListActivity.class).putExtra(CdRouteHelper.DATASIGN, mAccountBean));
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallet_bill, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setStatusBarBlue();
        initFilterTypeList();
        if (getIntent() == null)
            return;
        mAccountBean = (WalletModel.AccountListBean) getIntent().getSerializableExtra(CdRouteHelper.DATASIGN);
        initCallBack();
        initData();
        initView();
        initListener();
    }

    void initFilterTypeList() {

//        filterTypeList = new ArrayList<>();
//
//        String[] bizType = new String[]{"", "charge", "withdraw", "withdrawfee", "redpacket_back", "sendredpacket_in", "sendredpacket_out", "jf_lottery_in"};
//
//        String[] types = new String[]{getStrRes(R.string.bill_type_all), getStrRes(R.string.bill_type_charge), getStrRes(R.string.bill_type_withdraw),
//                getStrRes(R.string.bill_type_withdrawfee), getString(R.string.redpacket_back), getString(R.string.redpacket_get), getString(R.string.send_red_package), getString(R.string.lottery)};
//
//        for (int i = 0; i < types.length; i++) {
//            BillFilterModel billFilterModel = new BillFilterModel();
//            billFilterModel.setItemText(types[i]);
//            billFilterModel.setType(bizType[i]);
//            filterTypeList.add(billFilterModel);
//        }

        HashMap<String, String> map = new HashMap<>();
        map.put("parentKey", "app_jour_biz_type_user");
        Call<BaseResponseListModel<MoneyTransactionTypeModel>> moneyTransactionType = RetrofitUtils.createApi(MyApi.class).getMoneyTransactionType("630036", StringUtils.getRequestJsonString(map));
        addCall(moneyTransactionType);
        showLoadingDialog();
        moneyTransactionType.enqueue(new BaseResponseListCallBack<MoneyTransactionTypeModel>(this) {
            @Override
            protected void onSuccess(List<MoneyTransactionTypeModel> data, String SucMessage) {
                if (data == null) {
                    return;
                }
                BillListActivity.this.checkDate = data;
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    private void initView() {
//        ImgUtils.loadCircleImg(this, getCoinIconByCoinSymbol(mAccountBean.getCoinSymbol()), mBinding.ivIcon);
        mBinding.tvFilter.setVisibility(View.VISIBLE);

        mBinding.tvInMoney.setText("转入");
        mBinding.tvOutMoney.setText("转出");

        mBinding.tvSymbol.setText(mAccountBean.getCurrency());
        mBinding.tvAmount.setText(AmountUtil.toMinWithUnit(mAccountBean.getAmount(), mAccountBean.getCurrency(), 8));
    }

    private void initData() {
        if (mAccountBean != null) {
            mBinding.tvTitle.setText(mAccountBean.getCurrency());
            String coinLogo = SPUtilHelper.getQiniuUrl() + LocalCoinDBUtils.getCoinIconByCoinSymbol(mAccountBean.getCurrency());
            ImgUtils.loadCircular(this, coinLogo, mBinding.ivIcon);
        }
        refreshHelper = new RefreshHelper(this, refreshCallBackback);
        refreshHelper.init(10);
        // 刷新
        refreshHelper.onDefaluteMRefresh(true);
    }


    private void initListener() {

        mBinding.imgFinish.setOnClickListener(view -> {
            finish();
        });

        //筛选
        mBinding.tvFilter.setOnClickListener(view -> {
            if (checkDate == null) {
                initFilterTypeList();
            } else if (checkDate.size() == 0) {
                UITipDialog.showFail(this, "暂无筛选数据");
            } else {
                OptionsPickerView payTypePicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        MoneyTransactionTypeModel moneyTransactionTypeModel = checkDate.get(options1);
                        filterType = moneyTransactionTypeModel.getDkey();
                        refreshHelper.onDefaluteMRefresh(true);
                    }
                }).build();
                payTypePicker.setPicker(checkDate, null, null);
                payTypePicker.show();
            }
//
//            if (filterPickerPop == null) {
//                filterPickerPop = new PickerPop(this);
//                filterPickerPop.setPickerViewData(filterTypeList);
//                filterPickerPop.setOnSelectListener(selectPosition -> {
//                    if (selectPosition == null) {
//                        return;
//                    }
//                    filterType = selectPosition.getSelectType();
//                    refreshHelper.onDefaluteMRefresh(true);
//                });
//            }
//            filterPickerPop.showPopupWindow();
        });

        //充币
        mBinding.linLayoutInCoin.setOnClickListener(view -> {
            if (mAccountBean == null)
                return;
            CoinAddressShowModel coinAddressShowModel = new CoinAddressShowModel();
            coinAddressShowModel.setAddress(mAccountBean.getAddress());
            coinAddressShowModel.setCoinSymbol(mAccountBean.getCurrency());
            WalletAddressShowActivity.open(this, coinAddressShowModel);
        });

        //提币
        mBinding.linLayoutOutCoin.setOnClickListener(view -> {
            if (mAccountBean == null)
                return;
            WithdrawActivity.open(this, mAccountBean);
        });
    }

    private void initCallBack() {

        refreshCallBackback = new BaseRefreshCallBack(this) {
            @Override
            public SmartRefreshLayout getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.recyclerView;
            }

            @Override
            public BaseQuickAdapter getAdapter(List listData) {
                mBillAdapter = new BillListAdapter(listData);
                mBillAdapter.setOnItemClickListener((adapter, view, position) -> {
                    BillDetailActivity.open(BillListActivity.this, (BillModel.ListBean) adapter.getItem(position));
                });
                return mBillAdapter;
            }

            @Override
            public void getListDataRequest(int pageIndex, int limit, boolean isShowDialog) {
                getBillListData(pageIndex, limit, isShowDialog);
            }
        };

    }

    /**
     * 获取流水
     *
     * @param pageIndex
     * @param limit
     * @param isShowDialog
     */
    private void getBillListData(int pageIndex, int limit, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("limit", limit + "");
        map.put("start", pageIndex + "");
        map.put("bizType", filterType);
        map.put("type", "0");//type传0不查询冻结的数据
        map.put("accountNumber", mAccountBean.getAccountNumber());
        map.put("systemCode", AppConfig.SYSTEMCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getBillListData("802322", StringUtils.getRequestJsonString(map));

        addCall(call);

        if (isShowDialog)
            showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<BillModel>(this) {

            @Override
            protected void onSuccess(BillModel data, String SucMessage) {
                if (data == null)
                    return;


                refreshHelper.setData(data.getList(), getStrRes(R.string.bill_none), 0);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        if (filterPickerPop != null) {
////            filterPickerPop.dismiss();
////        }
//    }
}
