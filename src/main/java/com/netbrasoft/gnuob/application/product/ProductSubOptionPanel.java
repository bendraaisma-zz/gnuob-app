package com.netbrasoft.gnuob.application.product;

import static de.agilecoders.wicket.jquery.JQuery.$;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.api.Option;
import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.SubOption;
import com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class ProductSubOptionPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class ProductSubOptionEditFragement extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class SubOptionEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class AddAjaxLink extends BootstrapAjaxLink<Product> {

        private static final long serialVersionUID = 9191172039973638020L;

        public AddAjaxLink(String id, IModel<Product> model, Buttons.Type type, IModel<String> labelModel) {
          super(id, model, type, labelModel);
          setIconType(GlyphIconType.plus);
          setSize(Buttons.Size.Small);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
          ProductSubOptionPanel.this.selectedModel.getObject().getSubOptions().add(new SubOption());
          subOptionDataviewContainer.subOptionDataview.index = ProductSubOptionPanel.this.selectedModel.getObject().getSubOptions().size() - 1;
          productSubOptionViewOrEditPanel.removeAll();
          target.add(subOptionDataviewContainer.setOutputMarkupId(true));
          target.add(productSubOptionViewOrEditPanel.add(productSubOptionViewOrEditPanel.new ProductSubOptionEditFragement()).setOutputMarkupId(true));
        }
      }

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class SubOptionDataviewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
        class SubOptionDataview extends DataView<SubOption> {

          @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
          class RemoveAjaxLink extends BootstrapAjaxLink<SubOption> {

            private static final long serialVersionUID = -6950515027229520882L;

            public RemoveAjaxLink(String id, IModel<SubOption> model, Buttons.Type type, IModel<String> labelModel) {
              super(id, model, type, labelModel);
              setIconType(GlyphIconType.remove);
              setSize(Buttons.Size.Mini);
            }

            @Override
            public void onClick(AjaxRequestTarget target) {
              ProductSubOptionPanel.this.selectedModel.getObject().getSubOptions().remove(RemoveAjaxLink.this.getDefaultModelObject());
              subOptionDataview.index = ProductSubOptionPanel.this.selectedModel.getObject().getSubOptions().size() - 1;
              productSubOptionViewOrEditPanel.removeAll();
              target.add(subOptionDataviewContainer.setOutputMarkupId(true));
              target.add(productSubOptionViewOrEditPanel.add(productSubOptionViewOrEditPanel.new ProductSubOptionEditFragement()).setOutputMarkupId(true));
            }
          }

          private static final long serialVersionUID = 8343306073164168425L;

          private int index = 0;

          protected SubOptionDataview(final String id, final IDataProvider<SubOption> dataProvider, final long itemsPerPage) {
            super(id, dataProvider, itemsPerPage);
          }

          @Override
          protected Item<SubOption> newItem(String id, int index, IModel<SubOption> model) {
            final Item<SubOption> item = super.newItem(id, index, model);
            if (this.index == index) {
              item.add(new AttributeModifier("class", "info"));
            }
            return item;
          }

          @Override
          protected void onConfigure() {
            if (!ProductSubOptionPanel.this.selectedModel.getObject().getSubOptions().isEmpty()) {
              productSubOptionViewOrEditPanel.setEnabled(true);
              productSubOptionViewOrEditPanel.removeAll();
              productSubOptionViewOrEditPanel.setSelectedModel(Model.of(ProductSubOptionPanel.this.selectedModel.getObject().getSubOptions().get(index)),
                  Model.of(ProductSubOptionPanel.this.selectedModel.getObject()));
              productSubOptionViewOrEditPanel.add(productSubOptionViewOrEditPanel.new ProductSubOptionEditFragement()).setOutputMarkupId(true);
            } else {
              productSubOptionViewOrEditPanel.setEnabled(false);
              productSubOptionViewOrEditPanel.removeAll();
              productSubOptionViewOrEditPanel.setSelectedModel(Model.of(new SubOption()), Model.of(ProductSubOptionPanel.this.selectedModel.getObject()));
              productSubOptionViewOrEditPanel.add(productSubOptionViewOrEditPanel.new ProductSubOptionEditFragement()).setOutputMarkupId(true);
            }
            super.onConfigure();
          }

          @Override
          protected void populateItem(Item<SubOption> item) {
            item.setModel(new CompoundPropertyModel<SubOption>(item.getModelObject()));
            item.add(new Label("description").setOutputMarkupId(true));
            item.add(new Label("value").setOutputMarkupId(true));
            item.add(new Label("disabled").setOutputMarkupId(true));
            item.add(new AjaxEventBehavior("click") {

              private static final long serialVersionUID = 1L;

              @Override
              public void onEvent(AjaxRequestTarget target) {
                index = item.getIndex();
                productSubOptionViewOrEditPanel.setSelectedModel(item.getModel(), Model.of(ProductSubOptionPanel.this.selectedModel.getObject()));
                productSubOptionViewOrEditPanel.removeAll();
                target.add(subOptionDataviewContainer.setOutputMarkupId(true));
                target.add(productSubOptionViewOrEditPanel.add(productSubOptionViewOrEditPanel.new ProductSubOptionEditFragement()).setOutputMarkupId(true));
              }
            });
            item.add(new RemoveAjaxLink("remove", item.getModel(), Buttons.Type.Default,
                Model.of(ProductSubOptionPanel.this.getString(NetbrasoftApplicationConstants.REMOVE_MESSAGE_KEY))).add(new ConfirmationBehavior() {

                  private static final long serialVersionUID = 7744720444161839031L;

                  @Override
                  public void renderHead(Component component, IHeaderResponse response) {
                    response.render($(component).chain("confirmation",
                        new ConfirmationConfig().withTitle(getString(NetbrasoftApplicationConstants.CONFIRMATION_TITLE_MESSAGE_KEY)).withSingleton(true).withPopout(true)
                            .withBtnOkLabel(getString(NetbrasoftApplicationConstants.CONFIRM_MESSAGE_KEY))
                            .withBtnCancelLabel(getString(NetbrasoftApplicationConstants.CANCEL_MESSAGE_KEY)))
                        .asDomReadyScript());
                  }
                }));
          }
        }

        private static final long serialVersionUID = -3393999999422653255L;

        private final SubOptionDataview subOptionDataview;

        private final ListDataProvider<SubOption> subOptionListDataProvider;

        public SubOptionDataviewContainer(final String id, final IModel<Product> model) {
          super(id, model);
          subOptionListDataProvider = new ListDataProvider<SubOption>() {

            private static final long serialVersionUID = -3261859241046697057L;

            @Override
            protected List<SubOption> getData() {
              return ProductSubOptionPanel.this.selectedModel.getObject().getSubOptions();
            }
          };
          subOptionDataview = new SubOptionDataview("subOptionDataview", subOptionListDataProvider, ITEMS_PER_PAGE);
        }

        @Override
        protected void onInitialize() {
          add(subOptionDataview.setOutputMarkupId(true));
          super.onInitialize();
        }
      }

      private static final long serialVersionUID = -5522048858537112825L;

      private final AddAjaxLink addAjaxLink;

      private final SubOptionDataviewContainer subOptionDataviewContainer;

      private final BootstrapPagingNavigator subOptionPagingNavigator;

      private final ProductSubOptionViewOrEditPanel productSubOptionViewOrEditPanel;

      public SubOptionEditTable(final String id, final IModel<Product> model) {
        super(id, model);
        addAjaxLink = new AddAjaxLink("add", (IModel<Product>) SubOptionEditTable.this.getDefaultModel(), Buttons.Type.Primary,
            Model.of(ProductSubOptionPanel.this.getString(NetbrasoftApplicationConstants.ADD_MESSAGE_KEY)));
        subOptionDataviewContainer = new SubOptionDataviewContainer("subOptionDataviewContainer", (IModel<Product>) SubOptionEditTable.this.getDefaultModel());
        subOptionPagingNavigator = new BootstrapPagingNavigator("subOptionPagingNavigator", subOptionDataviewContainer.subOptionDataview);
        productSubOptionViewOrEditPanel = new ProductSubOptionViewOrEditPanel("productSubOptionViewOrEditPanel", (IModel<Product>) SubOptionEditTable.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
        add(addAjaxLink.setOutputMarkupId(true));
        add(subOptionDataviewContainer.setOutputMarkupId(true));
        add(subOptionPagingNavigator.setOutputMarkupId(true));
        add(productSubOptionViewOrEditPanel.add(productSubOptionViewOrEditPanel.new ProductSubOptionEditFragement()).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = -3434639671937275356L;

    private final SubOptionEditTable subOptionEditTable;

    public ProductSubOptionEditFragement() {
      super("productSubOptionViewOrEditFragement", "productSubOptionEditFragement", ProductSubOptionPanel.this, ProductSubOptionPanel.this.getDefaultModel());
      subOptionEditTable = new SubOptionEditTable("subOptionEditTable", (IModel<Product>) ProductSubOptionEditFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(subOptionEditTable.add(new TableBehavior().hover()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class ProductSubOptionViewFragement extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class SubOptionViewTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
      class SubOptionDataviewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
        class SubOptionDataview extends DataView<SubOption> {

          private static final long serialVersionUID = 8343306073164168425L;

          private int index = 0;

          protected SubOptionDataview(final String id, final IDataProvider<SubOption> dataProvider, final long itemsPerPage) {
            super(id, dataProvider, itemsPerPage);
          }

          @Override
          protected Item<SubOption> newItem(String id, int index, IModel<SubOption> model) {
            final Item<SubOption> item = super.newItem(id, index, model);
            if (this.index == index) {
              item.add(new AttributeModifier("class", "info"));
            }
            return item;
          }

          @Override
          protected void onConfigure() {
            if (!ProductSubOptionPanel.this.selectedModel.getObject().getSubOptions().isEmpty()) {
              productSubOptionViewOrEditPanel.removeAll();
              productSubOptionViewOrEditPanel.setSelectedModel(Model.of(ProductSubOptionPanel.this.selectedModel.getObject().getSubOptions().get(index)),
                  Model.of(ProductSubOptionPanel.this.selectedModel.getObject()));
              productSubOptionViewOrEditPanel.add(productSubOptionViewOrEditPanel.new ProductSubOptionViewFragement()).setOutputMarkupId(true);
            } else {
              productSubOptionViewOrEditPanel.removeAll();
              productSubOptionViewOrEditPanel.setSelectedModel(Model.of(new SubOption()), Model.of(ProductSubOptionPanel.this.selectedModel.getObject()));
              productSubOptionViewOrEditPanel.add(productSubOptionViewOrEditPanel.new ProductSubOptionViewFragement()).setOutputMarkupId(true);
            }
            super.onConfigure();
          }

          @Override
          protected void populateItem(Item<SubOption> item) {
            item.setModel(new CompoundPropertyModel<SubOption>(item.getModelObject()));
            item.add(new Label("description").setOutputMarkupId(true));
            item.add(new Label("value").setOutputMarkupId(true));
            item.add(new Label("disabled").setOutputMarkupId(true));
            item.add(new AjaxEventBehavior("click") {

              private static final long serialVersionUID = 1L;

              @Override
              public void onEvent(AjaxRequestTarget target) {
                index = item.getIndex();
                productSubOptionViewOrEditPanel.setSelectedModel(item.getModel(), Model.of(ProductSubOptionPanel.this.selectedModel.getObject()));
                productSubOptionViewOrEditPanel.removeAll();
                target.add(subOptionDataviewContainer.setOutputMarkupId(true));
                target.add(productSubOptionViewOrEditPanel.add(productSubOptionViewOrEditPanel.new ProductSubOptionViewFragement()).setOutputMarkupId(true));
              }
            });
          }
        }

        private static final long serialVersionUID = -3393999999422653255L;

        private final SubOptionDataview subOptionDataview;

        private final ListDataProvider<SubOption> subOptionListDataProvider;

        public SubOptionDataviewContainer(final String id, final IModel<Product> model) {
          super(id, model);
          subOptionListDataProvider = new ListDataProvider<SubOption>() {

            private static final long serialVersionUID = -3261859241046697057L;

            @Override
            protected List<SubOption> getData() {
              return ProductSubOptionPanel.this.selectedModel.getObject().getSubOptions();
            }
          };
          subOptionDataview = new SubOptionDataview("subOptionDataview", subOptionListDataProvider, ITEMS_PER_PAGE);
        }

        @Override
        protected void onInitialize() {
          add(subOptionDataview.setOutputMarkupId(true));
          super.onInitialize();
        }
      }

      private static final long serialVersionUID = -5522048858537112825L;

      private final NotificationPanel feedbackPanel;

      private final SubOptionDataviewContainer subOptionDataviewContainer;

      private final BootstrapPagingNavigator subOptionPagingNavigator;

      private final ProductSubOptionViewOrEditPanel productSubOptionViewOrEditPanel;

      public SubOptionViewTable(final String id, final IModel<Product> model) {
        super(id, model);
        feedbackPanel = new NotificationPanel("feedback");
        subOptionDataviewContainer = new SubOptionDataviewContainer("subOptionDataviewContainer", (IModel<Product>) SubOptionViewTable.this.getDefaultModel());
        subOptionPagingNavigator = new BootstrapPagingNavigator("subOptionPagingNavigator", subOptionDataviewContainer.subOptionDataview);
        productSubOptionViewOrEditPanel = new ProductSubOptionViewOrEditPanel("productSubOptionViewOrEditPanel", (IModel<Product>) SubOptionViewTable.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
        add(feedbackPanel.setOutputMarkupId(true));
        add(subOptionDataviewContainer.setOutputMarkupId(true));
        add(subOptionPagingNavigator.setOutputMarkupId(true));
        add(productSubOptionViewOrEditPanel.add(productSubOptionViewOrEditPanel.new ProductSubOptionViewFragement()).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = -3434639671937275356L;

    private final SubOptionViewTable subOptionViewTable;

    public ProductSubOptionViewFragement() {
      super("productSubOptionViewOrEditFragement", "productSubOptionViewFragement", ProductSubOptionPanel.this, ProductSubOptionPanel.this.getDefaultModel());
      subOptionViewTable = new SubOptionViewTable("subOptionViewTable", (IModel<Product>) ProductSubOptionViewFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(subOptionViewTable.add(new TableBehavior().hover()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final int ITEMS_PER_PAGE = 10;

  private static final long serialVersionUID = 2594347542853743933L;

  private IModel<Option> selectedModel;

  public ProductSubOptionPanel(final String id, final IModel<Product> model) {
    super(id, model);
    selectedModel = Model.of(new Option());
  }

  public void setSelectedModel(IModel<Option> selectedModel) {
    this.selectedModel = selectedModel;
  }
}
