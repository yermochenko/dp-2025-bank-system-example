package model.service;

import domain.Account;
import domain.Transfer;
import exception.ApplicationException;
import model.repository.AccountRepository;
import model.repository.TransferRepository;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class TransferService {
	private AccountRepository accountRepository;
	private TransferRepository transferRepository;

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public void setTransferRepository(TransferRepository transferRepository) {
		this.transferRepository = transferRepository;
	}

	public void creditFunds(String accountNumber, long amount) throws ApplicationException {
		Optional<Account> foundAccount = accountRepository.readByNumber(accountNumber);
		if(foundAccount.isPresent()) {
			Account account = foundAccount.get();
			if(account.isActive()) {
				try {
					account.setBalance(account.getBalance() + amount);
					accountRepository.update(account);
					Transfer transfer = new Transfer();
					transfer.setFromAccount(null);
					transfer.setToAccount(account);
					transfer.setAmount(amount);
					transfer.setTransferDate(new Date());
					transferRepository.create(transfer);
				} catch(IOException e) {
					throw new ApplicationException(e.getMessage());
				}
			} else {
				throw new ApplicationException("Account was blocked or closed");
			}
		} else {
			throw new ApplicationException("Account not found");
		}
	}
}
