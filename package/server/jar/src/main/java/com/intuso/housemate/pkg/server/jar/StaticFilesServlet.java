package com.intuso.housemate.pkg.server.jar;

import com.google.common.io.ByteStreams;
import com.intuso.housemate.web.server.service.CommsServiceImpl;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tomc on 28/10/14.
 */
public class StaticFilesServlet extends HttpServlet {

    // class loader of web ui module where static files are located
    private final static ClassLoader CLASS_LOADER = CommsServiceImpl.class.getClassLoader();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() != null && req.getPathInfo().length() > 2 ? req.getPathInfo().substring(1) : "index.html";
        InputStream inputStream = CLASS_LOADER.getResourceAsStream(path);
        if(inputStream == null)
            resp.setStatus(HttpStatus.NOT_FOUND_404);
        else
            ByteStreams.copy(inputStream, resp.getOutputStream());
    }
}
