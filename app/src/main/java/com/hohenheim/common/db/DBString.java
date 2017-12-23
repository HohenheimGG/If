package com.hohenheim.common.db;

/**
 * Created by hohenheim on 2017/12/18.
 */

public class DBString {

    /**
     * 旧数据库名称
     */
    @Deprecated
    public static final String SCAN_HISTORY_DB_NAME = "scan_history.db";

    /**
     * 新数据库名称
     */
    public static final String DB_NAME = "if.db";

    /**
     * 扫码历史记录
     */
    public static final String SCAN_HISTORY_TABLE_NAME = "scan_history_list";

    /**
     * 首页最近使用
     */
    public static final String HOME_RECENT_TABLE_NAME = "recent_used_list";

}
