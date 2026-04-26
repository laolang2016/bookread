package com.laolang.lightserver;

import com.laolang.lightserver.connector.http.Constants;
import com.laolang.lightserver.connector.http.HttpRequest;
import com.laolang.lightserver.connector.http.HttpResponse;
import com.laolang.lightserver.connector.http.HttpRequestFacade;
import com.laolang.lightserver.connector.http.HttpResponseFacade;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import javax.servlet.Servlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletProcessor {

    private static final Logger log = LoggerFactory.getLogger(ServletProcessor.class);

    public void process(HttpRequest request, HttpResponse response) {

        String uri = request.getRequestURI();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;
        try {
            // create a URLClassLoader
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            // File classPath = new File(Constants.WEB_ROOT);

            // 注意: 需要修改 idea 的运行路径为当前项目根目录
            String classPathMaven = System.getProperty("user.dir") + "/target/classes";
            log.info("classPathMaven: {}", classPathMaven);
            File classPath = new File(classPathMaven);

            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
        Class clazz = null;
        try {
            String fullClassName = "com.laolang.lightserver.example." + servletName;
            log.info("fullClassName: {}", fullClassName);
            clazz = loader.loadClass(fullClassName);
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }

        Servlet servlet = null;

        try {
            servlet = (Servlet) clazz.newInstance();
            HttpRequestFacade requestFacade = new HttpRequestFacade(request);
            HttpResponseFacade responseFacade = new HttpResponseFacade(response);

            servlet.service(requestFacade, responseFacade);
            ((HttpResponse) response).finishResponse();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

}