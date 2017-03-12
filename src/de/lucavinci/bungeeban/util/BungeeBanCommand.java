package de.lucavinci.bungeeban.util;

import de.lucavinci.bungeeban.BungeeBan;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 * This Class represents an async {@link BungeeCord} {@link Command}.
 * It is used by extending this class and overwriting the onCommand method.
 */
public class BungeeBanCommand extends Command {
    public BungeeBanCommand(String name) {
        super(name);
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] args) {
        BungeeCord.getInstance().getScheduler().runAsync(BungeeBan.getInstance(), new Runnable() {
            @Override
            public void run() {
                onCommand(commandSender, args);
            }
        });
    }

    /**
     * This method is executed everytime the command gets called.
     * @param sender {@link CommandSender} who executed the command
     * @param args Arguments of the command
     */
    public void onCommand(CommandSender sender, String[] args) {

    }

}
