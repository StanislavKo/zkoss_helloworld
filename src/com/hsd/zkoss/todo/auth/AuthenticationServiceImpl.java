package com.hsd.zkoss.todo.auth;

import java.io.Serializable;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

public class AuthenticationServiceImpl implements AuthenticationService, Serializable {

	@Override
	public boolean login(String account, String password) {
		if (account == null || !"asdf".equals(account) || !"qwer".equals(password)) {
			return false;
		}

		Session sess = Sessions.getCurrent();
		UserCredential cre = new UserCredential(account, "Asdf asdf");
		sess.setAttribute("userCredential", cre);

		return true;
	}

	@Override
	public void logout() {
		Session sess = Sessions.getCurrent();
		sess.removeAttribute("userCredential");
	}

	@Override
	public UserCredential getUserCredential() {
		Session sess = Sessions.getCurrent();
		UserCredential cre = (UserCredential) sess.getAttribute("userCredential");
		if (cre == null) {
			cre = new UserCredential();// new a anonymous user and set to
										// session
			sess.setAttribute("userCredential", cre);
		}
		return cre;
	}

}
