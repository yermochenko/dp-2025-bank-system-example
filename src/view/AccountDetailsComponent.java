package view;

import domain.Account;
import domain.Transfer;
import view.general.Color;
import view.general.TableComponent;
import view.general.TextComponent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AccountDetailsComponent extends Component {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

	private final Account account;

	public AccountDetailsComponent(Account account) {
		this.account = account;
	}

	@Override
	public List<String> format(int width) {
		List<String> result = new ArrayList<>(new TextComponent("ACCOUNT DETAILS", Color.BLUE).format(Math.min(width, 15)));
		List<List<Component>> accountInfoTable = List.of(
			List.of(
				new TextComponent("Account number:", Color.YELLOW),
				new TextComponent(account.getAccountNumber())
			),
			List.of(
				new TextComponent("Client (owner):", Color.YELLOW),
				new TextComponent(account.getClient())
			),
			List.of(
				new TextComponent("Balance:", Color.YELLOW),
				new TextComponent(account.getBalance().toString())
			),
			List.of(
				new TextComponent("Active:", Color.YELLOW),
				new TextComponent(Boolean.toString(account.isActive()), account.isActive() ? Color.GREEN : Color.RED)
			)
		);
		int accountInfoTableWidth = Math.min(width, 40);
		List<Double> widths = TableComponent.calcWidths(List.of(15, 0), accountInfoTableWidth);
		result.addAll(new TableComponent(accountInfoTable, widths, Color.YELLOW).format(accountInfoTableWidth));
		result.add("");
		result.addAll(new TextComponent("TRANSACTIONS HISTORY", Color.BLUE).format(Math.min(width, 20)));
		List<Transfer> history = account.getHistory();
		if(!history.isEmpty()) {
			List<List<Component>> historyTable = new ArrayList<>();
			historyTable.add(List.of(
				new TextComponent("ID", TextComponent.Alignment.CENTER, Color.YELLOW),
				new TextComponent("Date", TextComponent.Alignment.CENTER, Color.YELLOW),
				new TextComponent("Amount", TextComponent.Alignment.CENTER, Color.YELLOW),
				new TextComponent("Type", TextComponent.Alignment.CENTER, Color.YELLOW),
				new TextComponent("Account", TextComponent.Alignment.CENTER, Color.YELLOW)
			));
			for(Transfer transfer : history) {
				Component type;
				Component otherAccount;
				if(transfer.getFromAccount() == null) {
					type = new TextComponent("income", TextComponent.Alignment.CENTER, Color.GREEN);
					otherAccount = new TextComponent("cash", TextComponent.Alignment.CENTER, Color.BLUE);
				} else if(transfer.getToAccount() == null) {
					type = new TextComponent("expense", TextComponent.Alignment.CENTER, Color.RED);
					otherAccount = new TextComponent("cash", TextComponent.Alignment.CENTER, Color.BLUE);
				} else {
					if(transfer.getFromAccount().getId().equals(account.getId())) {
						type = new TextComponent("expense", TextComponent.Alignment.CENTER, Color.RED);
						otherAccount = new TextComponent(transfer.getToAccount().getAccountNumber(), TextComponent.Alignment.CENTER, Color.BLUE);
					} else {
						type = new TextComponent("income", TextComponent.Alignment.CENTER, Color.GREEN);
						otherAccount = new TextComponent(transfer.getFromAccount().getAccountNumber(), TextComponent.Alignment.CENTER, Color.BLUE);
					}
				}
				historyTable.add(List.of(
					new TextComponent(transfer.getId().toString(), TextComponent.Alignment.CENTER),
					new TextComponent(DATE_FORMAT.format(transfer.getTransferDate())),
					new TextComponent(transfer.getAmount().toString(), TextComponent.Alignment.CENTER),
					type,
					otherAccount
				));
			}
			int historyTableWidth = Math.min(width, 65);
			List<Double> historyWidths = TableComponent.calcWidths(List.of(4, 17, 0, 7, 19), historyTableWidth);
			result.addAll(new TableComponent(historyTable, historyWidths, Color.YELLOW).format(historyTableWidth));
		} else {
			result.addAll(new MessageComponent("There are no any transactions yet", MessageComponent.Type.WARNING).format(width));
		}
		return result;
	}
}
