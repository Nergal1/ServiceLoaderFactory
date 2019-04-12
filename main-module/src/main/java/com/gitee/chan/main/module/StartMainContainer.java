package com.gitee.chan.main.module;

import android.content.Context;

import com.gitee.chan.common.library.startmain.IStartMainContainer;
import com.google.auto.service.AutoService;


/**
 * ================================================
 * Description:  主页面启动。
 * 1、声明@AutoService 注解，为在app启动的mainactivity中跳转主页面。
 * 2、实现已定义的IStartMainContainer接口
 *
 * Created by JohnnyChan on 2018/8/1
 *
 * ================================================
 */
@AutoService(IStartMainContainer.class)
public class StartMainContainer implements IStartMainContainer {
    @Override
    public void startMainActivity(Context context) {
        MainContainerActivity.start(context);
    }
}
