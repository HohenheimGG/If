package com.felix.hohenheim.banner.zxing.modal;

import java.util.List;

/**
 * Created by hohenheim on 2017/10/1.
 */

public class ScanResultModal {

    private String yearToDate;//年月日

    private List<String> hourToSeconds;//时分秒

    private List<String> contents;//二维码扫码内容

    public String getYearToDate() {
        return yearToDate;
    }

    public void setYearToDate(String yearToDate) {
        this.yearToDate = yearToDate;
    }

    public List<String> getHourToSeconds() {
        return hourToSeconds;
    }

    public void setHourToSeconds(List<String> hourToSeconds) {
        this.hourToSeconds = hourToSeconds;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "ScanResultModal{" +
                "yearToDate='" + yearToDate + '\'' +
                ", hourToSeconds=" + hourToSeconds +
                ", contents=" + contents +
                '}';
    }
}
