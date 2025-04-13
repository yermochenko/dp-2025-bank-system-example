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
			new TextComponent("Owner", TextComponent.Alignment.CENTER, Color.YELLOW),
			new TextComponent("Balance", TextComponent.Alignment.CENTER, Color.YELLOW)
		));
		for(Account account : accounts) {
			table.add(List.of(
				new TextComponent(account.getAccountNumber()),
				new TextComponent(account.getClient()),
				new TextComponent(account.getBalance().toString(), TextComponent.Alignment.CENTER)
			));
		}
		TableComponent tableComponent = new TableComponent(table, List.of(0.2, 0.5, 0.3), Color.YELLOW);
		return tableComponent.format(width);
	}
}
