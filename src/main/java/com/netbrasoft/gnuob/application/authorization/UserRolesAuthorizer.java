package com.netbrasoft.gnuob.application.authorization;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;

public class UserRolesAuthorizer implements IRoleCheckingStrategy {

   @Override
   public boolean hasAnyRole(Roles roles) {
      RolesSession roleSession = (RolesSession) Session.get();
      return roleSession.hasAnyRole(roles);
   }
}
