package br.com.netbrasoft.gnuob.application.security.user;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import br.com.netbrasoft.gnuob.api.User;

public class UserTab extends AbstractTab {

  private static final long serialVersionUID = 4835579949680085443L;

  public UserTab(final IModel<String> title) {
    super(title);
  }

  @Override
  public WebMarkupContainer getPanel(final String panelId) {
    final User user = new User();
    user.setActive(true);
    return new UserPanel(panelId, Model.of(user));
  }
}
