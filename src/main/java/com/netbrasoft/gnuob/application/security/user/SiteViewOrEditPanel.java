package com.netbrasoft.gnuob.application.security.user;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.Site;
import com.netbrasoft.gnuob.api.User;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

public class SiteViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class RemoveAjaxLink extends AjaxLink<Void> {

    private static final long serialVersionUID = -6950515027229520882L;

    public RemoveAjaxLink() {
      super("remove");
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      // TODO Auto-generated method stub
    }
  }

  class SiteDataview extends DataView<Site> {

    private static final long serialVersionUID = 2246346365193989354L;

    public SiteDataview() {
      super("siteDataview", new SiteListDataProvider(), ITEMS_PER_PAGE);
    }

    @Override
    protected void populateItem(Item<Site> item) {
      IModel<Site> compound = new CompoundPropertyModel<Site>(item.getModelObject());
      item.setModel(compound);
      item.add(new Label("name"));
      item.add(new Label("description"));
      item.add(new RemoveAjaxLink());
    }
  }

  class SiteEditFragement extends Fragment {

    private static final long serialVersionUID = 8640403483040526601L;

    public SiteEditFragement() {
      super("siteViewOrEditFragement", "siteEditFragement", SiteViewOrEditPanel.this, SiteViewOrEditPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(siteDataviewContainer.setOutputMarkupId(true));
      add(sitePagingNavigator);
      super.onInitialize();
    }
  }

  class SiteListDataProvider extends ListDataProvider<Site> {

    private static final long serialVersionUID = 5259243752700177690L;

    @Override
    protected List<Site> getData() {
      return ((User) SiteViewOrEditPanel.this.getDefaultModelObject()).getSites();
    }
  }

  class SiteViewFragement extends Fragment {

    private static final long serialVersionUID = 8640403483040526601L;

    public SiteViewFragement() {
      super("siteViewOrEditFragement", "siteViewFragement", SiteViewOrEditPanel.this, SiteViewOrEditPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(siteDataviewContainer.setOutputMarkupId(true));
      add(sitePagingNavigator);
      super.onInitialize();
    }
  }

  private static final long ITEMS_PER_PAGE = 5;

  private static final long serialVersionUID = -2575007609797589274L;

  private WebMarkupContainer siteDataviewContainer = new WebMarkupContainer("siteDataviewContainer") {

    private static final long serialVersionUID = 1L;

    @Override
    protected void onInitialize() {
      add(siteDataview);
      super.onInitialize();
    }
  };

  private SiteDataview siteDataview = new SiteDataview();

  private BootstrapPagingNavigator sitePagingNavigator = new BootstrapPagingNavigator("sitePagingNavigator", siteDataview);

  public SiteViewOrEditPanel(final String id, final IModel<User> model) {
    super(id, model);
  }

}
