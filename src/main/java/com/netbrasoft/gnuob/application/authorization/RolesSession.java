package com.netbrasoft.gnuob.application.authorization;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.security.GNUOBPrincipal;

public class RolesSession extends WebSession {

    private static final long serialVersionUID = 2503512201455796747L;

    private static final Logger LOGGER = LoggerFactory.getLogger(RolesSession.class);

    public RolesSession(Request request) {
        super(request);
    }

    public String getPassword() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();

        GNUOBPrincipal gnuobPrincipal = (GNUOBPrincipal) httpServletRequest.getUserPrincipal();
        return gnuobPrincipal != null ? gnuobPrincipal.getPassword() : "-";
    }

    public String getSite() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();

        GNUOBPrincipal gnuobPrincipal = (GNUOBPrincipal) httpServletRequest.getUserPrincipal();
        return gnuobPrincipal != null ? gnuobPrincipal.getSite() : "-";
    }

    public String getUsername() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();

        GNUOBPrincipal gnuobPrincipal = (GNUOBPrincipal) httpServletRequest.getUserPrincipal();
        return gnuobPrincipal != null ? gnuobPrincipal.getName() : "-";
    }

    public boolean hasAnyRole(Roles roles) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();

        for (String role : roles) {
            if (httpServletRequest.isUserInRole(role)) {
                return true;
            }
        }
        return false;
    }

    public void login(String username, String password) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();

            httpServletRequest.login(username, password);
        } catch (ServletException e) {
            LOGGER.warn("Unable to login.", e);
        }
    }

    public void logout() {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();

            httpServletRequest.logout();
        } catch (ServletException e) {
            LOGGER.warn("Unable to logout.", e);
        }
    }
}
