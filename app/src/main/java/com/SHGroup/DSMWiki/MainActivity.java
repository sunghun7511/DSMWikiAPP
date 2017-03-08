package com.SHGroup.DSMWiki;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    public final String url = "http://wiki.dsmhs.xyz/"; //위키 주소
    public WebView wv;
    public Button back, front, home, favorite, more;
    private PopupMenu pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //버튼 및 웹뷰, 팝업메뉴 초기화
        initializeComponents();

        //url 불러옴.
        wv.loadUrl(url);

        //즐겨찾기 확인을 위해 웹뷰 클라이언트를 설정함.
        wv.setWebViewClient(new DetectWebViewClient(this));

        //자바 스크립트 활성화, 파일 저장을 위해 몇가지 설정
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAllowContentAccess(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setAppCacheEnabled(true);


        pm.getMenu().add(Menu.NONE, FAVORITES, Menu.NONE, "즐겨찾기");
        pm.getMenu().add(Menu.NONE, DEVELOPERS, Menu.NONE, "개발자");

        //처리를 위해 현재 클래스로 리스너 설정
        back.setOnClickListener(this);
        front.setOnClickListener(this);
        home.setOnClickListener(this);
        favorite.setOnClickListener(this);
        more.setOnClickListener(this);
        pm.setOnMenuItemClickListener(this);
    }

    //CPU 소모율 낭비를 줄이기 위해 내부적으로 즐겨찾기 불러오기 리스트는 저장해놓음
    private List<String> favorite_list = null;
    //즐겨찾기 목록을 List<String> 형식으로 가져옴.
    //만약 리스트가 없으면 새로 불러옴.
    public List<String> getFavorits(){
        if(favorite_list == null){
            favorite_list = new ArrayList<>();
            SharedPreferences sp = getSharedPreferences("favorits", MODE_PRIVATE);
            //파일 입출력은 SharedPreferences 를 사용
            if(sp.contains("size") &&
                    sp.getInt("size", -1) > 0) {
                for(int i = 0 ; i < sp.getInt("size", 0) ; i ++ ){
                    favorite_list.add(sp.getString(Integer.toString(i), ""));
                }
                //size 로 리스트의 길이를 저장함.
                //그 후, 0, 1, 2 ... 같은 방식으로 즐겨찾기를 저장함.
            }
        }
        return favorite_list;
    }

    public void setFavorite(List<String> list){
        favorite_list = list; //임시 리스트에 현재 리스트를 저장함.
        SharedPreferences sp = getSharedPreferences("favorits", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("size", favorite_list == null?0:favorite_list.size());
        //size를 저장함
        if(favorite_list != null){
            for(int i = 0 ; i < favorite_list.size() ; i ++ ){
                ed.putString(Integer.toString(i) ,favorite_list.get(i));
            }
            //즐겨찾기 문자열들을 저장함.
        }

        //SharedPreferences 저장
        ed.commit();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){ //뒤로가기 버튼
            if(!goBack()){
                Toast.makeText(this, "뒤로 갈 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId() == R.id.front){ //앞으로가기 버튼
            if(wv.canGoForward()) {
                wv.goForward();
            }else {
                Toast.makeText(this, "앞으로 갈 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId() == R.id.home){ //메인 버튼
            wv.loadUrl(url);
        }else if(v.getId() == R.id.favorite){ //즐겨찾기 버튼
            List<String> favs = getFavorits();
            if(favs.contains(wv.getUrl())){
                favs.remove(wv.getUrl());
                Toast.makeText(this, "즐겨찾기에서 제거되었습니다.", Toast.LENGTH_SHORT).show();
            }else{
                favs.add(wv.getUrl());
                Toast.makeText(this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
            }
            //즐겨찾기 목록 설정
            setFavorite(favs);

            //즐겨찾기의 별 모양 변경
            favorite.setText(favs.contains(wv.getUrl())?"☆":"★");
        }else if(v.getId() == R.id.more){
            pm.show(); //팝업메뉴 보여주기.
        }else{
            //데-헷
            Toast.makeText(this, "엥 이거 어케 했냐 ㄷㄷ;", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item){
        if(item.getItemId() == FAVORITES){

        }else if(item.getItemId() == DEVELOPERS){

        }else{
            //데-헷
            Toast.makeText(this, "엥 이거 어케 했냐 ㄷㄷ;", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    public final int FAVORITES = 123450001;
    public final int DEVELOPERS = 123450002;
    public void initializeComponents() { //웹뷰와 버튼 정의.
        wv = (WebView) findViewById(R.id.wv);
        back = (Button) findViewById(R.id.back);
        front = (Button) findViewById(R.id.front);
        home = (Button) findViewById(R.id.home);
        favorite = (Button) findViewById(R.id.favorite);
        more = (Button) findViewById(R.id.more);
        pm = new PopupMenu(this, more);
    }

    @Override
    public void onBackPressed() { //뒤로가기 버튼을 눌렀을때
        //super.onBackPressed();
        if(goBack()){ //만약 뒤로 갈 사이트가 있다면 간다.
            return;
        }
        //없으면 앱 종료.
        finish();
    }

    public boolean goBack(){ //뒤로가기
        if(wv.canGoBack()){
            wv.goBack(); //뒤로 갈 수 있으면 뒤로 간 후 return true;
            return true;
        }
        return false; //뒤로 갈 수 없으면 return false;
    }
}