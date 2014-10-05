package com.netbrasoft.gnuob.application.authorization;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.security.GNUOBPrincipal;

public class RolesSession extends WebSession {

    private static final long serialVersionUID = 2503512201455796747L;

    private static final Logger LOGGER = LoggerFactory.getLogger(RolesSession.class);

    private HttpServletRequest httpServletRequest;

    public RolesSession(Request request) {
        super(request);
        httpServletRequest = (HttpServletRequest) request.getContainerRequest();
    }

    public String getPassword() {
        GNUOBPrincipal gnuobPrincipal = (GNUOBPrincipal) httpServletRequest.getUserPrincipal();
        return gnuobPrincipal != null ? gnuobPrincipal.getPassword() : "-";
    }

    public String getSite() {
        GNUOBPrincipal gnuobPrincipal = (GNUOBPrincipal) httpServletRequest.getUserPrincipal();
        return gnuobPrincipal != null ? gnuobPrincipal.getSite() : "-";
    }

    public String getUsername() {
        GNUOBPrincipal gnuobPrincipal = (GNUOBPrincipal) httpServletRequest.getUserPrincipal();
        return gnuobPrincipal != null ? gnuobPrincipal.getName() : "-";
    }

    public boolean hasAnyRole(Roles roles) {
        for (String role : roles) {
            if (httpServletRequest.isUserInRole(role)) {
                return true;
            }
        }
        return false;
    }

    public void login(String username, String password) {
        try {
            httpServletRequest.login(username, password);
        } catch (ServletException e) {
            LOGGER.warn("Unable to login.", e);
        }
    }

    public void logout() {
        try {
            httpServletRequest.logout();
        } catch (ServletException e) {
            LOGGER.warn("Unable to logout.", e);
        }
    }
}
