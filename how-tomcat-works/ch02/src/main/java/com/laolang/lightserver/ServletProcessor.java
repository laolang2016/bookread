package com.laolang.lightserver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletProcessor {

    private static final Logger log = LoggerFactory.getLogger(ServletProcessor.class);

    public void process(Request request, Response response) {
        String uri = request.getUri();
        String serlvetName = uri.substring(uri.lastIndexOf("/") + 1);

        // 类加载器
        URLClassLoader loader = null;
        try {
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            // File classPath = new File(Constant.WEB_ROOT);
            // 注意: 需要修改 idea 的运行路径为当前项目根目录
            String classPathMaven = System.getProperty("user.dir") + "/target/classes";
            log.info("classPathMaven: {}", classPathMaven);
            File classPath = new File(classPathMaven);
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            log.error("process error: ", e);
        }

        // 实例化 servlet
        Class clazz = null;
        try {
            String fullClassName = "com.laolang.lightserver.example." + serlvetName;
            log.info("fullClassName: {}", fullClassName);
            clazz = loader.loadClass(fullClassName);
        } catch (ClassNotFoundException e) {
            log.error("{} not found. e: ", serlvetName, e);
        }

        // 使用门面模式, 屏蔽 Request 和 Response 细节
        Servlet servlet = null;
        RequestFacade requestFacade = new RequestFacade(request);
        ResponseFacade responseFacade = new ResponseFacade(response);
        try {
            servlet = (Servlet) clazz.newInstance();
            servlet.service((ServletRequest) requestFacade, (ServletResponse) responseFacade);
        } catch (Exception e) {
            log.error("new servlet error. servletName:{},e: ", serlvetName, e);
        } catch (Throwable e) {
            log.error("new servlet error. servletName:{},e: ", serlvetName, e);
        }
    }
}
