package ru.cwt.jetty;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.servlet.ErrorPageErrorHandler;

/**
 * @author e.chertikhin
 * @date 05/10/14 00:17
 * <p/>
 * Copyright (c) 2014 CrestWave Technologies LLC.
 * All right reserved.
 */
public class JettyErrorPageHandler extends ErrorPageErrorHandler {

    public JettyErrorPageHandler() {
        setShowStacks(false);
    }

    @Override
    protected void writeErrorPageBody(HttpServletRequest request, java.io.Writer writer, int code,
                                      String message, boolean showStacks) throws IOException {

        String uri = request.getRequestURI();

        writeErrorPageMessage(request, writer, code, message, uri);

        // if (showStacks)
        // writeErrorPageStacks(request,writer);
        // writer.write("<hr><i><small>Powered by Jetty://</small></i><hr/>\n");
    }
}
