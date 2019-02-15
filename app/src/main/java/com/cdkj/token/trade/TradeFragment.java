package com.cdkj.token.trade;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.DialogPayMessageBinding;
import com.cdkj.token.databinding.FragmentTradeBinding;
import com.cdkj.token.interfaces.EditInputFilter;
import com.cdkj.token.interfaces.IdentifyInterface;
import com.cdkj.token.interfaces.IdentifyPresenter;
import com.cdkj.token.interfaces.UserInfoInterface;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.model.LineChartDataModel;
import com.cdkj.token.model.PayTypeModel;
import com.cdkj.token.model.SuccessModel;
import com.cdkj.token.model.SymbolPriceModel;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.model.UserBankCardModel;
import com.cdkj.token.model.WalletModel;
import com.cdkj.token.user.UserBackCardActivity;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.MPChartUtils;
import com.cdkj.token.utils.NetWorrkUtils;
import com.cdkj.token.utils.StringUtil;
import com.cdkj.token.views.dialogs.PasswordInputDialog;
import com.github.mikephil.charting.data.Entry;
import com.zqzn.idauth.sdk.FaceResultCallback;
import com.zqzn.idauth.sdk.IdResultCallback;
import org.greenrobot.eventbus.Subscribe;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;

/**
 * Created by cdkj on 2018/12/27.
 */

public class TradeFragment extends BaseLazyFragment implements IdentifyInterface, UserInfoInterface {

    private FragmentTradeBinding mBinding;
    private int position;//0是  买入  1是卖出
    private SymbolPriceModel symbolPriceModel;
    private boolean isMoneyInput;//当前是不是输入的金额  是的话通过金额转换数量  数量就不用在进行计算转换金额了  如果当前输入的事数量的话相反
    private UserBankCardModel.ListBean payBankModel;
    private OptionsPickerView payTypePicker;
    private PayTypeModel payTypeModel;
    private Disposable subscribe;
    private UserInfoPresenter mGetUserInfoPresenter;
    private String symbol;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static TradeFragment getInstance() {
        TradeFragment fragment = new TradeFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.fragment_trade, null, false);
        mGetUserInfoPresenter = new UserInfoPresenter(this, mActivity);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initLineChart();
        initLineChartData();
        initSymbolPrice();
        initPayType();
        getDefaultBank();
        initMinCnyAmount();
        initClickListener();

