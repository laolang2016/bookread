package com.laolang.lightserver;

import java.io.File;

public class Constant {

    private Constant() {
    }

    /**
     * <code>user.dir</code> 指 <code>JVM</code> 的启动路径
     */
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
}
