package com.laolang.lightserver;

import com.laolang.lightserver.connector.http.HttpRequest;
import com.laolang.lightserver.connector.http.HttpResponse;
import java.io.IOException;

public class StaticResourceProcessor {

    public void process(HttpRequest request, HttpResponse response) {
        try {
            response.sendStaticResource();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
