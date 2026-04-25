package com.laolang.lightserver;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticResourceProcessor {

    private static final Logger log = LoggerFactory.getLogger(StaticResourceProcessor.class);

    public void process(Request request, Response response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            log.error("process error: ", e);
        }
    }
}
