package fr.theobessel.silkspawner;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


/**
 *
 *  Main plugin class
 *
 * @author : Théo Bessel & Samuel Delepoulle
 *
 */


public class PluginLoader extends JavaPlugin{

    private FileConfiguration config = getConfig();

    public static void main(String[] args) {}

    @Override
    public void onEnable() {

        config.addDefault("breakprobability", 10);
        config.addDefault("cheat", false);
        config.options().copyDefaults(true);
        saveConfig();

        boolean cheat = config.getBoolean("cheat");
        double breakProba = config.getDouble("breakprobability");

        SpawnerMining plugin = new SpawnerMining(this, cheat);
        plugin.setBreakProbability(breakProba);

        getServer().getPluginManager().registerEvents(plugin, this);

        String mode = cheat ? "(cheat mode)" : "" ;

        System.out.println("[PLUGIN INFO] Plugin started! "+mode);
    }

    @Override
    public void onDisable() {
        System.out.println("[PLUGIN INFO] Plugin closed !");
    }
}
