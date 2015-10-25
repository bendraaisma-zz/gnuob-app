package com.netbrasoft.gnuob.application.order;

import static de.agilecoders.wicket.jquery.JQuery.$;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.OrderRecord;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.core.util.Attributes;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class OrderRecordPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class AddAjaxLink extends BootstrapAjaxLink<String> {

    private static final long serialVersionUID = 9191172039973638020L;

    public AddAjaxLink() {
      super("add", Model.of(OrderRecordPanel.this.getString("addMessage")), Buttons.Type.Primary, Model.of(OrderRecordPanel.this.getString("addMessage")));
      setIconType(GlyphIconType.plus);
      setSize(Buttons.Size.Small);
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      orderRecordViewOrEditPanel.setDefaultModelObject(new OrderRecord());
      target.add(orderRecordViewOrEditPanel.setOutputMarkupId(true));
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class OrderRecordDataview extends DataView<OrderRecord> {

    private static final long serialVersionUID = 8996562822101409998L;

    private boolean removeAjaxLinkVisable;

    private long selectedObjectId;

    protected OrderRecordDataview() {
      super("orderRecordDataview", orderRecordListDataProvider, ITEMS_PER_PAGE);
    }

    public boolean isRemoveAjaxLinkVisable() {
      return removeAjaxLinkVisable;
    }

    @Override
    protected Item<OrderRecord> newItem(String id, int index, IModel<OrderRecord> model) {
      final Item<OrderRecord> item = super.newItem(id, index, model);
      final long modelObjectId = ((OrderRecord) orderRecordViewOrEditPanel.getDefaultModelObject()).getId();

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
      if (selectedObjectId != ((Order) OrderRecordPanel.this.getDefaultModelObject()).getId()) {
        selectedObjectId = ((Order) OrderRecordPanel.this.getDefaultModelObject()).getId();
        orderRecordViewOrEditPanel.setDefaultModelObject(new OrderRecord());
      }
      super.onConfigure();
    }

    @Override
    protected void populateItem(Item<OrderRecord> item) {
      final IModel<OrderRecord> compound = new CompoundPropertyModel<OrderRecord>(item.getModelObject());
      item.setModel(compound);
      item.add(new Label("name"));
      item.add(new Label("description"));
      item.add(new AjaxEventBehavior("click") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onEvent(AjaxRequestTarget target) {
          orderRecordViewOrEditPanel.setDefaultModelObject(item.getModelObject());
          target.add(orderRecordDataviewContainer.setOutputMarkupId(true));
          target.add(orderRecordViewOrEditPanel.setOutputMarkupId(true));
        }
      });
      item.add(new RemoveAjaxLink(item.getModel()).add(new ConfirmationBehavior() {

        private static final long serialVersionUID = 7744720444161839031L;

        @Override
        public void renderHead(Component component, IHeaderResponse response) {
          response.render($(component).chain("confirmation", new ConfirmationConfig().withTitle(getString("confirmationTitleMessage")).withSingleton(true).withPopout(true)
              .withBtnOkLabel(getString("confirmMessage")).withBtnCancelLabel(getString("cancelMessage"))).asDomReadyScript());
        }
      }).setVisible(isRemoveAjaxLinkVisable()));

      if (item.getIndex() == 0 && ((OrderRecord) orderRecordViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
        orderRecordViewOrEditPanel.setDefaultModelObject(item.getModelObject());
      }
    }

    public void setRemoveAjaxLinkVisable(boolean removeAjaxLinkVisable) {
      this.removeAjaxLinkVisable = removeAjaxLinkVisable;
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class OrderRecordEditFragment extends Fragment {

    private static final long serialVersionUID = 3709791409078428685L;

    public OrderRecordEditFragment() {
      super("orderRecordViewOrEditFragement", "orderRecordEditFragement", OrderRecordPanel.this, OrderRecordPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(orderRecordEditTable.setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  class OrderRecordListDataProvider extends ListDataProvider<OrderRecord> {

    private static final long serialVersionUID = 5259243752700177690L;

    @Override
    protected List<OrderRecord> getData() {
      return ((Order) OrderRecordPanel.this.getDefaultModelObject()).getRecords();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class OrderRecordViewFragement extends Fragment {

    private static final long serialVersionUID = 3709791409078428685L;

    public OrderRecordViewFragement() {
      super("orderRecordViewOrEditFragement", "orderRecordViewFragement", OrderRecordPanel.this, OrderRecordPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(orderRecordViewTable.setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class RemoveAjaxLink extends BootstrapAjaxLink<OrderRecord> {

    private static final long serialVersionUID = -6950515027229520882L;

    public RemoveAjaxLink(final IModel<OrderRecord> model) {
      super("remove", model, Buttons.Type.Default, Model.of(OrderRecordPanel.this.getString("removeMessage")));
      setIconType(GlyphIconType.remove);
      setSize(Buttons.Size.Mini);
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      try {
        orderRecordListDataProvider.getData().remove(getDefaultModelObject());
      } catch (final RuntimeException e) {
        LOGGER.warn(e.getMessage(), e);
        orderRecordEditTable.warn(e.getLocalizedMessage());
      } finally {
        target.add(orderRecordDataviewContainer.setOutputMarkupId(true));
      }
    }
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderRecordPanel.class);

  private static final long serialVersionUID = -4119480413347414297L;

  private static final long ITEMS_PER_PAGE = 10;

  private final WebMarkupContainer orderRecordDataviewContainer;

  private final OrderRecordDataview orderRecordDataview;

  private final BootstrapPagingNavigator orderRecordPagingNavigator;

  private final OrderRecordListDataProvider orderRecordListDataProvider;

  private final OrderRecordViewOrEditPanel orderRecordViewOrEditPanel;

  private final WebMarkupContainer orderRecordEditTable;

  private final WebMarkupContainer orderRecordViewTable;

  public OrderRecordPanel(final String id, final IModel<Order> model) {
    super(id, model);
    orderRecordListDataProvider = new OrderRecordListDataProvider();
    orderRecordDataview = new OrderRecordDataview();
    orderRecordPagingNavigator = new BootstrapPagingNavigator("orderRecordPagingNavigator", orderRecordDataview);
    orderRecordDataviewContainer = new WebMarkupContainer("orderRecordDataviewContainer", getDefaultModel()) {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onInitialize() {
        add(orderRecordDataview);
        super.onInitialize();
      }
    };
    orderRecordEditTable = new WebMarkupContainer("orderRecordEditTable", getDefaultModel()) {

      private static final long serialVersionUID = 459122691621477233L;

      @Override
      protected void onInitialize() {
        orderRecordDataview.setRemoveAjaxLinkVisable(true);
        add(new AddAjaxLink().setOutputMarkupId(true));
        add(orderRecordPagingNavigator.setOutputMarkupId(true));
        add(orderRecordDataviewContainer.setOutputMarkupId(true));
        add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
        add(orderRecordViewOrEditPanel.add(orderRecordViewOrEditPanel.new OrderRecordEditFragment()).setOutputMarkupId(true));
        add(new TableBehavior());
        super.onInitialize();
      }
    };
    orderRecordViewTable = new WebMarkupContainer("orderRecordViewTable", getDefaultModel()) {

      private static final long serialVersionUID = 8276100341510505878L;

      @Override
      protected void onInitialize() {
        orderRecordDataview.setRemoveAjaxLinkVisable(false);
        add(orderRecordPagingNavigator.setOutputMarkupId(true));
        add(orderRecordDataviewContainer.setOutputMarkupId(true));
        add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
        add(orderRecordViewOrEditPanel.add(orderRecordViewOrEditPanel.new OrderRecordViewFragment()).setOutputMarkupId(true));
        add(new TableBehavior());
        super.onInitialize();
      }
    };
    orderRecordViewOrEditPanel = new OrderRecordViewOrEditPanel("orderRecordViewOrEditPanel", Model.of(new OrderRecord()), orderRecordEditTable);
  }
}
