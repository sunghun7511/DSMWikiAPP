package com.SHGroup.DSMWiki;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by dsm2017 on 2017-03-08.
 */
public class DetectWebViewClient extends WebViewClient {
    private MainActivity main;

    public DetectWebViewClient(MainActivity main){
        this.main = main;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        main.favorite.setText(main.getFavorits().contains(url)?"★":"☆");
    }
}
