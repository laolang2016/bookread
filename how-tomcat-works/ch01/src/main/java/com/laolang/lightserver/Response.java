package com.laolang.lightserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Response {

    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private static final int BUFFER_SIZE = 1024;

    private Request request;

    private final OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            File file = new File(HttpServer.WEB_ROOT, request.getUri());
            if (file.exists()) {
                String httpHeader = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n";
                output.write(httpHeader.getBytes());

                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            }
            else {
                String errorMsg = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
                        + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";
                output.write(errorMsg.getBytes());
            }
        }
        catch (Exception e) {
            log.error("send response error: ", e);
        }
        finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

}
