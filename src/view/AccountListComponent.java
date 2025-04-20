package view;

import domain.Account;
import view.general.Color;
import view.general.TableComponent;
import view.general.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class AccountListComponent extends Component {
	private final List<Account> accounts;

	public AccountListComponent(List<Account> accounts) {
		this.accounts = accounts;
	}

	@Override
	public List<String> format(int width) {
		List<List<Component>> table = new ArrayList<>();
		table.add(List.of(
			new TextComponent("Account number", TextComponent.Alignment.CENTER, Color.YELLOW),
			new TextComponent("Balance", TextComponent.Alignment.CENTER, Color.YELLOW),
			new TextComponent("Owner", TextComponent.Alignment.CENTER, Color.YELLOW)
		));
		for(Account account : accounts) {
			table.add(List.of(
				new TextComponent(account.getAccountNumber()),
				new TextComponent(account.getBalance().toString(), TextComponent.Alignment.CENTER),
				new TextComponent(account.getClient())
			));
		}
		List<Double> widths = TableComponent.calcWidths(List.of(19, 12, 0), width);
		TableComponent tableComponent = new TableComponent(table, widths, Color.YELLOW);
		return tableComponent.format(width);
	}
}
