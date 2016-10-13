package com.hsd.zkoss.todo.ui.auth;

import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

import com.hsd.zkoss.todo.auth.AuthenticationService;
import com.hsd.zkoss.todo.auth.AuthenticationServiceImpl;
import com.hsd.zkoss.todo.auth.UserCredential;
import com.hsd.zkoss.todo.ui.composer.LoginComposer;

public class AuthenticationInit implements Initiator {

	private static Logger logger = Logger.getLogger(AuthenticationInit.class);
	
	// services
	private AuthenticationService authService = new AuthenticationServiceImpl();

	public void doInit(Page page, Map<String, Object> args) throws Exception {
		logger.info("Executions.getCurrent().getDesktop().getRequestPath():" + Executions.getCurrent().getDesktop().getRequestPath());
		
		UserCredential cre = authService.getUserCredential();
		if (cre == null || cre.isAnonymous()) {
			Executions.sendRedirect("/login.zul");
			return;
		} else if ("/index.zul".equals(Executions.getCurrent().getDesktop().getRequestPath())) {
			Executions.sendRedirect("/todos.zul");
			return;
		}
	}

}
