/*
 * Copyright 2016 Netbrasoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package br.com.netbrasoft.gnuob.application.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.wicketstuff.wicket.mount.core.annotation.MountPath;

import br.com.netbrasoft.gnuob.application.border.ContentBorder;
import br.com.netbrasoft.gnuob.application.panel.MainMenuPanel;
import br.com.netbrasoft.gnuob.application.security.AppRoles;

@MountPath("application.html")
@AuthorizeInstantiation({AppRoles.MANAGER, AppRoles.EMPLOYEE, AppRoles.ADMINISTRATOR})
public class MainPage extends BasePage {

  private static final String MAIN_MENU_PANEL_ID = "mainMenuPanel";

  private static final String CONTENT_BORDER_ID = "contentBorder";

  private static final long serialVersionUID = 2104311609974795936L;

  private final MainMenuPanel mainMenuPanel;

  private final ContentBorder contentBorder;

  public MainPage() {
    mainMenuPanel = new MainMenuPanel(MAIN_MENU_PANEL_ID);
    contentBorder = new ContentBorder(CONTENT_BORDER_ID);
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    contentBorder.add(mainMenuPanel.setOutputMarkupId(true));
    add(contentBorder.setOutputMarkupId(true));
  }
}
