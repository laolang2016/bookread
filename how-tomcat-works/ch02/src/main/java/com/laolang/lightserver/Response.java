package com.laolang.lightserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Response implements ServletResponse {

    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private static final int BUFFER_SIZE = 1024;
    private Request request;
    private final OutputStream output;
    private PrintWriter writer;
    private boolean headerSent = false;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    private void sendHeader() throws IOException {
        if (!headerSent) {
            String httpHeader = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n";
            output.write(httpHeader.getBytes());
            headerSent = true;
        }
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            File file = new File(Constant.WEB_ROOT, request.getUri());
            sendHeader();

            fis = new FileInputStream(file);
            int ch = fis.read(bytes, 0, BUFFER_SIZE);
            while (ch != -1) {
                output.write(bytes, 0, ch);
                ch = fis.read(bytes, 0, BUFFER_SIZE);
            }

        } catch (FileNotFoundException e) {
            String errorMsg = "HTTP/1.1 404 File Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 23\r\n" +
                    "\r\n" +
                    "<h1>File Not Found</h1>";
            output.write(errorMsg.getBytes());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        sendHeader();
        // true 表示调用 println 方法会刷新输出
        // 但是调用 print 方法不会刷新输出
        writer = new PrintWriter(output, true);
        return writer;
    }

    @Override
    public String getCharacterEncoding() {
        return "";
    }

    @Override
    public String getContentType() {
        return "";
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }


    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public void setContentLength(int len) {

    }

    @Override
    public void setContentLengthLong(long len) {

    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public void setBufferSize(int size) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
