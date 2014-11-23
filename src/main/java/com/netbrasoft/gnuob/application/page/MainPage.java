package com.netbrasoft.gnuob.application.page;

import java.util.ArrayList;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.application.border.ContentBorder;
import com.netbrasoft.gnuob.wicket.bootstrap.extensions.markup.html.tabs.BootstrapTabbedPanel;
import com.netbrasoft.gnuob.wicket.bootstrap.markup.html.BootstrapPage;

public class MainPage extends BootstrapPage {

   private static final long serialVersionUID = -549085276353464601L;

   private final ITab entityTab = new EntityTab(new Model<String>("Entities"));
   private final ITab crmTab = new CrmTab(new Model<String>("CRM"));
   private final ITab accountancyTab = new AccountancyTab(new Model<String>("Accountancy"));
   private final ITab alertTab = new AlertTab(new Model<String>("Alerts"));
   private final ITab reportTab = new ReportTab(new Model<String>("Reports"));
   private final ITab administrationTab = new AdministrationTab(new Model<String>("Administration"));

   private final BootstrapTabbedPanel<ITab> mainMenuTabbedPanel = new BootstrapTabbedPanel<ITab>("mainMenuTabbedPanel", new ArrayList<ITab>());

   private final ContentBorder contentBorder = new ContentBorder("contentBorder");

   @Override
   public String getTitle() {
      return getString("gnuob.site.title");
   }

   @Override
   protected void onInitialize() {
      mainMenuTabbedPanel.getTabs().add(entityTab);
      mainMenuTabbedPanel.getTabs().add(crmTab);
      mainMenuTabbedPanel.getTabs().add(accountancyTab);
      mainMenuTabbedPanel.getTabs().add(alertTab);
      mainMenuTabbedPanel.getTabs().add(reportTab);
      mainMenuTabbedPanel.getTabs().add(administrationTab);

      contentBorder.add(mainMenuTabbedPanel);
      add(contentBorder);

      super.onInitialize();
   }
}
