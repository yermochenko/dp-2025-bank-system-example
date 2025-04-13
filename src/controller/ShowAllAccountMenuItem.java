package controller;

import domain.Account;
import model.service.AccountService;
import view.AccountListComponent;
import view.MessageComponent;
import view.general.Color;

import java.util.List;

public class ShowAllAccountMenuItem extends MenuItem {
	private AccountService accountService;

	public ShowAllAccountMenuItem() {
		super("Show All Accounts", Color.GREEN);
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public boolean execute() {
		List<Account> accounts = accountService.showAll();
		if(!accounts.isEmpty()) {
			AccountListComponent accountListComponent = new AccountListComponent(accounts);
			accountListComponent.print();
		} else {
			MessageComponent.showWarning("There are no accounts in the system yet");
		}
		return true;
	}
}
