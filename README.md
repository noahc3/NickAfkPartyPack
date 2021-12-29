# Nick AFK Party Pack

Small Spigot plugin coded up in a day to add nicknames and AFK status for Minecraft 1.18.

### [REQUIRES PROTOCOLLIB (DEV BUILD FOR 1.18)](https://www.spigotmc.org/resources/protocollib.1997/)

![](https://i.imgur.com/GhYkvBx.png)

### Features

* Show nicknames above player heads, in tab list, in chat, and in player join/leave messages
* Nicknames support spaces.
* Show AFK status in tab list, and optionally above player heads (see caveat below)
* Broadcast message when player goes AFK.
* AFK status automatically removed when player moves their mouse (camera) or types in chat.
* Players auto AFK after configurable amount of time (can be disabled).
* AFK players auto-kicked after configurable amount of time (can be disabled).
* Administrator commands to nick/unnick other players.


### Caveats

* Nicknames are limited to 16 characters as this is the maximum allowed above player heads
    * Yes this can be upped to 48 with scoreboards but I chose not to add this for better plugin compatibility.
* If showing AFK status above player head is enabled and a player's username/nickname is longer than 10 characters, their nicknames will be truncated to 9 characters due to limitation above.
* Player will flicker for ~2 ticks when their AFK status or nickname changes.


### Compatibility

* This plugin doesn't touch scoreboards, so any plugins using that should work fine.
* Probably not compatible with other mods which inject data into Play.Server.PLAYER_INFO packets or depend on those packets not being sent randomly.


### Commands

* /nick [nickname]: Set your own nickname.
* /nonick: Remove your own nickname.
* /nicklist: List users and their nicknames.
* /afk: Toggle your AFK status.
* /setothernick [player] [nickname]: Set the nickname of another player. Needs OP (or permission node).
* /delothernick [player]: Remove the nickname of another player. Needs OP (or permission node).
* /nickafkreload: Reload plugin configuration file. Needs OP (or permission node).
