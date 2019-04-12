package com.gitee.chan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gitee.chan.common.library.services.Services;
import com.gitee.chan.common.library.startmain.IStartMainContainer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 通过Service.load 来加载已经标识IStartMainContainer.class 的注解的类，在main-module里面的StartMainContainer类
        Services.load(IStartMainContainer.class)
                .startMainActivity(this);

        finish();
    }
}
