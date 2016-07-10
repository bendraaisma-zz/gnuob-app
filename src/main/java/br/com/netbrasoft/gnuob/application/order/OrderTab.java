package br.com.netbrasoft.gnuob.application.order;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import br.com.netbrasoft.gnuob.api.Invoice;
import br.com.netbrasoft.gnuob.api.Order;

public class OrderTab extends AbstractTab {

  private static final long serialVersionUID = 4835579949680085443L;

  public OrderTab(final IModel<String> title) {
    super(title);
  }

  @Override
  public WebMarkupContainer getPanel(final String panelId) {
    final Order order = new Order();
    order.setActive(true);
    order.setInvoice(new Invoice());
    return new OrderPanel(panelId, Model.of(order));
  }
}
