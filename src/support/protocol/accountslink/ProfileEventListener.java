package support.protocol.accountslink;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import protocolsupport.api.events.PlayerProfileCompleteEvent;

public class ProfileEventListener implements Listener {

	protected final Storage storage;
	public ProfileEventListener(Storage storage) {
		this.storage = storage;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onProfileComplete(PlayerProfileCompleteEvent event) {
		storage.getMainAccount(event.getForcedUUID() != null ? event.getForcedUUID() : event.getConnection().getProfile().getUUID())
		.ifPresent(event::setForcedUUID);
	}

}
