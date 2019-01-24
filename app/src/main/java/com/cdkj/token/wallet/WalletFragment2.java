package com.cdkj.token.wallet;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.WalletAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentWalletBinding;
import com.cdkj.token.interfaces.LocalCoinCacheInterface;
import com.cdkj.token.interfaces.LocalCoinCachePresenter;
import com.cdkj.token.model.WalletModel;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.token.wallet.account_wallet.BillListActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/12/27.
 */

public class WalletFragment2 extends BaseLazyFragment {

    private FragmentWalletBinding mBinding;

    private RefreshHelper mRefreshHelper;
    private LocalCoinCachePresenter mlLocalCoinCachePresenter;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static WalletFragment2 getInstance() {
        WalletFragment2 fragment = new WalletFragment2();
        return fragment;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, null, false);
        initLocalCoinPresenter();//初始化币种,请求币种保存本地
        initRefresh();
        return mBinding.getRoot();
    }

    /**
     * 本地币种缓存Presenter回调
     * 获取缓存后再获取对应钱包数据
     */
    void initLocalCoinPresenter() {
        mlLocalCoinCachePresenter = new LocalCoinCachePresenter(new LocalCoinCacheInterface() {
            @Override
            public void cacheEnd(List<LocalCoinDbModel> data) {
//                getWalletAssetsData(true, false);
                mRefreshHelper.onDefaluteMRefresh(true);
            }
        });
    }

    private void initRefresh() {

        mRefreshHelper = new RefreshHelper(mActivity, new BaseRefreshCallBack(mActivity) {
            @Override
            public View getRefreshLayout() {
                mlLocalCoinCachePresenter.getCoinList(mActivity);  //开始时请求币种缓存
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
                getSymbolList();
            }
        });

        mBinding.recyclerView.setLayoutManager(getLayoutManager());
        mBinding.recyclerView.setNestedScrollingEnabled(false);

        mRefreshHelper.init(10);
    }

    private WalletAdapter getListAdapter(List listData) {
        WalletAdapter adapter = new WalletAdapter(listData);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            BillListActivity.open(mActivity, adapter.getItem(position));
        });
        return adapter;
    }

    /**
     * 获取 LinearLayoutManager
     *
     * @return LinearLayoutManager
     */
    private LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
    }

    /**
     * 获取默认币种余额
     */
    public void getSymbolList() {
        showLoadingDialog();
        if (TextUtils.isEmpty(SPUtilHelper.getUserId())) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getSymbolList("802301", StringUtils.getRequestJsonString(map));
        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<WalletModel>(mActivity) {
            @Override
            protected void onSuccess(WalletModel data, String SucMessage) {
                mRefreshHelper.setData(data.getAccountList(), getString(R.string.no_assets), R.mipmap.order_none);
                mBinding.tvTotalAmountCNY.setText("≈" + data.getTotalAmountCNY() + "  CNY");
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }
}
