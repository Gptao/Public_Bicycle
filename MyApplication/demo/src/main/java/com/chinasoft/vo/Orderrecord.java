package com.chinasoft.vo;

import org.litepal.crud.DataSupport;

/**
 * Created by MX on 2017/8/4.
 */

public class Orderrecord extends DataSupport {
    private String starttime;
    private String endtime;
    private Integer cost;
    private String bicyid;

    public String getBicyid() {
        return bicyid;
    }

    public void setBicyid(String bicyid) {
        this.bicyid = bicyid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
