package model.service;

import domain.Account;
import exception.ApplicationException;
import model.repository.AccountRepository;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
			Random random = new Random();
			String accountNumber;
			do {
				accountNumber = Stream.generate(() -> String.format("%04d", random.nextInt(10001) - 1)).limit(4).collect(Collectors.joining(" "));
			} while(accountRepository.readByNumber(accountNumber).isPresent());
			account.setAccountNumber(accountNumber);
			account.setBalance(0L);
			account.setActive(true);
			accountRepository.create(account);
		} catch(IOException e) {
			throw new ApplicationException(e.getMessage());
		}
	}
}
