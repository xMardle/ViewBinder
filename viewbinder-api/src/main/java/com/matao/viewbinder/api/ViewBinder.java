package com.matao.viewbinder.api;

import android.app.Activity;
import android.view.View;

import com.matao.viewbinder.api.provider.ActivityProvider;
import com.matao.viewbinder.api.provider.Provider;
import com.matao.viewbinder.api.provider.ViewProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by matao on 2016-10-31 14:38
 */

public class ViewBinder {

    private static final ActivityProvider PROVIDER_ACTIVITY = new ActivityProvider();
    private static final ViewProvider PROVIDER_VIEW = new ViewProvider();

    private static final Map<String, Binder> BINDER_MAP = new HashMap<>();

    public static void inject(Activity activity) {
        inject(activity, activity, PROVIDER_ACTIVITY);
    }

    public static void inject(View view) {
        inject(view, view);
    }

    // for fragment
    public static void inject(Object host, View view) {
        inject(host, view, PROVIDER_VIEW);
    }

    /**
     * @param host     注解 View 变量所在的类
     * @param source   查找 View 的地方，Activity & View 自身就可以查找，Fragment 需要在自己的 itemView 中查找
     * @param provider 是一个接口，定义了不同对象（比如 Activity、View 等）如何去查找目标 View
     */
    public static void inject(Object host, Object source, Provider provider) {
        String className = host.getClass().getName();
        try {
            Binder binder = BINDER_MAP.get(className);
            if (binder == null) {
                Class<?> binderClass = Class.forName(className + "$$Binder");
                binder = (Binder) binderClass.newInstance();
                BINDER_MAP.put(className, binder);
            }
            binder.inject(host, source, provider);
        } catch (Exception e) {
            throw new RuntimeException("Unable to inject for " + className, e);
        }
    }
}
