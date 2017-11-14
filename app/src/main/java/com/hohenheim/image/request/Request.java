package com.hohenheim.image.request;

import android.widget.ImageView;

import com.hohenheim.image.Callback;
import com.hohenheim.image.Decoder;
import com.hohenheim.image.encoder.Encoder;

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
