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

  public PmTab(final IModel<String> title) {
    super(title);
  }

  @Override
  public WebMarkupContainer getPanel(final String panelId) {
    final BootstrapTabbedPanel<ITab> productTabbedPanel = new BootstrapTabbedPanel<ITab>(panelId, new ArrayList<ITab>()) {

      private static final long serialVersionUID = -8650291789763661400L;

      @Override
      public String getTabContainerCssClass() {
        return "nav nav-pills nav-stacked col-md-2";
      }
    };

    productTabbedPanel.getTabs().add(new CategoryTab(Model.of("Category")));
    productTabbedPanel.getTabs().add(new ContentTab(Model.of("Content")));
    productTabbedPanel.getTabs().add(new ProductTab(Model.of("Product")));

    return productTabbedPanel;
  }
}
