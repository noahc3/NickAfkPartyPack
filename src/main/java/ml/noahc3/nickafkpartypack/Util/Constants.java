package ml.noahc3.nickafkpartypack.Util;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class Constants {
    public static Plugin plugin;
    public static NamespacedKey nickKey;
    public static NamespacedKey afkKey;
    public static FileConfiguration config;
    public static HashMap<UUID, Long> afkTimestamps;
    public static HashMap<UUID, Float> playerYaw;
    public static HashMap<UUID, Float> playerPitch;
    public static int SlowTickTaskId;
}
