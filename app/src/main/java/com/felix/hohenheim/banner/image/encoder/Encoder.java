package com.felix.hohenheim.banner.image.encoder;

import java.io.OutputStream;

/**
 * Created by hohenheim on 17/7/12.
 */

public interface Encoder<T> {

    boolean encode(T data, OutputStream os);
}
