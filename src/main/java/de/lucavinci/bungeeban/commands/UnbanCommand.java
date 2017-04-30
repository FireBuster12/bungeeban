package de.lucavinci.bungeeban.commands;

import de.lucavinci.bungeeban.BungeeBan;
import de.lucavinci.bungeeban.util.BungeeBanCommand;
import de.lucavinci.bungeeban.util.BungeeBanPlayer;
import de.lucavinci.bungeeban.util.ConfigManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/**
 * With this command you unban a player.
 */
public class UnbanCommand extends BungeeBanCommand {

    public UnbanCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission(ConfigManager.cv("general.permissions.unban"))) {
            if(args.length == 1) {
                String playername = args[0];
                if(BungeeBan.getApi().playerExists(playername)) {
                    UUID uuid = BungeeBan.getApi().getUuid(playername);
                    BungeeBanPlayer bbp = BungeeBan.getApi().getPlayer(uuid);
                    playername = bbp.getPlayername();
                    if(bbp.isBanned()) {
                        bbp.unban();
                        bbp.save();
                        sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("commands.unban.success").replace("%PLAYER%", playername));
                        for (ProxiedPlayer o : BungeeCord.getInstance().getPlayers()) {
                            if (o.hasPermission(ConfigManager.cv("general.permissions.ban-broadcast"))) {
                                for (String line : ConfigManager.txt3("commands.unban.broadcast")) {
                                    line = line.replace("%PLAYER%", playername);
                                    line = line.replace("%UNBANNEDBY%", sender.getName());
                                    o.sendMessage(BungeeBan.PREFIX + line);
                                }
                            }
                        }
                    } else {
                        sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.notbanned").replace("%PLAYERNAME%", playername));
                    }
                } else {
                    sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.playernotfound").replace("%PLAYERNAME%", playername));
                }
            } else {
                sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("commands.unban.syntax"));
            }
        } else {
            sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.nopermissions"));
        }
    }
}
