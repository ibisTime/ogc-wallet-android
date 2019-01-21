package com.cdkj.token.model;

/**
 * @updateDts 2019/1/21
 */
public class LineChartDataModel {


    /**
     * id : 310983
     * type : 0
     * symbol : BTC
     * period : 1
     * price : 24544
     * createDatetime : Jan 19, 2019 8:01:40 PM
     */

    private int id;
    private String type;
    private String symbol;
    private String period;
    private int price;
    private String createDatetime;

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }
}
