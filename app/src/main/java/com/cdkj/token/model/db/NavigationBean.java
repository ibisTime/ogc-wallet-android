package com.cdkj.token.model.db;

import org.litepal.crud.DataSupport;

/**
 * @updateDts 2019/3/1
 */
public class NavigationBean extends DataSupport {


    /**
     * code : DH201810120023250100000
     * name : 投资
     * type : app_menu
     * url : 123456
     * pic : 20190228185324.png
     * enPic : 20190228185347.png
     * status : 1
     * location : app
     * orderNo : 0
     * parentCode : DH201810120023250000000
     * remark :
     */

    private String code;
    private String name;
    private String type;
    private String url;
    private String pic;
    private String enPic;
    private String status;
    private String location;
    private int orderNo;
    private String parentCode;
    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getEnPic() {
        return enPic;
    }

    public void setEnPic(String enPic) {
        this.enPic = enPic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
