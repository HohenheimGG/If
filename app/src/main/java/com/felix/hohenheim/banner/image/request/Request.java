package com.felix.hohenheim.banner.image.request;

import android.widget.ImageView;

import com.felix.hohenheim.banner.image.Callback;
import com.felix.hohenheim.banner.image.Decoder;
import com.felix.hohenheim.banner.image.encoder.Encoder;

/**
 * Created by hohenheim on 17/7/14.
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
