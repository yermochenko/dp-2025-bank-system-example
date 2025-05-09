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

	public void transfer(
		String srcAccountNumber,
		String destAccountNumber,
		long amount
	) throws ApplicationException {
		Optional<Account> foundSrcAccount = accountRepository.readByNumber(srcAccountNumber);
		if(foundSrcAccount.isEmpty()) {
			throw new ApplicationException("Source account not found");
		}
		Account srcAccount = foundSrcAccount.get();
		if(!srcAccount.isActive()) {
			throw new ApplicationException("Source account was blocked or closed");
		}
		Optional<Account> foundDestAccount = accountRepository.readByNumber(destAccountNumber);
		if(foundDestAccount.isEmpty()) {
			throw new ApplicationException("Destination account not found");
		}
		Account destAccount = foundDestAccount.get();
		if(!destAccount.isActive()) {
			throw new ApplicationException("Destination account was blocked or closed");
		}
		if(amount > srcAccount.getBalance()) {
			throw new ApplicationException("Source account has insufficient funds");
		}
		try {
			srcAccount.setBalance(srcAccount.getBalance() - amount);
			accountRepository.update(srcAccount);
			destAccount.setBalance(destAccount.getBalance() + amount);
			accountRepository.update(destAccount);
			Transfer transfer = new Transfer();
			transfer.setFromAccount(srcAccount);
			transfer.setToAccount(destAccount);
			transfer.setAmount(amount);
			transfer.setTransferDate(new Date());
			transferRepository.create(transfer);
		} catch(IOException e) {
			throw new ApplicationException(e.getMessage());
		}
	}
}
