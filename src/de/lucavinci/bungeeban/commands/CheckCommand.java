package de.lucavinci.bungeeban.commands;

import de.lucavinci.bungeeban.BungeeBan;
import de.lucavinci.bungeeban.util.BungeeBanCommand;
import de.lucavinci.bungeeban.util.BungeeBanPlayer;
import de.lucavinci.bungeeban.util.ConfigManager;
import net.md_5.bungee.api.CommandSender;

import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * With this command you can check whether a player is banned/muted or not.
 */
public class CheckCommand extends BungeeBanCommand {

    public CheckCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission(ConfigManager.cv("general.permissions.check"))) {
            if(args.length == 1) {
                String playername = args[0];
                if(BungeeBan.getApi().playerExists(playername)) {
                    // Calculating all replace parameters
                    UUID uuid = BungeeBan.getApi().getUuid(playername);
                    BungeeBanPlayer bbp = BungeeBan.getApi().getPlayer(uuid);
                    playername = bbp.getPlayername();
                    String lastjoin = new SimpleDateFormat(ConfigManager.txt("simpledateformat")).format(bbp.getLastLogin());
                    boolean isbanned = bbp.isBanned(), ismuted = bbp.isMuted();
                    String banreason = "-", bannedby = "-", mutereason = "-", mutedby = "-";
                    if(isbanned) {
                        banreason = bbp.getActiveBan().getBanReason();
                        bannedby = bbp.getActiveBan().getBannedBy();
                    }
                    if(ismuted) {
                        mutereason = bbp.getActiveMute().getMuteReason();
                        mutedby = bbp.getActiveMute().getMutedBy();
                    }
                    // Sending messages
                    for(String msg : ConfigManager.txt3("commands.check.info")) {
                        msg = msg.replace("%PLAYERNAME%", playername);
                        msg = msg.replace("%UUID%", uuid.toString());
                        msg = msg.replace("%LASTJOIN%", lastjoin);
                        msg = msg.replace("%ISBANNED%", "" + isbanned);
                        msg = msg.replace("%BANREASON%", banreason);
                        msg = msg.replace("%BANNEDBY%", bannedby);
                        msg = msg.replace("%ISMUTED%", "" + ismuted);
                        msg = msg.replace("%MUTEREASON%", mutereason);
                        msg = msg.replace("%MUTEDBY%", mutedby);
                        sender.sendMessage(BungeeBan.PREFIX + msg);
                    }
                } else {
                    sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.playernotfound").replace("%PLAYERNAME%", playername));
                }
            } else {
                sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("commands.check.syntax"));
            }
        } else {
            sender.sendMessage(BungeeBan.PREFIX + ConfigManager.txt("errors.nopermissions"));
        }
    }
}
