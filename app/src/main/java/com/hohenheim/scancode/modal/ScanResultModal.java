package com.hohenheim.scancode.modal;

/**
 * Created by com.hohenheim on 2017/10/1.
 */

public class ScanResultModal implements Cloneable{

    private String yearToDate;//年月日

    private String hourToSecond;//时分秒

    private String content;//二维码扫码内容

    private int type;//modal类型

    public String getYearToDate() {
        return yearToDate;
    }

    public void setYearToDate(String yearToDate) {
        this.yearToDate = yearToDate;
    }

    public String getHourToSecond() {
        return hourToSecond;
    }

    public void setHourToSecond(String hourToSecond) {
        this.hourToSecond = hourToSecond;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ScanResultModal modal = new ScanResultModal();
        modal.setHourToSecond(this.hourToSecond);
        modal.setYearToDate(this.yearToDate);
        modal.setContent(this.content);
        modal.setType(this.type);
        return modal;
    }

    @Override
    public String toString() {
        return "ScanResultModal{" +
                "yearToDate='" + yearToDate + '\'' +
                ", hourToSeconds=" + hourToSecond +
                ", contents=" + content +
                ", type='" + type + '\'' +
                '}';
    }
}
