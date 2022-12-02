package fr.aureprod.tarkox.command.tarkox_admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.command.CommandController;
import fr.aureprod.tarkox.exception.TarkoxInstanceDoesNotExistException;
import fr.aureprod.tarkox.exception.TarkoxPlayerNotInInstanceException;
import fr.aureprod.tarkox.exception.TarkoxSenderIsNotPlayerException;
import fr.aureprod.tarkox.instance.TarkoxInstance;

public class TarkoxAdminController extends CommandController {
    private String statusSubCommand;
    private String stopSubCommand;
    
    private String statusSubCommandTemplate;
    private String stopSubCommandTemplate;

    public TarkoxAdminController(Plugin plugin) {
        super(plugin);

        this.statusSubCommand = "status";
        this.stopSubCommand = "stop";

        this.statusSubCommandTemplate = "§c/tarkox-admin status <mapName>";
        this.stopSubCommandTemplate = "§c/tarkox-admin stop <mapName>";
    }

    public TarkoxInstance getTarkoxInstanceBySenderAndName(CommandSender sender, String name) throws TarkoxPlayerNotInInstanceException, TarkoxSenderIsNotPlayerException, TarkoxInstanceDoesNotExistException {
        if (name.equals("this")) {
            if (!(sender instanceof Player)) throw new TarkoxSenderIsNotPlayerException();

            Player player = (Player) sender;

            return this.plugin.instanceController.getTarkoxInstanceByPlayer(player);
        }
        else return this.plugin.instanceController.getTarkoxInstanceByName(name);
    }

    public String getStatusSubCommand() {
        return statusSubCommand;
    }

    public String getStopSubCommand() {
        return stopSubCommand;
    }

    public void sendTarkoxAdminCommandTemplate(CommandSender sender) {
        sender.sendMessage(this.statusSubCommandTemplate);
        sender.sendMessage(this.stopSubCommandTemplate);
    }

    public void sendStatusSubCommandTemplate(CommandSender sender) {
        sender.sendMessage(this.statusSubCommandTemplate);
    }

    public void sendStopSubCommandTemplate(CommandSender sender) {
        sender.sendMessage(this.stopSubCommandTemplate);
    }
}
