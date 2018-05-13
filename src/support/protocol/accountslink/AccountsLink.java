package support.protocol.accountslink;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import protocolsupport.api.ProtocolSupportAPI;

public class AccountsLink extends JavaPlugin {

	protected static final String storage_file = "storage.yml";

	private final Storage storage = new Storage();

	@Override
	public void onEnable() {
		if (ProtocolSupportAPI.getAPIVersion().compareTo(BigInteger.valueOf(5)) < 0) {
			throw new IllegalStateException("ProtocolSupport api version is less than required");
		}
		storage.load(YamlConfiguration.loadConfiguration(new File(getDataFolder(), storage_file)));
		getServer().getScheduler().runTaskTimer(this, this::saveStorage, 0, TimeUnit.MINUTES.toSeconds(5) * 20);
		getServer().getPluginManager().registerEvents(new ProfileEventListener(storage), this);
		getCommand("accountslink").setExecutor(new GameAccountCommands(this, storage));
	}

	@Override
	public void onDisable() {
		saveStorage();
	}

	protected void saveStorage() {
		YamlConfiguration config = new YamlConfiguration();
		storage.save(config);
		try {
			config.save(new File(getDataFolder(), storage_file));
		} catch (IOException e) {
			System.err.println("Unable to save storage");
			e.printStackTrace();
		}
	}

}
