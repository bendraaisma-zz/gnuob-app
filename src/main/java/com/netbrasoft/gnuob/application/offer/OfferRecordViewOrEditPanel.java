package com.netbrasoft.gnuob.application.offer;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.UrlTextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Offer;
import com.netbrasoft.gnuob.api.OfferRecord;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class OfferRecordViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class OfferRecordEditFragement extends Fragment {

      private static final long serialVersionUID = 3709791409078428685L;

      private final WebMarkupContainer offerRecordEditTable;

      public OfferRecordEditFragement() {
         super("offerRecordViewOrEditFragement", "offerRecordEditFragement", OfferRecordViewOrEditPanel.this, OfferRecordViewOrEditPanel.this.getDefaultModel());

         offerRecordEditTable = new WebMarkupContainer("offerRecordEditTable", getDefaultModel()) {

            private static final long serialVersionUID = -6051033065197862976L;

            @Override
            protected void onInitialize() {
               final Form<OfferRecord> offerRecordEditForm = new Form<OfferRecord>("offerRecordEditForm");
               offerRecordEditForm.setModel(new CompoundPropertyModel<OfferRecord>((IModel<OfferRecord>) getDefaultModel()));
               offerRecordEditForm.add(new RequiredTextField<String>("offerRecordId").add(StringValidator.maximumLength(64)));
               offerRecordEditForm.add(new RequiredTextField<String>("productNumber").add(StringValidator.maximumLength(62)));
               offerRecordEditForm.add(new RequiredTextField<String>("name").add(StringValidator.maximumLength(128)));
               offerRecordEditForm.add(new TextField<String>("option").add(StringValidator.maximumLength(128)));
               offerRecordEditForm.add(new TextArea<String>("description").add(StringValidator.maximumLength(128)));
               offerRecordEditForm.add(new UrlTextField("itemUrl", new PropertyModel<String>(getDefaultModelObject(), "itemUrl")).add(StringValidator.maximumLength(255)));
               offerRecordEditForm.add(new NumberTextField<Integer>("quantity").setRequired(true));
               offerRecordEditForm.add(new NumberTextField<Integer>("amount").setRequired(true));
               offerRecordEditForm.add(new NumberTextField<Integer>("discount").setRequired(true));
               offerRecordEditForm.add(new NumberTextField<Integer>("shippingCost").setRequired(true));
               offerRecordEditForm.add(new NumberTextField<Integer>("tax").setRequired(true));
               offerRecordEditForm.add(new NumberTextField<Integer>("itemHeight"));
               offerRecordEditForm.add(new TextField<String>("itemHeightUnit").add(StringValidator.maximumLength(20)));
               offerRecordEditForm.add(new NumberTextField<Integer>("itemLength"));
               offerRecordEditForm.add(new TextField<String>("itemLengthUnit").add(StringValidator.maximumLength(20)));
               offerRecordEditForm.add(new NumberTextField<Integer>("itemWeight"));
               offerRecordEditForm.add(new TextField<String>("itemWeightUnit").add(StringValidator.maximumLength(20)));
               offerRecordEditForm.add(new NumberTextField<Integer>("itemWidth"));
               offerRecordEditForm.add(new TextField<String>("itemWidthUnit").add(StringValidator.maximumLength(20)));
               add(offerRecordEditForm.setOutputMarkupId(true));
               add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
               add(new SaveAjaxButton(offerRecordEditForm).setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
      }

      @Override
      protected void onInitialize() {
         add(offerRecordEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER })
   class OfferRecordViewFragement extends Fragment {

      private static final long serialVersionUID = 6927997909191615786L;

      private final WebMarkupContainer offerRecordViewTable;

      public OfferRecordViewFragement() {
         super("offerRecordViewOrEditFragement", "offerRecordViewFragement", OfferRecordViewOrEditPanel.this, OfferRecordViewOrEditPanel.this.getDefaultModel());
         offerRecordViewTable = new WebMarkupContainer("offerRecordViewTable", getDefaultModel()) {

            private static final long serialVersionUID = 4831933162858730026L;

            @Override
            protected void onInitialize() {
               final Form<OfferRecord> offerRecorViewForm = new Form<OfferRecord>("offerRecordViewForm");
               offerRecorViewForm.setModel(new CompoundPropertyModel<OfferRecord>((IModel<OfferRecord>) getDefaultModel()));
               offerRecorViewForm.add(new Label("offerRecordId"));
               offerRecorViewForm.add(new Label("productNumber"));
               offerRecorViewForm.add(new Label("name"));
               offerRecorViewForm.add(new Label("option"));
               offerRecorViewForm.add(new Label("description"));
               offerRecorViewForm.add(new Label("itemUrl"));
               offerRecorViewForm.add(new Label("quantity"));
               offerRecorViewForm.add(new Label("amount"));
               offerRecorViewForm.add(new Label("discount"));
               offerRecorViewForm.add(new Label("shippingCost"));
               offerRecorViewForm.add(new Label("tax"));
               offerRecorViewForm.add(new Label("itemHeight"));
               offerRecorViewForm.add(new Label("itemHeightUnit"));
               offerRecorViewForm.add(new Label("itemLength"));
               offerRecorViewForm.add(new Label("itemLengthUnit"));
               offerRecorViewForm.add(new Label("itemWeight"));
               offerRecorViewForm.add(new Label("itemWeightUnit"));
               offerRecorViewForm.add(new Label("itemWidth"));
               offerRecorViewForm.add(new Label("itemWidthUnit"));
               add(offerRecorViewForm.setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
      }

      @Override
      protected void onInitialize() {
         add(offerRecordViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends BootstrapAjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", Model.of(OfferRecordViewOrEditPanel.this.getString("saveMessage")), form, Buttons.Type.Primary);
         setSize(Buttons.Size.Small);
         add(new LoadingBehavior(Model.of(OfferRecordViewOrEditPanel.this.getString("saveMessage"))));
      }

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
         form.add(new TooltipValidation());
         target.add(form);
         target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OfferRecordViewOrEditPanel.this.getString("saveMessage")))));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            final OfferRecord offerRecordForm = (OfferRecord) form.getDefaultModelObject();

            if (offerRecordForm.getId() == 0) {
               ((Offer) markupContainer.getDefaultModelObject()).getRecords().add(offerRecordForm);
            }
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(markupContainer.setOutputMarkupId(true));
            target.add(form.setOutputMarkupId(true));
            target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OfferRecordViewOrEditPanel.this.getString("saveMessage")))));
         }
      }
   }

   private static final long serialVersionUID = -7002701340914975498L;

   private static final Logger LOGGER = LoggerFactory.getLogger(OfferRecordViewOrEditPanel.class);

   private final MarkupContainer markupContainer;

   public OfferRecordViewOrEditPanel(final String id, final IModel<OfferRecord> model, MarkupContainer markupContainer) {
      super(id, model);
      this.markupContainer = markupContainer;
   }
}
