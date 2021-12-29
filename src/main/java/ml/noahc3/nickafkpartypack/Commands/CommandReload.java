package ml.noahc3.nickafkpartypack.Commands;

import ml.noahc3.nickafkpartypack.Util.Constants;
import ml.noahc3.nickafkpartypack.Util.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Constants.plugin.reloadConfig();
        Constants.config = Constants.plugin.getConfig();
        for (Player p : Bukkit.getOnlinePlayers()) {
            Tasks.refreshPlayer(p);
        }
        sender.sendMessage("Reloaded configuration for NickAfkPartyPack.");

        return true;
    }
}
