package com.laolang.lightserver;

import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Request {

    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private final InputStream input;

    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    /**
     * 解析 request
     */
    public void parse() {
        // 读取 request
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        }
        catch (IOException e) {
            log.error("parse request error: ", e);
            i = -1;
        }

        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }
        log.info("request:{}", request);

        // 解析 uri
        uri = parseUri(request.toString());
    }

    /**
     * 解析 <code>uri</code> <br />
     * 请求的第一行类似 <code>GET /a HTTP/1.1</code>, 第一个和第二个空格之间的内容就是 <code>uri</code>
     * @param requestString 请求字符串
     * @return uri
     */
    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1) {
                return requestString.substring(index1 + 1, index2);
            }
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

}
