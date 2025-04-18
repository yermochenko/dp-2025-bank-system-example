package model.repository;

import domain.Account;
import domain.Transfer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TransferRepository {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
	private final String fileName;
	private final Map<Long, Transfer> transfers = new LinkedHashMap<>();

	public TransferRepository(String fileName) throws IOException {
		this.fileName = fileName;
		try(TsvFileReader reader = new TsvFileReader(fileName)) {
			List<String> line;
			for(int row = 1; (line = reader.readLine()) != null; row++) {
				if(line.size() == 5) {
					Transfer transfer = new Transfer();
					try {
						transfer.setId(Long.parseLong(line.getFirst()));
					} catch(NumberFormatException e) {
						throw new IOException(String.format("File %s format error: row %d contains not integer ID \"%s\" in 1st column", fileName, row, line.getFirst()));
					}
					try {
						String id = line.get(1);
						if(!"null".equals(id)) {
							transfer.setFromAccount(new Account());
							transfer.getFromAccount().setId(Long.parseLong(id));
						}
					} catch(NumberFormatException e) {
						throw new IOException(String.format("File %s format error: row %d contains not integer ID \"%s\" in 2nd column", fileName, row, line.get(1)));
					}
					try {
						String id = line.get(2);
						if(!"null".equals(id)) {
							transfer.setToAccount(new Account());
							transfer.getToAccount().setId(Long.parseLong(id));
						}
					} catch(NumberFormatException e) {
						throw new IOException(String.format("File %s format error: row %d contains not integer ID \"%s\" in 3rd column", fileName, row, line.get(2)));
					}
					try {
						transfer.setTransferDate(DATE_FORMAT.parse(line.get(3)));
					} catch(ParseException e) {
						throw new IOException(String.format("File %s format error: row %d contains incorrect date \"%s\" in 4th column. Format \"%s\" required", fileName, row, line.get(3), DATE_FORMAT.toPattern()));
					}
					try {
						transfer.setAmount(Long.valueOf(line.get(4)));
					} catch(NumberFormatException e) {
						throw new IOException(String.format("File %s format error: row %d contains not integer amount of money \"%s\" in 5th column", fileName, row, line.get(4)));
					}
					transfers.put(transfer.getId(), transfer);
				} else {
					throw new IOException(String.format("File %s format error: row %d contains %s columns instead of 5", fileName, row, line.size()));
				}
			}
		} catch(FileNotFoundException ignored) {}
	}

	public void create(Transfer transfer) throws IOException {
		Long id = transfers.keySet().stream().max(Long::compareTo).orElse(0L) + 1;
		transfer.setId(id);
		transfers.put(id, transfer);
		save();
	}

	public Optional<Transfer> read(Long id) {
		return Optional.ofNullable(transfers.get(id));
	}

	public List<Transfer> readByAccount(Long accountId) {
		return transfers.values().stream()
				.filter(transfer -> {
					Account from = transfer.getFromAccount();
					Account to = transfer.getToAccount();
					return (from != null && accountId.equals(from.getId())) || (to != null && accountId.equals(to.getId()));
				})
				.sorted(Comparator.comparing(Transfer::getTransferDate))
				.toList();
	}

	private void save() throws IOException {
		try(TsvFileWriter writer = new TsvFileWriter(fileName)) {
			for(Transfer transfer : transfers.values()) {
				List<String> row = List.of(
					transfer.getId().toString(),
					transfer.getFromAccount() != null ? transfer.getFromAccount().getId().toString() : "null",
					transfer.getToAccount() != null ? transfer.getToAccount().getId().toString() : "null",
					DATE_FORMAT.format(transfer.getTransferDate()),
					transfer.getAmount().toString()
				);
				writer.writeLine(row);
			}
		}
	}
}
