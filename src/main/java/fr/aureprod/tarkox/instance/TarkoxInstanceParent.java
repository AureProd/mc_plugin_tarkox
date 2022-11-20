package fr.aureprod.tarkox.instance;

import java.util.List;

import org.bukkit.block.Chest;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.datatype.ExtractionArea;
import fr.aureprod.tarkox.datatype.SpawnPosition;

public class TarkoxInstanceParent {
    protected Plugin plugin;
    private String name;
    private Integer minPlayers; 
    private Integer maxPlayers;
    private SpawnPosition waitAreaSpawn;
    protected List<ExtractionArea> extractionAreas;
    protected List<Chest> chests;
    protected List<SpawnPosition> spawns;

    private Integer currentTime;
    private TarkoxInstanceStatus status;

    public TarkoxInstanceParent (
        Plugin plugin, 
        String name, 
        Integer minPlayers, 
        Integer maxPlayers, 
        SpawnPosition waitAreaSpawn, 
        List<ExtractionArea> extractionAreas, 
        List<Chest> chests, 
        List<SpawnPosition> spawns
    ) {
        this.plugin = plugin;
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.waitAreaSpawn = waitAreaSpawn;
        this.extractionAreas = extractionAreas;
        this.chests = chests;
        this.spawns = spawns;

        this.status = TarkoxInstanceStatus.WAITING;
        this.currentTime = 0;
    }

    public String getName() {
        return this.name;
    }

    protected Integer getMinPlayers () {
        return this.minPlayers;
    }

    protected Integer getMaxPlayers() {
        return this.maxPlayers;
    }

    protected SpawnPosition getWaitAreaSpawn() {
        return this.waitAreaSpawn;
    }

    public String getStatus() {
        switch (this.status) {
            case WAITING:
                return "In waiting";
            case INGAME:
                return "In game";
            default:
                return "An error occured";
        }
    }

    public Integer getLeftTime() {
        return this.plugin.configController.getGameMaxTime() - this.currentTime;
    }

    public Boolean isInGame() {
        return this.status.equals(TarkoxInstanceStatus.INGAME);
    }

    public Boolean isWaiting() {
        return this.status.equals(TarkoxInstanceStatus.WAITING);
    }

    protected void setStatusInGame() {
        this.status = TarkoxInstanceStatus.INGAME;
    }

    protected void setStatusWaiting() {
        this.status = TarkoxInstanceStatus.WAITING;
    }

    protected Integer getCurrentTime() {
        return this.currentTime;
    }
    
    protected void resetCurrentTime() {
        this.currentTime = 0;
    }

    protected void incrementCurrentTime() {
        this.currentTime++;
    }

    protected SpawnPosition getRandomSpawn() {
        int randomIndex = (int) (Math.random() * this.spawns.size());
        return this.spawns.get(randomIndex);
    }
}
