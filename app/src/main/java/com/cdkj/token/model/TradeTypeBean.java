package com.cdkj.token.model;

/**
 * @updateDts 2019/3/24
 */
public class TradeTypeBean {


    /**
     * id : 3
     * symbol : ETH
     * ename : Ethereun
     * cname : 以太坊
     * type : 0
     * unit : 18
     * icon : JDKKCR[AZUP]S3IH6T{WU0M.png
     * pic1 : JDKKCR[AZUP]S3IH6T{WU0M.png
     * pic2 : JDKKCR[AZUP]S3IH6T{WU0M.png
     * pic3 : JDKKCR[AZUP]S3IH6T{WU0M.png
     * orderNo : 1
     * withdrawFee : 10
     * contractAddress : kkkk
     * status : 0
     * isAccept : 1
     */

    private int id;
    private String symbol;
    private String ename;
    private String cname;
    private String type;
    private int unit;
    private String icon;
    private String pic1;
    private String pic2;
    private String pic3;
    private int orderNo;
    private int withdrawFee;
    private String contractAddress;
    private String status;
    private String isAccept;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(int withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(String isAccept) {
        this.isAccept = isAccept;
    }
}
