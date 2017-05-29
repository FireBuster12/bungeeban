package de.lucavinci.bungeeban.commands;

import de.lucavinci.bungeeban.BungeeBan;
import de.lucavinci.bungeeban.util.BungeeBanPlayer;
import de.lucavinci.bungeeban.util.*;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/**
 * With this command you mute a player permanently.
 */
public class MuteCommand extends BungeeBanCommand {

    public MuteCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission(ConfigManager.cv("general.permissions.mute"))) {
            if(args.length >= 2) {
                String playername = args[0];
                if(BungeeBan.getApi().playerExists(playername)) {
                    UUID uuid = BungeeBan.getApi().getUuid(playername);
                    BungeeBanPlayer bbp = BungeeBan.getApi().getPlayer(uuid);
                    playername = bbp.getPlayername();
                    String reason = "";
                    for(int i = 1; i < args.length; i++) {
                        reason += args[i] + " ";
                    }
                    Mute mute = new Mute(uuid, sender.getName(), reason, -1);
                    boolean cancelled = !bbp.mute(mute);
                    bbp.save();
                    if(!cancelled) {
                        sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("commands.mute.success").replace("%PLAYER%", playername));
                        for (ProxiedPlayer o : BungeeCord.getInstance().getPlayers()) {
                            if (o.hasPermission(ConfigManager.cv("general.permissions.mute-broadcast"))) {
                                for (String line : ConfigManager.txt3("commands.mute.broadcast")) {
                                    line = line.replace("%PLAYER%", playername);
                                    line = line.replace("%MUTEDBY%", sender.getName());
                                    line = line.replace("%REASON%", reason);
                                    line = line.replace("%LENGTH%", ConfigManager.txt("permanenttime"));
                                    o.sendMessage(BungeeBan.PREFIX + line);
                                }
                            }
                        }
                    } else {
                        sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.internalerror").replace("%ERROR%", "The mute was cancelled by an event"));
                    }
                } else {
                    sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.playernotfound").replace("%PLAYERNAME%", playername));
                }
            } else {
                sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("commands.mute.syntax"));
            }
        } else {
            sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.nopermissions"));
        }
    }
}
