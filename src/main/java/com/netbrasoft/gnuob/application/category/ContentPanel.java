package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.application.image.ImageUploadPanel;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ContentPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class RemoveAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = -6950515027229520882L;

      public RemoveAjaxLink() {
         super("remove");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         // TODO Auto-generated method stub
      }
   }

   private static final long serialVersionUID = 180343040391839545L;
   private static final long ITEMS_PER_PAGE = 5;

   private boolean enableOperations = true;

   public ContentPanel(final String id, final IModel<Category> model, final boolean enableOperations) {
      super(id, model);
      this.enableOperations = enableOperations;
   }

   private DataView<Content> createContentDataView(ListDataProvider<Content> contentListDataProvider) {
      return new DataView<Content>("contentDataview", contentListDataProvider, ITEMS_PER_PAGE) {

         private static final long serialVersionUID = -7353992345622657728L;

         @Override
         protected Item<Content> newItem(String id, int index, IModel<Content> model) {
            Item<Content> item = super.newItem(id, index, model);

            if (model.getObject().getId() == ((Content) ContentPanel.this.getDefaultModelObject()).getId()) {
               item.add(new AttributeModifier("class", "info"));
            }

            return item;
         }

         @Override
         protected void populateItem(Item<Content> paramItem) {
            IModel<Content> compound = new CompoundPropertyModel<Content>(paramItem.getModelObject());
            paramItem.setModel(compound);
            paramItem.add(new Label("name"));
            paramItem.add(new Label("format"));
            paramItem.add(new RemoveAjaxLink().setVisible(enableOperations));
         }
      };
   }

   private ImageUploadPanel createImageUploadPanel() {
      return new ImageUploadPanel("imageUploadPanel", new Model<Content>(new Content())) {

         private static final long serialVersionUID = -7913720234767091477L;

         @Override
         public void uploadedImage(AjaxRequestTarget target) {
            ((Category) ContentPanel.this.getDefaultModelObject()).getContents().add((Content) getDefaultModel().getObject());
            target.add(getPage());
         }
      };
   }

   @Override
   protected void onInitialize() {
      IModel<Category> model = (IModel<Category>) getDefaultModel();
      WebMarkupContainer contentDataviewContainer = new WebMarkupContainer("contentDataviewContainer");
      ContentListDataProvider categoryContentListDataProvider = new ContentListDataProvider(model);
      DataView<Content> contentDataview = createContentDataView(categoryContentListDataProvider);
      BootstrapPagingNavigator itemsPerPagePagingNavigator = new BootstrapPagingNavigator("contentPagingNavigator", contentDataview);
      ImageUploadPanel imageUploadPanel = createImageUploadPanel();

      add(contentDataviewContainer.add(contentDataview));
      add(itemsPerPagePagingNavigator);
      add(imageUploadPanel);

      super.onInitialize();
   }
}
