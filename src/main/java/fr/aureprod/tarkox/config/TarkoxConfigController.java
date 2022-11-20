package fr.aureprod.tarkox.config;

import java.util.HashMap;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.datatype.SpawnPosition;

public class TarkoxConfigController {
    private World world;
    private SpawnPosition worldSpawn;
    private Integer gameMaxTime;
    private String inGameStatus;
    private String waitingStatus;
    private HashMap<String, String> customStrings;
    private List<List<ItemStack>> lootsLists;

    private String scoreBoardTitle;
    private List<String> scoreBoardLines;

    public TarkoxConfigController(Plugin plugin) {
        TarkoxConfigImporter configImporter = new TarkoxConfigImporter(plugin);

        this.world = configImporter.getWorld();
        this.worldSpawn = configImporter.getWorldSpawn();
        this.gameMaxTime = configImporter.getGameMaxTime();
        this.inGameStatus = configImporter.getInGameStatus();
        this.waitingStatus = configImporter.getWaitingStatus();
        this.customStrings = configImporter.getCustomStrings();
        this.lootsLists = configImporter.getLootsLists();

        this.scoreBoardTitle = configImporter.getScoreBoardTitle();
        this.scoreBoardLines = configImporter.getScoreBoardLines();
    }

    public World getWorld() {
        return this.world;
    }

    public SpawnPosition getWorldSpawn() {
        return this.worldSpawn;
    }

    public Integer getGameMaxTime() {
        return this.gameMaxTime;
    } 

    public String getInGameStatus() {
        return this.inGameStatus;
    }

    public String getWaitingStatus() {
        return this.waitingStatus;
    }

    public String getCustomString(String key) {
        return this.customStrings.get(key);
    }

    public String getCustomString(String key, Player player) {
        String customString = this.getCustomString(key);
        customString = customString.replace("<PLAYER>", player.getName());
        return customString;
    }

    public List<ItemStack> getRandomLootsList() {
        int randomIndex = (int) (Math.random() * this.lootsLists.size());
        return this.lootsLists.get(randomIndex);
    }  
    
    public String getScoreBoardTitle() {
        return this.scoreBoardTitle;
    }

    public List<String> getScoreBoardLines() {
        return this.scoreBoardLines;
    }
}
