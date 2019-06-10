package com.nisshoku.mgnt.exceptions;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse,
                       final AccessDeniedException ex) throws IOException, ServletException {

        httpServletResponse.sendError(403, "Access denied!");
    }
}
