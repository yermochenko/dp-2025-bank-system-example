package model.service;

import domain.Account;
import exception.ApplicationException;
import model.repository.AccountRepository;

import java.io.IOException;
import java.util.List;

public class AccountService {
	private AccountRepository accountRepository;

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public List<Account> showAll() {
		return accountRepository.readAll();
	}

	public void create(Account account) throws ApplicationException {
		try {
			accountRepository.create(account);
		} catch(IOException e) {
			throw new ApplicationException(e.getMessage());
		}
	}
}
