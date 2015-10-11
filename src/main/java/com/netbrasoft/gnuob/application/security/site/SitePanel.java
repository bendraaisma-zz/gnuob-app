package com.netbrasoft.gnuob.application.security.site;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.netbrasoft.gnuob.api.Site;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.ADMINISTRATOR, AppRoles.MANAGER})
public class SitePanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.ADMINISTRATOR})
  class AddAjaxLink extends AjaxLink<Void> {

    private static final long serialVersionUID = -8317730269644885290L;

    public AddAjaxLink() {
      super("add");
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      // TODO Auto-generated method stub
    }
  }

  class SiteDataview extends DataView<Site> {

    private static final long serialVersionUID = -5039874949058607907L;

    protected SiteDataview() {
      super("siteDataview", siteDataProvider, ITEMS_PER_PAGE);
    }

    @Override
    protected Item<Site> newItem(String id, int index, IModel<Site> model) {
      Item<Site> item = super.newItem(id, index, model);

      if (model.getObject().getId() == ((Site) siteViewOrEditPanel.getDefaultModelObject()).getId()) {
        item.add(new AttributeModifier("class", "info"));
      }

      return item;
    }

    @Override
    protected void populateItem(Item<Site> paramItem) {
      paramItem.setModel(new CompoundPropertyModel<Site>(paramItem.getModelObject()));
      paramItem.add(new Label("name"));
      paramItem.add(new Label("description"));
      paramItem.add(new AjaxEventBehavior("click") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onEvent(AjaxRequestTarget target) {
          siteViewOrEditPanel.setDefaultModelObject(paramItem.getModelObject());
          target.add(getPage());
        }
      });
    }
  }

  private static final long serialVersionUID = 3703226064705246155L;

  private static final int ITEMS_PER_PAGE = 10;

  private SiteDataview siteDataview = new SiteDataview();

  @SpringBean(name = "SiteDataProvider", required = true)
  private GenericTypeDataProvider<Site> siteDataProvider;

  private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", siteDataProvider);

  private OrderByBorder<String> orderByDescription = new OrderByBorder<String>("orderByDescription", "description", siteDataProvider);

  private WebMarkupContainer siteDataviewContainer = new WebMarkupContainer("siteDataviewContainer") {

    private static final long serialVersionUID = -497527332092449028L;

    @Override
    protected void onInitialize() {
      add(siteDataview);
      super.onInitialize();
    }
  };

  private BootstrapPagingNavigator sitePagingNavigator = new BootstrapPagingNavigator("sitePagingNavigator", siteDataview);

  private SiteViewOrEditPanel siteViewOrEditPanel = new SiteViewOrEditPanel("siteViewOrEditPanel", (IModel<Site>) getDefaultModel());

  public SitePanel(final String id, final IModel<Site> model) {
    super(id, model);
  }

  @Override
  protected void onInitialize() {
    siteDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    siteDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    siteDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    siteDataProvider.setType(new Site());
    siteDataProvider.getType().setActive(true);

    add(new AddAjaxLink());
    add(orderByName);
    add(orderByDescription);
    add(siteDataviewContainer.setOutputMarkupId(true));
    add(sitePagingNavigator);
    add(siteViewOrEditPanel.add(siteViewOrEditPanel.new SiteViewFragement()).setOutputMarkupId(true));

    super.onInitialize();
  }
}
