package com.netbrasoft.gnuob.application.content;

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

import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.Product;
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

@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class ContentPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class AddAjaxLink extends BootstrapAjaxLink<String> {

    private static final long serialVersionUID = -8317730269644885290L;

    public AddAjaxLink() {
      super("add", Model.of(ContentPanel.this.getString("addMessage")), Buttons.Type.Primary, Model.of(ContentPanel.this.getString("addMessage")));
      setIconType(GlyphIconType.plus);
      setSize(Buttons.Size.Small);
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      contentViewOrEditPanel.setDefaultModelObject(new Product());
      target.add(contentViewOrEditPanel.setOutputMarkupId(true));
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class ContentDataview extends DataView<Content> {

    private static final long serialVersionUID = -5039874949058607907L;

    private long selectedObjectId;

    protected ContentDataview() {
      super("contentDataview", contentDataProvider, ITEMS_PER_PAGE);
    }

    @Override
    protected Item<Content> newItem(String id, int index, IModel<Content> model) {
      final Item<Content> item = super.newItem(id, index, model);
      final long modelObjectId = ((Content) contentViewOrEditPanel.getDefaultModelObject()).getId();

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
      if (selectedObjectId != ((Content) ContentPanel.this.getDefaultModelObject()).getId()) {
        selectedObjectId = ((Content) ContentPanel.this.getDefaultModelObject()).getId();
      }
      super.onConfigure();
    }

    @Override
    protected void populateItem(Item<Content> item) {
      item.setModel(new CompoundPropertyModel<Content>(item.getModelObject()));
      item.add(new Label("name"));
      item.add(new Label("format"));
      item.add(new AjaxEventBehavior("click") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onEvent(AjaxRequestTarget target) {
          contentViewOrEditPanel.setDefaultModelObject(item.getModelObject());
          target.add(contentDataviewContainer.setOutputMarkupId(true));
          target.add(contentViewOrEditPanel.setOutputMarkupId(true));
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

      if (item.getIndex() == 0 && ((Content) contentViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
        contentViewOrEditPanel.setDefaultModelObject(item.getModelObject());
      }
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class RemoveAjaxLink extends BootstrapAjaxLink<Content> {

    private static final long serialVersionUID = -8317730269644885290L;

    public RemoveAjaxLink(final IModel<Content> model) {
      super("remove", model, Buttons.Type.Default, Model.of(ContentPanel.this.getString("removeMessage")));
      setIconType(GlyphIconType.remove);
      setSize(Buttons.Size.Mini);
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      try {
        getModelObject().setActive(false);
        contentDataProvider.merge(getModelObject());
        contentViewOrEditPanel.setDefaultModelObject(new Product());
      } catch (final RuntimeException e) {
        LOGGER.warn(e.getMessage(), e);
        contentTableContainer.warn(e.getLocalizedMessage());
      } finally {
        target.add(getPage());
      }
    }
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(ContentPanel.class);

  private static final long serialVersionUID = 3703226064705246155L;

  private static final int ITEMS_PER_PAGE = 10;

  @SpringBean(name = "ContentDataProvider", required = true)
  private GenericTypeDataProvider<Content> contentDataProvider;

  private final OrderByBorder<String> orderByFormat;

  private final OrderByBorder<String> orderByName;

  private final DataView<Content> contentDataview;

  private final WebMarkupContainer contentDataviewContainer;

  private final BootstrapPagingNavigator contentPagingNavigator;

  private final ContentViewOrEditPanel contentViewOrEditPanel;

  private final WebMarkupContainer contentPanelContainer;

  private final WebMarkupContainer contentTableContainer;

  public ContentPanel(final String id, final IModel<Content> model) {
    super(id, model);

    orderByFormat = new OrderByBorder<String>("orderByFormat", "format", contentDataProvider);
    orderByName = new OrderByBorder<String>("orderByName", "name", contentDataProvider);
    contentDataview = new ContentDataview();
    contentPagingNavigator = new BootstrapPagingNavigator("contentPagingNavigator", contentDataview);
    contentDataviewContainer = new WebMarkupContainer("contentDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
        add(contentDataview);
        super.onInitialize();
      }
    };
    contentTableContainer = new WebMarkupContainer("contentTableContainer", getDefaultModel()) {

      private static final long serialVersionUID = -4706369076595798457L;

      @Override
      protected void onInitialize() {
        add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
        add(new AddAjaxLink().setOutputMarkupId(true));
        add(orderByFormat.setOutputMarkupId(true));
        add(orderByName.setOutputMarkupId(true));
        add(contentDataviewContainer.setOutputMarkupId(true));
        add(contentPagingNavigator.setOutputMarkupId(true));
        add(new TableBehavior().hover());
        super.onInitialize();
      }
    };
    contentPanelContainer = new WebMarkupContainer("contentPanelContainer", getDefaultModel()) {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
        add(contentTableContainer.setOutputMarkupId(true));
        add(contentViewOrEditPanel.add(contentViewOrEditPanel.new ContentViewFragement()).setOutputMarkupId(true));
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
    contentViewOrEditPanel = new ContentViewOrEditPanel("contentViewOrEditPanel", Model.of(new Content())) {

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
    contentDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    contentDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    contentDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    contentDataProvider.setType((Content) getDefaultModelObject());
    contentDataProvider.getType().setActive(true);
    add(contentPanelContainer.setOutputMarkupId(true));
    super.onInitialize();
  }
}
