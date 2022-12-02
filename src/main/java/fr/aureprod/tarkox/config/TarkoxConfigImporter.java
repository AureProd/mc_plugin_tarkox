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

    private String pathWorldSpawn;
    private String pathInGameStatus;
    private String pathWaitingStatus;
    private String pathStrings;
    private String pathLoots;

    private String pathScoreBoardTitle;
    private String pathScoreBoardLines;

    private String pathTarkoxInstances;
    private String pathTarkoxInstancesWorldIndex;
    private String pathTarkoxInstancesName;
    private String pathTarkoxInstancesDurationTime;
    private String pathTarkoxInstancesWaitTimeBeforeTp;
    private String pathTarkoxInstancesMaxPlayers;
    private String pathTarkoxInstancesExtractionAreas;
    private String pathTarkoxInstancesExtractionAreasMin;
    private String pathTarkoxInstancesExtractionAreasMax;
    private String pathTarkoxInstancesExtractionAreasWaitTime;
    private String pathTarkoxInstancesChests;
    private String pathTarkoxInstancesSpawns;

    public TarkoxConfigImporter(Plugin plugin) {
        this.plugin = plugin;

        this.pathWorldSpawn = "world_spawn";
        this.pathInGameStatus = "in_game_status";
        this.pathWaitingStatus = "waiting_status";
        this.pathStrings = "strings";
        this.pathLoots = "loots";
        
        this.pathScoreBoardTitle = "scoreboard.title";
        this.pathScoreBoardLines = "scoreboard.lines";

        this.pathTarkoxInstances = "games";
        this.pathTarkoxInstancesWorldIndex = "world_index";
        this.pathTarkoxInstancesName = "name";
        this.pathTarkoxInstancesDurationTime = "duration_time";
        this.pathTarkoxInstancesWaitTimeBeforeTp = "wait_time_before_tp";
        this.pathTarkoxInstancesMaxPlayers = "max_players_nb";
        this.pathTarkoxInstancesExtractionAreas = "extraction_areas";
        this.pathTarkoxInstancesExtractionAreasMin = "min";
        this.pathTarkoxInstancesExtractionAreasMax = "max";
        this.pathTarkoxInstancesExtractionAreasWaitTime = "wait_time";
        this.pathTarkoxInstancesChests = "chests";
        this.pathTarkoxInstancesSpawns = "spawns";
    }

    public SpawnPosition getWorldSpawn() {
        List<Integer> configSpawn = this.plugin.getConfig().getIntegerList(this.pathWorldSpawn);
        
        return new SpawnPosition(
            this.plugin.getServer().getWorlds().get(configSpawn.get(0)),
            configSpawn.get(1),
            configSpawn.get(2),
            configSpawn.get(3)
        );
    }

    public String getInGameStatus() {
        return plugin.getConfig().getString(this.pathInGameStatus);
    }

    public String getWaitingStatus() {
        return plugin.getConfig().getString(this.pathWaitingStatus);
    }

    public HashMap<String, String> getCustomStrings() {
        HashMap<String, String> customStrings = new HashMap<String, String>();
        Set<String> customStringKeys = plugin.getConfig().getConfigurationSection(this.pathStrings).getKeys(false);
        
        for (String customStringKey : customStringKeys) {
            String configPath = this.pathStrings + "." + customStringKey;
            String customString = plugin.getConfig().getString(configPath);
            customStrings.put(customStringKey, customString);
        }

        return customStrings;
    }

    public HashMap<String, List<List<ItemStack>>> getLoots() {
        HashMap<String, List<List<ItemStack>>> loots = new HashMap<String, List<List<ItemStack>>>();

        Set<String> configLootsTypes = plugin.getConfig().getConfigurationSection(this.pathLoots).getKeys(false);

        for (String configLootsType : configLootsTypes) {
            List<List<ItemStack>> listsitems = new ArrayList<List<ItemStack>>();

            String configPath = this.pathLoots + "." + configLootsType;
            Set<String> configLootsKeys = plugin.getConfig().getConfigurationSection(configPath).getKeys(false);

            for (String configLootsKey : configLootsKeys) {
                List<ItemStack> items = new ArrayList<ItemStack>();

                String configLootPathBis = configPath + "." + configLootsKey;
                Set<String> configItems = plugin.getConfig().getConfigurationSection(configLootPathBis).getKeys(false);

                for (String configItem : configItems) {
                    String configLootPath = configLootPathBis + "." + configItem;
                    Integer itemQuantity = plugin.getConfig().getInt(configLootPath);

                    if (Material.getMaterial(configItem) == null) throw new IllegalArgumentException("Invalid config file (" + configPath + ") : " + configItem + " is not a valid material name");
    
                    Material itemMaterial = Material.getMaterial(configItem);

                    if (itemQuantity <= 0) throw new IllegalArgumentException("Invalid config file (" + configLootPath + ") : " + itemQuantity + " is not a valid quantity");
                    
                    Integer numberFullStack = itemQuantity / 64;
                    Integer numberLeftOver = itemQuantity % 64;

                    for (int i = 0; i < numberFullStack; i++) {
                        ItemStack itemStack = new ItemStack(itemMaterial, 64);
                        items.add(itemStack);
                    }
                    
                    if (numberLeftOver > 0) {
                        ItemStack itemStack = new ItemStack(itemMaterial, numberLeftOver);
                        items.add(itemStack);
                    }
                }

                listsitems.add(items);
            }

            loots.put(configLootsType, listsitems);            
        }

        return loots;
    }

    public String getScoreBoardTitle() {
        return plugin.getConfig().getString(this.pathScoreBoardTitle);
    }

    public List<String> getScoreBoardLines() {
        return plugin.getConfig().getStringList(this.pathScoreBoardLines);
    }

    public List<TarkoxInstance> getTarkoxInstances() {
        List<TarkoxInstance> instances = new ArrayList<TarkoxInstance>();
        HashMap<String, List<List<ItemStack>>> loots = this.getLoots();

        Set<String> configInstancesKeys = plugin.getConfig().getConfigurationSection(this.pathTarkoxInstances).getKeys(false);

        for (String configInstancesKey : configInstancesKeys) {
            String configPath = this.pathTarkoxInstances + "." + configInstancesKey;

            Integer worldIndex = plugin.getConfig().getInt(configPath + "." + this.pathTarkoxInstancesWorldIndex);
            World world = this.plugin.getServer().getWorlds().get(worldIndex);

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

            Integer durationTime = plugin.getConfig().getInt(configPath + "." + this.pathTarkoxInstancesDurationTime);
            Integer waitTimeBeforeTp = plugin.getConfig().getInt(configPath + "." + this.pathTarkoxInstancesWaitTimeBeforeTp);
            
            Integer maxPlayers = plugin.getConfig().getInt(configPath + "." + this.pathTarkoxInstancesMaxPlayers);

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

            HashMap<String, List<Chest>> chests = new HashMap<String, List<Chest>>();
            String configCestsTypesPath = configPath + "." + this.pathTarkoxInstancesChests;
            Set<String> configChestsTypes = plugin.getConfig().getConfigurationSection(configCestsTypesPath).getKeys(false);

            for (String configChestsType : configChestsTypes) {
                if (!loots.containsKey(configChestsType)) throw new IllegalArgumentException("Invalid config file (" + configCestsTypesPath + ") : " + configChestsType + " is not a valid loot type");

                List<Chest> chestsList = new ArrayList<Chest>();

                String configChestsPath = configCestsTypesPath + "." + configChestsType;
                Set<String> configChests = plugin.getConfig().getConfigurationSection(configChestsPath).getKeys(false);
                
                for (String configChest : configChests) {
                    String configChestPath = configChestsPath + "." + configChest;
                    List<Integer> configChestLocation = plugin.getConfig().getIntegerList(configChestPath);

                    BlockState block = world.getBlockAt(
                        configChestLocation.get(0),
                        configChestLocation.get(1),
                        configChestLocation.get(2)
                    ).getState();

                    if (!(block instanceof Chest)) throw new IllegalArgumentException("Invalid config file (" + configChestPath + ") : " + configChestLocation.get(0) + ", " + configChestLocation.get(1) + ", " + configChestLocation.get(2) + " is not a chest");

                    Chest chest = (Chest) block;

                    chestsList.add(chest);
                }

                chests.put(configChestsType, chestsList);
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
                durationTime,
                waitTimeBeforeTp,
                maxPlayers,
                extractionAreas,
                chests,
                spawns
            );

            instances.add(instance);
        }

        return instances;
    }
}
