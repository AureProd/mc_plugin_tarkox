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
    private String kickSubCommand;
    private String statusSubCommand;
    private String startSubCommand;
    private String stopSubCommand;
    
    private String kickSubCommandTemplate;
    private String statusSubCommandTemplate;
    private String startSubCommandTemplate;
    private String stopSubCommandTemplate;

    public TarkoxAdminController(Plugin plugin) {
        super(plugin);

        this.kickSubCommand = "kick";
        this.statusSubCommand = "status";
        this.startSubCommand = "start";
        this.stopSubCommand = "stop";

        this.kickSubCommandTemplate = "§c/tarkox-admin kick <mapName> <playerName>";
        this.statusSubCommandTemplate = "§c/tarkox-admin status <mapName>";
        this.startSubCommandTemplate = "§c/tarkox-admin start <mapName>";
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

    public String getKickSubCommand() {
        return kickSubCommand;
    }

    public String getStatusSubCommand() {
        return statusSubCommand;
    }

    public String getStartSubCommand() {
        return startSubCommand;
    }

    public String getStopSubCommand() {
        return stopSubCommand;
    }

    public void sendTarkoxAdminCommandTemplate(CommandSender sender) {
        sender.sendMessage(this.kickSubCommandTemplate);
        sender.sendMessage(this.statusSubCommandTemplate);
        sender.sendMessage(this.startSubCommandTemplate);
        sender.sendMessage(this.stopSubCommandTemplate);
    }

    public void sendKickSubCommandTemplate(CommandSender sender) {
        sender.sendMessage(this.kickSubCommandTemplate);
    }

    public void sendStatusSubCommandTemplate(CommandSender sender) {
        sender.sendMessage(this.statusSubCommandTemplate);
    }

    public void sendStartSubCommandTemplate(CommandSender sender) {
        sender.sendMessage(this.startSubCommandTemplate);
    }

    public void sendStopSubCommandTemplate(CommandSender sender) {
        sender.sendMessage(this.stopSubCommandTemplate);
    }
}
