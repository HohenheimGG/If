package com.felix.hohenheim.banner.image;

import java.io.File;
import java.io.OutputStream;

/**
 * Created by hohenheim on 11/07/2017.
 */

public class SourceWrite implements DiskCache.Write {

    @Override
    public boolean write(OutputStream stream) {
        return false;
    }
}
