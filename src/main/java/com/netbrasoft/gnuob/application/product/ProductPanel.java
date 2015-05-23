package com.netbrasoft.gnuob.application.product;

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
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ProductPanel extends Panel {

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

   class ProductDataview extends DataView<Product> {

      private static final long serialVersionUID = -5039874949058607907L;

      protected ProductDataview() {
         super("productDataview", productDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected Item<Product> newItem(String id, int index, IModel<Product> model) {
         Item<Product> item = super.newItem(id, index, model);

         if (model.getObject().getId() == ((Product) productViewOrEditPanel.getDefaultModelObject()).getId()) {
            item.add(new AttributeModifier("class", "info"));
         }

         return item;
      }

      @Override
      protected void populateItem(Item<Product> item) {
         item.setModel(new CompoundPropertyModel<Product>(item.getModelObject()));
         item.add(new Label("number"));
         item.add(new Label("name"));
         item.add(new AjaxEventBehavior("click") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               productViewOrEditPanel.setDefaultModelObject(item.getModelObject());
               target.add(getPage());
            }
         });
      }
   }

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   private ProductDataview productDataview = new ProductDataview();

   @SpringBean(name = "ProductDataProvider", required = true)
   private GenericTypeDataProvider<Product> productDataProvider;

   private OrderByBorder<String> orderByNumber = new OrderByBorder<String>("orderByNumber", "number", productDataProvider);

   private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", productDataProvider);

   private WebMarkupContainer productDataviewContainer = new WebMarkupContainer("productDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(productDataview);
         super.onInitialize();
      }
   };

   private BootstrapPagingNavigator productPagingNavigator = new BootstrapPagingNavigator("productPagingNavigator", productDataview);

   private ProductViewOrEditPanel productViewOrEditPanel = new ProductViewOrEditPanel("productViewOrEditPanel", Model.of(new Product()));

   public ProductPanel(final String id, final IModel<Product> model) {
      super(id, model);
   }

   @Override
   protected void onInitialize() {
      productDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      productDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      productDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      productDataProvider.setType((Product) getDefaultModelObject());

      add(new AddAjaxLink());
      add(orderByNumber);
      add(orderByName);
      add(productDataviewContainer.setOutputMarkupId(true));
      add(productPagingNavigator);
      add(productViewOrEditPanel.add(productViewOrEditPanel.new ProductViewFragement()).setOutputMarkupId(true));

      super.onInitialize();
   }
}
