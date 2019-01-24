package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.model.SendVerificationCode;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.common.ThaAppConstant;
import com.cdkj.token.databinding.ActivitySignUp2Binding;
import com.cdkj.token.user.CountryCodeListActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.StringUtils.isEmail;


public class SignUpActivity extends AbsStatusBarTranslucentActivity implements SendCodeInterface {

    private static final int TAB_MOBILE = 0;
    private static final int TAB_EMAIL = 1;

    private SendPhoneCodePresenter mSendCodePresenter;
    private ActivitySignUp2Binding mBinding;

    private int tabPosition = 0;

    private boolean agreeFlag = false; // 是否统一协议，默认不同意

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_up2, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setPageBgRes(R.color.white);

        mSendCodePresenter = new SendPhoneCodePresenter(this, this);

        mBinding.edtPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtRePassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtMobile.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);

        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.edtMobile.getLeftTextView().setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryInterCode()));
        ImgUtils.loadActImg(this, SPUtilHelper.getCountryFlag(), mBinding.edtMobile.getLeftImage());
    }


    private void initListener() {

        mBinding.tlWay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabPosition = tab.getPosition();

                if (TAB_MOBILE == tabPosition){

                    mBinding.edtMobile.setVisibility(View.VISIBLE);
                    mBinding.edtEmail.setVisibility(View.GONE);

                    mBinding.edtMobile.requestFocus();

                }else if (TAB_EMAIL == tabPosition){

                    mBinding.edtMobile.setVisibility(View.GONE);
                    mBinding.edtEmail.setVisibility(View.VISIBLE);

                    mBinding.edtEmail.requestFocus();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //国家区号选择
        mBinding.edtMobile.getLeftRootView().setOnClickListener(view -> {
            CountryCodeListActivity.open(this, true);
        });

        mBinding.edtCode.getSendCodeBtn().setOnClickListener(view -> {
            if (check("code")) {

                String bizType = "";
                String loginName = "";
                if (TAB_MOBILE == tabPosition){
                    bizType = "805041";
                    loginName = mBinding.edtMobile.getText().toString().trim();
                } else if (TAB_EMAIL == tabPosition){
                    bizType = "805043";
                    loginName = mBinding.edtEmail.getText().toString().trim();
                }

                SendVerificationCode sendVerificationCode = new SendVerificationCode(
                        loginName, bizType, "C", SPUtilHelper.getCountryInterCode());

                mSendCodePresenter.openVerificationActivity(sendVerificationCode);

            }
        });

        mBinding.ivAgree.setOnClickListener(view -> {

            agreeFlag = !agreeFlag;

            mBinding.ivAgree.setImageResource(agreeFlag ? R.mipmap.sign_agree : R.mipmap.sign_no_agree);
        });

        mBinding.tvProtocol.setOnClickListener(view -> {
            WebViewActivity.openkey(this, getString(R.string.pop_protocol), ThaAppConstant.getH5UrlLangage(""));
        });


        mBinding.btnConfirm.setOnClickListener(view -> {
            if (check("all")) {
                signUp();
            }
        });


    }

    private boolean check(String type) {

        if (TAB_MOBILE == tabPosition){

            if (TextUtils.isEmpty(mBinding.edtMobile.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_mobile_hint));
                return false;
            }

        }else if (TAB_EMAIL == tabPosition){

            if (TextUtils.isEmpty(mBinding.edtEmail.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_email_hint));
                return false;
            }

            if (isEmail(mBinding.edtEmail.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getString(R.string.signup_email_format_wrong));
                return false;
            }

        }

        if (type.equals("all")) {
            if (TextUtils.isEmpty(mBinding.edtCode.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_code_hint));
                return false;
            }

            if (TextUtils.isEmpty(mBinding.edtPassword.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_password_hint));
                return false;
            }

            if (TextUtils.isEmpty(mBinding.edtRePassword.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_repassword_hint));
                return false;
            }

            if (!mBinding.edtRePassword.getText().toString().trim().equals(mBinding.edtPassword.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_repassword_two_hint));
                return false;
            }

            if (!agreeFlag){
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_protocol_hint));
                return false;
            }

        }


        return true;
    }

    private void signUp() {
        Map<String, Object> map = new HashMap<>();
        map.put("kind", "C");
        if (TAB_MOBILE == tabPosition){
            map.put("mobile", mBinding.edtMobile.getText().toString().trim());
        } else if (TAB_EMAIL == tabPosition){
            map.put("email", mBinding.edtMobile.getText().toString().trim());
        }
        map.put("mobile", mBinding.edtMobile.getText().toString().trim());
        map.put("loginPwd", mBinding.edtPassword.getText().toString().trim());
        map.put("smsCaptcha", mBinding.edtCode.getText().toString().trim());
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);
        map.put("countryCode", SPUtilHelper.getCountryCode());
        map.put("interCode", SPUtilHelper.getCountryInterCode());

        String code = "";
        if (TAB_MOBILE == tabPosition){
            code = "805041";
        } else if (TAB_EMAIL == tabPosition){
            code = "805043";
        }
        Call call = RetrofitUtils.createApi(MyApi.class).signUp(code, StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserLoginModel>(this) {

            @Override
            protected void onSuccess(UserLoginModel data, String SucMessage) {
                if (!TextUtils.isEmpty(data.getToken()) || !TextUtils.isEmpty(data.getUserId())) {
                    showToast(getStrRes(R.string.user_sign_up_success));

                    SPUtilHelper.saveUserId(data.getUserId());
                    SPUtilHelper.saveUserToken(data.getToken());
                    SPUtilHelper.saveUserPhoneNum(mBinding.edtMobile.getText().toString().trim());
                    OtherLibManager.uemProfileSignIn(data.getUserId());
                    EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面

                    MainActivity.open(SignUpActivity.this);
                    finish();
                } else {
                    UITipDialog.showInfoNoIcon(SignUpActivity.this, getStrRes(R.string.user_sign_up_failure));
                }

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }


    //获取验证码相关
    @Override
    public void CodeSuccess(String msg,int req) {
        //启动倒计时
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.edtCode.getSendCodeBtn(), R.drawable.btn_code_blue_bg, R.drawable.gray,
                ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.white)));

    }

    @Override
    public void CodeFailed(String code, String msg,int req) {
        UITipDialog.showInfoNoIcon(this, msg);
    }

    @Override
    public void StartSend() {
        showLoadingDialog();
    }

    @Override
    public void EndSend() {
        disMissLoadingDialog();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSendCodePresenter != null) {
            mSendCodePresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSendCodePresenter != null) {
            mSendCodePresenter.clear();
            mSendCodePresenter = null;
        }
    }

}
