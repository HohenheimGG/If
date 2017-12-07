package com.hohenheim.common.deprecated.image.request;

import android.widget.ImageView;

import com.hohenheim.common.deprecated.image.Callback;
import com.hohenheim.common.deprecated.image.Decoder;
import com.hohenheim.common.deprecated.image.encoder.Encoder;

/**
 * Created by com.hohenheim on 17/7/14.
 */

public interface Request extends Callback {

    Request error(int resourceId);
    Request decoder(Decoder decoder);
    Request encoder(Encoder encoder);
    Request placeholder(int resourceId);
    Request into(ImageView view);
    void begin();
    void recycler();
}
