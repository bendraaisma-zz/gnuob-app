package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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
            paramItem.add(new Label("name"));
            paramItem.add(new Label("format"));
            paramItem.add(createRemoveAjaxLink(paramItem).setVisible(enableOperations));
         }
      };
   }

   private ImageUploadPanel createImageUploadPanel() {
      return new ImageUploadPanel("imageUploadPanel", new Model<Content>(new Content())) {

         private static final long serialVersionUID = -7913720234767091477L;

         @Override
         public void uploadedImage(AjaxRequestTarget paramAjaxRequestTarget) {
            Category category = ((Category) ContentPanel.this.getDefaultModelObject());
            Content content = (Content) getDefaultModel().getObject();

            category.getContents().add(content);

            paramAjaxRequestTarget.add(getPage());
         }
      };
   }

   private AjaxLink<Void> createRemoveAjaxLink(Item<Content> paramItem) {
      return new AjaxLink<Void>("remove") {

         private static final long serialVersionUID = -6950515027229520882L;

         @Override
         public void onClick(AjaxRequestTarget target) {
            // TODO Auto-generated method stub
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

      add(contentDataviewContainer.add(contentDataview));
      add(itemsPerPagePagingNavigator);
      add(imageUploadPanel);

      setOutputMarkupId(true);
      super.onInitialize();
   }

}
