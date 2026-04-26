package com.laolang.lightserver.example;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleServlet implements Servlet {

    private static final Logger log = LoggerFactory.getLogger(ExampleServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("{} init", this.getClass().getSimpleName());
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        log.info("{} service start", this.getClass().getSimpleName());

        PrintWriter writer = response.getWriter();

        writer.println("Hello. Roses are red.");
        writer.print("Violets are blue.");

        log.info("{} service end", this.getClass().getSimpleName());
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        log.info("{} destroy", this.getClass().getSimpleName());
    }

}
