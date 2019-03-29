package com.cdkj.token.app;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.AppCionFragmentAdapter;
import com.cdkj.token.adapter.AppFragmentAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.common.loader.BannerImageLoader;
import com.cdkj.token.databinding.FragmentAppBinding;
import com.cdkj.token.model.BannerModel;
import com.cdkj.token.model.DAppModel;
import com.cdkj.token.model.RecommendAppModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youth.banner.BannerConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


/**
 * A simple {@link Fragment} subclass.
 */
public class APPFragment extends BaseLazyFragment {

    private FragmentAppBinding mBinding;
    private List<BannerModel> bannerData;

    public String CATEGORY_ETH = "0";
    public String CATEGORY_RTX = "1";
    public String CATEGORY_OTHER = "2";
    private String category = CATEGORY_ETH;
    private AppFragmentAdapter appListAdapter;
    private AppCionFragmentAdapter appCionFragmentAdapter;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static APPFragment getInstance() {
        APPFragment fragment = new APPFragment();
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        if (mBinding == null) {
            return;
        }

        // 刷新轮播图
        if (bannerData != null && !bannerData.isEmpty()) {
            mBinding.banner.startAutoPlay();
            return;
        }

        getBanner(true);
    }

    @Override
    protected void onInvisible() {
        if (mBinding == null) {
            return;
        }
        mBinding.banner.stopAutoPlay();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.fragment_app, null, false);

        initListener();

        initBanner();

        initRecyclerViewAndAdapter();

        initCionView();

        initRefresh();
//        getLocalAppList();

