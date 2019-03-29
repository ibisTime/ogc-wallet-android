package com.cdkj.token.jinmi;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.WalletBalanceAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityPrivateJinMinMoneyBinding;
import com.cdkj.token.model.BalanceListModel;
import com.cdkj.token.model.CoinTypeAndAddress;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.user.WalletToolActivity;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.wallet.private_wallet.WalletCoinDetailsActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;

import static com.cdkj.token.utils.LocalCoinDBUtils.getCoinIconByCoinSymbol;

/**
 * 金米私有钱包
 */
public class PrivateJinMinMoneyActivity extends AbsLoadActivity {

    private ActivityPrivateJinMinMoneyBinding mBinding;
    private RefreshHelper mRefreshHelper;
    private BalanceListModel mPrivateWalletData;

    public static void open(Context context) {
        Intent intent = new Intent(context, PrivateJinMinMoneyActivity.class);
        context.startActivity(intent);

    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_private_jin_min_money, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setShowTitle(false);
        setStatusBarBlue();
        setTitleBgBlue();
        initRefresh();
        initListener();
    }

    /**
     * 点击事件
     */
    private void initListener() {
        mBinding.ivBack.setOnClickListener(view -> finish());
        mBinding.tvMoneyTools.setOnClickListener(view -> {
//                finish();
            WalletToolActivity.open(PrivateJinMinMoneyActivity.this);
        });

    }

