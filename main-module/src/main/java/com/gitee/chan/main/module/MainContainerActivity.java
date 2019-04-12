package com.gitee.chan.main.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.gitee.chan.common.library.tab.ITabPage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;


/**
 * ================================================
 * Description:  主框架页面
 *
 * Created by JohnnyChan on 2018/8/1
 *
 * ================================================
 */
public class MainContainerActivity extends AppCompatActivity {

    private BottomNavigationView mNavigation;

    private List<Fragment> mFragmentList = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context,MainContainerActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container_activity);

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        serviceLoad();
        initEvent();

        mNavigation.setSelectedItemId(mNavigation.getMenu().getItem(0).getItemId());
    }

    private void serviceLoad(){
        //加载Fragment 类型的ServiceLoader
        ServiceLoader<Fragment> iterator = ServiceLoader.load(Fragment.class);

        for (Iterator<Fragment> iterator1 = iterator.iterator(); iterator1.hasNext(); ) {
            final Fragment fragment = iterator1.next();

            //获取ITabPage 注解的类
            ITabPage property = fragment.getClass().getAnnotation(ITabPage.class);
            if (property == null) {
                continue;
            }
            Menu menu = mNavigation.getMenu();
            MenuItem item = menu.add(property.tabName());
            int drawable = getResources().getIdentifier(property.iconName(), "drawable", getPackageName());
            item.setIcon(drawable);
            mFragmentList.add(fragment);
        }

    }

    private void initEvent(){
        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int size = mNavigation.getMenu().size();
                int index = 0;

                for (int i = 0; i < size; i++) {
                    if (mNavigation.getMenu().getItem(i) == item) {
                        index = i;
                        break;
                    }
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, mFragmentList.get(index))
                        .commit();

                return true;
            }
        });
    }

}
