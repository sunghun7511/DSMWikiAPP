package com.SHGroup.DSMWiki;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener{

    private final String url = "http://wiki.dsmhs.xyz/";
    private WebView wv;
    private Button back, front, home, favorite, test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();

        wv.loadUrl(url);

        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAllowContentAccess(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setAppCacheEnabled(true);

        back.setOnClickListener(this);
        front.setOnClickListener(this);
        home.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            if(!goBack()){
                Toast.makeText(this, "뒤로 갈 수 없습니다.", Toast.LENGTH_LONG).show();
            }
        }else if(v.getId() == R.id.front){
            if(wv.canGoForward()) {
                wv.goForward();
            }else {
                Toast.makeText(this, "앞으로 갈 수 없습니다.", Toast.LENGTH_LONG).show();
            }
        }else if(v.getId() == R.id.home){
            wv.loadUrl(url);
        }else if(v.getId() == R.id.favorite){

        //}else if(v.getId() == R.id.test){

        }else{
            Toast.makeText(this, "엥 이거 어케 했냐 ㄷㄷ;", Toast.LENGTH_LONG).show();
        }
    }

    public void initializeComponents() {
        wv = (WebView) findViewById(R.id.wv);
        back = (Button) findViewById(R.id.back);
        front = (Button) findViewById(R.id.front);
        home = (Button) findViewById(R.id.home);
        favorite = (Button) findViewById(R.id.favorite);
        //test = (Button) findViewById(R.id.test);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(goBack()){
            return;
        }
        finish();
    }

    public boolean goBack(){
        if(wv.canGoBack()){
            wv.goBack();
            return true;
        }
        return false;
    }
}