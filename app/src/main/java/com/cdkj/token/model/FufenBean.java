package com.cdkj.token.model;

import java.math.BigDecimal;

/**
 * @updateDts 2019/3/17
 */
public class FufenBean {


    /**
     * regAcount : 0.0
     * firstBbAcount : 0.0
     * totalUnCashAmount : 4.0E8
     * totalAward : 4.0E8
     * inviteCount : 2.0
     */

    private BigDecimal regAcount;
    private BigDecimal firstBbAcount;
    private BigDecimal totalUnCashAmount;
    private BigDecimal totalAward;
    private BigDecimal inviteCount;

    public BigDecimal getRegAcount() {
        return regAcount;
    }

    public void setRegAcount(BigDecimal regAcount) {
        this.regAcount = regAcount;
    }

    public BigDecimal getFirstBbAcount() {
        return firstBbAcount;
    }

    public void setFirstBbAcount(BigDecimal firstBbAcount) {
        this.firstBbAcount = firstBbAcount;
    }

    public BigDecimal getTotalUnCashAmount() {
        return totalUnCashAmount;
    }

    public void setTotalUnCashAmount(BigDecimal totalUnCashAmount) {
        this.totalUnCashAmount = totalUnCashAmount;
    }

    public BigDecimal getTotalAward() {
        return totalAward;
    }

    public void setTotalAward(BigDecimal totalAward) {
        this.totalAward = totalAward;
    }

    public BigDecimal getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(BigDecimal inviteCount) {
        this.inviteCount = inviteCount;
    }
}
