package com.netbrasoft.gnuob.application.offer;

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

import com.netbrasoft.gnuob.api.Offer;
import com.netbrasoft.gnuob.api.OfferRecord;
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
public class OfferRecordPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class AddAjaxLink extends BootstrapAjaxLink<String> {

    private static final long serialVersionUID = 9191172039973638020L;

    public AddAjaxLink() {
      super("add", Model.of(OfferRecordPanel.this.getString("addMessage")), Buttons.Type.Primary, Model.of(OfferRecordPanel.this.getString("addMessage")));
      setIconType(GlyphIconType.plus);
      setSize(Buttons.Size.Small);
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      offerRecordViewOrEditPanel.setDefaultModelObject(new OfferRecord());
      target.add(offerRecordViewOrEditPanel.setOutputMarkupId(true));
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class OfferRecordDataview extends DataView<OfferRecord> {

    private static final long serialVersionUID = 8996562822101409998L;

    private boolean removeAjaxLinkVisable;

    private long selectedObjectId;

    protected OfferRecordDataview() {
      super("offerRecordDataview", offerRecordListDataProvider, ITEMS_PER_PAGE);
    }

    public boolean isRemoveAjaxLinkVisable() {
      return removeAjaxLinkVisable;
    }

    @Override
    protected Item<OfferRecord> newItem(String id, int index, IModel<OfferRecord> model) {
      final Item<OfferRecord> item = super.newItem(id, index, model);
      final long modelObjectId = ((OfferRecord) offerRecordViewOrEditPanel.getDefaultModelObject()).getId();

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
      if (selectedObjectId != ((Offer) OfferRecordPanel.this.getDefaultModelObject()).getId()) {
        selectedObjectId = ((Offer) OfferRecordPanel.this.getDefaultModelObject()).getId();
        offerRecordViewOrEditPanel.setDefaultModelObject(new OfferRecord());
      }
      super.onConfigure();
    }

    @Override
    protected void populateItem(Item<OfferRecord> item) {
      final IModel<OfferRecord> compound = new CompoundPropertyModel<OfferRecord>(item.getModelObject());
      item.setModel(compound);
      item.add(new Label("name"));
      item.add(new Label("description"));
      item.add(new AjaxEventBehavior("click") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onEvent(AjaxRequestTarget target) {
          offerRecordViewOrEditPanel.setDefaultModelObject(item.getModelObject());
          target.add(offerRecordDataviewContainer.setOutputMarkupId(true));
          target.add(offerRecordViewOrEditPanel.setOutputMarkupId(true));
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

      if (item.getIndex() == 0 && ((OfferRecord) offerRecordViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
        offerRecordViewOrEditPanel.setDefaultModelObject(item.getModelObject());
      }
    }

    public void setRemoveAjaxLinkVisable(boolean removeAjaxLinkVisable) {
      this.removeAjaxLinkVisable = removeAjaxLinkVisable;
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class OfferRecordEditFragement extends Fragment {

    private static final long serialVersionUID = 3709791409078428685L;

    public OfferRecordEditFragement() {
      super("offerRecordViewOrEditFragement", "offerRecordEditFragement", OfferRecordPanel.this, OfferRecordPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(offerRecordEditTable.setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  class OfferRecordListDataProvider extends ListDataProvider<OfferRecord> {

    private static final long serialVersionUID = 5259243752700177690L;

    @Override
    protected List<OfferRecord> getData() {
      return ((Offer) OfferRecordPanel.this.getDefaultModelObject()).getRecords();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER})
  class OfferRecordViewFragement extends Fragment {

    private static final long serialVersionUID = 3709791409078428685L;

    public OfferRecordViewFragement() {
      super("offerRecordViewOrEditFragement", "offerRecordViewFragement", OfferRecordPanel.this, OfferRecordPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(offerRecordViewTable.setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class RemoveAjaxLink extends BootstrapAjaxLink<OfferRecord> {

    private static final long serialVersionUID = -6950515027229520882L;

    public RemoveAjaxLink(final IModel<OfferRecord> model) {
      super("remove", model, Buttons.Type.Default, Model.of(OfferRecordPanel.this.getString("removeMessage")));
      setIconType(GlyphIconType.remove);
      setSize(Buttons.Size.Mini);
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      try {
        offerRecordListDataProvider.getData().remove(getDefaultModelObject());
      } catch (final RuntimeException e) {
        LOGGER.warn(e.getMessage(), e);
        warn(e.getLocalizedMessage());
      } finally {
        target.add(offerRecordDataviewContainer.setOutputMarkupId(true));
      }
    }
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(OfferRecordPanel.class);

  private static final long serialVersionUID = -4119480413347414297L;

  private static final long ITEMS_PER_PAGE = 10;

  private final WebMarkupContainer offerRecordDataviewContainer;

  private final OfferRecordDataview offerRecordDataview;

  private final BootstrapPagingNavigator offerRecordPagingNavigator;

  private final OfferRecordListDataProvider offerRecordListDataProvider;

  private final OfferRecordViewOrEditPanel offerRecordViewOrEditPanel;

  private final WebMarkupContainer offerRecordEditTable;

  private final WebMarkupContainer offerRecordViewTable;

  public OfferRecordPanel(final String id, final IModel<Offer> model) {
    super(id, model);
    offerRecordListDataProvider = new OfferRecordListDataProvider();
    offerRecordDataview = new OfferRecordDataview();
    offerRecordPagingNavigator = new BootstrapPagingNavigator("offerRecordPagingNavigator", offerRecordDataview);
    offerRecordDataviewContainer = new WebMarkupContainer("offerRecordDataviewContainer") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onInitialize() {
        add(offerRecordDataview.setOutputMarkupId(true));
        super.onInitialize();
      }
    };
    offerRecordEditTable = new WebMarkupContainer("offerRecordEditTable", getDefaultModel()) {

      private static final long serialVersionUID = 459122691621477233L;

      @Override
      protected void onInitialize() {
        offerRecordDataview.setRemoveAjaxLinkVisable(true);
        add(new AddAjaxLink().setOutputMarkupId(true));
        add(offerRecordPagingNavigator.setOutputMarkupId(true));
        add(offerRecordDataviewContainer.setOutputMarkupId(true));
        add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
        add(offerRecordViewOrEditPanel.add(offerRecordViewOrEditPanel.new OfferRecordEditFragement()).setOutputMarkupId(true));
        add(new TableBehavior());
        super.onInitialize();
      }
    };
    offerRecordViewTable = new WebMarkupContainer("offerRecordViewTable", getDefaultModel()) {

      private static final long serialVersionUID = 8276100341510505878L;

      @Override
      protected void onInitialize() {
        offerRecordDataview.setRemoveAjaxLinkVisable(false);
        add(offerRecordPagingNavigator.setOutputMarkupId(true));
        add(offerRecordDataviewContainer.setOutputMarkupId(true));
        add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
        add(offerRecordViewOrEditPanel.add(offerRecordViewOrEditPanel.new OfferRecordViewFragement()).setOutputMarkupId(true));
        add(new TableBehavior());
        super.onInitialize();
      }
    };
    offerRecordViewOrEditPanel = new OfferRecordViewOrEditPanel("offerRecordViewOrEditPanel", Model.of(new OfferRecord()), offerRecordEditTable);
  }
}
