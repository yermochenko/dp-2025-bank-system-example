package controller;

import exception.ApplicationException;
import model.service.AccountService;
import view.MessageComponent;
import view.general.Color;

import java.util.Scanner;

public class DeleteAccountMenuItem extends MenuItem {
	private AccountService accountService;

	public DeleteAccountMenuItem() {
		super("Delete Account", Color.RED);
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public boolean execute() {
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Enter account number: ");
		String accountNumber = keyboard.nextLine();
		if(!accountNumber.matches("\\d{4} \\d{4} \\d{4} \\d{4}")) {
			MessageComponent.showError("Entered account number has invalid format. Format must be \"XXXX XXXX XXXX XXXX\", where \"X\" is digit.");
			return true;
		}
		try {
			accountService.delete(accountNumber);
			MessageComponent.showInfo("Account deleted successfully.");
		} catch(ApplicationException e) {
			MessageComponent.showError(e.getMessage());
		}
		return true;
	}
}
