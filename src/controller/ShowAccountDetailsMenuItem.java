package controller;

import domain.Account;
import model.service.AccountService;
import view.AccountDetailsComponent;
import view.MessageComponent;
import view.general.Color;

import java.util.Optional;
import java.util.Scanner;

public class ShowAccountDetailsMenuItem extends MenuItem {
	private AccountService accountService;

	public ShowAccountDetailsMenuItem() {
		super("Show Account Detail", Color.GREEN);
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
		Optional<Account> foundAccount = accountService.showOne(accountNumber);
		if(foundAccount.isPresent()) {
			AccountDetailsComponent accountDetailsComponent = new AccountDetailsComponent(foundAccount.get());
			accountDetailsComponent.print();
		} else {
			MessageComponent.showWarning("There is no account with the specified account number");
		}
		return true;
	}
}
