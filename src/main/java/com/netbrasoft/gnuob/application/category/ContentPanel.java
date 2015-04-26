package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.ajax.AjaxRequestTarget;
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
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;
import com.netbrasoft.gnuob.wicket.bootstrap.ajax.markup.html.BootstrapConfirmationAjaxLink;

@SuppressWarnings("unchecked")
public class ContentPanel extends Panel {

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
         protected void populateItem(Item<Content> paramItem) {
            IModel<Content> compound = new CompoundPropertyModel<Content>(paramItem.getModelObject());
            paramItem.setModel(compound);
            paramItem.add(new Label("format"));
            paramItem.add(new Label("name"));
            paramItem.add(createRemoveBootstrapConfirmationAjaxLink(paramItem).setVisible(enableOperations));
         }
      };
   }

   private ImageUploadPanel createImageUploadPanel() {
      return new ImageUploadPanel("imageUploadPanel", new Model<Content>(new Content())) {

         private static final long serialVersionUID = -7913720234767091477L;

         @Override
         public void uploadedImage(AjaxRequestTarget paramAjaxRequestTarget) {
            ((Category) getDefaultModelObject()).getContents().add((Content) getDefaultModel().getObject());
            paramAjaxRequestTarget.add(ContentPanel.this);
         }
      };
   }

   private BootstrapConfirmationAjaxLink<Void> createRemoveBootstrapConfirmationAjaxLink(Item<Content> paramItem) {
      return new BootstrapConfirmationAjaxLink<Void>("remove") {

         private static final long serialVersionUID = -6950515027229520882L;

         @Override
         public void onCancel(AjaxRequestTarget paramAjaxRequestTarget) {
            return; // Do noting here, only return.
         }

         @Override
         public void onConfirm(AjaxRequestTarget paramAjaxRequestTarget) {
            ((Category) getDefaultModelObject()).getContents().remove(paramItem.getModelObject());
            paramAjaxRequestTarget.add(ContentPanel.this);
         }
      };
   }

   @Override
   protected void onInitialize() {
      IModel<Category> model = (IModel<Category>) getDefaultModel();
      WebMarkupContainer contentDataviewContainer = new WebMarkupContainer("contentDataviewContainer");
      ContentListDataProvider categoryContentListDataProvider = new ContentListDataProvider(model);
      DataView<Content> contentDataview = createContentDataView(categoryContentListDataProvider);
      ItemsPerPagePagingNavigator itemsPerPagePagingNavigator = new ItemsPerPagePagingNavigator("contentPagingNavigator", contentDataview);
      ImageUploadPanel imageUploadPanel = createImageUploadPanel();

      add(contentDataviewContainer.add(contentDataview).add(imageUploadPanel));
      add(itemsPerPagePagingNavigator);

      setOutputMarkupId(true);
      super.onInitialize();
   }

}
