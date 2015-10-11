package com.netbrasoft.gnuob.application.security.group;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Group;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.ADMINISTRATOR, AppRoles.MANAGER})
public class GroupViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.ADMINISTRATOR})
  class CancelAjaxLink extends AjaxLink<Void> {

    private static final long serialVersionUID = 4267535261864907719L;

    public CancelAjaxLink() {
      super("cancel");
    }

    @Override
    public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
      GroupViewOrEditPanel.this.removeAll();
      GroupViewOrEditPanel.this.add(new GroupViewFragement()).setOutputMarkupId(true);
      paramAjaxRequestTarget.add(paramAjaxRequestTarget.getPage());
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.ADMINISTRATOR})
  class EditAjaxLink extends AjaxLink<Void> {

    private static final long serialVersionUID = 4267535261864907719L;

    public EditAjaxLink() {
      super("edit");
    }

    @Override
    public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
      GroupViewOrEditPanel.this.removeAll();
      GroupViewOrEditPanel.this.add(new GroupEditFragement().setOutputMarkupId(true));
      paramAjaxRequestTarget.add(GroupViewOrEditPanel.this);
    }
  }

  class GroupEditFragement extends Fragment {

    private static final long serialVersionUID = 8971798392355786447L;

    public GroupEditFragement() {
      super("groupViewOrEditFragement", "groupEditFragement", GroupViewOrEditPanel.this, GroupViewOrEditPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      Form<Group> groupEditForm = new Form<Group>("groupEditForm");

      groupEditForm.setModel(new CompoundPropertyModel<Group>((IModel<Group>) getDefaultModel()));
      groupEditForm.add(new TextField<String>("name"));
      groupEditForm.add(new TextArea<String>("description"));

      add(groupEditForm.setOutputMarkupId(true));
      add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
      add(new SaveAjaxButton(groupEditForm).setOutputMarkupId(true));
      add(new CancelAjaxLink().setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  class GroupViewFragement extends Fragment {

    private static final long serialVersionUID = 498703216819003839L;

    public GroupViewFragement() {
      super("groupViewOrEditFragement", "groupViewFragement", GroupViewOrEditPanel.this, GroupViewOrEditPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      Form<Group> groupViewForm = new Form<Group>("groupViewForm");

      groupViewForm.setModel(new CompoundPropertyModel<Group>((IModel<Group>) getDefaultModel()));
      groupViewForm.add(new Label("name"));
      groupViewForm.add(new Label("description"));

      add(new EditAjaxLink());
      add(groupViewForm.setOutputMarkupId(true));
      super.onInitialize();
    }

  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.ADMINISTRATOR})
  class SaveAjaxButton extends AjaxButton {

    private static final long serialVersionUID = 2695394292963384938L;

    public SaveAjaxButton(Form<?> form) {
      super("save", form);
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
      try {
        Group group = (Group) form.getDefaultModelObject();

        if (group.getId() == 0) {
          group.setActive(true);

          groupDataProvider.persist(group);
        } else {
          groupDataProvider.merge(group);
        }

        GroupViewOrEditPanel.this.removeAll();
        GroupViewOrEditPanel.this.add(new GroupViewFragement().setOutputMarkupId(true));
      } catch (RuntimeException e) {
        LOGGER.warn(e.getMessage(), e);

        String[] messages = e.getMessage().split(": ");
        String message = messages[messages.length - 1];

        warn(message.substring(0, 1).toUpperCase() + message.substring(1));
      } finally {
        target.add(target.getPage());
      }
    }
  }

  private static final long serialVersionUID = -8401960249843479048L;

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupViewOrEditPanel.class);

  @SpringBean(name = "GroupDataProvider", required = true)
  private GenericTypeDataProvider<Group> groupDataProvider;

  public GroupViewOrEditPanel(final String id, final IModel<Group> model) {
    super(id, model);
  }
}
