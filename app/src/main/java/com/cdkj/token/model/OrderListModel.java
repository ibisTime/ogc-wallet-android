package com.cdkj.token.model;

import java.io.Serializable;

/**
 * @updateDts 2019/1/14
 */
public class OrderListModel implements Serializable {
    private int type;
    private int nameType;
    private int payType;

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNameType() {
        return nameType;
    }

    public void setNameType(int nameType) {
        this.nameType = nameType;
    }
}
