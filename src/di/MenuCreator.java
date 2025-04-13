package di;

import controller.AddNewAccountMenuItem;
import controller.ExitMenuItem;
import controller.MenuItem;
import exception.ApplicationException;
import model.repository.AccountRepository;
import model.service.AccountService;

import java.io.IOException;
import java.util.List;

public class MenuCreator {
	public static final String ACCOUNT_FILE_NAME = "accounts.tsv";

	public static List<MenuItem> createMenu() throws ApplicationException {
		AccountRepository accountRepository;
		try {
			accountRepository = new AccountRepository(ACCOUNT_FILE_NAME);
		} catch(IOException e) {
			throw new ApplicationException("Error while reading file " + ACCOUNT_FILE_NAME);
		}

		AccountService accountService = new AccountService();
		accountService.setAccountRepository(accountRepository);

		AddNewAccountMenuItem addNewAccountMenuItem = new AddNewAccountMenuItem();
		addNewAccountMenuItem.setAccountService(accountService);
		ExitMenuItem exitMenuItem = new ExitMenuItem();

		return List.of(
			addNewAccountMenuItem,
			exitMenuItem
		);
	}
}
