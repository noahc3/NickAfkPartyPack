package ml.noahc3.nickafkpartypack.Packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import ml.noahc3.nickafkpartypack.Util.Constants;
import ml.noahc3.nickafkpartypack.Util.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;

public class PacketListener {
    public static void init() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(Constants.plugin, PacketType.Play.Server.PLAYER_INFO) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacket().getPlayerInfoAction().read(0) != EnumWrappers.PlayerInfoAction.ADD_PLAYER) return;

                        PlayerInfoData pid = event.getPacket().getPlayerInfoDataLists().read(0).get(0);

                        Player player = Bukkit.getPlayer(pid.getProfile().getUUID());
                        if (player == null) return;

                        String displayName = Tasks.getPlayerDisplayName(player);
                        String prefix = Tasks.getPlayerPrefix(player);
                        String fullName = prefix + displayName;
                        String headName = Constants.config.getBoolean("show-afk-tag-over-heads") ? fullName : displayName;

                        player.setDisplayName(displayName);
                        PlayerInfoData injectPid = new PlayerInfoData(pid.getProfile().withName(Tasks.cropString(headName, 16)), pid.getLatency(), pid.getGameMode(), WrappedChatComponent.fromText(fullName));
                        event.getPacket().getPlayerInfoDataLists().write(0, Collections.singletonList(injectPid));
                    }
                }
        );
    }
}
