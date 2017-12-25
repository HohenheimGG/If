package com.hohenheim.scancode.event;

/**
 * Created by hohenheim on 2017/12/25.
 */

public class CaptureEvent {

    /**
     * R.id.decode_succeeded 成功
     * R.id.return_scan_result 扫描成功后返回上个页面
     */
    private int result;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
