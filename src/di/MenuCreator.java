package di;

import controller.*;
import exception.ApplicationException;
import model.repository.AccountRepository;
import model.repository.TransferRepository;
import model.service.AccountService;
import model.service.TransferService;

import java.io.IOException;
import java.util.List;

public class MenuCreator {
	public static final String ACCOUNT_FILE_NAME = "accounts.tsv";
	public static final String TRANSFER_FILE_NAME = "transfers.tsv";

	public static List<MenuItem> createMenu() throws ApplicationException {
		AccountRepository accountRepository;
		TransferRepository transferRepository;
		try {
			accountRepository = new AccountRepository(ACCOUNT_FILE_NAME);
		} catch(IOException e) {
			throw new ApplicationException("Error while reading file " + ACCOUNT_FILE_NAME);
		}
		try {
			transferRepository = new TransferRepository(TRANSFER_FILE_NAME);
		} catch(IOException e) {
			throw new ApplicationException("Error while reading file " + TRANSFER_FILE_NAME);
		}

		AccountService accountService = new AccountService();
		accountService.setAccountRepository(accountRepository);
		TransferService transferService = new TransferService();
		transferService.setAccountRepository(accountRepository);
		transferService.setTransferRepository(transferRepository);

		ShowAllAccountMenuItem showAllAccountMenuItem = new ShowAllAccountMenuItem();
		showAllAccountMenuItem.setAccountService(accountService);
		AddNewAccountMenuItem addNewAccountMenuItem = new AddNewAccountMenuItem();
		addNewAccountMenuItem.setAccountService(accountService);
		CreditFundsToAccountMenuItem creditFundsToAccountMenuItem = new CreditFundsToAccountMenuItem();
		creditFundsToAccountMenuItem.setTransferService(transferService);
		ExitMenuItem exitMenuItem = new ExitMenuItem();

		return List.of(
			showAllAccountMenuItem,
			addNewAccountMenuItem,
			creditFundsToAccountMenuItem,
			exitMenuItem
		);
	}
}
