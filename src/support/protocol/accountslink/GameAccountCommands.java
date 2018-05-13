package support.protocol.accountslink;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameAccountCommands implements CommandExecutor {

	protected final AccountsLink plugin;
	protected final Storage storage;
	public GameAccountCommands(AccountsLink plugin, Storage storage) {
		this.plugin = plugin;
		this.storage = storage;
	}

	protected final HashMap<String, UUID> codeToUUID = new HashMap<>();
	protected final HashMap<UUID, String> uuidToCode = new HashMap<>();

	protected String generateCode(UUID uuid) {
		if (uuidToCode.containsKey(uuid)) {
			removeCode(uuid);
		}
		String randomCode = UUID.randomUUID().toString();
		uuidToCode.put(uuid, randomCode);
		codeToUUID.put(randomCode, uuid);
		Bukkit.getScheduler().runTaskLater(plugin, () -> removeCode(uuid), TimeUnit.MINUTES.toSeconds(2) * 20);
		return randomCode;
	}

	protected void removeCode(UUID uuid) {
		codeToUUID.remove(uuidToCode.remove(uuid));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.GREEN + "ProtocolSupportAccountsLink");
			//TODO: help
			return true;
		}
		if (!Player.class.isInstance(sender)) {
			sender.sendMessage(ChatColor.DARK_RED + "This command is only avilable to players");
			return true;
		}
		Player player = (Player) sender;
		switch (args[0].toLowerCase()) {
			case ("code"): {
				sender.sendMessage("Use this code for linking alts to this account, code is valid for 2 minutes: " + generateCode(player.getUniqueId()));
				break;
			}
			case ("link"): {
				if (args.length < 2) {
					sender.sendMessage(ChatColor.DARK_RED + "Code is needed to use this command");
				} else {
					String code = args[1];
					UUID mainAccountUUID = codeToUUID.get(code);
					if (mainAccountUUID == null) {
						sender.sendMessage(ChatColor.DARK_RED + "This code is invalid");
					} else {
						try {
							storage.addAltAccount(mainAccountUUID, player.getUniqueId());
							removeCode(mainAccountUUID);
							sender.sendMessage(ChatColor.GREEN + "Alt account added, you can now relog");
						} catch (Exception e) {
							sender.sendMessage(ChatColor.DARK_RED + "Unable to add alt account: " + e.getMessage());
						}
					}
				}
				break;
			}
		}
		return true;
	}

}
