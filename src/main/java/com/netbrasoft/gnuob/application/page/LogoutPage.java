package com.netbrasoft.gnuob.application.page;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;

import com.netbrasoft.gnuob.application.authorization.RolesSession;

public class LogoutPage extends WebPage {

	private static final long serialVersionUID = 9159077355372212288L;

	public LogoutPage() {
		RolesSession roleSession = (RolesSession) Session.get();
		roleSession.logout();
	}
}
