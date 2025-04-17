package controller;

import exception.ApplicationException;
import model.service.TransferService;
import view.MessageComponent;
import view.general.Color;

import java.util.Scanner;

public class TransferMoneyMenuItem extends MenuItem {
	private TransferService transferService;

	public TransferMoneyMenuItem() {
		super("Transfer Money", Color.YELLOW);
	}

	public void setTransferService(TransferService transferService) {
		this.transferService = transferService;
	}

	@Override
	public boolean execute() throws ApplicationException {
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Enter source account number: ");
		String srcAccountNumber = keyboard.nextLine();
		if(!srcAccountNumber.matches("\\d{4} \\d{4} \\d{4} \\d{4}")) {
			MessageComponent.showError("Entered account number has invalid format. Format must be \"XXXX XXXX XXXX XXXX\", where \"X\" is digit.");
			return true;
		}
		System.out.print("Enter destination account number: ");
		String destAccountNumber = keyboard.nextLine();
		if(!destAccountNumber.matches("\\d{4} \\d{4} \\d{4} \\d{4}")) {
			MessageComponent.showError("Entered account number has invalid format. Format must be \"XXXX XXXX XXXX XXXX\", where \"X\" is digit.");
			return true;
		}
		System.out.print("Enter money amount: ");
		long amount;
		try {
			amount = Long.parseLong(keyboard.nextLine());
		} catch(NumberFormatException e) {
			MessageComponent.showError("Entered money amount is not integer");
			return true;
		}
		if(amount <= 0) {
			MessageComponent.showError("Entered money amount is not positive");
			return true;
		}
		transferService.transfer(srcAccountNumber, destAccountNumber, amount);
		MessageComponent.showSuccess("Operation successful");
		return true;
	}
}
