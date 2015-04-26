package com.netbrasoft.gnuob.application.page;

import java.util.ArrayList;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.application.security.GroupTab;
import com.netbrasoft.gnuob.application.security.SiteTab;
import com.netbrasoft.gnuob.application.security.UserTab;
import com.netbrasoft.gnuob.application.setting.SettingTab;

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel;

public class AdministrationTab extends AbstractTab {

   private static final long serialVersionUID = 4835579949680085443L;

   private ITab groupTab = new GroupTab(new Model<String>("Group"));
   private ITab settingTab = new SettingTab(new Model<String>("Setting"));
   private ITab siteTab = new SiteTab(new Model<String>("Site"));
   private ITab userTab = new UserTab(new Model<String>("User"));

   public AdministrationTab(IModel<String> title) {
      super(title);
   }

   @Override
   public WebMarkupContainer getPanel(String panelId) {
      BootstrapTabbedPanel<ITab> administrationTabbedPanel = new BootstrapTabbedPanel<ITab>(panelId, new ArrayList<ITab>()) {

         private static final long serialVersionUID = -8650291789763661400L;

         @Override
         public String getTabContainerCssClass() {
            return "nav nav-pills nav-stacked col-md-2";
         };
      };

      administrationTabbedPanel.getTabs().add(userTab);
      administrationTabbedPanel.getTabs().add(groupTab);
      administrationTabbedPanel.getTabs().add(siteTab);
      administrationTabbedPanel.getTabs().add(settingTab);

      return administrationTabbedPanel;
   }
}
