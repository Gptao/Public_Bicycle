package com.chinasoft.extra;

import org.litepal.LitePalApplication;

/**
 * Created by Blue on 2017/7/30.
 */

public class MyApplication extends LitePalApplication {
    private String name;
    @Override
    public void onCreate() {
        super.onCreate();
        setName("hello");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
