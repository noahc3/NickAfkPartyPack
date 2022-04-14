package ml.noahc3.nickafkpartypack.Events;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import ml.noahc3.nickafkpartypack.Util.Constants;
import ml.noahc3.nickafkpartypack.Util.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.UUID;

public class EventListener implements Listener {

    private static int SlowTickTaskId;

    public static void init() {
        SlowTickTaskId = Constants.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(Constants.plugin, EventListener::SlowTick, 100, 10);
        Constants.plugin.getServer().getPluginManager().registerEvents(new EventListener(), Constants.plugin);
    }

    public static void deinit() {
        Constants.plugin.getServer().getScheduler().cancelTask(SlowTickTaskId);
    }

    public static void SlowTick() {
        long afkTime = Constants.config.getInt("auto-afk-time-seconds") * 1000L;
        long kickTime = Constants.config.getInt("afk-kick-time-seconds") * 1000L;

        for(UUID u : Constants.afkTimestamps.keySet()) {
            Player player = Bukkit.getPlayer(u);
            if (player == null) continue;

            if (player.getLocation().getYaw() != Constants.playerYaw.get(player.getUniqueId())
                    || player.getLocation().getPitch() != Constants.playerPitch.get(player.getUniqueId())) {
                updatePlayerStamps(player);
            }

            if (kickTime >= 0 && System.currentTimeMillis() > Constants.afkTimestamps.get(u) + kickTime) {
                player.kickPlayer("You have been AFK for too long.");
            } else if (afkTime >= 0 && System.currentTimeMillis() > Constants.afkTimestamps.get(u) + afkTime) {
                Tasks.setAfk(player, true);
            }
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        //async event, need to be on main thread to use bukkit apis.
        Constants.plugin.getServer().getScheduler().runTask(Constants.plugin, () -> {
            updatePlayerStamps(event.getPlayer());
        });
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PersistentDataContainer data = player.getPersistentDataContainer();
        data.remove(Constants.afkKey);

        updatePlayerStamps(player);

        try { //this string isnt marked as not nullable and idk why it would be null so imma not deal with it cause if its null then i dont care nothing to replace anyways right
            event.setJoinMessage(event.getJoinMessage().replace(WrappedGameProfile.fromPlayer(player).getName(), Tasks.getPlayerDisplayName(player)));
        } catch (NullPointerException ignored) { }
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PersistentDataContainer data = player.getPersistentDataContainer();
        data.remove(Constants.afkKey);

        clearPlayerStamps(player);

        try {
            event.setQuitMessage(event.getQuitMessage().replace(WrappedGameProfile.fromPlayer(player).getName(), Tasks.getPlayerDisplayName(player)));
        } catch (NullPointerException ignored) { }
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();

        PersistentDataContainer data = player.getPersistentDataContainer();
        data.remove(Constants.afkKey);

        clearPlayerStamps(player);

        try {
            event.setLeaveMessage(event.getLeaveMessage().replace(WrappedGameProfile.fromPlayer(player).getName(), Tasks.getPlayerDisplayName(player)));
        } catch (NullPointerException ignored) { }
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        PersistentDataContainer data = player.getPersistentDataContainer();
        data.remove(Constants.afkKey);

        updatePlayerStamps(player);

        try {
            event.setDeathMessage(event.getDeathMessage().replace(WrappedGameProfile.fromPlayer(player).getName(), Tasks.getPlayerDisplayName(player)));
        } catch (NullPointerException ignored) { }
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (! Tasks.isPlayerAfk(player)) return;

        PersistentDataContainer data = player.getPersistentDataContainer();
        data.remove(Constants.afkKey);

        updatePlayerStamps(player);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (! Tasks.isPlayerAfk(player)) return;

        PersistentDataContainer data = player.getPersistentDataContainer();
        data.remove(Constants.afkKey);

        updatePlayerStamps(player);
    }

    private static void updatePlayerStamps(Player player) {
        Constants.afkTimestamps.put(player.getUniqueId(), System.currentTimeMillis());
        Constants.playerYaw.put(player.getUniqueId(), player.getLocation().getYaw());
        Constants.playerPitch.put(player.getUniqueId(), player.getLocation().getPitch());
        Tasks.setAfk(player, false);
    }

    private static void clearPlayerStamps(Player player) {
        Constants.afkTimestamps.remove(player.getUniqueId());
        Constants.playerYaw.remove(player.getUniqueId());
        Constants.playerPitch.remove(player.getUniqueId());
    }
}
