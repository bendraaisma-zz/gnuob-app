package com.netbrasoft.gnuob.application.category;

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
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class CategoryViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CancelAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel", Model.of(CategoryViewOrEditPanel.this.getString("cancelMessage")), Buttons.Type.Default, Model.of(CategoryViewOrEditPanel.this.getString("cancelMessage")));
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         CategoryViewOrEditPanel.this.removeAll();
         CategoryViewOrEditPanel.this.add(new CategoryViewFragement()).setOutputMarkupId(true);

         if (((Category) CategoryViewOrEditPanel.this.getDefaultModelObject()).getId() > 0) {
            CategoryViewOrEditPanel.this.setDefaultModelObject(categoryDataProvider.findById((Category) CategoryViewOrEditPanel.this.getDefaultModelObject()));
         }

         target.add(target.getPage());
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CategoryEditFragement extends Fragment {

      private static final long serialVersionUID = 5133082553128798473L;

      private final CategoryContentPanel contentViewOrEditPanel;

      private final SubCategoryPanel subCategoryViewOrEditPanel ;

      private final WebMarkupContainer categoryEditTable;

      public CategoryEditFragement() {
         super("categoryViewOrEditFragement", "categoryEditFragement", CategoryViewOrEditPanel.this, CategoryViewOrEditPanel.this.getDefaultModel());

         categoryEditTable = new WebMarkupContainer("categoryEditTable", getDefaultModel()) {

            private static final long serialVersionUID = 36890638168463585L;

            @Override
            protected void onInitialize() {
               final Form<Category> categoryEditForm = new Form<Category>("categoryEditForm");
               categoryEditForm.setModel(new CompoundPropertyModel<Category>((IModel<Category>) getDefaultModel()));
               categoryEditForm.add(new NumberTextField<Integer>("position"));
               categoryEditForm.add(new TextField<String>("name"));
               categoryEditForm.add(new TextArea<String>("description"));
               categoryEditForm.add(contentViewOrEditPanel.add(contentViewOrEditPanel.new CategoryContentEditFragement()).setOutputMarkupId(true));
               categoryEditForm.add(subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new SubCategoryEditFragement()).setOutputMarkupId(true));
               add(categoryEditForm.setOutputMarkupId(true));
               add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
               add(new CancelAjaxLink().setOutputMarkupId(true));
               add(new SaveAjaxButton(categoryEditForm).setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
         contentViewOrEditPanel = new CategoryContentPanel("contentViewOrEditPanel", (IModel<Category>) getDefaultModel());
         subCategoryViewOrEditPanel = new SubCategoryPanel("subCategoryViewOrEditPanel", (IModel<Category>) getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(categoryEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class CategoryViewFragement extends Fragment {

      private static final long serialVersionUID = 5863708936560086113L;

      private final CategoryContentPanel contentViewOrEditPanel;

      private final SubCategoryPanel subCategoryViewOrEditPanel;

      private final WebMarkupContainer categoryViewTable;

      public CategoryViewFragement() {
         super("categoryViewOrEditFragement", "categoryViewFragement", CategoryViewOrEditPanel.this, CategoryViewOrEditPanel.this.getDefaultModel());

         categoryViewTable = new WebMarkupContainer("categoryViewTable", getDefaultModel()) {

            private static final long serialVersionUID = -1715737954826293137L;

            @Override
            protected void onInitialize() {
               final Form<Category> categoryViewForm = new Form<Category>("categoryViewForm");
               categoryViewForm.setModel(new CompoundPropertyModel<Category>((IModel<Category>) getDefaultModel()));
               categoryViewForm.add(new NumberTextField<Integer>("position"));
               categoryViewForm.add(new RequiredTextField<String>("name"));
               categoryViewForm.add(new Label("description"));
               categoryViewForm.add(contentViewOrEditPanel.add(contentViewOrEditPanel.new CategoryContentViewFragement()).setOutputMarkupId(true));
               categoryViewForm.add(subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new SubCategoryViewFragement()).setOutputMarkupId(true));
               add(new EditAjaxLink().setOutputMarkupId(true));
               add(categoryViewForm.setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
         contentViewOrEditPanel = new CategoryContentPanel("contentViewOrEditPanel", (IModel<Category>) getDefaultModel());
         subCategoryViewOrEditPanel = new SubCategoryPanel("subCategoryViewOrEditPanel", (IModel<Category>) getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(categoryViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class EditAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit", Model.of(CategoryViewOrEditPanel.this.getString("editMessage")), Buttons.Type.Primary, Model.of(CategoryViewOrEditPanel.this.getString("editMessage")));
         setIconType(GlyphIconType.edit);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         CategoryViewOrEditPanel.this.removeAll();
         CategoryViewOrEditPanel.this.add(new CategoryEditFragement().setOutputMarkupId(true));
         target.add(CategoryViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends BootstrapAjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", Model.of(CategoryViewOrEditPanel.this.getString("saveAndCloseMessage")), form, Buttons.Type.Primary);
         setSize(Buttons.Size.Small);
      }

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
         form.add(new TooltipValidation());
         target.add(form);
         target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(CategoryViewOrEditPanel.this.getString("saveAndCloseMessage")))));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            final Category category = (Category) form.getDefaultModelObject();
            category.setActive(true);

            if (category.getId() == 0) {
               CategoryViewOrEditPanel.this.setDefaultModel(Model.of(categoryDataProvider.findById(categoryDataProvider.persist(category))));;
            } else {
               CategoryViewOrEditPanel.this.setDefaultModel(Model.of(categoryDataProvider.findById(categoryDataProvider.merge(category))));
            }

            CategoryViewOrEditPanel.this.removeAll();
            CategoryViewOrEditPanel.this.add(new CategoryViewFragement().setOutputMarkupId(true));
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(target.getPage());
         }
      }
   }

   private static final Logger LOGGER = LoggerFactory.getLogger(CategoryViewOrEditPanel.class);

   private static final long serialVersionUID = 3968615764565588442L;

   @SpringBean(name = "CategoryDataProvider", required = true)
   private GenericTypeDataProvider<Category> categoryDataProvider;

   public CategoryViewOrEditPanel(String id, IModel<Category> model) {
      super(id, model);
   }

   @Override
   protected void onInitialize() {
      categoryDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      categoryDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      categoryDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      categoryDataProvider.setType(new Category());
      categoryDataProvider.getType().setActive(true);
      super.onInitialize();
   }
}
