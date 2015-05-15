package com.netbrasoft.gnuob.application.panel;

import java.util.ArrayList;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.application.page.tab.AdministrationTab;
import com.netbrasoft.gnuob.application.page.tab.AlertTab;
import com.netbrasoft.gnuob.application.page.tab.CrmTab;
import com.netbrasoft.gnuob.application.page.tab.PmTab;
import com.netbrasoft.gnuob.application.page.tab.ReportTab;

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel;

public class MainMenuPanel extends Panel {

   private static final long serialVersionUID = -1204513473673934094L;

   private final ITab crmTab = new CrmTab(new Model<String>("CRM"));

   private final ITab pmTab = new PmTab(new Model<String>("PM"));

   private final ITab alertTab = new AlertTab(new Model<String>("Alerts"));

   private final ITab reportTab = new ReportTab(new Model<String>("Reports"));

   private final ITab administrationTab = new AdministrationTab(new Model<String>("Administration"));

   private final BootstrapTabbedPanel<ITab> mainMenuTabbedPanel = new BootstrapTabbedPanel<ITab>("mainMenuTabbedPanel", new ArrayList<ITab>());

   public MainMenuPanel(final String id) {
      super(id);
   }

   @Override
   protected void onInitialize() {
      mainMenuTabbedPanel.getTabs().add(crmTab);
      mainMenuTabbedPanel.getTabs().add(pmTab);
      mainMenuTabbedPanel.getTabs().add(alertTab);
      mainMenuTabbedPanel.getTabs().add(reportTab);
      mainMenuTabbedPanel.getTabs().add(administrationTab);

      add(mainMenuTabbedPanel);

      super.onInitialize();
   }
}
