package com.hohenheim.common.image.diskCache;

import com.hohenheim.common.image.encoder.Encoder;
import com.hohenheim.common.utils.CloseUtil;

import java.io.OutputStream;

/**
 * Created by com.hohenheim on 11/07/2017.
 */

public class SourceWrite<Data> implements DiskCache.Write {

    private Encoder<Data> encoder;
    private Data data;

    public SourceWrite(Encoder<Data> encoder, Data data) {
        this.encoder = encoder;
        this.data = data;
    }

    @Override
    public boolean write(OutputStream stream) {
        if(stream == null)
            return false;
        boolean success = encoder.encode(data, stream);
        CloseUtil.close(stream);
        return success;
    }


}
