package com.cdkj.token.model;


import java.math.BigDecimal;

/**
 * @updateDts 2019/1/21
 */
public class SymbolPriceModel {


    /**
     * buyPrice : 24179
     * sellerPrice : 24668
     * buyFeeRate : 0.001
     * sellerFeeRate : 0.002
     */

    private BigDecimal buyPrice;
    private BigDecimal sellerPrice;
    private BigDecimal buyFeeRate;
    private BigDecimal sellerFeeRate;

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getSellerPrice() {
        return sellerPrice;
    }

    public void setSellerPrice(BigDecimal sellerPrice) {
        this.sellerPrice = sellerPrice;
    }

    public BigDecimal getBuyFeeRate() {
        return buyFeeRate;
    }

    public void setBuyFeeRate(BigDecimal buyFeeRate) {
        this.buyFeeRate = buyFeeRate;
    }

    public BigDecimal getSellerFeeRate() {
        return sellerFeeRate;
    }

    public void setSellerFeeRate(BigDecimal sellerFeeRate) {
        this.sellerFeeRate = sellerFeeRate;
    }
}
