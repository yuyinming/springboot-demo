package com.example.valve;

import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;

/**
 * 定义一个tomcat容器里边的valve,用于给请求设置traceId
 */
public class TraceValve extends ValveBase {
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {

        request.getCoyoteRequest().getMimeHeaders().
                addValue("traceid").setString(UUID.randomUUID().toString());

        Valve next = getNext();
        if (null == next) {
            return;
        }

        next.invoke(request, response);
    }

}
