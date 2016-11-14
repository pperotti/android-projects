package com.pabloperotti.backup.in.the.cloud;


/**
 * Hold the information related with the selected google account
 */
public class AccountInfo {

	/**
	 * The google account entered
	 */
	private String selectedAccount = null;

	/**
	 * The token that permit this account to authenticate
	 */
	private String token = null;

	public static final String SCOPE = "oauth2:https://www.googleapis.com/auth/drive.file";

	public String getSelectedAccount() {
		return selectedAccount;
	}

	public void setSelectedAccount(String selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
