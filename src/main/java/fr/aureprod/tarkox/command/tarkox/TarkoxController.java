package fr.aureprod.tarkox.command.tarkox;

import org.bukkit.command.CommandSender;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.command.CommandController;

public class TarkoxController extends CommandController {
    private String joinSubCommand;
    private String leaveSubCommand;

    private String joinSubCommandTemplate;
    private String leaveSubCommandTemplate;

    public TarkoxController(Plugin plugin) {
        super(plugin);

        this.joinSubCommand = "join";
        this.leaveSubCommand = "leave";

        this.joinSubCommandTemplate = "§c/tarkox join <mapName>";
        this.leaveSubCommandTemplate = "§c/tarkox leave";
    }

    public String getJoinSubCommand() {
        return joinSubCommand;
    }

    public String getLeaveSubCommand() {
        return leaveSubCommand;
    }

    public void sendTarkoxCommandTemplate(CommandSender sender) {
        sender.sendMessage(this.joinSubCommandTemplate);
        sender.sendMessage(this.leaveSubCommandTemplate);
    }

    public void sendJoinSubCommandTemplate(CommandSender sender) {
        sender.sendMessage(this.joinSubCommandTemplate);
    }

    public void sendLeaveSubCommandTemplate(CommandSender sender) {
        sender.sendMessage(this.leaveSubCommandTemplate);
    }
}
