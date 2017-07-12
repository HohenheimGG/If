package com.felix.hohenheim.banner.image;

import com.felix.hohenheim.banner.utils.CloseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by hohenheim on 17/7/12.
 */

public class StreamEncoder implements Encoder<InputStream> {

    /**64 KB**/
    private static final int TEMP_BYTES_SIZE = 64 * 1024;

    @Override
    public boolean encode(InputStream data, OutputStream os) {
        byte[] buffer = new byte[TEMP_BYTES_SIZE];
        try{
            int read;
            while ((read = data.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(data);
        }
        return false;
    }
}
