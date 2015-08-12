package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
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

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.OrderBy;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class CategoryPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class AddAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = -8317730269644885290L;

      public AddAjaxLink() {
         super("add", Model.of(CategoryPanel.this.getString("addMessage")), Buttons.Type.Primary, Model.of(CategoryPanel.this.getString("addMessage")));
         setIconType(GlyphIconType.plus);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         // TODO Auto-generated method stub
      }
   }

   class CategoryDataview extends DataView<Category> {

      private static final long serialVersionUID = -5039874949058607907L;

      private static final int ITEMS_PER_PAGE = 5;

      protected CategoryDataview() {
         super("categoryDataview", categoryDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected Item<Category> newItem(String id, int index, IModel<Category> model) {
         final Item<Category> item = super.newItem(id, index, model);

         if (model.getObject().getId() == ((Category) categoryViewOrEditPanel.getDefaultModelObject()).getId()) {
            item.add(new AttributeModifier("class", "info"));
         }

         return item;
      }

      @Override
      protected void populateItem(Item<Category> item) {
         item.setModel(new CompoundPropertyModel<Category>(item.getModelObject()));
         item.add(new Label("name"));
         item.add(new Label("position"));
         item.add(new AjaxEventBehavior("click") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               categoryViewOrEditPanel.setDefaultModelObject(item.getModelObject());
               target.add(getPage());
            }
         });
      }
   }

   private static final long serialVersionUID = 3703226064705246155L;

   private final CategoryDataview categoryDataview = new CategoryDataview();

   @SpringBean(name = "CategoryDataProvider", required = true)
   private GenericTypeDataProvider<Category> categoryDataProvider;

   private final OrderByBorder<String> orderByposition = new OrderByBorder<String>("orderByPosition", "position", categoryDataProvider);

   private final OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", categoryDataProvider);

   private final WebMarkupContainer categoryDataviewContainer = new WebMarkupContainer("categoryDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(categoryDataview);
         super.onInitialize();
      }
   };

   private final BootstrapPagingNavigator categoryPagingNavigator = new BootstrapPagingNavigator("categoryPagingNavigator", categoryDataview);

   private final CategoryViewOrEditPanel categoryViewOrEditPanel = new CategoryViewOrEditPanel("categoryViewOrEditPanel", (IModel<Category>) getDefaultModel());

   public CategoryPanel(final String id, final IModel<Category> model) {
      super(id, model);
   }

   @Override
   protected void onInitialize() {
      categoryDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      categoryDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      categoryDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      categoryDataProvider.setType(new Category());
      categoryDataProvider.getType().setActive(true);
      categoryDataProvider.setOrderBy(OrderBy.POSITION_A_Z);

      add(new AddAjaxLink());
      add(orderByposition);
      add(orderByName);
      add(categoryDataviewContainer.setOutputMarkupId(true));
      add(categoryPagingNavigator);
      add(categoryViewOrEditPanel.add(categoryViewOrEditPanel.new CategoryViewFragement()).setOutputMarkupId(true));

      super.onInitialize();
   }
}
