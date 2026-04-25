package com.laolang.lightserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动后访问:
 * <ul>
 *     <li><code>servlet</code>: <a href="http://localhost:8092/servlet/ExampleServlet">http://localhost:8092/servlet/ExampleServlet</a> </li>
 *     <li>静态资源: <a href="http://localhost:8092/static/index.html">http://localhost:8092/static/index.html</a> </li>
 * </ul>
 * 如果使用 <code>idea</code> 启动, 则需要配置 <code>Working Directory</code> 为项目 <code>pom.xml</code> 所在目录
 */
public class HttpServer {
    private static final Logger log = LoggerFactory.getLogger(HttpServer.class);

    /**
     * 退出命令 <br />
     * 请求 <a href="http://localhost:8092/SHUTDOWN">http://localhost:8092/SHUTDOWN</a> 即可停止服务
     */
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        int port = 8092;
        try {
            log.info("server is starting");
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
            log.info("server is running in port: {}", port);
        } catch (IOException e) {
            log.error("server start failed. ", e);
            System.exit(1);
        }

        while (!shutdown) {
            Socket socket;
            InputStream input;
            OutputStream output;
            try {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();

                Request request = new Request(input);
                request.parse();

                Response response = new Response(output);
                response.setRequest(request);

                // 检查是动态请求还是静态请求
                if (request.getUri().startsWith("/servlet")) {
                    ServletProcessor processor = new ServletProcessor();
                    processor.process(request, response);
                } else {
                    StaticResourceProcessor processor = new StaticResourceProcessor();
                    processor.process(request, response);
                }

                socket.close();

                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
                if (shutdown) {
                    log.info("server shutdown");
                }
            } catch (Exception e) {
                log.error("server error: ", e);
            }
        }
    }

}
