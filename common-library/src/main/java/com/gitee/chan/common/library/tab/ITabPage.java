package com.gitee.chan.common.library.tab;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * ================================================
 * Description:  Tab配置页注解
 *
 * Created by JohnnyChan on 2018/8/1
 *
 * ================================================
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ITabPage {

    // tab标题
    String tabName();
    // tab 图标
    String iconName();
}
