package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.adapter.UserFriendsAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.UserFriendsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class UserFriendsActivity extends AbsRefreshListActivity {

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserFriendsActivity.class));
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("我的好友");
        initRefreshHelper(RefreshHelper.LIMITE);

    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        return new UserFriendsAdapter(listData);
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {
        ArrayList<UserFriendsModel> data = new ArrayList<>();
//        data.add(new UserFriendsModel());
//        data.add(new UserFriendsModel());
//        data.add(new UserFriendsModel());


        getData(pageindex, limit, isShowDialog);
    }

    /**
     * 获取数据
     */
    private void getData(int pageindex, int limit, boolean isShowDialog) {
//        805122
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("limit", limit + "");
        map.put("start", pageindex + "");
        Call call = RetrofitUtils.createApi(MyApi.class).getMyFriend("805122", StringUtils.getRequestJsonString(map));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<UserFriendsModel>(this) {
            @Override
            protected void onSuccess(UserFriendsModel data, String SucMessage) {
                if (data == null) {
                    return;
                }

                mRefreshHelper.setData(data.getList(), "暂无好友", 0);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }
}
