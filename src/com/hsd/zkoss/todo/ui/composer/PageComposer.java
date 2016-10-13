package com.hsd.zkoss.todo.ui.composer;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;

import com.hsd.zkoss.todo.auth.AuthenticationService;
import com.hsd.zkoss.todo.auth.AuthenticationServiceImpl;

public abstract class PageComposer extends SelectorComposer<Component> {

	private static Logger logger = Logger.getLogger(PageComposer.class);
	
	// services
	private AuthenticationService authService = new AuthenticationServiceImpl();

	@Listen("onClick = #logoutButton")
	public void logout() {
		logger.info("logout()");
		authService.logout();
		Executions.sendRedirect("/index.zul");
	}
	
}
