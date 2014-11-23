package com.netbrasoft.gnuob.application.page;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class AccountancyTab extends AbstractTab {

   private static final long serialVersionUID = 4835579949680085443L;

   public AccountancyTab(IModel<String> title) {
      super(title);
   }
   
   @Override
   public WebMarkupContainer getPanel(String panelId) {
      return new WebMarkupContainer(panelId);
   }
}
