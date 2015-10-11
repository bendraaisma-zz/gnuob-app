package com.netbrasoft.gnuob.application.security.user;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
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

import com.netbrasoft.gnuob.api.Rule;
import com.netbrasoft.gnuob.api.User;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.ADMINISTRATOR, AppRoles.MANAGER})
public class UserViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.ADMINISTRATOR})
  class CancelAjaxLink extends AjaxLink<Void> {

    private static final long serialVersionUID = 4267535261864907719L;

    public CancelAjaxLink() {
      super("cancel");
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      UserViewOrEditPanel.this.removeAll();
      UserViewOrEditPanel.this.add(new UserViewFragement()).setOutputMarkupId(true);
      target.add(target.getPage());
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.ADMINISTRATOR})
  class EditAjaxLink extends AjaxLink<Void> {

    private static final long serialVersionUID = 4267535261864907719L;

    public EditAjaxLink() {
      super("edit");
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
      UserViewOrEditPanel.this.removeAll();
      UserViewOrEditPanel.this.add(new UserEditFragement().setOutputMarkupId(true));
      target.add(UserViewOrEditPanel.this);
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
        User user = (User) form.getDefaultModelObject();

        if (user.getId() == 0) {
          userDataProvider.persist(user);
        } else {
          userDataProvider.merge(user);
        }

        UserViewOrEditPanel.this.removeAll();
        UserViewOrEditPanel.this.add(new UserViewFragement().setOutputMarkupId(true));
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

  class UserEditFragement extends Fragment {

    private static final long serialVersionUID = -8347518285280404106L;

    public UserEditFragement() {
      super("userViewOrEditFragement", "userEditFragement", UserViewOrEditPanel.this, UserViewOrEditPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      GroupViewOrEditPanel groupViewOrEditPanel = new GroupViewOrEditPanel("groupViewOrEditPanel", (IModel<User>) getDefaultModel());
      SiteViewOrEditPanel siteViewOrEditPanel = new SiteViewOrEditPanel("siteViewOrEditPanel", (IModel<User>) getDefaultModel());
      Form<User> userEditForm = new Form<User>("userEditForm");

      userEditForm.setModel(new CompoundPropertyModel<User>((IModel<User>) getDefaultModel()));
      userEditForm.add(new BootstrapSelect<Rule>("access", Arrays.asList(Rule.values())));
      userEditForm.add(new TextField<String>("name"));
      userEditForm.add(new PasswordTextField("password"));
      userEditForm.add(new PasswordTextField("cpassword", Model.of("")));
      userEditForm.add(new TextField<String>("role"));
      userEditForm.add(new TextArea<String>("description"));

      add(userEditForm.setOutputMarkupId(true));
      add(groupViewOrEditPanel.add(groupViewOrEditPanel.new GroupEditFragement()).setOutputMarkupId(true));
      add(siteViewOrEditPanel.add(siteViewOrEditPanel.new SiteEditFragement()).setOutputMarkupId(true));
      add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
      add(new CancelAjaxLink().setOutputMarkupId(true));
      add(new SaveAjaxButton(userEditForm).setOutputMarkupId(true));

      super.onInitialize();
    }
  }

  class UserViewFragement extends Fragment {

    private static final long serialVersionUID = -8347518285280404106L;

    public UserViewFragement() {
      super("userViewOrEditFragement", "userViewFragement", UserViewOrEditPanel.this, UserViewOrEditPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      GroupViewOrEditPanel groupViewOrEditPanel = new GroupViewOrEditPanel("groupViewOrEditPanel", (IModel<User>) getDefaultModel());
      SiteViewOrEditPanel siteViewOrEditPanel = new SiteViewOrEditPanel("siteViewOrEditPanel", (IModel<User>) getDefaultModel());
      Form<User> userViewForm = new Form<User>("userViewForm");

      userViewForm.setModel(new CompoundPropertyModel<User>((IModel<User>) getDefaultModel()));
      userViewForm.add(new Label("access"));
      userViewForm.add(new Label("name"));
      userViewForm.add(new Label("role"));
      userViewForm.add(new Label("description"));

      add(new EditAjaxLink().setOutputMarkupId(true));
      add(groupViewOrEditPanel.add(groupViewOrEditPanel.new GroupViewFragement()).setOutputMarkupId(true));
      add(siteViewOrEditPanel.add(siteViewOrEditPanel.new SiteViewFragement()).setOutputMarkupId(true));
      add(userViewForm.setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(UserViewOrEditPanel.class);

  private static final long serialVersionUID = 7692943659495394940L;

  @SpringBean(name = "UserDataProvider", required = true)
  private GenericTypeDataProvider<User> userDataProvider;

  public UserViewOrEditPanel(final String id, final IModel<User> model) {
    super(id, model);
  }
}
