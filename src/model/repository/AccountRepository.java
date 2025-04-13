package model.repository;

import domain.Account;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AccountRepository {
	private final String fileName;
	private final Map<Long, Account> accountsById = new HashMap<>();
	private final Map<String, Account> accountsByNumber = new HashMap<>();

	public AccountRepository(String fileName) throws IOException {
		this.fileName = fileName;
		try(TsvFileReader reader = new TsvFileReader(fileName)) {
			List<String> line;
			for(int row = 1; (line = reader.readLine()) != null; row++) {
				if(line.size() == 4) {
					Account account = new Account();
					try {
						account.setId(Long.parseLong(line.get(0)));
					} catch(NumberFormatException e) {
						throw new IOException(String.format("File %s format error: row %d contains not integer ID \"%s\" in 1st column", fileName, row, line.get(0)));
					}
					account.setAccountNumber(line.get(1));
					account.setClient(line.get(2));
					try {
						account.setBalance(Long.valueOf(line.get(3)));
					} catch(NumberFormatException e) {
						throw new IOException(String.format("File %s format error: row %d contains not integer balance \"%s\" in 4th column", fileName, row, line.get(3)));
					}
					accountsById.put(account.getId(), account);
					accountsByNumber.put(account.getAccountNumber(), account);
				} else {
					throw new IOException(String.format("File %s format error: row %d contains %s columns instead of 4", fileName, row, line.size()));
				}
			}
		} catch(FileNotFoundException ignored) {}
	}

	public void create(Account account) throws IOException {
		Long id = accountsById.keySet().stream().max(Long::compareTo).orElse(0L) + 1;
		account.setId(id);
		accountsById.put(id, account);
		accountsByNumber.put(account.getAccountNumber(), account);
		save();
	}

	public Optional<Account> read(Long id) {
		return Optional.ofNullable(accountsById.get(id));
	}

	public Optional<Account> readByNumber(String accountNumber) {
		return Optional.ofNullable(accountsByNumber.get(accountNumber));
	}

	public List<Account> readAll() {
		return accountsById.values().stream()
				.sorted(Comparator.comparing(Account::getClient))
				.toList();
	}

	public void update(Account account) throws IOException {
		if(read(account.getId()).isPresent()) {
			accountsById.put(account.getId(), account);
			accountsByNumber.put(account.getAccountNumber(), account);
			save();
		}
	}

	public void delete(Long id) throws IOException {
		Optional<Account> account = read(id);
		if(account.isPresent()) {
			accountsById.remove(account.get().getId());
			accountsByNumber.remove(account.get().getAccountNumber());
			save();
		}
	}

	public void deleteByNumber(String number) throws IOException {
		Optional<Account> account = readByNumber(number);
		if(account.isPresent()) {
			accountsById.remove(account.get().getId());
			accountsByNumber.remove(account.get().getAccountNumber());
			save();
		}
	}

	private void save() throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
			for(Account account : accountsById.values()) {
				List<String> row = List.of(
					account.getId().toString(),
					account.getAccountNumber(),
					account.getClient(),
					account.getBalance().toString()
				);
				writer.write(String.join("\t", row));
				writer.newLine();
			}
		}
	}
}
