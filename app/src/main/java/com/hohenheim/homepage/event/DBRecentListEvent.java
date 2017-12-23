package com.hohenheim.homepage.event;

import java.util.LinkedList;

/**
 * Created by hohenheim on 2017/12/23.
 */

public class DBRecentListEvent {

    private LinkedList<String> contents;

    public LinkedList<String> getContents() {
        return contents;
    }

    public void setContents(LinkedList<String> contents) {
        this.contents = contents;
    }
}
