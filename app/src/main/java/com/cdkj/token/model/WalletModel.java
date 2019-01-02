package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by cdkj on 2018/12/29.
 */

public class WalletModel implements Parcelable {

    /**
     * accountNumber : A201812291103482478930
     * userId : U20190120150901336752451
     * currency : ETH
     * address : 0x0222a52739885ee19666ca2616d9a518800d516f
     * type : C
     * status : 0
     * amount : 0
     * frozenAmount : 0
     * md5 : 4122cb13c7a474c1976c9706ae36521d
     * inAmount : 0
     * outAmount : 0
     * createDatetime : Jan 20, 2019 3:09:02 PM
     */

    private String accountNumber;
    private String userId;
    private String currency;
    private String address;
    private String type;
    private String status;
    private BigDecimal amount;
    private BigDecimal frozenAmount;
    private String md5;
    private int inAmount;
    private int outAmount;
    private String createDatetime;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getInAmount() {
        return inAmount;
    }

    public void setInAmount(int inAmount) {
        this.inAmount = inAmount;
    }

    public int getOutAmount() {
        return outAmount;
    }

    public void setOutAmount(int outAmount) {
        this.outAmount = outAmount;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(accountNumber);
        parcel.writeString(userId);
        parcel.writeString(currency);
        parcel.writeString(address);
        parcel.writeString(type);
        parcel.writeString(status);
        parcel.writeString(md5);
        parcel.writeInt(inAmount);
        parcel.writeInt(outAmount);
        parcel.writeString(createDatetime);
    }

    public WalletModel(){

    }

    public WalletModel(Parcel in) {
        accountNumber = in.readString();
        userId = in.readString();
        currency = in.readString();
        address = in.readString();
        type = in.readString();
        status = in.readString();
        md5 = in.readString();
        inAmount = in.readInt();
        outAmount = in.readInt();
        createDatetime = in.readString();
    }

    public static final Creator<WalletModel> CREATOR = new Creator<WalletModel>() {
        @Override
        public WalletModel createFromParcel(Parcel in) {
            return new WalletModel(in);
        }

        @Override
        public WalletModel[] newArray(int size) {
            return new WalletModel[size];
        }
    };
}