    /**
     * 初始化请求数据
     */
    private void initRefresh() {

        //开始时请求币种缓存
        mRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
//                mlLocalCoinCachePresenter.getCoinList(mActivity);  //开始时请求币种缓存
                mBinding.refreshLayout.setEnableLoadmore(false);
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.recyclerView;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                return getListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getPriWalletAssetsData(true, true, true);
            }
        });

        mBinding.recyclerView.setLayoutManager(getLayoutManager());
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mRefreshHelper.init(10);
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    private WalletBalanceAdapter getListAdapter(List listData) {
        WalletBalanceAdapter walletBalanceAdapter = new WalletBalanceAdapter(listData);

        walletBalanceAdapter.setOnItemChildClickListener((adapter1, view, position) -> {

            WalletBalanceModel accountListBean = walletBalanceAdapter.getItem(position);
            boolean isPastBtc = TextUtils.equals(SPUtilHelper.getPastBtcInfo().split("\\+")[0],
                    accountListBean.getAddress());

            switch (view.getId()) {
                case R.id.ll_item: //充值
                    WalletCoinDetailsActivity.open(this, accountListBean, isPastBtc);
                    break;
            }

        });

        return walletBalanceAdapter;
    }

    /**
     * 获取 LinearLayoutManager
     */
    private LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
    }

    private List<CoinTypeAndAddress> mChooseCoinList; // 用户自选的币种

    /**
     * 异步获取本地货币并请求钱包数据
     *
     * @param isSetRecyclerData 用户调用 getPriWalletAssetsData方法
     * @param isShowDialog
     */
    public void getLocalCoinAndRequestWalletData(boolean isSetRecyclerData, boolean isShowDialog, boolean isFirstLoad) {
        Disposable disposable = WalletHelper.getLocalCoinListAsync(localCoinDbModels -> {
            if (localCoinDbModels != null) {
                mChooseCoinList = getChooseCoinList(localCoinDbModels);
                getPriWalletAssetsData(isSetRecyclerData, isShowDialog, isFirstLoad);
            }
        });
        mSubscription.add(disposable); //用于结束异步
    }

    /**
     * 获取到本地币种构造接口入参
     */
    private List<CoinTypeAndAddress> getChooseCoinList(List<LocalCoinDbModel> localCoinDbModels) {

        List<CoinTypeAndAddress> chooseCoinList = new ArrayList<>();

        WalletDBModel walletDBModel = WalletHelper.getUserWalletInfoByUserId(WalletHelper.WALLET_USER);//获取钱包信息

        for (LocalCoinDbModel localCoinDbModel : localCoinDbModels) {           //获取本地缓存的币种

            if (localCoinDbModel == null) {
                continue;
            }
            //        ETH("0", "以太币"), BTC("1", "比特币"), WAN("2", "万维"), USDT("3", "泰达币")
            //          基于某条公链的token币
            //        , ETH_TOKEN("0T", "以太token币"), WAN_TOKEN("2T", "万维token币");
            CoinTypeAndAddress coinTypeAndAddress = new CoinTypeAndAddress();
//            if (TextUtils.equals(localCoinDbModel.getSymbol(), "KCC"))
//                continue;
            coinTypeAndAddress.setSymbol(localCoinDbModel.getSymbol());
            switch (localCoinDbModel.getType()) {
                case "0"://
                    coinTypeAndAddress.setAddress(walletDBModel.getEthAddress());
                    break;
                case "1":
                    coinTypeAndAddress.setAddress(walletDBModel.getBtcAddress());
                    break;
                case "2":
                    coinTypeAndAddress.setAddress(walletDBModel.getWanAddress());
                    break;
                case "3":
                    coinTypeAndAddress.setAddress(walletDBModel.getUsdtAddress());
                    break;
                case "0T":
                    coinTypeAndAddress.setAddress(walletDBModel.getEthAddress());
                    break;
                case "2T":
                    coinTypeAndAddress.setAddress(walletDBModel.getWanTokenAddress());
                    break;
            }

//            CoinTypeAndAddress coinTypeAndAddress = setCoinTypeAndAddress(walletDBModel, localCoinDbModel);

            if (!TextUtils.isEmpty(coinTypeAndAddress.getAddress())) {
                chooseCoinList.add(coinTypeAndAddress);
            }

        }

        return chooseCoinList;
    }


    /**
     * 根据币种类型 设置不同的币种地址
     *
     * @param walletDBModel
     * @param localCoinDbModel
     * @return
     */
    @NonNull
    private CoinTypeAndAddress setCoinTypeAndAddress(WalletDBModel walletDBModel, LocalCoinDbModel localCoinDbModel) {
        CoinTypeAndAddress coinTypeAndAddress = new CoinTypeAndAddress();    //0 公链币（ETH BTC WAN） 1 ethtoken（ETH） 2 wantoken（WAN）        通过币种和type 添加地址

        if (LocalCoinDBUtils.isCommonChainCoinByType(localCoinDbModel.getType())) {

            if (TextUtils.equals(WalletHelper.COIN_BTC, localCoinDbModel.getSymbol())) {
                coinTypeAndAddress.setAddress(walletDBModel.getBtcAddress());
            } else if (TextUtils.equals(WalletHelper.COIN_ETH, localCoinDbModel.getSymbol())) {
                coinTypeAndAddress.setAddress(walletDBModel.getEthAddress());
            } else if (TextUtils.equals(WalletHelper.COIN_WAN, localCoinDbModel.getSymbol())) {
                coinTypeAndAddress.setAddress(walletDBModel.getWanAddress());
            } else if (TextUtils.equals(WalletHelper.COIN_USDT, localCoinDbModel.getSymbol())) {
                coinTypeAndAddress.setAddress(walletDBModel.getBtcAddress());
            }

        } else if (LocalCoinDBUtils.isEthTokenCoin(localCoinDbModel.getType())) {

            coinTypeAndAddress.setAddress(walletDBModel.getEthAddress());

        } else if (LocalCoinDBUtils.isWanTokenCoin(localCoinDbModel.getType())) {

            coinTypeAndAddress.setAddress(walletDBModel.getWanAddress());

        }
        coinTypeAndAddress.setSymbol(localCoinDbModel.getSymbol());
        return coinTypeAndAddress;
    }


    /**
     * 转换为adapter数据 （秘钥钱包）
     *
     * @param data
     * @return
     */
    @NonNull
    private List<WalletBalanceModel> transformToPrivateAdapterData(BalanceListModel data) {
        List<WalletBalanceModel> walletBalanceModels = new ArrayList<>();
        if (data == null) {
            return walletBalanceModels;
        }

        for (BalanceListModel.AccountListBean accountListBean : data.getAccountList()) {

            WalletBalanceModel walletBalanceModel = new WalletBalanceModel();

            walletBalanceModel.setCoinSymbol(accountListBean.getSymbol());

            walletBalanceModel.setAvailableAmount(new BigDecimal(accountListBean.getBalance()));

            walletBalanceModel.setCoinImgUrl(getCoinIconByCoinSymbol(accountListBean.getSymbol()));

            walletBalanceModel.setLocalMarketPrice(accountListBean.getMarketStringByLocalSymbol());

            walletBalanceModel.setLocalAmount(accountListBean.getAmountStringByLocalMarket());

            walletBalanceModel.setAddress(accountListBean.getAddress());

//            walletBalanceModel.setPercentChange24h(accountListBean.getPercentChange24h());

            if (accountListBean.getBalance() != null) {
                walletBalanceModel.setCoinBalance(accountListBean.getBalance().toString());
            }

            walletBalanceModels.add(walletBalanceModel);
        }
        return walletBalanceModels;
    }

    /**
     * 获取私有钱包数据
     *
     * @param isSetRecyclerData 是否设置recyclerData
     */

    private void getPriWalletAssetsData(boolean isSetRecyclerData, boolean isShowDialog, boolean isFirstLoad) {


        if (mChooseCoinList == null) {
            getLocalCoinAndRequestWalletData(isSetRecyclerData, isShowDialog, isFirstLoad);
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("accountList", mChooseCoinList);
        if (isShowDialog) {
            showLoadingDialog();
        }

        Call<BaseResponseModel<BalanceListModel>> call = RetrofitUtils.createApi(MyApi.class).getBalanceList("802270", StringUtils.getRequestJsonString(map));

        addCall(call);

        if (isShowDialog) {
            showLoadingDialog();
        }

        call.enqueue(new BaseResponseModelCallBack<BalanceListModel>(this) {
            @Override
            protected void onSuccess(BalanceListModel data, String SucMessage) {
                mPrivateWalletData = data;
                if (AppConfig.LOCAL_MARKET_CNY.equals(SPUtilHelper.getLocalMarketSymbol())) {
                    mBinding.tvTotalAmountCNY.setText("≈" + data.getTotalAmountCNY() + AppConfig.LOCAL_MARKET_CNY);
                } else {
                    mBinding.tvTotalAmountCNY.setText("≈" + data.getTotalAmountUSD() + AppConfig.LOCAL_MARKET_USD);
                }
//                if (isSetRecyclerData) {
                List<WalletBalanceModel> walletBalanceModels = checkWalletBalanceModels();
//                    mRefreshHelper.setPageIndex(1);
                mRefreshHelper.setData(walletBalanceModels, getString(R.string.no_assets), R.mipmap.order_none);
//                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                if (isSetRecyclerData) {
                    mRefreshHelper.loadError(errorMessage, 0);
                }
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    @NonNull
    private List<WalletBalanceModel> checkWalletBalanceModels() {
        if (mPrivateWalletData == null) return null;
        List<WalletBalanceModel> walletBalanceModels = transformToPrivateAdapterData(mPrivateWalletData);
        for (int i = 0; i < walletBalanceModels.size(); i++) {
            WalletBalanceModel item = walletBalanceModels.get(i);
            if (TextUtils.equals(item.getAddress(), SPUtilHelper.getPastBtcInfo().split("\\+")[0])) {
                String availablemountString = AmountUtil.transformFormatToString(item.getAvailableAmount(), item.getCoinSymbol(), 8);
                if (Double.parseDouble(availablemountString) <= 0.0) {
                    walletBalanceModels.remove(item);
                }
            }
        }
        return walletBalanceModels;
    }
}
