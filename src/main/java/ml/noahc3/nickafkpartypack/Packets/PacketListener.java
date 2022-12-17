package ml.noahc3.nickafkpartypack.Packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import ml.noahc3.nickafkpartypack.Util.Constants;
import ml.noahc3.nickafkpartypack.Util.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PacketListener {
    private static PacketAdapter playServerPlayerInfo;
    public static void init() {
        playServerPlayerInfo = new PacketAdapter(Constants.plugin, PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Set<EnumWrappers.PlayerInfoAction> actions = event.getPacket().getPlayerInfoActions().read(0);
                if (!actions.contains(EnumWrappers.PlayerInfoAction.ADD_PLAYER)) return;
                boolean bShowAfkTagOverHeads = Constants.config.getBoolean("show-afk-tag-over-heads");
                List<PlayerInfoData> newPlayerInfoDataList = new ArrayList<>();
                List<PlayerInfoData> playerInfoDataList = event.getPacket().getPlayerInfoDataLists().read(1);
                for (PlayerInfoData pid : playerInfoDataList) {
                    PlayerInfoData newPid = null;
                    WrappedGameProfile profile = pid != null ? pid.getProfile() : null;
                    if (profile != null) {
                        Player player = Bukkit.getPlayer(profile.getUUID());

                        String displayName = Tasks.getPlayerDisplayName(player);
                        String prefix = Tasks.getPlayerPrefix(player);
                        String fullName = prefix + displayName;
                        String headName = bShowAfkTagOverHeads ? fullName : displayName;

                        player.setDisplayName(displayName);
                        WrappedGameProfile newProfile = profile.withName(Tasks.cropString(headName, 16));
                        newProfile.getProperties().putAll(profile.getProperties());
                        newPid = new PlayerInfoData(newProfile, pid.getLatency(), pid.getGameMode(), WrappedChatComponent.fromText(fullName));
                     }
                     newPlayerInfoDataList.add(newPid != null ? newPid : pid);
                }
                event.getPacket().getPlayerInfoDataLists().write(1, newPlayerInfoDataList);
            }
        };

        ProtocolLibrary.getProtocolManager().addPacketListener(playServerPlayerInfo);
    }

    public static void deinit() {
        ProtocolLibrary.getProtocolManager().removePacketListener(playServerPlayerInfo);
    }
}
