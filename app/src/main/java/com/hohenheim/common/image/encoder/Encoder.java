package com.hohenheim.common.image.encoder;

import java.io.OutputStream;

/**
 * Created by com.hohenheim on 17/7/12.
 */

public interface Encoder<T> {

    boolean encode(T data, OutputStream os);
}
