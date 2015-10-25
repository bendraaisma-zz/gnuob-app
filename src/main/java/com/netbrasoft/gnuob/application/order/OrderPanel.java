package com.netbrasoft.gnuob.application.order;

import static de.agilecoders.wicket.jquery.JQuery.$;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.WellBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.WellBehavior.Size;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.layout.col.MediumSpanType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.core.util.Attributes;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

/**
 * Panel for viewing, selecting and editing {@link Order} entities.
 *
 * @author Bernard Arjan Draaisma
 *
 */
@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class OrderPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class AddAjaxLink extends BootstrapAjaxLink<String> {

    private static final long serialVersionUID = 9191172039973638020L;

    public AddAjaxLink() {
      super("add", Model.of(OrderPanel.this.getString("addMessage")), Buttons.Type.Primary, Model.of(OrderPanel.this.getString("addMessage")));
      setIconType(GlyphIconType.plus);
      setSize(Buttons.Size.Small);
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      orderViewOrEditPanel.setDefaultModelObject(new Order());
      target.add(orderViewOrEditPanel.setOutputMarkupId(true));
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class OrderDataview extends DataView<Order> {

    private static final long serialVersionUID = -5039874949058607907L;

    private long selectedObjectId;

    protected OrderDataview() {
      super("orderDataview", orderDataProvider, ITEMS_PER_PAGE);
    }

    @Override
    protected Item<Order> newItem(String id, int index, IModel<Order> model) {
      final Item<Order> item = super.newItem(id, index, model);
      final long modelObjectId = ((Order) orderViewOrEditPanel.getDefaultModelObject()).getId();

      if ((model.getObject().getId() == modelObjectId) || modelObjectId == 0) {
        item.add(new BootstrapBaseBehavior() {

          private static final long serialVersionUID = -4903722864597601489L;

          @Override
          public void onComponentTag(Component component, ComponentTag tag) {
            Attributes.addClass(tag, "info");
          }
        });
      }

      return item;
    }

    @Override
    protected void onConfigure() {
      if (selectedObjectId != ((Order) OrderPanel.this.getDefaultModelObject()).getId()) {
        selectedObjectId = ((Order) OrderPanel.this.getDefaultModelObject()).getId();
      }

      super.onConfigure();
    }

    @Override
    protected void populateItem(Item<Order> item) {
      item.setModel(new CompoundPropertyModel<Order>(item.getModelObject()));
      item.add(new Label("orderId"));
      item.add(new Label("contract.contractId"));
      item.add(new Label("contract.customer.firstName"));
      item.add(new Label("contract.customer.lastName"));
      item.add(new AjaxEventBehavior("click") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onEvent(AjaxRequestTarget target) {
          orderViewOrEditPanel.setDefaultModelObject(item.getModelObject());
          target.add(orderDataviewContainer.setOutputMarkupId(true));
          target.add(orderViewOrEditPanel.setOutputMarkupId(true));
        }
      });
      item.add(new RemoveAjaxLink(item.getModel()).add(new ConfirmationBehavior() {

        private static final long serialVersionUID = 7744720444161839031L;

        @Override
        public void renderHead(Component component, IHeaderResponse response) {
          response.render($(component).chain("confirmation", new ConfirmationConfig().withTitle(getString("confirmationTitleMessage")).withSingleton(true).withPopout(true)
              .withBtnOkLabel(getString("confirmMessage")).withBtnCancelLabel(getString("cancelMessage"))).asDomReadyScript());
        }
      }));

      if (item.getIndex() == 0 && ((Order) orderViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
        orderViewOrEditPanel.setDefaultModelObject(item.getModelObject());
      }
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class RemoveAjaxLink extends BootstrapAjaxLink<Order> {

    private static final long serialVersionUID = -8317730269644885290L;

    public RemoveAjaxLink(final IModel<Order> model) {
      super("remove", model, Buttons.Type.Default, Model.of(OrderPanel.this.getString("removeMessage")));
      setIconType(GlyphIconType.remove);
      setSize(Buttons.Size.Mini);
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      try {
        getModelObject().setActive(false);
        orderDataProvider.merge(getModelObject());
        orderViewOrEditPanel.setDefaultModelObject(new Order());
      } catch (final RuntimeException e) {
        LOGGER.warn(e.getMessage(), e);
        orderTableContainer.warn(e.getLocalizedMessage());
      } finally {
        target.add(orderTableContainer);
      }
    }
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderPanel.class);

  private static final long serialVersionUID = 3703226064705246155L;

  private static final int ITEMS_PER_PAGE = 10;

  @SpringBean(name = "OrderDataProvider", required = true)
  private GenericTypeDataProvider<Order> orderDataProvider;

  private final OrderByBorder<String> orderByFirstName;

  private final OrderByBorder<String> orderByLastName;

  private final OrderByBorder<String> orderByOrderId;

  private final OrderByBorder<String> orderByContractId;

  private final OrderDataview orderDataview;

  private final WebMarkupContainer orderPanelContainer;

  private final WebMarkupContainer orderTableContainer;

  private final WebMarkupContainer orderDataviewContainer;

  private final BootstrapPagingNavigator orderPagingNavigator;

  private final OrderViewOrEditPanel orderViewOrEditPanel;

  public OrderPanel(final String id, final IModel<Order> model) {
    super(id, model);

    orderByFirstName = new OrderByBorder<String>("orderByFirstName", "contract.customer.firstName", orderDataProvider);
    orderByLastName = new OrderByBorder<String>("orderByLastName", "contract.customer.lastName", orderDataProvider);
    orderByOrderId = new OrderByBorder<String>("orderByOrderId", "orderId", orderDataProvider);
    orderByContractId = new OrderByBorder<String>("orderByContractId", "contract.contractId", orderDataProvider);
    orderDataview = new OrderDataview();
    orderPagingNavigator = new BootstrapPagingNavigator("orderPagingNavigator", orderDataview);
    orderDataviewContainer = new WebMarkupContainer("orderDataviewContainer", getDefaultModel()) {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
        add(orderDataview.setOutputMarkupId(true));
        super.onInitialize();
      }
    };
    orderTableContainer = new WebMarkupContainer("orderTableContainer", getDefaultModel()) {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
        add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
        add(new AddAjaxLink().setOutputMarkupId(true));
        add(orderByFirstName.setOutputMarkupId(true));
        add(orderByLastName.setOutputMarkupId(true));
        add(orderByOrderId.setOutputMarkupId(true));
        add(orderByContractId.setOutputMarkupId(true));
        add(orderDataviewContainer.setOutputMarkupId(true));
        add(orderPagingNavigator.setOutputMarkupId(true));
        add(new TableBehavior().hover());
        super.onInitialize();
      }
    };
    orderPanelContainer = new WebMarkupContainer("orderPanelContainer", getDefaultModel()) {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
        add(orderTableContainer.setOutputMarkupId(true));
        add(orderViewOrEditPanel.add(orderViewOrEditPanel.new OrderViewFragement()).setOutputMarkupId(true));
        add(new BootstrapBaseBehavior() {

          private static final long serialVersionUID = -4903722864597601489L;

          @Override
          public void onComponentTag(Component component, ComponentTag tag) {
            Attributes.addClass(tag, MediumSpanType.SPAN10);
          }
        });
        super.onInitialize();
      }
    };
    orderViewOrEditPanel = new OrderViewOrEditPanel("orderViewOrEditPanel", (IModel<Order>) getDefaultModel()) {

      private static final long serialVersionUID = -8723947139234708667L;

      @Override
      protected void onInitialize() {
        add(new WellBehavior(Size.Small));
        super.onInitialize();
      }
    };
  }

  @Override
  protected void onInitialize() {
    orderDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    orderDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    orderDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    orderDataProvider.setType(new Order());
    orderDataProvider.getType().setActive(true);
    add(orderPanelContainer.setOutputMarkupId(true));
    super.onInitialize();
  }
}
