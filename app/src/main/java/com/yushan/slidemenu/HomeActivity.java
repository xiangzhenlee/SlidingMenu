package com.yushan.slidemenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView iv_menu;
    private LinearLayout dl_main;
    private SlideMenu slideMenu;
    private ArrayList<String> menuData;
    private ListView lv_menu;
    private MenuListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData(){
        menuData = new ArrayList<>();
        for (int i = 0; i < 50; i++){
            String menuStr = "第"+ (i + 1) +"个条目";
            menuData.add(menuStr);
        }

        adapter = new MenuListAdapter(this,menuData);
        lv_menu.setAdapter(adapter);
    }

    private void initView(){
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(this);
        dl_main = (LinearLayout) findViewById(R.id.dl_main);
        slideMenu = (SlideMenu) findViewById(R.id.slideMenu);

        lv_menu = (ListView)findViewById(R.id.lv_menu);
        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String str = menuData.get(position);
                Toast.makeText(HomeActivity.this,str,Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_menu:
                slideMenu.showMenu();
                break;
        }
    }

}
