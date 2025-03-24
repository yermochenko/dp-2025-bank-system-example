package model.service;

import domain.Account;
import model.repository.AccountRepository;

import java.util.List;

public class AccountService {
	private AccountRepository accountRepository;

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public List<Account> showAll() {
		return accountRepository.readAll();
	}
}
