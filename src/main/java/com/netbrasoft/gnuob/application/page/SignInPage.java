package com.netbrasoft.gnuob.application.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.netbrasoft.gnuob.application.panel.LoginPanel;

public class SignInPage extends BasePage {

   private static final long serialVersionUID = -8080808937122686622L;

   public SignInPage() {
      this(null);
   }

   public SignInPage(final PageParameters parameters) {
      add(new LoginPanel("signInPanel"));
   }
}
