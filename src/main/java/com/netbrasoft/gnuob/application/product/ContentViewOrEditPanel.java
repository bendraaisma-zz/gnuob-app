package com.netbrasoft.gnuob.application.product;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
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

import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.application.image.ImageUploadPanel;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ContentViewOrEditPanel extends Panel {

   class ContentDataview extends DataView<Content> {

      private static final long serialVersionUID = 2246346365193989354L;

      protected ContentDataview() {
         super("contentDataview", new ContentListDataProvider(), ITEMS_PER_PAGE);
      }

      @Override
      protected void populateItem(Item<Content> item) {
         IModel<Content> compound = new CompoundPropertyModel<Content>(item.getModelObject());
         item.setModel(compound);
         item.add(new Label("name"));
         item.add(new Label("format"));
         item.add(new RemoveAjaxLink());
      }
   }

   class ContentEditFragement extends Fragment {

      private static final long serialVersionUID = 8640403483040526601L;

      public ContentEditFragement() {
         super("contentViewOrEditFragement", "contentEditFragement", ContentViewOrEditPanel.this, ContentViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(contentDataviewContainer.setOutputMarkupId(true));
         add(contentPagingNavigator);
         add(imageUploadPanel);
         super.onInitialize();
      }
   }

   class ContentListDataProvider extends ListDataProvider<Content> {

      private static final long serialVersionUID = 5259243752700177690L;

      @Override
      protected List<Content> getData() {
         return ((Product) ContentViewOrEditPanel.this.getDefaultModelObject()).getContents();
      }
   }

   class ContentViewFragement extends Fragment {

      private static final long serialVersionUID = 8640403483040526601L;

      public ContentViewFragement() {
         super("contentViewOrEditFragement", "contentViewFragement", ContentViewOrEditPanel.this, ContentViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(contentDataviewContainer.setOutputMarkupId(true));
         add(contentPagingNavigator);
         super.onInitialize();
      }
   }

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

   private WebMarkupContainer contentDataviewContainer = new WebMarkupContainer("contentDataviewContainer") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onInitialize() {
         add(contentDataview);
         super.onInitialize();
      }
   };

   private ContentDataview contentDataview = new ContentDataview();

   private BootstrapPagingNavigator contentPagingNavigator = new BootstrapPagingNavigator("contentPagingNavigator", contentDataview);

   private ImageUploadPanel imageUploadPanel = new ImageUploadPanel("imageUploadPanel", new Model<Content>(new Content())) {

      private static final long serialVersionUID = -7913720234767091477L;

      @Override
      public void uploadedImage(AjaxRequestTarget target) {
         ((Product) ContentViewOrEditPanel.this.getDefaultModelObject()).getContents().add((Content) this.getDefaultModel().getObject());
         target.add(getPage());
      }
   };

   public ContentViewOrEditPanel(final String id, final IModel<Product> model) {
      super(id, model);
   }
}
