package br.com.netbrasoft.gnuob.application.product;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import br.com.netbrasoft.gnuob.api.Product;

public class ProductTab extends AbstractTab {

  private static final long serialVersionUID = 4835579949680085443L;

  public ProductTab(final IModel<String> title) {
    super(title);
  }

  @Override
  public WebMarkupContainer getPanel(final String panelId) {
    final Product product = new Product();
    product.setActive(true);
    return new ProductPanel(panelId, Model.of(product));
  }
}
