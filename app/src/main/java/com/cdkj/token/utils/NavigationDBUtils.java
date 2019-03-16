package com.cdkj.token.utils;

import android.content.ContentValues;
import android.text.TextUtils;

import com.cdkj.token.model.db.NavigationBean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @updateDts 2019/3/1
 */
public class NavigationDBUtils {

    /**
     * 存储所有的  配置
     *
     * @param navigationList
     */
    public static void updateNavigationList(List<NavigationBean> navigationList) {
        // 如果数据库没有数据，则保存所有数据
        if (!DataSupport.isExist(NavigationBean.class)) {
            if (navigationList != null && navigationList.size() > 0) {
                DataSupport.saveAll(navigationList);
            }
            return;
        }

        for (NavigationBean bean : navigationList) {
            if (!navigationList.contains(bean)) {  //如果数据库不存在 则保存
                bean.save();
            } else { // 数据存在则，更新Icon
                ContentValues values = new ContentValues();
                values.put("pic", bean.getPic());
                values.put("code", bean.getCode());
                values.put("status", bean.getStatus());
                values.put("orderNo", bean.getOrderNo());
                values.put("url", bean.getUrl());
                values.put("type", bean.getType());
                values.put("enPic", bean.getEnPic());
                values.put("location", bean.getLocation());
                values.put("parentCode", bean.getParentCode());

                DataSupport.updateAll(NavigationBean.class, values, "name=?", bean.getName());

            }
        }
    }

    /**
     * 获取所有的配置
     *
     * @return
     */
    public static List<NavigationBean> getNavigationList() {

        List<NavigationBean> allNavigationList = DataSupport.findAll(NavigationBean.class);

        return allNavigationList;

    }

    public static NavigationBean getNavigationName(String name) {
        List<NavigationBean> navigationList = getNavigationList();

        if (navigationList != null && navigationList.size() > 0) {
            for (NavigationBean navigationBean : navigationList) {
                if (TextUtils.equals(name, navigationBean.getName())) {
                    return navigationBean;
                }
            }
        }
        return null;
    }

    public static ArrayList<NavigationBean> getNavigationparentCode(String parentCode) {
        List<NavigationBean> navigationList = getNavigationList();
        ArrayList<NavigationBean> parentCodeNavigationList = new ArrayList<>();
        if (navigationList != null && navigationList.size() > 0) {
            for (NavigationBean navigationBean : navigationList) {
                if (TextUtils.equals(parentCode, navigationBean.getParentCode())) {
                    parentCodeNavigationList.add(navigationBean);
                }
            }
        }
        return parentCodeNavigationList;
    }

}
