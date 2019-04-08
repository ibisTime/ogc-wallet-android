package com.cdkj.token.model.db;

import org.litepal.crud.DataSupport;

/**
 * 钱包数据库 字段名  对应 WalletDBColumn类
 * <p>
 * 数据库修改要考虑版本兼容问题
 * <p>
 * Created by cdkj on 2018/6/6.
 */
public class WalletDBModel extends DataSupport {

    public String userId;
    public String walletName; //钱包名称
    public String helpWordsEn;// 助记词 英文
    public String walletPassWord;

    public String btcAddress;//地址
    public String btcPrivateKey;//私钥

    public String ethAddress;//地址
    public String ethPrivateKey;//私钥

    public String wanAddress;//地址
    public String wanPrivateKey;//私钥

    public String usdtAddress;//地址
    public String usdtPrivateKey;//私钥

    public String ethTokenAddress;//地址
    public String ethTokenPrivateKey;//私钥

    public String wanTokenAddress;//地址
    public String wanTokenPrivateKey;//私钥

    public String trxAddress;//波场地址
    public String trxPrivateKey;//波场私钥


    //以下为140之前版本兼容  历史遗留问题............
    private String helpcenterEn;// 助记词 英文
    private String helpWordsrEn;// 助记词 英文
    private String coinType;//所属币类型
    private String privataeKey;//私钥

    public String getHelpcenterEn() {
        return helpcenterEn;
    }

    public void setHelpcenterEn(String helpcenterEn) {
        this.helpcenterEn = helpcenterEn;
    }

    public String getHelpWordsrEn() {
        return helpWordsrEn;
    }

    public void setHelpWordsrEn(String helpWordsrEn) {
        this.helpWordsrEn = helpWordsrEn;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getPrivataeKey() {
        return privataeKey;
    }

    public void setPrivataeKey(String privataeKey) {
        this.privataeKey = privataeKey;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHelpWordsEn() {
        return helpWordsEn;
    }

    public void setHelpWordsEn(String helpWordsEn) {
        this.helpWordsEn = helpWordsEn;
    }

    public String getWalletPassWord() {
        return walletPassWord;
    }

    public void setWalletPassWord(String walletPassWord) {
        this.walletPassWord = walletPassWord;
    }

    public String getBtcAddress() {
        return btcAddress;
    }

    public void setBtcAddress(String btcAddress) {
        this.btcAddress = btcAddress;
    }

    public String getBtcPrivateKey() {
        return btcPrivateKey;
    }

    public void setBtcPrivateKey(String btcPrivateKey) {
        this.btcPrivateKey = btcPrivateKey;
    }

    public String getEthAddress() {
        return ethAddress;
    }

    public void setEthAddress(String ethAddress) {
        this.ethAddress = ethAddress;
    }

    public String getEthPrivateKey() {
        return ethPrivateKey;
    }

    public void setEthPrivateKey(String ethPrivateKey) {
        this.ethPrivateKey = ethPrivateKey;
    }

    public String getWanAddress() {
        return wanAddress;
    }

    public void setWanAddress(String wanAddress) {
        this.wanAddress = wanAddress;
    }

    public String getWanPrivateKey() {
        return wanPrivateKey;
    }

    public void setWanPrivateKey(String wanPrivateKey) {
        this.wanPrivateKey = wanPrivateKey;
    }

    public String getUsdtAddress() {
        return usdtAddress;
    }

    public void setUsdtAddress(String usdtAddress) {
        this.usdtAddress = usdtAddress;
    }

    public String getUsdtPrivateKey() {
        return usdtPrivateKey;
    }

    public void setUsdtPrivateKey(String usdtPrivateKey) {
        this.usdtPrivateKey = usdtPrivateKey;
    }

    public String getEthTokenAddress() {
        return ethTokenAddress;
    }

    public void setEthTokenAddress(String ethTokenAddress) {
        this.ethTokenAddress = ethTokenAddress;
    }

    public String getEthTokenPrivateKey() {
        return ethTokenPrivateKey;
    }

    public void setEthTokenPrivateKey(String ethTokenPrivateKey) {
        this.ethTokenPrivateKey = ethTokenPrivateKey;
    }

    public String getWanTokenAddress() {
        return wanTokenAddress;
    }

    public void setWanTokenAddress(String wanTokenAddress) {
        this.wanTokenAddress = wanTokenAddress;
    }

    public String getWanTokenPrivateKey() {
        return wanTokenPrivateKey;
    }

    public void setWanTokenPrivateKey(String wanTokenPrivateKey) {
        this.wanTokenPrivateKey = wanTokenPrivateKey;
    }

    public String getTrxAddress() {
        return trxAddress;
    }

    public void setTrxAddress(String trxAddress) {
        this.trxAddress = trxAddress;
    }

    public String getTrxPrivateKey() {
        return trxPrivateKey;
    }

    public void setTrxPrivateKey(String trxPrivateKey) {
        this.trxPrivateKey = trxPrivateKey;
    }
}

