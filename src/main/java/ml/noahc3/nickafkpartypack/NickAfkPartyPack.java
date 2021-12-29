package ml.noahc3.nickafkpartypack;

import ml.noahc3.nickafkpartypack.Commands.*;
import ml.noahc3.nickafkpartypack.Events.EventListener;
import ml.noahc3.nickafkpartypack.Packets.PacketListener;
import ml.noahc3.nickafkpartypack.Util.Constants;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class NickAfkPartyPack extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Initializing NickAfkPartyPack.");

        this.saveDefaultConfig();

        Constants.plugin = this;
        Constants.nickKey = new NamespacedKey(Constants.plugin, "nickname");
        Constants.afkKey = new NamespacedKey(Constants.plugin, "isAfk");
        Constants.config = this.getConfig();
        Constants.afkTimestamps = new HashMap<>();
        Constants.playerYaw = new HashMap<>();
        Constants.playerPitch = new HashMap<>();


        PacketListener.init();
        EventListener.init();
        getServer().getPluginManager().registerEvents(new EventListener(), Constants.plugin);

        this.getCommand("nick").setExecutor(new CommandNick());
        this.getCommand("afk").setExecutor(new CommandAfk());
        this.getCommand("nonick").setExecutor(new CommandNoNick());
        this.getCommand("nicklist").setExecutor(new CommandNickList());
        this.getCommand("setothernick").setExecutor(new CommandSetOtherNick());
        this.getCommand("delothernick").setExecutor(new CommandDelOtherNick());
        this.getCommand("nickafkreload").setExecutor(new CommandReload());
    }

    @Override
    public void onDisable() {
        getLogger().info("Unloading NickAfkPartyPack.");
        Constants.plugin.getServer().getScheduler().cancelTask(Constants.SlowTickTaskId);
    }
}
