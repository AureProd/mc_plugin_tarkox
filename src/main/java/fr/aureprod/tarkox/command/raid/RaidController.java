package fr.aureprod.tarkox.command.raid;

import org.bukkit.command.CommandSender;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.command.CommandController;

public class RaidController extends CommandController {
    private String joinSubCommand;
    private String leaveSubCommand;

    private String joinSubCommandTemplate;
    private String leaveSubCommandTemplate;

    public RaidController(Plugin plugin) {
        super(plugin);

        this.joinSubCommand = "join";
        this.leaveSubCommand = "leave";

        this.joinSubCommandTemplate = "§c/raid join <mapName>";
        this.leaveSubCommandTemplate = "§c/raid leave";
    }

    public String getJoinSubCommand() {
        return joinSubCommand;
    }

    public String getLeaveSubCommand() {
        return leaveSubCommand;
    }

    public void sendRaidCommandTemplate(CommandSender sender) {
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
