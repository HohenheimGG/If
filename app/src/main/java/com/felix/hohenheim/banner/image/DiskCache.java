package com.felix.hohenheim.banner.image;

import java.io.File;
import java.io.OutputStream;

/**
 * Created by hohenheim on 11/07/2017.
 */

public interface DiskCache {

    interface Write {
        boolean write(OutputStream stream);
    }

    void put(String s, Write write);
    File get(String s);
    void delete(String s);
    void clear();
}
