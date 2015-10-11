package com.netbrasoft.gnuob.application.panel;

import org.apache.wicket.authroles.authentication.panel.SignInPanel;
import org.apache.wicket.util.time.Duration;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

public class LoginPanel extends SignInPanel {

  private static final String SIGN_IN_FORM = "signInForm";

  private static final long serialVersionUID = -438674592477878425L;

  public LoginPanel(final String id) {
    this(id, true);
  }

  public LoginPanel(final String id, final boolean includeRememberMe) {
    super(id, includeRememberMe);

    removeAll();
    add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
    add(new SignInForm(SIGN_IN_FORM));
  }
}
