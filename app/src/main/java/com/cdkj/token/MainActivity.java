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
        fragments = new ArrayList<>();
        picList = new ArrayList<>();
        unpicList = new ArrayList<>();
        bottomTitle = new ArrayList<>();

        for (NavigationBean navigationBean : navigationList) {
            picList.add(navigationBean.getOrderNo(), navigationBean.getEnPic());
            unpicList.add(navigationBean.getOrderNo(), navigationBean.getPic());
            bottomTitle.add(navigationBean.getOrderNo(), getBottomTitle(navigationBean.getName()));

            switch (navigationBean.getName()) {
                case "投资":
                    if (TextUtils.equals("1", navigationBean.getStatus())) {
                        if (CURRENT_INDEX == -1) {
                            CURRENT_INDEX = INVEST;
                        }
                        fragments.add(InvestFragment.getInstance());
                        mBinding.layoutMainBottom.llInvest.setVisibility(View.VISIBLE);
                    } else {
                        fragments.add(new Fragment());
                        mBinding.layoutMainBottom.llInvest.setVisibility(View.GONE);
                    }
                    break;
                case "交易":
                    if (TextUtils.equals("1", navigationBean.getStatus())) {
                        if (CURRENT_INDEX == -1) {
                            CURRENT_INDEX = TRADE;
                        }
                        fragments.add(TradeFragment.getInstance());
                        mBinding.layoutMainBottom.llTrade.setVisibility(View.VISIBLE);
                    } else {
                        fragments.add(new Fragment());
                        mBinding.layoutMainBottom.llTrade.setVisibility(View.GONE);
                    }
                    break;
                case "资产":
                    if (TextUtils.equals("1", navigationBean.getStatus())) {
                        //这个是默认页面   如果这个页面显示的话 就默认先选中这个界面
                        CURRENT_INDEX=WALLET;

                        fragments.add(WalletFragment2.getInstance());
                        mBinding.layoutMainBottom.llWallet.setVisibility(View.VISIBLE);
                    } else {
                        fragments.add(new Fragment());
                        mBinding.layoutMainBottom.llWallet.setVisibility(View.GONE);
                    }
                    break;
                case "我的":
                    if (TextUtils.equals("1", navigationBean.getStatus())) {
                        if (CURRENT_INDEX == -1) {
                            CURRENT_INDEX = USER;
                        }
                        fragments.add(UserFragment.getInstance());
                        mBinding.layoutMainBottom.llMy.setVisibility(View.VISIBLE);
                    } else {
                        fragments.add(new Fragment());
                        mBinding.layoutMainBottom.llMy.setVisibility(View.GONE);
                    }
                    break;
            }
        }

        mBinding.layoutMainBottom.tvInvest.setText(bottomTitle.get(0));
        mBinding.layoutMainBottom.tvTrade.setText(bottomTitle.get(1));
        mBinding.layoutMainBottom.tvWallet.setText(bottomTitle.get(2));
        mBinding.layoutMainBottom.tvMy.setText(bottomTitle.get(3));
    }

    private String getBottomTitle(String name) {
        switch (name) {
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

        mBinding.layoutMainBottom.llInvest.setOnClickListener(v -> {
            setShowIndex(INVEST);
        });

        mBinding.layoutMainBottom.llTrade.setOnClickListener(v -> {
            setShowIndex(TRADE);
        });

        mBinding.layoutMainBottom.llWallet.setOnClickListener(v -> {
            setShowIndex(WALLET);
        });

        mBinding.layoutMainBottom.llMy.setOnClickListener(v -> {
            setShowIndex(USER);
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

            case INVEST:
//                mBinding.layoutMainBottom.ivInvest.setImageResource(R.mipmap.main_invest_light);
                ImgUtils.loadImage(this, picList.get(0), mBinding.layoutMainBottom.ivInvest);
                mBinding.layoutMainBottom.tvInvest.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;

            case TRADE:
//                mBinding.layoutMainBottom.ivTrade.setImageResource(R.mipmap.main_trade_light);
                ImgUtils.loadImage(this, picList.get(1), mBinding.layoutMainBottom.ivTrade);
                mBinding.layoutMainBottom.tvTrade.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;

            case WALLET:
//                mBinding.layoutMainBottom.ivWallet.setImageResource(R.mipmap.main_wallet_light);
                ImgUtils.loadImage(this, picList.get(2), mBinding.layoutMainBottom.ivWallet);
                mBinding.layoutMainBottom.tvWallet.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;

            case USER:
//                mBinding.layoutMainBottom.ivMy.setImageResource(R.mipmap.main_my_light);
                ImgUtils.loadImage(this, picList.get(3), mBinding.layoutMainBottom.ivMy);
                mBinding.layoutMainBottom.tvMy.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
        }

    }

    private void setTabDark() {

//        mBinding.layoutMainBottom.ivInvest.setImageResource(R.mipmap.main_invest_dark);
        ImgUtils.loadImage(this, unpicList.get(0), mBinding.layoutMainBottom.ivInvest);
        mBinding.layoutMainBottom.tvInvest.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

//        mBinding.layoutMainBottom.ivTrade.setImageResource(R.mipmap.main_trade_dark);
        ImgUtils.loadImage(this, unpicList.get(1), mBinding.layoutMainBottom.ivTrade);
        mBinding.layoutMainBottom.tvTrade.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

//        mBinding.layoutMainBottom.ivWallet.setImageResource(R.mipmap.main_wallet_dark);
        ImgUtils.loadImage(this, unpicList.get(2), mBinding.layoutMainBottom.ivWallet);
        mBinding.layoutMainBottom.tvWallet.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

//        mBinding.layoutMainBottom.ivMy.setImageResource(R.mipmap.main_my_dark);
        ImgUtils.loadImage(this, unpicList.get(3), mBinding.layoutMainBottom.ivMy);
        mBinding.layoutMainBottom.tvMy.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));
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
