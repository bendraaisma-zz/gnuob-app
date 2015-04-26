package com.netbrasoft.gnuob.application.security;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.api.Group;

public class GroupTab extends AbstractTab {

   private static final long serialVersionUID = 4835579949680085443L;

   public GroupTab(IModel<String> title) {
      super(title);
   }

   @Override
   public WebMarkupContainer getPanel(String panelId) {
      Group group = new Group();
      group.setActive(true);
      return new GroupPanel(panelId, new Model<Group>(group));
   }
}
