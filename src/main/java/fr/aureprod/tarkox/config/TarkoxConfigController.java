package fr.aureprod.tarkox.config;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.datatype.SpawnPosition;

public class TarkoxConfigController {
    private SpawnPosition worldSpawn;
    private String inGameStatus;
    private String waitingStatus;
    private HashMap<String, String> strings;
    private HashMap<String, List<List<ItemStack>>> loots;

    private String scoreBoardTitle;
    private List<String> scoreBoardLines;

    public TarkoxConfigController(Plugin plugin) {
        TarkoxConfigImporter configImporter = new TarkoxConfigImporter(plugin);

        this.worldSpawn = configImporter.getWorldSpawn();
        this.inGameStatus = configImporter.getInGameStatus();
        this.waitingStatus = configImporter.getWaitingStatus();
        this.strings = configImporter.getCustomStrings();
        this.loots = configImporter.getLoots();

        this.scoreBoardTitle = configImporter.getScoreBoardTitle();
        this.scoreBoardLines = configImporter.getScoreBoardLines();
    }

    public SpawnPosition getWorldSpawn() {
        return this.worldSpawn;
    }

    public String getInGameStatus() {
        return this.inGameStatus;
    }

    public String getWaitingStatus() {
        return this.waitingStatus;
    }

    public String getString(String key) {
        if (!this.strings.containsKey(key)) throw new IllegalArgumentException("The key " + key + " doesn't exist in the strings config file");

        return this.strings.get(key);
    }

    public String getString(String key, String replaceKey, Integer replaceValue) {
        return this.getString(key, replaceKey, replaceValue.toString());
    }

    public String getString(String key, String replaceKey, String replaceValue) {
        String customString = this.getString(key);
        customString = customString.replace("<" + replaceKey + ">", replaceValue);
        return customString;
    }

    public String getString(String key, Player player) {
        String customString = this.getString(key);
        customString = customString.replace("<PLAYER>", player.getName());
        return customString;
    }

    public List<ItemStack> getRandomLoots(String lootsType) {
        if (!this.loots.containsKey(lootsType)) throw new IllegalArgumentException("Loots type not found");

        List<List<ItemStack>> listsItems = this.loots.get(lootsType);

        int randomIndex = (int) (Math.random() * listsItems.size());
        return listsItems.get(randomIndex);
    }  
    
    public String getScoreBoardTitle() {
        return this.scoreBoardTitle;
    }

    public List<String> getScoreBoardLines() {
        return this.scoreBoardLines;
    }
}
