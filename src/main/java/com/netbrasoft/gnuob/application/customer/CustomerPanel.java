package com.netbrasoft.gnuob.application.customer;

import org.apache.wicket.Session;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.netbrasoft.gnuob.api.Customer;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class CustomerPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
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

   class CustomerDataview extends DataView<Customer> {

      private static final long serialVersionUID = -5039874949058607907L;

      protected CustomerDataview() {
         super("customerDataview", customerDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected void populateItem(Item<Customer> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Customer>(paramItem.getModelObject()));
         paramItem.add(new Label("firstName"));
         paramItem.add(new Label("lastName"));
         paramItem.add(new AjaxEventBehavior("onclick") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               customerViewOrEditPanel.setDefaultModelObject(paramItem.getModelObject());
               target.add(customerViewOrEditPanel);
            }
         });
      }
   }

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "CustomerDataProvider", required = true)
   private GenericTypeDataProvider<Customer> customerDataProvider;

   private OrderByBorder<String> orderByFirstName = new OrderByBorder<String>("orderByFirstName", "firstName", customerDataProvider);

   private OrderByBorder<String> orderByLastName = new OrderByBorder<String>("orderByLastName", "lastName", customerDataProvider);

   private DataView<Customer> customerDataview = new CustomerDataview();

   private WebMarkupContainer customerDataviewContainer = new WebMarkupContainer("customerDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(customerDataview);
         super.onInitialize();
      }
   };

   private BootstrapPagingNavigator customerPagingNavigator = new BootstrapPagingNavigator("customerPagingNavigator", customerDataview);

   private CustomerViewOrEditPanel customerViewOrEditPanel = new CustomerViewOrEditPanel("customerViewOrEditPanel", new Model<Customer>(new Customer()));

   public CustomerPanel(final String id, final IModel<Customer> model) {
      super(id, model);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      customerDataProvider.setUser(roleSession.getUsername());
      customerDataProvider.setPassword(roleSession.getPassword());
      customerDataProvider.setSite(roleSession.getSite());
      customerDataProvider.setType((Customer) getDefaultModelObject());

      add(new AddAjaxLink());
      add(orderByFirstName);
      add(orderByLastName);
      add(customerDataviewContainer.setOutputMarkupId(true));
      add(customerPagingNavigator);
      add(customerViewOrEditPanel.setOutputMarkupId(true));
   }
}
