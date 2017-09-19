package com.felix.hohenheim.banner.zxing.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.felix.hohenheim.banner.R;


public class ScanResultDialog extends Dialog {

    private TextView resultTextView;

    public ScanResultDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_scan_result);

        Button cancelButton = (Button) findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        resultTextView = (TextView) findViewById(R.id.tv_result);

        getWindow().setBackgroundDrawable(new BitmapDrawable());
    }

    public void setResultText(String result){
        resultTextView.setText(result);
    }
}
