package controller;

import domain.Account;
import exception.ApplicationException;
import model.service.AccountService;
import view.MessageComponent;
import view.general.Color;

import java.util.Scanner;

public class AddNewAccountMenuItem extends MenuItem {
	private AccountService accountService;

	public AddNewAccountMenuItem() {
		super("Add New Account", Color.YELLOW);
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public boolean execute() throws ApplicationException {
		Scanner keyboard = new Scanner(System.in);
		Account account = new Account();
		System.out.print("Enter client name (owner of new account): ");
		String client = keyboard.nextLine();
		if(client.isBlank()) throw new ApplicationException("Client name can't be empty");
		account.setClient(client);
		accountService.create(account);
		MessageComponent.showSuccess("New account added");
		return true;
	}
}
