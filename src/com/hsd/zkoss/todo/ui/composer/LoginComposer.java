package com.hsd.zkoss.todo.ui.composer;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import com.hsd.zkoss.todo.auth.AuthenticationService;
import com.hsd.zkoss.todo.auth.AuthenticationServiceImpl;

public class LoginComposer extends SelectorComposer<Component> {

	private static Logger logger = Logger.getLogger(LoginComposer.class);
	
	// services
	private AuthenticationService authService = new AuthenticationServiceImpl();

	@Wire
	private Textbox accountText;

	@Wire
	private Textbox passwordText;

	@Wire
	private Button nextButton;

	@Listen("onClick = #nextButton")
	public void next() {
		logger.info("next_01 [account:" + accountText.getValue() + "] [password:" + passwordText.getValue() + "]");
		if (authService.login(accountText.getValue(), passwordText.getValue())) {
			Executions.sendRedirect("/todos.zul");
		}
	}
	
}
