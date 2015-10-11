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

  private final BootstrapTabbedPanel<ITab> mainMenuTabbedPanel;

  public MainMenuPanel(final String id) {
    super(id);
    mainMenuTabbedPanel = new BootstrapTabbedPanel<ITab>("mainMenuTabbedPanel", new ArrayList<ITab>());
  }

  @Override
  protected void onInitialize() {
    mainMenuTabbedPanel.getTabs().add(new CrmTab(Model.of("CRM")));
    mainMenuTabbedPanel.getTabs().add(new PmTab(Model.of("PM")));
    mainMenuTabbedPanel.getTabs().add(new AlertTab(Model.of("Alerts")));
    mainMenuTabbedPanel.getTabs().add(new ReportTab(Model.of("Reports")));
    mainMenuTabbedPanel.getTabs().add(new AdministrationTab(Model.of("Administration")));

    add(mainMenuTabbedPanel);

    super.onInitialize();
  }
}
