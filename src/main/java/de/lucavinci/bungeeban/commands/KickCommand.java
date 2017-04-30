package de.lucavinci.bungeeban.commands;

import de.lucavinci.bungeeban.BungeeBan;
import de.lucavinci.bungeeban.util.BungeeBanCommand;
import de.lucavinci.bungeeban.util.ConfigManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class KickCommand extends BungeeBanCommand {

    public KickCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission(ConfigManager.cv("general.permissions.kick"))) {
            if(args.length >= 2) {
                String playername = args[0];
                if(BungeeCord.getInstance().getPlayer(playername) != null) {
                    String reason = "";
                    for(int i = 1; i < args.length; i++) {
                        reason += args[i] + " ";
                    }
                    BungeeCord.getInstance().getPlayer(playername).disconnect(ConfigManager.txt2("messages.kick").replace("%KICKEDBY%", sender.getName()).replace("%REASON%", reason));
                    sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("commands.kick.success").replace("%PLAYER%", playername));
                    for(ProxiedPlayer o : BungeeCord.getInstance().getPlayers()) {
                        if(o.hasPermission(ConfigManager.cv("general.permissions.kick-broadcast"))) {
                            for(String line : ConfigManager.txt3("commands.kick.broadcast")) {
                                line = line.replace("%PLAYER%", playername);
                                line = line.replace("%KICKEDBY%", sender.getName());
                                line = line.replace("%REASON%", reason);
                                o.sendMessage(BungeeBan.PREFIX + line);
                            }
                        }
                    }
                } else {
                    sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.notonline").replace("%PLAYERNAME%", playername));
                }
            } else {
                sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("commands.kick.syntax"));
            }
        } else {
            sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.nopermissions"));
        }
    }
}
