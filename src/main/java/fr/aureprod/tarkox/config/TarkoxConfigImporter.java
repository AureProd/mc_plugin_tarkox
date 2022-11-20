package fr.aureprod.tarkox.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.datatype.ExtractionArea;
import fr.aureprod.tarkox.datatype.Position;
import fr.aureprod.tarkox.datatype.SpawnPosition;
import fr.aureprod.tarkox.instance.TarkoxInstance;

public class TarkoxConfigImporter {
    private Plugin plugin;

    private String pathWorldName;
    private String pathWorldSpawn;
    private String pathGameMaxTime;
    private String pathCustomStrings;
    private String pathLootsLists;
    private String pathInGameStatus;
    private String pathWaitingStatus;

    private String pathScoreBoardTitle;
    private String pathScoreBoardLines;

    private String pathTarkoxInstances;
    private String pathTarkoxInstancesName;
    private String pathTarkoxInstancesMinPlayers;
    private String pathTarkoxInstancesMaxPlayers;
    private String pathTarkoxInstancesWaitAreaSpawn;
    private String pathTarkoxInstancesExtractionAreas;
    private String pathTarkoxInstancesExtractionAreasMin;
    private String pathTarkoxInstancesExtractionAreasMax;
    private String pathTarkoxInstancesExtractionAreasWaitTime;
    private String pathTarkoxInstancesChests;
    private String pathTarkoxInstancesSpawns;

    public TarkoxConfigImporter(Plugin plugin) {
        this.plugin = plugin;

        this.pathWorldName = "world_name";
        this.pathWorldSpawn = "world_spawn";
        this.pathGameMaxTime = "game_time";
        this.pathCustomStrings = "strings";
        this.pathLootsLists = "loots_lists";
        this.pathInGameStatus = "in_game_status";
        this.pathWaitingStatus = "waiting_status";
        
        this.pathScoreBoardTitle = "scoreboard.title";
        this.pathScoreBoardLines = "scoreboard.lines";

        this.pathTarkoxInstances = "games";
        this.pathTarkoxInstancesName = "name";
        this.pathTarkoxInstancesMinPlayers = "min_players_nb";
        this.pathTarkoxInstancesMaxPlayers = "max_players_nb";
        this.pathTarkoxInstancesWaitAreaSpawn = "wait_area_spawn";
        this.pathTarkoxInstancesExtractionAreas = "extraction_areas";
        this.pathTarkoxInstancesExtractionAreasMin = "min";
        this.pathTarkoxInstancesExtractionAreasMax = "max";
        this.pathTarkoxInstancesExtractionAreasWaitTime = "wait_time";
        this.pathTarkoxInstancesChests = "chests";
        this.pathTarkoxInstancesSpawns = "game_spawns";
    }

    public World getWorld() {
        String worldName = this.plugin.getConfig().getString(this.pathWorldName);
        return this.plugin.getServer().getWorld(worldName);
    }

    public SpawnPosition getWorldSpawn() {
        List<Integer> configSpawn = plugin.getConfig().getIntegerList(this.pathWorldSpawn);
        
        return new SpawnPosition(
            this.getWorld(),
            configSpawn.get(0),
            configSpawn.get(1),
            configSpawn.get(2)
        );
    }

    public Integer getGameMaxTime() {
        return plugin.getConfig().getInt(this.pathGameMaxTime);
    }

    public String getInGameStatus() {
        return plugin.getConfig().getString(this.pathInGameStatus);
    }

    public String getWaitingStatus() {
        return plugin.getConfig().getString(this.pathWaitingStatus);
    }

    public HashMap<String, String> getCustomStrings() {
        HashMap<String, String> customStrings = new HashMap<String, String>();
        Set<String> customStringKeys = plugin.getConfig().getConfigurationSection(this.pathCustomStrings).getKeys(false);
        
        for (String customStringKey : customStringKeys) {
            String configPath = this.pathCustomStrings + "." + customStringKey;
            String customString = plugin.getConfig().getString(configPath);
            customStrings.put(customStringKey, customString);
        }

        return customStrings;
    }

    public List<List<ItemStack>> getLootsLists() {
        List<List<ItemStack>> lootsLists = new ArrayList<List<ItemStack>>();

        Set<String> configLootsListKeys = plugin.getConfig().getConfigurationSection(this.pathLootsLists).getKeys(false);

        for (String configLootsListKey : configLootsListKeys) {
            List<ItemStack> lootsList = new ArrayList<ItemStack>();

            String configPath = this.pathLootsLists + "." + configLootsListKey;
            Set<String> configLootKeys = plugin.getConfig().getConfigurationSection(configPath).getKeys(false);

            for (String itemName : configLootKeys) {
                String configLootPath = configPath + "." + itemName;
                Integer itemQuantity = plugin.getConfig().getInt(configLootPath);
            
                if (Material.getMaterial(itemName) == null) throw new IllegalArgumentException("Invalid config file (" + configPath + ") : " + itemName + " is not a valid material name");

                Material itemMaterial = Material.getMaterial(itemName);

                if (itemQuantity <= 0) throw new IllegalArgumentException("Invalid config file (" + configLootPath + ") : " + itemQuantity + " is not a valid quantity");
                
                Integer numberFullStack = itemQuantity / 64;
                Integer numberLeftOver = itemQuantity % 64;

                for (int i = 0; i < numberFullStack; i++) {
                    ItemStack itemStack = new ItemStack(itemMaterial, 64);
                    lootsList.add(itemStack);
                }
                
                if (numberLeftOver > 0) {
                    ItemStack itemStack = new ItemStack(itemMaterial, numberLeftOver);
                    lootsList.add(itemStack);
                }
            }

            lootsLists.add(lootsList);
        }

        return lootsLists;
    }

