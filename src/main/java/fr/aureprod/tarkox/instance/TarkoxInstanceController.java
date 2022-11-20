package fr.aureprod.tarkox.instance;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.config.TarkoxConfigImporter;
import fr.aureprod.tarkox.exception.TarkoxInstanceDoesNotExistException;
import fr.aureprod.tarkox.exception.TarkoxPlayerNotInInstanceException;

public class TarkoxInstanceController {
    private List<TarkoxInstance> tarkoxInstances;

    public TarkoxInstanceController(Plugin plugin) {
        TarkoxConfigImporter configImporter = new TarkoxConfigImporter(plugin);

        this.tarkoxInstances = configImporter.getTarkoxInstances();
    }

    public Boolean isPlayerInInstance(Player player) {
        for (TarkoxInstance tarkoxInstance : this.tarkoxInstances) {
            if (tarkoxInstance.isInGamePlayer(player)) {
                return true;
            }
        }

        return false;
    }

    public List<TarkoxInstance> getTarkoxInstances() {
        return this.tarkoxInstances;
    }

    public List<String> getNamesTarkoxInstances() {
        List<String> names = new ArrayList<String>();
        
        for (TarkoxInstance tarkoxInstance : this.tarkoxInstances) {
            names.add(tarkoxInstance.getName());    
        }

        return names;
    }

    public TarkoxInstance getTarkoxInstanceByName(String name) throws TarkoxInstanceDoesNotExistException {
        for (TarkoxInstance tarkoxInstance : this.tarkoxInstances) {
            if (tarkoxInstance.getName().equalsIgnoreCase(name)) {
                return tarkoxInstance;
            }
        }

        throw new TarkoxInstanceDoesNotExistException(name);
    }

    public TarkoxInstance getTarkoxInstanceByPlayer(Player player) throws TarkoxPlayerNotInInstanceException {
        for (TarkoxInstance tarkoxInstance : this.tarkoxInstances) {
            if (tarkoxInstance.isInGamePlayer(player)) {
                return tarkoxInstance;
            }
        }

        throw new TarkoxPlayerNotInInstanceException(player);
    }

    public TarkoxInstancePlayer getTarkoxInstancePlayerByPlayer(Player player) throws TarkoxPlayerNotInInstanceException {
        for (TarkoxInstance tarkoxInstance : this.tarkoxInstances) {
            if (tarkoxInstance.isInGamePlayer(player)) {
                return tarkoxInstance.getTarkoxInstancePlayerByPlayer(player);
            }
        }

        throw new TarkoxPlayerNotInInstanceException(player);
    }
}
