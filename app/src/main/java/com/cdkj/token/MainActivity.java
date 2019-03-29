package com.cdkj.token;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.app.APPFragment;
import com.cdkj.token.databinding.ActivityMainBinding;
import com.cdkj.token.invest.InvestFragment;
import com.cdkj.token.model.db.NavigationBean;
import com.cdkj.token.trade.TradeFragment;
import com.cdkj.token.user.UserFragment;
import com.cdkj.token.utils.NavigationDBUtils;
import com.cdkj.token.wallet.WalletFragment2;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

@Route(path = CdRouteHelper.APPMAIN)
public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;

    public static final int APP = 0;
    public static final int INVEST = 0;
    public static final int TRADE = 1;
    public static final int WALLET = 2;
    public static final int USER = 3;
    public int CURRENT_INDEX = -1;


    //    public static final int CONSULT = 1;
    private List<Fragment> fragments;
    public List<NavigationBean> navigationList;
    private ArrayList<String> picList;
    private ArrayList<String> unpicList;
    private ArrayList<String> bottomTitle;
    private String mainParentCode = "DH201810120023250000000";//这个是MainActivity的  ParentCode

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initListener();
        initBottomData();
        initViewPager();
        init();
        hideKeyboard(this);
    }

    private void initBottomData() {
        navigationList = NavigationDBUtils.getNavigationparentCode(mainParentCode);
        LogUtil.E("数据长度为:" + navigationList.size());
//        //设置fragment数据
        fragments = new ArrayList<>(navigationList.size());
        picList = new ArrayList<>();
        unpicList = new ArrayList<>(10);
        bottomTitle = new ArrayList<>();

        for (NavigationBean navigationBean : navigationList) {
            switch (navigationBean.getName()) {
                case "应用":
                    if (TextUtils.equals("1", navigationBean.getStatus())) {
                        if (CURRENT_INDEX == -1) {
                            CURRENT_INDEX = navigationBean.getOrderNo();
                        }
                        fragments.add(APPFragment.getInstance());
                        picList.add(navigationBean.getOrderNo(), navigationBean.getEnPic());
                        unpicList.add(navigationBean.getOrderNo(), navigationBean.getPic());
                        bottomTitle.add(navigationBean.getOrderNo(), getBottomTitle(navigationBean.getName()));
                    } else {
                        //为什吗要加这个  因为  假如集合目前的长度为10  但是调用方法 fragments.add(15,null);  就会出现下标越界的错误
                        //为了避免这个错误  先加入一个空的 之后再移除掉
                        fragments.add(null);
                        picList.add(null);
                        unpicList.add(null);
                        bottomTitle.add(null);
                    }
                    break;

                case "投资":
                    if (TextUtils.equals("1", navigationBean.getStatus())) {
                        if (CURRENT_INDEX == -1) {
                            CURRENT_INDEX = navigationBean.getOrderNo();
                        }
                        fragments.add(InvestFragment.getInstance());
                        picList.add(navigationBean.getOrderNo(), navigationBean.getEnPic());
                        unpicList.add(navigationBean.getOrderNo(), navigationBean.getPic());
                        bottomTitle.add(navigationBean.getOrderNo(), getBottomTitle(navigationBean.getName()));
                    } else {
                        fragments.add(null);
                        picList.add(null);
                        unpicList.add(null);
                        bottomTitle.add(null);
                    }
                    break;
                case "交易":
                    if (TextUtils.equals("1", navigationBean.getStatus())) {
                        if (CURRENT_INDEX == -1) {
                            CURRENT_INDEX = navigationBean.getOrderNo();
                        }
                        fragments.add(TradeFragment.getInstance());
                        picList.add(navigationBean.getOrderNo(), navigationBean.getEnPic());
                        unpicList.add(navigationBean.getOrderNo(), navigationBean.getPic());
                        bottomTitle.add(navigationBean.getOrderNo(), getBottomTitle(navigationBean.getName()));
                    } else {
                        fragments.add(null);
                        picList.add(null);
                        unpicList.add(null);
                        bottomTitle.add(null);
                    }
                    break;
                case "资产":
                    if (TextUtils.equals("1", navigationBean.getStatus())) {
                        //这个是默认页面   如果这个页面显示的话 就默认先选中这个界面
                        CURRENT_INDEX = navigationBean.getOrderNo();
                        fragments.add(WalletFragment2.getInstance());
                        picList.add(navigationBean.getOrderNo(), navigationBean.getEnPic());
                        unpicList.add(navigationBean.getOrderNo(), navigationBean.getPic());
                        bottomTitle.add(navigationBean.getOrderNo(), getBottomTitle(navigationBean.getName()));
                    } else {
                        fragments.add(null);
                        picList.add(null);
                        unpicList.add(null);
                        bottomTitle.add(null);
                    }
                    break;
                case "我的":
                    if (TextUtils.equals("1", navigationBean.getStatus())) {
                        if (CURRENT_INDEX == -1) {
                            CURRENT_INDEX = navigationBean.getOrderNo();
                        }
                        fragments.add(UserFragment.getInstance());
                        picList.add(navigationBean.getOrderNo(), navigationBean.getEnPic());
                        unpicList.add(navigationBean.getOrderNo(), navigationBean.getPic());
                        bottomTitle.add(navigationBean.getOrderNo(), getBottomTitle(navigationBean.getName()));
                    } else {
                        fragments.add(null);
                        picList.add(null);
                        unpicList.add(null);
                        bottomTitle.add(null);
                    }
                    break;
            }
        }

        fragments.remove(null);
        picList.remove("");
        picList.remove(null);
        unpicList.remove("");
        unpicList.remove(null);
        bottomTitle.remove("");
        bottomTitle.remove(null);
        //设置显示的底部按钮
        switch (fragments.size()) {
            case 1:
                mBinding.layoutMainBottom.tvApp.setText(bottomTitle.get(0));
                if (mBinding.layoutMainBottom.llApp.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llApp.setVisibility(View.VISIBLE);
                break;
            case 2:
                mBinding.layoutMainBottom.tvApp.setText(bottomTitle.get(0));
                mBinding.layoutMainBottom.tvInvest.setText(bottomTitle.get(1));
                if (mBinding.layoutMainBottom.llApp.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llApp.setVisibility(View.VISIBLE);
                if (mBinding.layoutMainBottom.llInvest.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llInvest.setVisibility(View.VISIBLE);
                break;
            case 3:
                mBinding.layoutMainBottom.tvApp.setText(bottomTitle.get(0));
                mBinding.layoutMainBottom.tvInvest.setText(bottomTitle.get(1));
                mBinding.layoutMainBottom.tvTrade.setText(bottomTitle.get(2));
                if (mBinding.layoutMainBottom.llApp.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llApp.setVisibility(View.VISIBLE);
                if (mBinding.layoutMainBottom.llInvest.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llInvest.setVisibility(View.VISIBLE);
                if (mBinding.layoutMainBottom.llTrade.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llTrade.setVisibility(View.VISIBLE);
                break;
            case 4:
                mBinding.layoutMainBottom.tvApp.setText(bottomTitle.get(0));
                mBinding.layoutMainBottom.tvInvest.setText(bottomTitle.get(1));
                mBinding.layoutMainBottom.tvTrade.setText(bottomTitle.get(2));
                mBinding.layoutMainBottom.tvWallet.setText(bottomTitle.get(3));
                if (mBinding.layoutMainBottom.llApp.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llApp.setVisibility(View.VISIBLE);
                if (mBinding.layoutMainBottom.llInvest.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llInvest.setVisibility(View.VISIBLE);
                if (mBinding.layoutMainBottom.llTrade.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llTrade.setVisibility(View.VISIBLE);
                if (mBinding.layoutMainBottom.llWallet.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llWallet.setVisibility(View.VISIBLE);
                break;
            case 5:
                mBinding.layoutMainBottom.tvApp.setText(bottomTitle.get(0));
                mBinding.layoutMainBottom.tvInvest.setText(bottomTitle.get(1));
                mBinding.layoutMainBottom.tvTrade.setText(bottomTitle.get(2));
                mBinding.layoutMainBottom.tvWallet.setText(bottomTitle.get(3));
                mBinding.layoutMainBottom.tvMy.setText(bottomTitle.get(4));

                if (mBinding.layoutMainBottom.llApp.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llApp.setVisibility(View.VISIBLE);
                if (mBinding.layoutMainBottom.llInvest.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llInvest.setVisibility(View.VISIBLE);
                if (mBinding.layoutMainBottom.llTrade.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llTrade.setVisibility(View.VISIBLE);
                if (mBinding.layoutMainBottom.llWallet.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llWallet.setVisibility(View.VISIBLE);
                if (mBinding.layoutMainBottom.llMy.getVisibility() != View.VISIBLE)
                    mBinding.layoutMainBottom.llMy.setVisibility(View.VISIBLE);
                break;
        }
    }

    private String getBottomTitle(String name) {
        switch (name) {
            case "应用":
                return "应用";
            case "投资":
                return getResources().getString(R.string.mian_tab_invest);
            case "交易":
                return getResources().getString(R.string.mian_tab_trade);
            case "资产":
                return getResources().getString(R.string.mian_tab_assets);
            case "我的":
                return getResources().getString(R.string.main_tab_my);
        }
        return "";
    }


    private void init() {
        UIStatusBarHelper.setStatusBarDarkMode(this);
//        CoinListService.open(this);
        //如果 值为-1的话就说明  四个界面一个 也不显示  这种情况不考虑 正常也不会出现 sb操作
        setShowIndex(CURRENT_INDEX);
    }

    /**
     * 初始化事件
     */
    private void initListener() {

        mBinding.layoutMainBottom.llApp.setOnClickListener(v -> {
            setShowIndex(0);
        });

        mBinding.layoutMainBottom.llInvest.setOnClickListener(v -> {
            setShowIndex(1);
        });

        mBinding.layoutMainBottom.llTrade.setOnClickListener(v -> {
            setShowIndex(2);
        });

        mBinding.layoutMainBottom.llWallet.setOnClickListener(v -> {
            setShowIndex(3);
        });

        mBinding.layoutMainBottom.llMy.setOnClickListener(v -> {
            setShowIndex(4);
            getNavigation();
        });

    }

    /**
     * 获取本地导航列表  为了是动态刷新
     */
    public void getNavigation() {
        Map<String, String> map = new HashMap<>();
//        map.put("parentCode", "DH201810120023250000000");
        map.put("type", "app_menu");

        Call<BaseResponseListModel<NavigationBean>> navigation = RetrofitUtils.createApi(MyApi.class).getNavigation("630508", StringUtils.getRequestJsonString(map));
        navigation.enqueue(new BaseResponseListCallBack<NavigationBean>(null) {
            @Override
            protected void onSuccess(List<NavigationBean> data, String SucMessage) {
                NavigationDBUtils.updateNavigationList(data);
                EventBus.getDefault().post("更新布局");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);

            }

            @Override
            protected void onFinish() {

            }
        });

    }


    public void setTabIndex(int index) {
        setTabDark();
        switch (index) {
            case 0:
                ImgUtils.loadImage(this, picList.get(0), mBinding.layoutMainBottom.ivApp);
                mBinding.layoutMainBottom.tvApp.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case 1:
                ImgUtils.loadImage(this, picList.get(1), mBinding.layoutMainBottom.ivInvest);
                mBinding.layoutMainBottom.tvInvest.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case 2:
                ImgUtils.loadImage(this, picList.get(2), mBinding.layoutMainBottom.ivTrade);
                mBinding.layoutMainBottom.tvTrade.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case 3:
                ImgUtils.loadImage(this, picList.get(3), mBinding.layoutMainBottom.ivWallet);
                mBinding.layoutMainBottom.tvWallet.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case 4:
                ImgUtils.loadImage(this, picList.get(4), mBinding.layoutMainBottom.ivMy);
                mBinding.layoutMainBottom.tvMy.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
        }

    }

    private void setTabDark() {

        switch (unpicList.size()) {
            case 0:
                break;
            case 1:
                ImgUtils.loadImage(this, unpicList.get(0), mBinding.layoutMainBottom.ivApp);
                mBinding.layoutMainBottom.tvApp.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));
                break;
            case 2:
                ImgUtils.loadImage(this, unpicList.get(0), mBinding.layoutMainBottom.ivApp);
                mBinding.layoutMainBottom.tvApp.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

                ImgUtils.loadImage(this, unpicList.get(1), mBinding.layoutMainBottom.ivInvest);
                mBinding.layoutMainBottom.tvInvest.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));
                break;
            case 3:
                ImgUtils.loadImage(this, unpicList.get(0), mBinding.layoutMainBottom.ivApp);
                mBinding.layoutMainBottom.tvApp.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

                ImgUtils.loadImage(this, unpicList.get(1), mBinding.layoutMainBottom.ivInvest);
                mBinding.layoutMainBottom.tvInvest.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

                ImgUtils.loadImage(this, unpicList.get(2), mBinding.layoutMainBottom.ivTrade);
                mBinding.layoutMainBottom.tvTrade.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));
                break;
            case 4:
                ImgUtils.loadImage(this, unpicList.get(0), mBinding.layoutMainBottom.ivApp);
                mBinding.layoutMainBottom.tvApp.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

                ImgUtils.loadImage(this, unpicList.get(1), mBinding.layoutMainBottom.ivInvest);
                mBinding.layoutMainBottom.tvInvest.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

                ImgUtils.loadImage(this, unpicList.get(2), mBinding.layoutMainBottom.ivTrade);
                mBinding.layoutMainBottom.tvTrade.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

                ImgUtils.loadImage(this, unpicList.get(3), mBinding.layoutMainBottom.ivWallet);
                mBinding.layoutMainBottom.tvWallet.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));
                break;
            case 5:
                ImgUtils.loadImage(this, unpicList.get(0), mBinding.layoutMainBottom.ivApp);
                mBinding.layoutMainBottom.tvApp.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

                ImgUtils.loadImage(this, unpicList.get(1), mBinding.layoutMainBottom.ivInvest);
                mBinding.layoutMainBottom.tvInvest.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

                ImgUtils.loadImage(this, unpicList.get(2), mBinding.layoutMainBottom.ivTrade);
                mBinding.layoutMainBottom.tvTrade.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

                ImgUtils.loadImage(this, unpicList.get(3), mBinding.layoutMainBottom.ivWallet);
                mBinding.layoutMainBottom.tvWallet.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

                ImgUtils.loadImage(this, unpicList.get(4), mBinding.layoutMainBottom.ivMy);
                mBinding.layoutMainBottom.tvMy.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));
                break;
        }

    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mBinding.pagerMain.setPagingEnabled(false);//禁止左右切换
        mBinding.pagerMain.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.pagerMain.setOffscreenPageLimit(fragments.size());
        mBinding.pagerMain.setCurrentItem(0);
    }


    /**
     * 设置要显示的界面
     *
     * @param index
     */
    private void setShowIndex(int index) {
        if (index < 0 && index >= fragments.size()) {
            return;
        }
        mBinding.pagerMain.setCurrentItem(index, false);
        setTabIndex(index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = fragments.get(fragments.size() - 1);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        showDoubleWarnListen(getStrRes(R.string.exit_confirm), view -> {
            EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        CoinListService.close(this);
    }
}
