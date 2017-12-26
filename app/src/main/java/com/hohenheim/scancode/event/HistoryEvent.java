package com.hohenheim.scancode.event;

import com.hohenheim.scancode.modal.ScanResultModal;

import java.util.List;

/**
 * Created by hohenheim on 2017/12/26.
 */

public class HistoryEvent {

    private List<ScanResultModal> list;

    public List<ScanResultModal> getList() {
        return list;
    }

    public void setList(List<ScanResultModal> list) {
        this.list = list;
    }
}
