package ml.noahc3.nickafkpartypack.Commands;

import ml.noahc3.nickafkpartypack.Util.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandSetOtherNick implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) return false;

            String nick = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            Tasks.setPlayerNick(sender, player, nick);

            return true;
        }

        return false;
    }
}
