package com.netbrasoft.gnuob.application.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.page.SignInPage;
import com.netbrasoft.gnuob.application.security.AppRoles;

public class HeaderPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.ADMINISTRATOR, AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class LogoutAjaxLink extends AjaxLink<Void> {

    private static final long serialVersionUID = -6950515027229520882L;

    public LogoutAjaxLink() {
      super("logout");
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      AppServletContainerAuthenticatedWebSession.get().signOut();
      AppServletContainerAuthenticatedWebSession.get().invalidate();
      AppServletContainerAuthenticatedWebSession.get().invalidateNow();
      setResponsePage(SignInPage.class);
    }
  }

  private static final long serialVersionUID = 3137234732197409313L;

  private static final Logger LOGGER = LoggerFactory.getLogger(HeaderPanel.class);

  public HeaderPanel(String id) {
    super(id);
  }

  @Override
  protected void onInitialize() {
    try {
      ServletContext application = WebApplication.get().getServletContext();
      InputStream inputStream = application.getResourceAsStream("/META-INF/MANIFEST.MF");
      Attributes attributes = new Manifest(inputStream).getMainAttributes();

      add(new Label("implementationVendor", attributes.getValue("Implementation-Vendor")));
      add(new Label("implementationTitle", attributes.getValue("Implementation-Title")));
      add(new Label("implementationVersion", attributes.getValue("Implementation-Version")));

    } catch (IOException e) {
      LOGGER.warn(e.getMessage(), e);

      add(new Label("implementationVendor", "-"));
      add(new Label("implementationTitle", "-"));
      add(new Label("implementationVersion", "-"));
    }

    add(new LogoutAjaxLink());

    super.onInitialize();
  }
}
