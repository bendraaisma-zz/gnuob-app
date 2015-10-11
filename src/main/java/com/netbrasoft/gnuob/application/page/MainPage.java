package com.netbrasoft.gnuob.application.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.wicketstuff.wicket.mount.core.annotation.MountPath;

import com.netbrasoft.gnuob.application.border.ContentBorder;
import com.netbrasoft.gnuob.application.panel.MainMenuPanel;
import com.netbrasoft.gnuob.application.security.AppRoles;

@MountPath("application.html")
@AuthorizeInstantiation({AppRoles.MANAGER, AppRoles.EMPLOYEE, AppRoles.ADMINISTRATOR})
public class MainPage extends BasePage {

  private static final long serialVersionUID = 2104311609974795936L;

  private MainMenuPanel mainMenuPanel = new MainMenuPanel("mainMenuPanel");

  private final ContentBorder contentBorder = new ContentBorder("contentBorder");

  @Override
  protected void onInitialize() {

    contentBorder.add(mainMenuPanel);
    add(contentBorder);

    super.onInitialize();
  }
}
