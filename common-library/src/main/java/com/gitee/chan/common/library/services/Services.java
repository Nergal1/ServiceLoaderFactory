package com.gitee.chan.common.library.services;


import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * ================================================
 * Description:  Service加载类
 *
 * Created by JohnnyChan on 2018/8/1
 *
 * ================================================
 */
public final class Services {

    public static <T> T load(Class<T> tClass) throws ServiceNotFoundException {
        Iterator<T> iterator = ServiceLoader.load(tClass)
                .iterator();
        if (iterator.hasNext()) {
            return iterator
                    .next();
        } else {
            throw new ServiceNotFoundException();
        }
    }
}
