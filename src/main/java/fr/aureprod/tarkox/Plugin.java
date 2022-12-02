package fr.aureprod.tarkox;

import org.bukkit.plugin.java.JavaPlugin;

import fr.aureprod.tarkox.command.raid.RaidCommand;
import fr.aureprod.tarkox.command.raid.RaidTabCompleter;
import fr.aureprod.tarkox.command.tarkox_admin.TarkoxAdminCommand;
import fr.aureprod.tarkox.command.tarkox_admin.TarkoxAdminTabCompleter;
import fr.aureprod.tarkox.config.TarkoxConfigController;
import fr.aureprod.tarkox.instance.TarkoxInstanceController;
import fr.aureprod.tarkox.listener.TarkoxListener;

/*
 * tarkox java plugin
 */
public class Plugin extends JavaPlugin {
  public TarkoxConfigController configController;
  public TarkoxInstanceController instanceController;

  @Override
  public void onEnable()
  {
    System.out.println("[Tarkox] Tarkox plugin enabled");

    this.saveDefaultConfig();

    this.configController = new TarkoxConfigController(this);
    this.instanceController = new TarkoxInstanceController(this);

    // register events
    this.getServer().getPluginManager().registerEvents(new TarkoxListener(this), this);

    // register commands
    this.getCommand("raid").setExecutor(new RaidCommand(this));
    this.getCommand("raid").setTabCompleter(new RaidTabCompleter(this));
    this.getCommand("tarkox-admin").setExecutor(new TarkoxAdminCommand(this));
    this.getCommand("tarkox-admin").setTabCompleter(new TarkoxAdminTabCompleter(this)); 
  }

  @Override
  public void onDisable()
  {
    System.out.println("[Tarkox] Tarkox plugin disabled");
  }
}