        return mBinding.getRoot();
    }

    /**
     * 设置中间的数据
     */
    private void initCionView() {

        appCionFragmentAdapter = new AppCionFragmentAdapter(null);
        mBinding.rvCion.setLayoutManager(new GridLayoutManager(mActivity, 3, GridLayoutManager.VERTICAL, false));
        mBinding.rvCion.setAdapter(appCionFragmentAdapter);

        appCionFragmentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RecommendAppModel item = (RecommendAppModel) adapter.getItem(position);
                if (item != null && TextUtils.equals("0", item.getAction())) {
                    WebViewActivity.openURL(mActivity, item.getName(), item.getPosition());
                }
            }
        });
    }

    /**
     * 初始化recyclerView适配器
     */
    void initRecyclerViewAndAdapter() {
        mBinding.recyclerViewApp.setLayoutManager(new GridLayoutManager(mActivity, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        appListAdapter = new AppFragmentAdapter(null);

        appListAdapter.setOnItemClickListener((adapter, view, position) -> {

            if (position < 0 || position > adapter.getData().size()) {
                return;
            }
            DAppActivity.open(mActivity, appListAdapter.getItem(position).getId()+"");

        });

        appListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //WebViewActivity.openURL(mActivity, appListAdapter.getItem(position).getName(), appListAdapter.getItem(position).getUrl());
        });

        mBinding.recyclerViewApp.setAdapter(appListAdapter);
    }

    private void initListener() {

        mBinding.tvGameETH.setOnClickListener(view -> {
            initCategoryView();

            mBinding.tvGameETH.setTextColor(ContextCompat.getColor(mActivity, R.color.blue_0064ff));
            mBinding.lineGameETH.setVisibility(View.VISIBLE);

            category = CATEGORY_ETH;
            getDAPPList();
        });


        mBinding.tvGameTRX.setOnClickListener(view -> {
            initCategoryView();

            mBinding.tvGameTRX.setTextColor(ContextCompat.getColor(mActivity, R.color.blue_0064ff));
            mBinding.lineGameTRX.setVisibility(View.VISIBLE);

            category = CATEGORY_RTX;
            getDAPPList();
        });

    }

    private void initCategoryView() {
        mBinding.tvGameTRX.setTextColor(ContextCompat.getColor(mActivity, R.color.black));
        mBinding.lineGameTRX.setVisibility(View.GONE);

        mBinding.tvTypeInfo.setTextColor(ContextCompat.getColor(mActivity, R.color.black));
        mBinding.lineTypeInfo.setVisibility(View.GONE);

        mBinding.tvGameETH.setTextColor(ContextCompat.getColor(mActivity, R.color.black));
        mBinding.lineGameETH.setVisibility(View.GONE);
    }

    /**
     * 获取中间应用列表
     */
    public void getLocalAppList() {

        Map<String, String> map = new HashMap<>();

        map.put("language", SPUtilHelper.getLanguage());
        map.put("location", "0");
        map.put("status", "1");

        Call<BaseResponseListModel<RecommendAppModel>> call = RetrofitUtils.createApi(MyApi.class).getAppList("625412", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseListCallBack<RecommendAppModel>(mActivity) {
            @Override
            protected void onSuccess(List<RecommendAppModel> data, String SucMessage) {
                appCionFragmentAdapter.replaceData(data);
                appCionFragmentAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onFinish() {
                getDAPPList();
            }
        });

    }


    /**
     * 下方应用数据
     */
    public void getDAPPList() {
        showLoadingDialog();

        Map<String, String> map = new HashMap<>();

        map.put("category", category);
        map.put("name", "");
        map.put("start", "1");
        map.put("limit", "20");

        Call<BaseResponseModel<ResponseInListModel<DAppModel>>> call = RetrofitUtils.createApi(MyApi.class).getDAppList("625456", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<DAppModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<DAppModel> data, String SucMessage) {
                appListAdapter.replaceData(data.getList());
                appListAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onFinish() {
                if (mBinding.refreshLayout.isRefreshing()) {
                    mBinding.refreshLayout.finishRefresh();
                }
                disMissLoading();
            }
        });
    }

    /**
     * 获取banner
     */
    private void getBanner(boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("location", "app_home"); // 交易位置轮播
        map.put("systemCode", "");
        map.put("companyCode", "");

        Call call = RetrofitUtils.createApi(MyApi.class).getBanner("630506", StringUtils.getRequestJsonString(map));
        addCall(call);
        if (isShowDialog) {
            showLoadingDialog();
        }
        call.enqueue(new BaseResponseListCallBack<BannerModel>(mActivity) {

            @Override
            protected void onSuccess(List<BannerModel> data, String SucMessage) {
                bannerData = data;
                if (bannerData == null || bannerData.isEmpty()) return;
                //设置图片集合
                mBinding.banner.setImages(bannerData);
                //banner设置方法全部调用完毕时最后调用
                mBinding.banner.start();
                mBinding.banner.startAutoPlay();

            }

            @Override
            protected void onFinish() {
                getLocalAppList();
            }
        });
    }

    private void initBanner() {

        //设置banner样式
        mBinding.banner.setBannerStyle(BannerConfig.CENTER);
        //设置图片加载器
        mBinding.banner.setImageLoader(new BannerImageLoader());

        //设置banner动画效果
//        mBinding.banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        mBinding.banner.isAutoPlay(true);
        //设置轮播时间
        mBinding.banner.setDelayTime(3500);
        //设置指示器位置（当banner模式中有指示器时）
        mBinding.banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置banner点击事件
        mBinding.banner.setOnBannerListener(position -> {

            if (bannerData == null || position > bannerData.size()) return;

            if (bannerData.get(position) != null) {

                //0 不跳转  1跳转网页  2跳转应用
                String type = bannerData.get(position).getType();
                if (TextUtils.equals("1", type)) {
                    //跳转网页
                    WebViewActivity.openURL(mActivity, bannerData.get(position).getName(), bannerData.get(position).getUrl());

                } else if (TextUtils.equals("2", type)) {
                    //跳转应用  这个时候  url  会返回appid
                    DAppActivity.open(mActivity, bannerData.get(position).getUrl());
                }

            }

        });

        // 设置在操作Banner时listView事件不触发
//        mBinding.banner.setOnPageChangeListener(new MyPageChangeListener());
    }

    void initRefresh() {
        mBinding.refreshLayout.setEnableLoadmore(false);
        mBinding.refreshLayout.setOnRefreshListener(refreshlayout -> {
            getBanner(false);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.banner.stopAutoPlay();
    }

}
