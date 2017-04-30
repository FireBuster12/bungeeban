package de.lucavinci.bungeeban.commands;

import de.lucavinci.bungeeban.BungeeBan;
import de.lucavinci.bungeeban.util.BungeeBanPlayer;
import de.lucavinci.bungeeban.util.*;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/**
 * With this command you mute a player temporarily.
 */
public class TempmuteCommand extends BungeeBanCommand {

    public TempmuteCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission(ConfigManager.cv("general.permissions.tempmute"))) {
            if (args.length >= 4) {
                String playername = args[0];
                if(BungeeBan.getApi().playerExists(playername)) {
                    UUID uuid = BungeeBan.getApi().getUuid(playername);
                    BungeeBanPlayer bbp = BungeeBan.getApi().getPlayer(uuid);
                    playername = bbp.getPlayername();
                    long millis = Long.parseLong(args[1]);
                    TimeUnit tu = TimeUnit.find(args[2]);
                    if(tu != null) {
                        long units = millis;
                        String timeunit = tu.toString();
                        if(units != 1) {
                            timeunit += "S";
                        }
                        millis *= tu.getMilliseconds();
                        String reason = "";
                        for(int i = 3; i < args.length; i++) {
                            reason += args[i] + " ";
                        }
                        Mute mute = new Mute(uuid, sender.getName(), reason, millis);
                        bbp.mute(mute);
                        bbp.save();
                        sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("commands.tempmute.success").replace("%PLAYER%", playername));
                        for(ProxiedPlayer o : BungeeCord.getInstance().getPlayers()) {
                            if(o.hasPermission(ConfigManager.cv("general.permissions.mute-broadcast"))) {
                                for(String line : ConfigManager.txt3("commands.tempmute.broadcast")) {
                                    line = line.replace("%PLAYER%", playername);
                                    line = line.replace("%MUTEDBY%", sender.getName());
                                    line = line.replace("%REASON%", reason);
                                    line = line.replace("%LENGTH%", units + " " + timeunit);
                                    o.sendMessage(BungeeBan.PREFIX + line);
                                }
                            }
                        }
                    } else {
                        sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.unknowntimeunit").replace("%TIMEUNIT%", args[2]));
                    }
                } else {
                    sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.playernotfound").replace("%PLAYERNAME%", playername));
                }
            } else {
                sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("commands.tempmute.syntax"));
            }
        } else {
            sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.nopermissions"));
        }
    }
}
