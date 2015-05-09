package com.netbrasoft.gnuob.application.page;

import java.util.ArrayList;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.model.Model;
import org.wicketstuff.wicket.mount.core.annotation.MountPath;

import com.netbrasoft.gnuob.application.border.ContentBorder;
import com.netbrasoft.gnuob.application.page.tab.AdministrationTab;
import com.netbrasoft.gnuob.application.page.tab.AlertTab;
import com.netbrasoft.gnuob.application.page.tab.CrmTab;
import com.netbrasoft.gnuob.application.page.tab.PmTab;
import com.netbrasoft.gnuob.application.page.tab.ReportTab;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel;

@MountPath("application.html")
@AuthorizeInstantiation({ AppRoles.MANAGER, AppRoles.EMPLOYEE, AppRoles.ADMINISTRATOR })
public class MainPage extends BasePage {

   private static final long serialVersionUID = 2104311609974795936L;

   private final ITab crmTab = new CrmTab(new Model<String>("CRM"));

   private final ITab pmTab = new PmTab(new Model<String>("PM"));

   private final ITab alertTab = new AlertTab(new Model<String>("Alerts"));

   private final ITab reportTab = new ReportTab(new Model<String>("Reports"));

   private final ITab administrationTab = new AdministrationTab(new Model<String>("Administration"));

   private final BootstrapTabbedPanel<ITab> mainMenuTabbedPanel = new BootstrapTabbedPanel<ITab>("mainMenuTabbedPanel", new ArrayList<ITab>());

   private final ContentBorder contentBorder = new ContentBorder("contentBorder");

   @Override
   protected void onInitialize() {
      mainMenuTabbedPanel.getTabs().add(crmTab);
      mainMenuTabbedPanel.getTabs().add(pmTab);
      mainMenuTabbedPanel.getTabs().add(alertTab);
      mainMenuTabbedPanel.getTabs().add(reportTab);
      mainMenuTabbedPanel.getTabs().add(administrationTab);

      contentBorder.add(mainMenuTabbedPanel);
      add(contentBorder);

      super.onInitialize();
   }
}
