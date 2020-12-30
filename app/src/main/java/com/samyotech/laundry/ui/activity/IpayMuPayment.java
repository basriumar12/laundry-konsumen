package com.samyotech.laundry.ui.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivityOrderDetailsBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.model.OrderListDTO;
import com.samyotech.laundry.preferences.SharedPrefrence;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class IpayMuPayment extends AppCompatActivity {

    private final String TAG = IpayMuPayment.class.getSimpleName();
    ActivityOrderDetailsBinding binding;
    Context mContext;
    SharedPrefrence sharedPrefrence;
    String orderLink;
    WebView wv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ipaymu);
        mContext = IpayMuPayment.this;

        sharedPrefrence = SharedPrefrence.getInstance(mContext);

        if (getIntent().hasExtra(Consts.ORDERLINK)) {
            orderLink = getIntent().getStringExtra(Consts.ORDERLINK);
        }

        wv = (WebView) findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setUserAgentString("Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");
        wv.loadUrl(orderLink);
        wv.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                WebView.HitTestResult hr = ((WebView)v).getHitTestResult();

                Log.i("TEST", "getExtra = "+ hr.getExtra() + "\t\t Type=" + hr.getType());
                return false;
            }
        });
    }

    private class VideoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);

            return false;
        }
    }
}
