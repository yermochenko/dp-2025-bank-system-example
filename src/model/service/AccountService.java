package model.service;

import domain.Account;
import domain.Transfer;
import exception.ApplicationException;
import model.repository.AccountRepository;
import model.repository.TransferRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountService {
	private AccountRepository accountRepository;
	private TransferRepository transferRepository;

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public void setTransferRepository(TransferRepository transferRepository) {
		this.transferRepository = transferRepository;
	}

	public List<Account> showAll() {
		return accountRepository.readAll();
	}

	public Optional<Account> showOne(String accountNumber) {
		Optional<Account> account = accountRepository.readByNumber(accountNumber);
		if(account.isPresent()) {
			List<Transfer> history = transferRepository.readByAccount(account.get().getId());
			for(Transfer transfer : history) {
				Account from = transfer.getFromAccount();
				if(from != null) {
					from = accountRepository.read(from.getId()).orElse(null);
					transfer.setFromAccount(from);
				}
				Account to = transfer.getToAccount();
				if(to != null) {
					to = accountRepository.read(to.getId()).orElse(null);
					transfer.setToAccount(to);
				}
			}
			account.get().setHistory(history);
		}
		return account;
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

	public void delete(String accountNumber) throws ApplicationException {
		Optional<Account> foundAccount = accountRepository.readByNumber(accountNumber);
		if(foundAccount.isPresent()) {
			Account account = foundAccount.get();
			if(account.isActive()) {
				if(account.getBalance() == 0) {
					List<Transfer> history = transferRepository.readByAccount(foundAccount.get().getId());
					try {
						if(history.isEmpty()) {
							accountRepository.delete(account.getId());
						} else {
							account.setActive(false);
							accountRepository.update(account);
						}
					} catch(IOException e) {
						throw new ApplicationException(e.getMessage());
					}
				} else {
					throw new ApplicationException("Account has non zero balance");
				}
			} else {
				throw new ApplicationException("Account is not active");
			}
		} else {
			throw new ApplicationException("Account not exists");
		}
	}
}
