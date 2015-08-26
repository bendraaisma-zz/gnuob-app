package com.netbrasoft.gnuob.application.customer;

import static de.agilecoders.wicket.jquery.JQuery.$;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
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

import com.netbrasoft.gnuob.api.Customer;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class CustomerPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class AddAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = -8317730269644885290L;

      public AddAjaxLink() {
         super("add", Model.of(CustomerPanel.this.getString("addMessage")), Buttons.Type.Primary, Model.of(CustomerPanel.this.getString("addMessage")));
         setIconType(GlyphIconType.plus);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         customerViewOrEditPanel.setDefaultModelObject(new Customer());
         target.add(customerViewOrEditPanel.setOutputMarkupId(true));
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class CustomerDataview extends DataView<Customer> {

      private static final long serialVersionUID = -5039874949058607907L;

      protected CustomerDataview() {
         super("customerDataview", customerDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected Item<Customer> newItem(String id, int index, final IModel<Customer> model) {
         final Item<Customer> item = super.newItem(id, index, model);

         if (model.getObject().getId() == ((Customer) customerViewOrEditPanel.getDefaultModelObject()).getId()) {
            // FIXME BD: use wicket bootstrap for this attribute / table.
            item.add(new AttributeModifier("class", "info"));
         } else {
            if (index == 0 && ((Customer) customerViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
               // FIXME BD: use wicket bootstrap for this attribute / table.
               item.add(new AttributeModifier("class", "info"));
            }
         }

         return item;
      }

      @Override
      protected void populateItem(Item<Customer> item) {
         item.setModel(new CompoundPropertyModel<Customer>(item.getModelObject()));
         item.add(new Label("firstName"));
         item.add(new Label("lastName"));
         item.add(new AjaxEventBehavior("click") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               customerViewOrEditPanel.setDefaultModelObject(item.getModelObject());
               target.add(customerDataviewContainer.setOutputMarkupId(true));
               target.add(customerViewOrEditPanel.setOutputMarkupId(true));
            }
         });
         item.add(new RemoveAjaxLink(item.getModel()).add(new ConfirmationBehavior() {

            private static final long serialVersionUID = 7744720444161839031L;

            @Override
            public void renderHead(Component component, IHeaderResponse response) {
               response.render($(component)
                     .chain("confirmation", new ConfirmationConfig().withTitle(getString("confirmationTitleMessage")).withSingleton(true).withPopout(true).withBtnOkLabel(getString("confirmMessage")).withBtnCancelLabel(getString("cancelMessage")))
                     .asDomReadyScript());
            }
         }));

         if (item.getIndex() == 0 && ((Customer) customerViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
            customerViewOrEditPanel.setDefaultModelObject(item.getModelObject());
         }
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class RemoveAjaxLink extends BootstrapAjaxLink<Customer> {

      private static final long serialVersionUID = -8317730269644885290L;

      public RemoveAjaxLink(final IModel<Customer> model) {
         super("remove", model, Buttons.Type.Default, Model.of(CustomerPanel.this.getString("removeMessage")));
         setIconType(GlyphIconType.remove);
         setSize(Buttons.Size.Mini);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         try {
            getModelObject().setActive(false);
            customerDataProvider.merge(getModelObject());
            customerViewOrEditPanel.setDefaultModelObject(new Customer());
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(getPage());
         }
      }
   }

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   private static final Logger LOGGER = LoggerFactory.getLogger(CustomerPanel.class);

   @SpringBean(name = "CustomerDataProvider", required = true)
   private GenericTypeDataProvider<Customer> customerDataProvider;

   private final OrderByBorder<String> orderByFirstName;

   private final OrderByBorder<String> orderByLastName;

   private final DataView<Customer> customerDataview;

   private final WebMarkupContainer customerDataviewContainer;

   private final BootstrapPagingNavigator customerPagingNavigator;

   private final CustomerViewOrEditPanel customerViewOrEditPanel;

   public CustomerPanel(final String id, final IModel<Customer> model) {
      super(id, model);

      orderByFirstName = new OrderByBorder<String>("orderByFirstName", "firstName", customerDataProvider);
      orderByLastName = new OrderByBorder<String>("orderByLastName", "lastName", customerDataProvider);
      customerDataview = new CustomerDataview();
      customerDataviewContainer = new WebMarkupContainer("customerDataviewContainer") {

         private static final long serialVersionUID = -497527332092449028L;

         @Override
         protected void onInitialize() {
            add(customerDataview.setOutputMarkupId(true));
            super.onInitialize();
         }
      };
      customerPagingNavigator = new BootstrapPagingNavigator("customerPagingNavigator", customerDataview);
      customerViewOrEditPanel = new CustomerViewOrEditPanel("customerViewOrEditPanel", (IModel<Customer>) getDefaultModel());
   }

   @Override
   protected void onInitialize() {
      customerDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      customerDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      customerDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      customerDataProvider.setType(new Customer());
      customerDataProvider.getType().setActive(true);

      add(new AddAjaxLink().setOutputMarkupId(true));
      add(orderByFirstName.setOutputMarkupId(true));
      add(orderByLastName.setOutputMarkupId(true));
      add(customerDataviewContainer.setOutputMarkupId(true));
      add(customerPagingNavigator.setOutputMarkupId(true));
      add(customerViewOrEditPanel.add(customerViewOrEditPanel.new CustomerViewFragement()).setOutputMarkupId(true));

      add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));

      super.onInitialize();
   }
}
