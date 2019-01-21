package com.cdkj.token.model;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * @updateDts 2019/1/21
 */
public class PayTypeModel implements IPickerViewData {


    /**
     * type : 0
     * name : 银行卡
     */

    private String type;
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
