package me.bluemond.pocketfurnace.datahandler;

import me.bluemond.pocketfurnace.PocketFurnace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class DataHandler {
    private final AbstractConfig dataConfig;
    private FileConfiguration dataFile;

    private final PocketFurnace plugin;

    private final String furnacePath = "furnaces";

    public DataHandler(PocketFurnace plugin) {
        this.plugin = plugin;
        dataConfig = new AbstractConfig(plugin, "data.yml");

        // Load data.yml
        try {
            dataConfig.createNewFile();
            dataFile = dataConfig.getConfig();
            dataConfig.saveConfig();
        } catch (InvalidConfigurationException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not load data file!");
            e.printStackTrace();
        }
    }

    public void setFurnaceLocations(UUID uuid, List<Location> furnaceLocs){
        List<String> stringLocations = stringifyLocations(furnaceLocs);

        dataFile.set(furnacePath + "." + uuid.toString(), stringLocations);
        dataConfig.saveConfig();
    }

    public List<Location> getFurnaceLocations(UUID uuid){
        List<String> stringLocations = dataFile.getStringList(furnacePath + "." + uuid.toString());

        return parseLocations(stringLocations);
    }

    private List<String> stringifyLocations(List<Location> furnaceLocs) {
        List<String> stringLocations = new ArrayList<>();
        for(Location location : furnaceLocs){
            stringLocations.add(location.getWorld().getName() + ":" + location.getX() + ":"
                    + location.getY() + ":" + location.getZ());
        }

        return stringLocations;
    }

    private List<Location> parseLocations(List<String> stringLocations) {
        List<Location> locations = new ArrayList<>();

        for (String stringLocation : stringLocations) {
            String[] locationData = stringLocation.split(":");
            World world = plugin.getServer().getWorld(locationData[0]);
            double x = Double.parseDouble(locationData[1]);
            double y = Double.parseDouble(locationData[2]);
            double z = Double.parseDouble(locationData[3]);
            locations.add(new Location(world, x, y, z));
        }

        return locations;
    }

    public Map<UUID, List<Location>> getAllFurnaceLocations(){
        ConfigurationSection configurationSection = dataFile.getConfigurationSection(furnacePath);
        Set<String> set;
        Map<UUID, List<Location>> furnaceMapUsed = new HashMap<>();

        if(configurationSection != null){
            set = configurationSection.getKeys(false);
            for(String uuid : set){
                furnaceMapUsed.put(UUID.fromString(uuid), getFurnaceLocations(UUID.fromString(uuid)));
            }
        }

        return furnaceMapUsed;
    }

}