    public String getScoreBoardTitle() {
        return plugin.getConfig().getString(this.pathScoreBoardTitle);
    }

    public List<String> getScoreBoardLines() {
        return plugin.getConfig().getStringList(this.pathScoreBoardLines);
    }

    public List<TarkoxInstance> getTarkoxInstances() {
        List<TarkoxInstance> instances = new ArrayList<TarkoxInstance>();
        World world = this.getWorld();

        Set<String> configInstancesKeys = plugin.getConfig().getConfigurationSection(this.pathTarkoxInstances).getKeys(false);

        for (String configInstancesKey : configInstancesKeys) {
            String configPath = this.pathTarkoxInstances + "." + configInstancesKey;

            String namePath = configPath + "." + this.pathTarkoxInstancesName;
            String name = plugin.getConfig().getString(namePath);
            name = name.toLowerCase();
            name = name.replace(" ", "_");
            if (name == null) throw new IllegalArgumentException("Invalid config file (" + namePath + ") : " + name + " is not a valid name");
            if (name.equals("")) throw new IllegalArgumentException("Invalid config file (" + namePath + ") : " + name + " is not a valid name");
            if (name.equals("this")) throw new IllegalArgumentException("Invalid config file (" + namePath + ") : 'this' (reserved keyword)");
            
            for (TarkoxInstance tarkoxInstance : instances) {
                if (tarkoxInstance.getName().equalsIgnoreCase(name)) throw new IllegalArgumentException("Invalid config file (" + namePath + ") : " + name + " is already used");
            }
            
            Integer minPlayers = plugin.getConfig().getInt(configPath + "." + this.pathTarkoxInstancesMinPlayers);
            Integer maxPlayers = plugin.getConfig().getInt(configPath + "." + this.pathTarkoxInstancesMaxPlayers);
            
            List<Integer> configWaitAreaSpawn = plugin.getConfig().getIntegerList(configPath + "." + this.pathTarkoxInstancesWaitAreaSpawn);
            SpawnPosition waitAreaSpawn = new SpawnPosition(
                world,
                configWaitAreaSpawn.get(0),
                configWaitAreaSpawn.get(1),
                configWaitAreaSpawn.get(2)
            );

            String extractionAreasPath = configPath + "." + this.pathTarkoxInstancesExtractionAreas;
            Set<String> configExtractionAreasKeys = plugin.getConfig().getConfigurationSection(extractionAreasPath).getKeys(false);
            List<ExtractionArea> extractionAreas = new ArrayList<ExtractionArea>();

            for (String configExtractionAreaKey : configExtractionAreasKeys) {
                String configExtractionAreaPath = extractionAreasPath + "." + configExtractionAreaKey;

                Integer extractionAreaWaitTime = plugin.getConfig().getInt(configExtractionAreaPath + "." + this.pathTarkoxInstancesExtractionAreasWaitTime);

                List<Integer> configExtractionAreaMin = plugin.getConfig().getIntegerList(configExtractionAreaPath + "." + this.pathTarkoxInstancesExtractionAreasMin);
                List<Integer> configExtractionAreaMax = plugin.getConfig().getIntegerList(configExtractionAreaPath + "." + this.pathTarkoxInstancesExtractionAreasMax);

                Position extractionAreaMin = new Position(
                    world,
                    configExtractionAreaMin.get(0),
                    configExtractionAreaMin.get(1),
                    configExtractionAreaMin.get(2)
                );

                Position extractionAreaMax = new Position(
                    world,
                    configExtractionAreaMax.get(0),
                    configExtractionAreaMax.get(1),
                    configExtractionAreaMax.get(2)
                );

                ExtractionArea extractionArea = new ExtractionArea(extractionAreaMin, extractionAreaMax, extractionAreaWaitTime);

                extractionAreas.add(extractionArea);
            }

            String chestsPath = configPath + "." + this.pathTarkoxInstancesChests;
            Set<String> configChestsKeys = plugin.getConfig().getConfigurationSection(chestsPath).getKeys(false);
            List<Chest> chests = new ArrayList<Chest>();

            for (String configChestKey : configChestsKeys) {
                String configChestPath = chestsPath + "." + configChestKey;
                List<Integer> configChest = plugin.getConfig().getIntegerList(configChestPath);

                BlockState block = world.getBlockAt(
                    configChest.get(0),
                    configChest.get(1),
                    configChest.get(2)
                ).getState();

                if (!(block instanceof Chest)) throw new IllegalArgumentException("Invalid config file (" + configChestPath + ") : " + configChest.get(0) + ", " + configChest.get(1) + ", " + configChest.get(2) + " is not a chest");

                Chest chest = (Chest) block;

                chests.add(chest);
            }

            String spawnsPath = configPath + "." + this.pathTarkoxInstancesSpawns;
            Set<String> configSpawnsKeys = plugin.getConfig().getConfigurationSection(spawnsPath).getKeys(false);
            List<SpawnPosition> spawns = new ArrayList<SpawnPosition>();

            for (String configSpawnsKey : configSpawnsKeys) {
                List<Integer> configSpawnPointPositions = plugin.getConfig().getIntegerList(spawnsPath + "." + configSpawnsKey);

                SpawnPosition spawn = new SpawnPosition(
                    world,
                    configSpawnPointPositions.get(0),
                    configSpawnPointPositions.get(1),
                    configSpawnPointPositions.get(2)
                );

                spawns.add(spawn);
            }

            TarkoxInstance instance = new TarkoxInstance(
                plugin,
                name,
                minPlayers,
                maxPlayers,
                waitAreaSpawn,
                extractionAreas,
                chests,
                spawns
            );

            instances.add(instance);
        }

        return instances;
    }
}
