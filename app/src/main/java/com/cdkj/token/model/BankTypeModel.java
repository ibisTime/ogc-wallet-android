package com.cdkj.token.model;

/**
 * @updateDts 2019/1/17
 */
public class BankTypeModel {


    /**
     * id : 17
     * bankCode : alipay
     * bankName : 支付宝
     * channelType : 40
     * status : 1
     * channelBank :
     */

    private int id;
    private String bankCode;
    private String bankName;
    private String channelType;
    private String status;
    private String channelBank;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChannelBank() {
        return channelBank;
    }

    public void setChannelBank(String channelBank) {
        this.channelBank = channelBank;
    }
}
