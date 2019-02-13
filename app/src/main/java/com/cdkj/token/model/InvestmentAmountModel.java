package com.cdkj.token.model;

import java.math.BigDecimal;

/**
 * 用户投资数据
 * Created by cdkj on 2018/9/28.
 */

public class InvestmentAmountModel {


    /**
     * btcTotalIncome : 0.0
     * btcTotalInvest : 0.0
     * usdtTotalIncome : 0.0
     * usdtTotalInvest : 0.0
     */

    private BigDecimal btcTotalIncome;
    private BigDecimal btcTotalInvest;
    private BigDecimal usdtTotalIncome;
    private BigDecimal usdtTotalInvest;

    public BigDecimal getBtcTotalIncome() {
        return btcTotalIncome;
    }

    public void setBtcTotalIncome(BigDecimal btcTotalIncome) {
        this.btcTotalIncome = btcTotalIncome;
    }

    public BigDecimal getBtcTotalInvest() {
        return btcTotalInvest;
    }

    public void setBtcTotalInvest(BigDecimal btcTotalInvest) {
        this.btcTotalInvest = btcTotalInvest;
    }

    public BigDecimal getUsdtTotalIncome() {
        return usdtTotalIncome;
    }

    public void setUsdtTotalIncome(BigDecimal usdtTotalIncome) {
        this.usdtTotalIncome = usdtTotalIncome;
    }

    public BigDecimal getUsdtTotalInvest() {
        return usdtTotalInvest;
    }

    public void setUsdtTotalInvest(BigDecimal usdtTotalInvest) {
        this.usdtTotalInvest = usdtTotalInvest;
    }
}