        return mBinding.getRoot();

    }


    /**
     * 获取用户默认的银行卡  没有就不显示
     */
    private void getDefaultBank() {

        Map<String, String> map = new HashMap<>();
        map.put("status", "0");//0是可用的   1是 已解绑的
//        map.put("type", "0");//0银行卡  1支付宝
        map.put("userId", SPUtilHelper.getUserId());
        Call<BaseResponseListModel<UserBankCardModel.ListBean>> bankListData = RetrofitUtils.createApi(MyApi.class).getBankListData("802031", StringUtils.getRequestJsonString(map));
        addCall(bankListData);
        bankListData.enqueue(new BaseResponseListCallBack<UserBankCardModel.ListBean>(mActivity) {
            @Override
            protected void onSuccess(List<UserBankCardModel.ListBean> data, String SucMessage) {

                if (data != null && data.size() > 0) {
                    for (UserBankCardModel.ListBean listBean : data) {
                        if (TextUtils.equals("1", listBean.getIsDefault())) {
                            payBankModel = listBean;
                            break;
                        }
                    }
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 获取付款方式  默认第一条
     */
    private void initPayType() {

        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", "BTC");
        Call<BaseResponseListModel<PayTypeModel>> payType = RetrofitUtils.createApi(MyApi.class).getPayType("802034", StringUtils.getRequestJsonString(map));
        addCall(payType);
        showLoadingDialog();
        payType.enqueue(new BaseResponseListCallBack<PayTypeModel>(mActivity) {
            @Override
            protected void onSuccess(List<PayTypeModel> data, String SucMessage) {
                if (data == null || data.size() == 0)
                    return;
                payTypeModel = data.get(0);
                initBuyView();
                payTypePicker = new OptionsPickerBuilder(mActivity, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        payTypeModel = data.get(options1);
                        initBuyView();
                    }
                }).setTitleText("支付类型").isDialog(true).build();
                payTypePicker.setPicker(data, null, null);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 查询承兑商币种最新价格
     */
    private void initSymbolPrice() {
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", "BTC");
        Call<BaseResponseModel<SymbolPriceModel>> symbolPrice = RetrofitUtils.createApi(MyApi.class).getSymbolPrice("650201", StringUtils.getRequestJsonString(map));
        addCall(symbolPrice);
        symbolPrice.enqueue(new BaseResponseModelCallBack<SymbolPriceModel>(mActivity) {
            @Override
            protected void onSuccess(SymbolPriceModel data, String SucMessage) {
                symbolPriceModel = data;
                mBinding.tvBuyingPrice.setText(data.getBuyPrice().toString());//买入价
                mBinding.tvSellingPrice.setText(data.getSellerPrice().toString());//卖出价

                if (position == 0) {
                    mBinding.tvServiceCharge.setText(symbolPriceModel == null ? "" : AmountUtil.multiplyBigDecimalToDouble(symbolPriceModel.getBuyFeeRate(), 100, 2) + "%");
                } else {
                    mBinding.tvServiceCharge.setText(symbolPriceModel == null ? "" : AmountUtil.multiplyBigDecimalToDouble(symbolPriceModel.getSellerFeeRate(), 100, 2) + "%");
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 初始化图表  设置图表的样式
     */
    private void initLineChart() {
        MPChartUtils.setLineChartStyle(mBinding.lineChart);
        initLineChartData();
    }

    /**
     * 初始化图表数据
     */
    private void initLineChartData() {

        HashMap<String, String> map = new HashMap<>();

        map.put("startDatetime", DateUtil.fristMonth(DateUtil.DEFAULT_DATE_FMT));//开始时间就是  前一个月
        map.put("endDatetime", DateUtil.format(new Date(), DateUtil.DEFAULT_DATE_FMT));//结束时间就是 当前时间
        map.put("period", "1");
        map.put("symbol", "BTC");
        map.put("type", position + "");//0是  买入 1是卖出
        Call<BaseResponseListModel<LineChartDataModel>> lineChartData = RetrofitUtils.createApi(MyApi.class).getLineChartData("650200", StringUtils.getRequestJsonString(map));
        addCall(lineChartData);
        lineChartData.enqueue(new BaseResponseListCallBack<LineChartDataModel>(mActivity) {
            @Override
            protected void onSuccess(List<LineChartDataModel> data, String SucMessage) {
                if (data == null || data.size() == 0) {
                    return;
                }
                ArrayList<Entry> values1 = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    values1.add(new Entry(i, data.get(i).getPrice()));
                }
                MPChartUtils.setLineChartData(mBinding.lineChart, values1);
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 承兑商单笔买入最小金额
     */
    private void initMinCnyAmount() {
        NetWorrkUtils.getSystemServer("accept_order_min_cny_amount", new NetWorrkUtils.SystemServerListener() {
            @Override
            public void onSuccer(SystemParameterModel data) {
                mBinding.tvMinAmount.setText(data.getCvalue());
            }

            @Override
            public void onError(String error) {

            }
        });

        NetWorrkUtils.getSystemServer("accept_order_max_cny_amount", new NetWorrkUtils.SystemServerListener() {
            @Override
            public void onSuccer(SystemParameterModel data) {
                mBinding.tvMaxAmount.setText(data.getCvalue());
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void initClickListener() {
        //账单
        mBinding.tvBill.setOnClickListener(view -> OrderListActivity.open(mActivity));
        //支付方式  或者选择银行卡
        mBinding.llSelectBank.setOnClickListener(v -> {
            if (position == 0) {
                if (payTypePicker != null)
                    payTypePicker.show();
                else
                    initPayType();
            } else {
                UserBackCardActivity.open(mActivity, true);
            }
        });
        //提交按钮
        mBinding.btnConfirm.setOnClickListener(view -> {
//            SPUtilHelper.get
            if (SPUtilHelper.getRealName() == null || SPUtilHelper.getRealName().equals("")) {
//                startIdentity();
                new CommonDialog(mActivity).builder()
                        .setTitle("请先进行实名认证")
                        .setNegativeBtn("取消", null)
                        .setPositiveBtn("确定", new CommonDialog.OnPositiveListener() {
                            @Override
                            public void onPositive(View view) {
                                startIdentity();
                            }
                        }).show();
                return;
            }
            if (checkSubmit()) {
                showSubmitDialog();
            }
        });

        mBinding.tlWay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                cleanEditText();
                position = tab.getPosition();
                if (position == 0) {
                    mBinding.tlWay.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
                    mBinding.tlWay.setTabTextColors(getResources().getColor(R.color.invite_color_gray), getResources().getColor(R.color.colorPrimary));
                    initBuyView();
                } else {
                    mBinding.tlWay.setSelectedTabIndicatorColor(getResources().getColor(R.color.indicator_yealo_color));
                    mBinding.tlWay.setTabTextColors(getResources().getColor(R.color.invite_color_gray), getResources().getColor(R.color.indicator_yealo_color));

                    initSellerView();
                }
                //重新获取数据
                initLineChartData();
                initSymbolPrice();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mBinding.etMoney.setOnTouchListener((view, motionEvent) -> {
            isMoneyInput = true;
            return false;
        });
        mBinding.etNumber.setOnTouchListener((view, motionEvent) -> {
            isMoneyInput = false;
            return false;
        });
        //用点击事件会有bug
//        mBinding.etNumber.setOnClickListener(view -> isMoneyInput = false);
//        mBinding.etMoney.setOnClickListener(view -> isMoneyInput = true);
        //设置只能输入小数和整数
        mBinding.etMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mBinding.etNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //设置最多输入小数的位数
        mBinding.etMoney.setFilters(new InputFilter[]{new EditInputFilter(2)});
        mBinding.etNumber.setFilters(new InputFilter[]{new EditInputFilter(8)});
        mBinding.etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (!isMoneyInput || symbolPriceModel == null) {
                    return;
                }
                String moneyText = mBinding.etMoney.getText().toString().trim();
                if (TextUtils.isEmpty(moneyText)) {
                    mBinding.etNumber.setText("");
                    return;
                }
                double moneyDou = Double.parseDouble(moneyText);
                String number;
                if (position == 0) {
                    number = AmountUtil.divideDoubleToBigDecimal(moneyDou, symbolPriceModel.getBuyPrice(), 8);
                } else {
                    number = AmountUtil.divideDoubleToBigDecimal(moneyDou, symbolPriceModel.getSellerPrice(), 8);
                }
                mBinding.etNumber.setText(number);
            }
        });
        mBinding.etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isMoneyInput || symbolPriceModel == null) {
                    return;
                }
                String numberText = mBinding.etNumber.getText().toString().trim();
                if (TextUtils.isEmpty(numberText)) {
                    mBinding.etMoney.setText("");
                    return;
                }
                double moneyDou = Double.parseDouble(numberText);
                String money;
                if (position == 0) {
                    money = AmountUtil.multiplyBigDecimalToDouble(symbolPriceModel.getBuyPrice(), moneyDou, 2);
                } else {
                    money = AmountUtil.multiplyBigDecimalToDouble(symbolPriceModel.getSellerPrice(), moneyDou, 2);
                }
                mBinding.etMoney.setText(money);
            }
        });
    }

    /**
     * 获取BTC币种余额  每次重新进入页面就重新获取一次  避免买币买币的额度没减去
     */
    public void getSymbolList() {
        showLoadingDialog();
        if (TextUtils.isEmpty(SPUtilHelper.getUserId())) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("currency", "BTC");

        Call call = RetrofitUtils.createApi(MyApi.class).getSymbolList("802301", StringUtils.getRequestJsonString(map));
        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<WalletModel>(mActivity) {
            @Override
            protected void onSuccess(WalletModel data, String SucMessage) {
                if (data != null && data.getAccountList() != null && data.getAccountList().size() > 0) {
                    WalletModel.AccountListBean item = data.getAccountList().get(0);
                    String amount = AmountUtil.toMinWithUnit(item.getAmount(), item.getCurrency(), 8);
                    mBinding.tvAvailableAssets.setText(amount);
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 初始化买入界面
     */
    private void initBuyView() {
        mBinding.tvAvailableAssetsTitle.setVisibility(View.GONE);
        mBinding.tvAvailableAssets.setVisibility(View.GONE);//可用资产
        mBinding.btnConfirm.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mBinding.ivPayLogo.setVisibility(View.VISIBLE);
        if (payTypeModel != null) {
            mBinding.tvPayName.setText(payTypeModel.getName());
            mBinding.ivPayLogo.setImageResource(TextUtils.equals("支付宝", payTypeModel.getName()) ? R.mipmap.icon_ali_logo : R.mipmap.icon_pay_bank_logo);
        }
        mBinding.tvServiceCharge.setText(symbolPriceModel == null ? "" : AmountUtil.multiplyBigDecimalToDouble(symbolPriceModel.getBuyFeeRate(), 100, 2) + "%");
        mBinding.btnConfirm.setText("买入BTC");
    }

    /**
     * 初始化卖出界面
     */
    private void initSellerView() {
        mBinding.tvAvailableAssetsTitle.setVisibility(View.VISIBLE);
        mBinding.tvAvailableAssets.setVisibility(View.VISIBLE);//可用资产
        mBinding.ivPayLogo.setVisibility(View.GONE);
        if (payBankModel != null) {

            if (!TextUtils.isEmpty(payBankModel.getBankcardNumber()) && payBankModel.getBankcardNumber().length() > 5) {
                if (TextUtils.equals("支付宝", payBankModel.getBankName())) {
                    mBinding.tvPayName.setText("支付宝尾号为:" + payBankModel.getBankcardNumber().substring(payBankModel.getBankcardNumber().length() - 4, payBankModel.getBankcardNumber().length()));
                } else {
                    mBinding.tvPayName.setText("银行卡尾号为:" + payBankModel.getBankcardNumber().substring(payBankModel.getBankcardNumber().length() - 4, payBankModel.getBankcardNumber().length()));
                }
            } else {
                if (TextUtils.equals("支付宝", payBankModel.getBankName())) {
                    mBinding.tvPayName.setText("支付宝尾号为:" + payBankModel.getBankcardNumber());
                } else {
                    mBinding.tvPayName.setText("银行卡尾号为:" + payBankModel.getBankcardNumber());
                }
            }
        } else {
            mBinding.tvPayName.setText("");
        }
        mBinding.tvServiceCharge.setText(symbolPriceModel == null ? "" : AmountUtil.multiplyBigDecimalToDouble(symbolPriceModel.getSellerFeeRate(), 100, 2) + "%");
        mBinding.btnConfirm.setBackgroundColor(getResources().getColor(R.color.indicator_yealo_color));
        mBinding.btnConfirm.setText("卖出BTC");
    }

    /**
     * 检查购买的参数
     */
    private boolean checkSubmit() {
        if (TextUtils.isEmpty(mBinding.etNumber.getText().toString().trim())) {
            UITipDialog.showFail(mActivity, "请输入有效的数量");
            return false;
        }
        if (TextUtils.isEmpty(mBinding.etMoney.getText().toString().trim())) {
            UITipDialog.showFail(mActivity, "请输入有效的金额");
            return false;
        }
        if (position == 0) {
            if (payTypeModel == null) {
                UITipDialog.showFail(mActivity, "请选支付方式");
                return false;
            }
        } else {
            if (payBankModel == null) {
                UITipDialog.showFail(mActivity, "请选择银行卡");
                return false;
            }
        }
        return true;
    }

    private void showSubmitDialog() {
        DialogPayMessageBinding dialogView = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.dialog_pay_message, null, false);
        AlertDialog dialog = new AlertDialog.Builder(mActivity).create();
        dialog.setView(dialogView.getRoot());
        dialog.show();
        //设置弹窗里面的 文字高亮显示
        dialogView.tvMessage1.setText(StringUtil.highlight(mActivity, "请在10分钟内完成付款", "10分钟", "#4064E6", 0, 0));
        dialogView.tvMessage2.setText(StringUtil.highlight(mActivity, "转账请务必填写转账附信", "转账附信", "#4064E6", 0, 0));
        dialogView.tvConfirm.setOnClickListener(view1 -> {
            dialog.dismiss();
            boolean tradePwdFlag = SPUtilHelper.getTradePwdFlag();
            if (!tradePwdFlag) {
                UITipDialog.showSuccess(mActivity, "请先设置资金密码", new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (!TextUtils.isEmpty(SPUtilHelper.getUserPhoneNum())) {
                            PayPwdModifyActivity.open(mActivity, SPUtilHelper.getTradePwdFlag(), SPUtilHelper.getUserPhoneNum());
                        } else if (!TextUtils.isEmpty(SPUtilHelper.getUserEmail())) {
                            PayPwdModifyActivity.open(mActivity, SPUtilHelper.getTradePwdFlag(), SPUtilHelper.getUserEmail());
                        } else {
                            UITipDialog.showInfo(mActivity, "请先绑定手机号或者邮箱");
                        }
                    }
                });
                return;
            }

            //买入的时候不弹出输入密码的弹窗
            if (position == 0) {
                submitData("");
                return;
            }
            //卖出的时候需要弹出资金密码的弹框
            PasswordInputDialog pswDialog = PasswordInputDialog.build(mActivity)
                    .setView(position == 0 ? "买入BTC" : "卖出BTC", mBinding.etNumber.getText().toString().trim() + "BTC")
                    .setOnNegativeListener(null)
                    .setOnPositiveListener((view2, psaaword) -> {
//                        String walletPasswordByUserId = WalletHelper.getWalletPasswordByUserId(SPUtilHelper.getUserId());
//                        if (!TextUtils.equals(walletPasswordByUserId, psaaword)) {
//                            UITipDialog.showFail(mActivity, "密码错误");
//                            return;
//                        }
                        submitData(psaaword);
                    });
            pswDialog.show();
        });
    }

    /**
     * 购买或者出售btc
     */
    private void submitData(String tradePwd) {

        HashMap<String, String> map = new HashMap<>();
        String code;
        if (position == 0) {
            code = "625270";
            if (symbolPriceModel == null || payTypeModel == null)
                return;
            map.put("tradePrice", symbolPriceModel.getBuyPrice().toString());//	必填,交易价格，当时行情价
            map.put("receiveType", payTypeModel.getType());//必填,收款方式
        } else {
            code = "625271";
            if (payBankModel == null || symbolPriceModel == null)
                return;
            map.put("bankCardCode", payBankModel.getCode());
            map.put("tradePrice", symbolPriceModel.getSellerPrice().toString());//	必填,交易价格，当时行情价
            map.put("tradePwd", tradePwd);//	资金密码
        }
        map.put("count", BigDecimalUtils.multiply(new BigDecimal(mBinding.etNumber.getText().toString().trim()), LocalCoinDBUtils.getLocalCoinUnit("BTC")).toString());//必填，出售数量  btc的数量需要乘以10的八次方   每个币种的单位不同
        map.put("tradeAmount", mBinding.etMoney.getText().toString().trim());//必填,交易金额
        map.put("tradeCurrency", "CNY");//必填,交易币种
        map.put("userId", SPUtilHelper.getUserId());//userId

        Call<BaseResponseModel<SuccessModel>> baseResponseModelCall = RetrofitUtils.createApi(MyApi.class).submitSuccess(code, StringUtils.getRequestJsonString(map));
        addCall(baseResponseModelCall);
        showLoadingDialog();
        baseResponseModelCall.enqueue(new BaseResponseModelCallBack<SuccessModel>(mActivity) {
            @Override
            protected void onSuccess(SuccessModel data, String SucMessage) {
                UITipDialog.showInfo(mActivity, "交易成功");
                OrderDetailsActivity.open(mActivity, data.getCode());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 每次切换  买入卖出的时候  清楚  因为不会自802030动换算
     */
    private void cleanEditText() {
        mBinding.etMoney.setText("");
        mBinding.etNumber.setText("");
    }


    /**
     * 启动定时器 每隔10秒 获取一次价格数据,
     */
    private void starttimer() {
        if (subscribe != null) {
            return;
        }
        //10秒后  开始  间隔10秒一次
        subscribe = Observable.interval(10, 10, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        initSymbolPrice();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        starttimer();
        getSymbolList();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (subscribe != null) {
            subscribe.dispose();
            subscribe = null;
        }
    }

    /**
     * 启动身份证实名认证
     */
    private void startIdentity() {
        IdentifyPresenter mIdentifyPresenter = new IdentifyPresenter(mActivity, this);

        if (TextUtils.isEmpty(SPUtilHelper.getRealName())) {
            mIdentifyPresenter.startCardIndentify();
        } else {
            UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_iden_ok));
        }
    }

    //************身份证实名认证回调
    @Override
    public void onError(String msg) {
        disMissLoading();
    }


    @Override
    public void onIdStart() {
        showLoadingDialog();
    }

    @Override
    public void onIdEnd(IdResultCallback.IdResult result) {

    }

    @Override
    public void onFaceStart() {

    }

    @Override
    public void onFaceEnd(FaceResultCallback.FaceResult result) {
    }

    @Override
    public void onUpLoadStart() {
        showLoadingDialog();
    }

    @Override
    public void onUpLoadFailure(String info) {
        disMissLoading();
        ToastUtil.show(mActivity, info);
    }

    @Override
    public void onUpLoadSuccess() {
        //重新获取用户信息
        showLoadingDialog();
        mGetUserInfoPresenter.getUserInfoRequest();
    }


    //身份认证上传回调完成  无论成功失败
    @Override
    public void onUpLoadFinish() {
//        disMissLoading();
    }

    /**
     * 用户信息回调
     */
    @Override
    public void onStartGetUserInfo() {
        showLoadingDialog();
    }

    //用户信息  获取成功回调
    @Override
    public void onFinishedGetUserInfo(UserInfoModel userInfo, String errorMsg) {
        disMissLoading();
        if (TextUtils.isEmpty(errorMsg)) {
            new CommonDialog(mActivity).builder()
                    .setTitle("认证成功")
                    .setPositiveBtn("确定", null)
                    .show();
        }
    }

    //************身份证实名认证回调

    /**
     * 选择银行卡的回调
     *
     * @param userBankCardModel
     */
    @Subscribe
    public void selectBank(UserBankCardModel.ListBean userBankCardModel) {
        this.payBankModel = userBankCardModel;
        if (userBankCardModel == null)
            return;
        if (!TextUtils.isEmpty(userBankCardModel.getBankcardNumber()) && userBankCardModel.getBankcardNumber().length() > 5) {
            if (TextUtils.equals("支付宝", payBankModel.getBankName())) {
                mBinding.tvPayName.setText("支付宝尾号为:" + payBankModel.getBankcardNumber().substring(payBankModel.getBankcardNumber().length() - 4, payBankModel.getBankcardNumber().length()));
            } else {
                mBinding.tvPayName.setText("银行卡尾号为:" + payBankModel.getBankcardNumber().substring(payBankModel.getBankcardNumber().length() - 4, payBankModel.getBankcardNumber().length()));
            }
        } else {
            if (TextUtils.equals("支付宝", payBankModel.getBankName())) {
                mBinding.tvPayName.setText("支付宝尾号为:" + payBankModel.getBankcardNumber());
            } else {
                mBinding.tvPayName.setText("银行卡尾号为:" + payBankModel.getBankcardNumber());
            }
        }
    }
}