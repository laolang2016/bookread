package com.laolang.lightserver.startup;

import com.laolang.lightserver.connector.http.HttpConnector;

/**
 * 启动后访问:
 * <ul>
 *     <li><code>servlet</code>: <a href="http://localhost:8092/servlet/ExampleServlet">http://localhost:8092/servlet/ExampleServlet</a> </li>
 *     <li>静态资源: <a href="http://localhost:8092/static/index.html">http://localhost:8092/static/index.html</a> </li>
 * </ul>
 * 如果使用 <code>idea</code> 启动, 则需要配置 <code>Working Directory</code> 为项目 <code>pom.xml</code> 所在目录
 */
public final class Bootstrap {

    public static void main(String[] args) {
        HttpConnector connector = new HttpConnector();
        connector.start();
    }

}