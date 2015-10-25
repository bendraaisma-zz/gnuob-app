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
public class ProductOptionPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class ProductOptionEditFragement extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class OptionEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class AddAjaxLink extends BootstrapAjaxLink<Product> {

        private static final long serialVersionUID = 9191172039973638020L;

        public AddAjaxLink(String id, IModel<Product> model, Buttons.Type type,
            IModel<String> labelModel) {
          super(id, model, type, labelModel);
          setIconType(GlyphIconType.plus);
          setSize(Buttons.Size.Small);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
          ((Product) AddAjaxLink.this.getDefaultModelObject()).getOptions().add(new Option());
          optionDataviewContainer.optionDataview.index =
              ((Product) AddAjaxLink.this.getDefaultModelObject()).getOptions().size() - 1;
          productOptionViewOrEditPanel.removeAll();
          target.add(optionDataviewContainer.setOutputMarkupId(true));
          target.add(productOptionViewOrEditPanel
              .add(productOptionViewOrEditPanel.new ProductOptionEditFragment())
              .setOutputMarkupId(true));
        }
      }

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class OptionDataviewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
        class OptionDataview extends DataView<Option> {

          @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
          class RemoveAjaxLink extends BootstrapAjaxLink<Option> {

            private static final long serialVersionUID = -6950515027229520882L;

            public RemoveAjaxLink(String id, IModel<Option> model, Buttons.Type type,
                IModel<String> labelModel) {
              super(id, model, type, labelModel);
              setIconType(GlyphIconType.remove);
              setSize(Buttons.Size.Mini);
            }

            @Override
            public void onClick(AjaxRequestTarget target) {
              ((Product) OptionDataviewContainer.this.getDefaultModelObject()).getOptions()
                  .remove(RemoveAjaxLink.this.getDefaultModelObject());
              optionDataview.index =
                  ((Product) OptionDataviewContainer.this.getDefaultModelObject()).getOptions()
                      .size() - 1;
              productOptionViewOrEditPanel.removeAll();
              target.add(optionDataviewContainer.setOutputMarkupId(true));
              target.add(productOptionViewOrEditPanel
                  .add(productOptionViewOrEditPanel.new ProductOptionEditFragment())
                  .setOutputMarkupId(true));
            }
          }

          private static final long serialVersionUID = 8343306073164168425L;

          private int index = 0;

          protected OptionDataview(final String id, final IDataProvider<Option> dataProvider,
              final long itemsPerPage) {
            super(id, dataProvider, itemsPerPage);
          }

          @Override
          protected Item<Option> newItem(String id, int index, IModel<Option> model) {
            final Item<Option> item = super.newItem(id, index, model);
            if (this.index == index) {
              item.add(new AttributeModifier("class", "info"));
            }
            return item;
          }

          @Override
          protected void onConfigure() {
            final IModel<Product> model =
                (IModel<Product>) OptionDataviewContainer.this.getDefaultModel();
            if (!model.getObject().getOptions().isEmpty()) {
              productOptionViewOrEditPanel.setEnabled(true);
              productOptionViewOrEditPanel.removeAll();
              productOptionViewOrEditPanel
                  .setSelectedModel(Model.of(model.getObject().getOptions().get(index)));
              productOptionViewOrEditPanel
                  .add(productOptionViewOrEditPanel.new ProductOptionEditFragment())
                  .setOutputMarkupId(true);
            } else {
              productOptionViewOrEditPanel.setEnabled(false);
              productOptionViewOrEditPanel.removeAll();
              productOptionViewOrEditPanel.setSelectedModel(Model.of(new Option()));
              productOptionViewOrEditPanel
                  .add(productOptionViewOrEditPanel.new ProductOptionEditFragment())
                  .setOutputMarkupId(true);
            }
            super.onConfigure();
          }

          @Override
          protected void populateItem(Item<Option> item) {
            item.setModel(new CompoundPropertyModel<Option>(item.getModelObject()));
            item.add(new Label("description").setOutputMarkupId(true));
            item.add(new Label("value").setOutputMarkupId(true));
            item.add(new Label("disabled").setOutputMarkupId(true));
            item.add(new AjaxEventBehavior("click") {

              private static final long serialVersionUID = 1L;

              @Override
              public void onEvent(AjaxRequestTarget target) {
                index = item.getIndex();
                productOptionViewOrEditPanel.setSelectedModel(item.getModel());
                productOptionViewOrEditPanel.removeAll();
                target.add(optionDataviewContainer.setOutputMarkupId(true));
                target.add(productOptionViewOrEditPanel
                    .add(productOptionViewOrEditPanel.new ProductOptionEditFragment())
                    .setOutputMarkupId(true));
              }
            });
            item.add(new RemoveAjaxLink("remove", item.getModel(), Buttons.Type.Default,
                Model.of(ProductOptionPanel.this
                    .getString(NetbrasoftApplicationConstants.REMOVE_MESSAGE_KEY)))
                        .add(new ConfirmationBehavior() {

                          private static final long serialVersionUID = 7744720444161839031L;

                          @Override
                          public void renderHead(Component component, IHeaderResponse response) {
                            response.render($(component)
                                .chain("confirmation",
                                    new ConfirmationConfig()
                                        .withTitle(getString(
                                            NetbrasoftApplicationConstants.CONFIRMATION_TITLE_MESSAGE_KEY))
                                    .withSingleton(true).withPopout(true)
                                    .withBtnOkLabel(getString(
                                        NetbrasoftApplicationConstants.CONFIRM_MESSAGE_KEY))
                                    .withBtnCancelLabel(getString(
                                        NetbrasoftApplicationConstants.CANCEL_MESSAGE_KEY)))
                                .asDomReadyScript());
                          }
                        }));
          }
        }

        private static final long serialVersionUID = -3393999999422653255L;

        private final OptionDataview optionDataview;

        private final ListDataProvider<Option> optionListDataProvider;

        public OptionDataviewContainer(final String id, final IModel<Product> model) {
          super("optionDataviewContainer", model);
          optionListDataProvider = new ListDataProvider<Option>() {

            private static final long serialVersionUID = -3261859241046697057L;

            @Override
            protected List<Option> getData() {
              return ((Product) OptionDataviewContainer.this.getDefaultModelObject()).getOptions();
            }
          };
          optionDataview =
              new OptionDataview("optionDataview", optionListDataProvider, ITEMS_PER_PAGE);
        }

        @Override
        protected void onInitialize() {
          add(optionDataview.setOutputMarkupId(true));
          super.onInitialize();
        }
      }

      private static final long serialVersionUID = -5522048858537112825L;

      private final AddAjaxLink addAjaxLink;

      private final OptionDataviewContainer optionDataviewContainer;

      private final BootstrapPagingNavigator optionPagingNavigator;

      private final ProductOptionViewOrEditPanel productOptionViewOrEditPanel;

      public OptionEditTable(final String id, final IModel<Product> model) {
        super(id, model);
        addAjaxLink = new AddAjaxLink("add",
            (IModel<Product>) OptionEditTable.this.getDefaultModel(), Buttons.Type.Primary,
            Model.of(
                ProductOptionPanel.this.getString(NetbrasoftApplicationConstants.ADD_MESSAGE_KEY)));
        optionDataviewContainer = new OptionDataviewContainer("optionDataviewContainer",
            (IModel<Product>) OptionEditTable.this.getDefaultModel());
        optionPagingNavigator = new BootstrapPagingNavigator("optionPagingNavigator",
            optionDataviewContainer.optionDataview);
        productOptionViewOrEditPanel =
            new ProductOptionViewOrEditPanel("productOptionViewOrEditPanel",
                (IModel<Product>) OptionEditTable.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
        add(addAjaxLink.setOutputMarkupId(true));
        add(optionDataviewContainer.setOutputMarkupId(true));
        add(optionPagingNavigator.setOutputMarkupId(true));
        add(productOptionViewOrEditPanel
            .add(productOptionViewOrEditPanel.new ProductOptionEditFragment())
            .setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = -3434639671937275356L;

    private final OptionEditTable optionEditTable;

    public ProductOptionEditFragement() {
      super("productOptionViewOrEditFragement", "productOptionEditFragement",
          ProductOptionPanel.this, ProductOptionPanel.this.getDefaultModel());
      optionEditTable = new OptionEditTable("optionEditTable",
          (IModel<Product>) ProductOptionEditFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(optionEditTable.add(new TableBehavior().hover()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class ProductOptionViewFragement extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class OptionViewTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
      class OptionDataviewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
        class OptionDataview extends DataView<Option> {

          private static final long serialVersionUID = 8343306073164168425L;

          private int index = 0;

          protected OptionDataview(final String id, final IDataProvider<Option> dataProvider,
              final long itemsPerPage) {
            super(id, dataProvider, itemsPerPage);
          }

          @Override
          protected Item<Option> newItem(String id, int index, IModel<Option> model) {
            final Item<Option> item = super.newItem(id, index, model);
            if (this.index == index) {
              item.add(new AttributeModifier("class", "info"));
            }
            return item;
          }

          @Override
          protected void onConfigure() {
            final IModel<Product> model =
                (IModel<Product>) OptionDataviewContainer.this.getDefaultModel();
            if (!model.getObject().getOptions().isEmpty()) {
              productOptionViewOrEditPanel.removeAll();
              productOptionViewOrEditPanel
                  .setSelectedModel(Model.of(model.getObject().getOptions().get(index)));
              productOptionViewOrEditPanel
                  .add(productOptionViewOrEditPanel.new ProductOptionViewFragment())
                  .setOutputMarkupId(true);
            } else {
              productOptionViewOrEditPanel.removeAll();
              productOptionViewOrEditPanel.setSelectedModel(Model.of(new Option()));
              productOptionViewOrEditPanel
                  .add(productOptionViewOrEditPanel.new ProductOptionViewFragment())
                  .setOutputMarkupId(true);
            }
            super.onConfigure();
          }

          @Override
          protected void populateItem(Item<Option> item) {
            item.setModel(new CompoundPropertyModel<Option>(item.getModelObject()));
            item.add(new Label("description").setOutputMarkupId(true));
            item.add(new Label("value").setOutputMarkupId(true));
            item.add(new Label("disabled").setOutputMarkupId(true));
            item.add(new AjaxEventBehavior("click") {

              private static final long serialVersionUID = 1L;

              @Override
              public void onEvent(AjaxRequestTarget target) {
                index = item.getIndex();
                productOptionViewOrEditPanel.setSelectedModel(item.getModel());
                productOptionViewOrEditPanel.removeAll();
                target.add(optionDataviewContainer.setOutputMarkupId(true));
                target.add(productOptionViewOrEditPanel
                    .add(productOptionViewOrEditPanel.new ProductOptionViewFragment())
                    .setOutputMarkupId(true));
              }
            });
          }
        }

        private static final long serialVersionUID = -3393999999422653255L;

        private final OptionDataview optionDataview;

        private final ListDataProvider<Option> optionListDataProvider;

        public OptionDataviewContainer(final String id, final IModel<Product> model) {
          super("optionDataviewContainer", model);
          optionListDataProvider = new ListDataProvider<Option>() {

            private static final long serialVersionUID = -3261859241046697057L;

            @Override
            protected List<Option> getData() {
              return ((Product) OptionDataviewContainer.this.getDefaultModelObject()).getOptions();
            }
          };
          optionDataview =
              new OptionDataview("optionDataview", optionListDataProvider, ITEMS_PER_PAGE);
        }

        @Override
        protected void onInitialize() {
          add(optionDataview.setOutputMarkupId(true));
          super.onInitialize();
        }
      }

      private static final long serialVersionUID = -5522048858537112825L;

      private final NotificationPanel feedbackPanel;

      private final OptionDataviewContainer optionDataviewContainer;

      private final BootstrapPagingNavigator optionPagingNavigator;

      private final ProductOptionViewOrEditPanel productOptionViewOrEditPanel;

      public OptionViewTable(final String id, final IModel<Product> model) {
        super(id, model);
        feedbackPanel = new NotificationPanel("feedback");
        optionDataviewContainer = new OptionDataviewContainer("optionDataviewContainer",
            (IModel<Product>) OptionViewTable.this.getDefaultModel());
        optionPagingNavigator = new BootstrapPagingNavigator("optionPagingNavigator",
            optionDataviewContainer.optionDataview);
        productOptionViewOrEditPanel =
            new ProductOptionViewOrEditPanel("productOptionViewOrEditPanel",
                (IModel<Product>) OptionViewTable.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
        add(feedbackPanel.setOutputMarkupId(true));
        add(optionDataviewContainer.setOutputMarkupId(true));
        add(optionPagingNavigator.setOutputMarkupId(true));
        add(productOptionViewOrEditPanel
            .add(productOptionViewOrEditPanel.new ProductOptionViewFragment())
            .setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = -3434639671937275356L;

    private final OptionViewTable optionViewTable;

    public ProductOptionViewFragement() {
      super("productOptionViewOrEditFragement", "productOptionViewFragement",
          ProductOptionPanel.this, ProductOptionPanel.this.getDefaultModel());
      optionViewTable = new OptionViewTable("optionViewTable",
          (IModel<Product>) ProductOptionViewFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(optionViewTable.add(new TableBehavior().hover()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final int ITEMS_PER_PAGE = 10;

  private static final long serialVersionUID = 2594347542853743933L;

  public ProductOptionPanel(final String id, final IModel<Product> model) {
    super(id, model);
  }
}
