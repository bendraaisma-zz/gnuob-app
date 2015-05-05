package com.netbrasoft.gnuob.application.page.tab;

import java.util.ArrayList;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.application.category.CategoryTab;
import com.netbrasoft.gnuob.application.content.ContentTab;
import com.netbrasoft.gnuob.application.product.ProductTab;

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel;

public class PmTab extends AbstractTab {

   private static final long serialVersionUID = 4835579949680085443L;

   private ITab categoryTab = new CategoryTab(new Model<String>("Category"));
   private ITab contentTab = new ContentTab(new Model<String>("Content"));
   private ITab productTab = new ProductTab(new Model<String>("Product"));

   public PmTab(final IModel<String> title) {
      super(title);
   }

   @Override
   public WebMarkupContainer getPanel(final String panelId) {
      BootstrapTabbedPanel<ITab> productTabbedPanel = new BootstrapTabbedPanel<ITab>(panelId, new ArrayList<ITab>()) {

         private static final long serialVersionUID = -8650291789763661400L;

         @Override
         public String getTabContainerCssClass() {
            return "nav nav-pills nav-stacked col-md-2";
         }
      };

      productTabbedPanel.getTabs().add(categoryTab);
      productTabbedPanel.getTabs().add(productTab);
      productTabbedPanel.getTabs().add(contentTab);

      return productTabbedPanel;
   }
}
