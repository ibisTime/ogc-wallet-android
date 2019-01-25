package com.cdkj.token.model;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * @updateDts 2019/1/25
 */
public class MoneyTransactionTypeModel implements IPickerViewData {


    /**
     * id : 10
     * type : 1
     * parentKey : jour_biz_type_user
     * dkey : charge
     * dvalue : 充币
     * enDvalue : Deposit
     * updater : admin
     * updateDatetime : Nov 19, 2018 6:02:34 PM
     */

    private int id;
    private String type;
    private String parentKey;
    private String dkey;
    private String dvalue;
    private String enDvalue;
    private String updater;
    private String updateDatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getDkey() {
        return dkey;
    }

    public void setDkey(String dkey) {
        this.dkey = dkey;
    }

    public String getDvalue() {
        return dvalue;
    }

    public void setDvalue(String dvalue) {
        this.dvalue = dvalue;
    }

    public String getEnDvalue() {
        return enDvalue;
    }

    public void setEnDvalue(String enDvalue) {
        this.enDvalue = enDvalue;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public String getPickerViewText() {
        return dvalue;
    }
}
