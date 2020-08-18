package me.bluemond.pocketfurnace;

import me.bluemond.pocketfurnace.datahandler.DataHandler;
import org.bukkit.plugin.java.JavaPlugin;


public class PocketFurnace extends JavaPlugin {

    private CommandManager commandManager;
    private AllocationManager allocationManager;
    private DataHandler dataHandler;

    // think about command for assigned the furnace you're pointing at as one of your pocketfurnaces
    // would need to support worldguard and griefprevention


    @Override
    public void onEnable() {
        // on server enabling the plugin

        dataHandler = new DataHandler(this);
        commandManager = new CommandManager(this);
        allocationManager = new AllocationManager(this);


        getCommand("pocketfurnace").setExecutor(commandManager);

        getLogger().info("PocketFurnace v" + getDescription().getVersion() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        // on server disabling the plugin

        getLogger().info("PocketFurnace v" + getDescription().getVersion() + " has been disabled!");
    }

    public AllocationManager getAllocationManager() {
        return allocationManager;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }
}
