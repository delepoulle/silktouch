package fr.theobessel.silkspawner;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginLoader extends JavaPlugin{

    FileConfiguration config = getConfig();

    public static void main(String[] args) {}

    @Override
    public void onEnable() {
        System.out.println("[PLUGIN INFO] Plugin started !");

        config.addDefault("cheat", false);
        config.options().copyDefaults(true);
        saveConfig();

        boolean cheat = config.getBoolean("cheat");
        getServer().getPluginManager().registerEvents(new SpawnerMining(this, cheat), this);
    }

    @Override
    public void onDisable() {
        System.out.println("[PLUGIN INFO] Plugin closed !");
    }
}
