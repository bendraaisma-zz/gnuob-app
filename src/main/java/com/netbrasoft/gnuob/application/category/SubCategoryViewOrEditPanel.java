package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.SubCategory;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class SubCategoryViewOrEditPanel extends AbstractToolbar {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class SaveAjaxButton extends BootstrapAjaxButton {

    private static final long serialVersionUID = 2695394292963384938L;

    public SaveAjaxButton(Form<?> form) {
      super("save", Model.of(SubCategoryViewOrEditPanel.this.getString("saveMessage")), form, Buttons.Type.Primary);
      setSize(Buttons.Size.Small);
    }

    @Override
    protected void onError(AjaxRequestTarget target, Form<?> form) {
      form.add(new TooltipValidation());
      target.add(form);
      target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(SubCategoryViewOrEditPanel.this.getString("saveMessage")))));
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
      try {
        final SubCategory subCategory = (SubCategory) form.getDefaultModelObject();

        if (subCategory.getId() == 0) {
          // TODO: add new subCategory...
          // markupContainer.getDefaultModelObject()
        }

      } catch (final RuntimeException e) {
        LOGGER.warn(e.getMessage(), e);
        warn(e.getLocalizedMessage());
      } finally {
        target.add(markupContainer.setOutputMarkupId(true));
        target.add(form.setOutputMarkupId(true));
        target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(SubCategoryViewOrEditPanel.this.getString("saveMessage")))));
      }
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class SubCategoryEditFragement extends Fragment {

    private static final long serialVersionUID = 5133082553128798473L;

    private final SubCategoryContentPanel contentViewOrEditPanel;

    private final WebMarkupContainer subCategoryEditTable;

    public SubCategoryEditFragement() {
      super("subCategoryViewOrEditFragement", "subCategoryEditFragement", SubCategoryViewOrEditPanel.this, SubCategoryViewOrEditPanel.this.getDefaultModel());

      subCategoryEditTable = new WebMarkupContainer("subCategoryEditTable", getDefaultModel()) {

        private static final long serialVersionUID = 36890638168463585L;

        @Override
        protected void onInitialize() {
          final Form<SubCategory> subCategoryEditForm = new Form<SubCategory>("subCategoryEditForm");
          subCategoryEditForm.setModel(new CompoundPropertyModel<SubCategory>((IModel<SubCategory>) getDefaultModel()));
          subCategoryEditForm.add(new TextField<String>("name"));
          subCategoryEditForm.add(new TextArea<String>("description"));
          subCategoryEditForm.add(contentViewOrEditPanel.add(contentViewOrEditPanel.new SubCategoryContentEditFragement()).setOutputMarkupId(true));
          subCategoryEditForm.add(new SaveAjaxButton(subCategoryEditForm).setOutputMarkupId(true));
          add(subCategoryEditForm.setOutputMarkupId(true));
          add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
          add(new TableBehavior());
          super.onInitialize();
        }
      };
      contentViewOrEditPanel = new SubCategoryContentPanel("contentViewOrEditPanel", (IModel<SubCategory>) getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(subCategoryEditTable.setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class SubCategoryViewFragement extends Fragment {

    private static final long serialVersionUID = 5863708936560086113L;

    private final SubCategoryContentPanel contentViewOrEditPanel;

    private final WebMarkupContainer subCategoryViewTable;

    public SubCategoryViewFragement() {
      super("subCategoryViewOrEditFragement", "subCategoryViewFragement", SubCategoryViewOrEditPanel.this, SubCategoryViewOrEditPanel.this.getDefaultModel());

      subCategoryViewTable = new WebMarkupContainer("subCategoryViewTable", getDefaultModel()) {

        private static final long serialVersionUID = -1715737954826293137L;

        @Override
        protected void onInitialize() {

          final Form<SubCategory> subCategoryViewForm = new Form<SubCategory>("subCategoryViewForm");

          subCategoryViewForm.setModel(new CompoundPropertyModel<SubCategory>((IModel<SubCategory>) getDefaultModel()));
          subCategoryViewForm.add(new RequiredTextField<String>("name"));
          subCategoryViewForm.add(new Label("description"));
          subCategoryViewForm.add(contentViewOrEditPanel.add(contentViewOrEditPanel.new SubCategoryContentViewFragement()).setOutputMarkupId(true));
          add(subCategoryViewForm.setOutputMarkupId(true));
          add(new TableBehavior());
          super.onInitialize();
        }
      };
      contentViewOrEditPanel = new SubCategoryContentPanel("contentViewOrEditPanel", (IModel<SubCategory>) getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(subCategoryViewTable.setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(SubCategoryViewOrEditPanel.class);

  private static final long serialVersionUID = 3968615764565588442L;

  private final MarkupContainer markupContainer;

  public <T> SubCategoryViewOrEditPanel(final IModel<SubCategory> model, final DataTable<T, String> table, final MarkupContainer markupContainer) {
    super(model, table);
    this.markupContainer = markupContainer;
  }
}
