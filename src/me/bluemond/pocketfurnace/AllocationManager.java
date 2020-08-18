package me.bluemond.pocketfurnace;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;

import java.util.*;

public class AllocationManager {

    private PocketFurnace plugin;
    private List<Location> furnaceMapUnused;
    private Map<UUID, List<Location>> furnaceMapUsed;

    public AllocationManager(PocketFurnace plugin) {
        this.plugin = plugin;
        furnaceMapUnused = new ArrayList<>();
        furnaceMapUsed = new HashMap<>();

        loadFurnaceLocations();
    }

    private void loadFurnaceLocations(){
        World world = plugin.getServer().getWorld("world");
        Chunk spawnChunk = world.getSpawnLocation().getChunk();

        for(int y = 0; y <= 5; y++){
            for(int x = 0; x <= 15; x++){
                for(int z = 0; z <= 15; z++){

                    Block block = spawnChunk.getBlock(x, y, z);
                    Location location = block.getLocation();

                    if(block.getType() != Material.FURNACE){
                        block.setType(Material.FURNACE);
                    }

                    furnaceMapUnused.add(location);

                }
            }
        }

        furnaceMapUsed = plugin.getDataHandler().getAllFurnaceLocations();

        //clean unused
        for(List<Location> locations : furnaceMapUsed.values()){
            for(Location location : locations){
                furnaceMapUnused.remove(location);
            }
        }

        int counter = 0;
        for(List<Location> list : furnaceMapUsed.values()){
            counter += list.size();
        }

        plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "" + counter
                + ChatColor.YELLOW + " allocated furnaces loaded.");
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "" + furnaceMapUnused.size()
                + ChatColor.YELLOW + " unallocated furnaces remaining.");
    }

    public void openFurnace(Player player, int index){
        UUID uuid = player.getUniqueId();
        List<Location> furnaceLocs = furnaceMapUsed.get(uuid);


        // this can be condensed

        // check for null furnace locations
        if(furnaceLocs == null){

            // generate list of missing furnaces up until index
            furnaceLocs = new ArrayList<>();
            for(int i = 0; i < index; i++){
                furnaceLocs.add(furnaceMapUnused.get(0));
                furnaceMapUnused.remove(0);
            }

            updateFurnaceMapping(uuid, furnaceLocs);

        // check for missing furnace locations
        }else if(furnaceLocs.size() < index){

            // add missing elements to furnace locations
            int diff = index - furnaceLocs.size();
            for(int i = 0; i < diff; i++){
                furnaceLocs.add(furnaceMapUnused.get(0));
                furnaceMapUnused.remove(0);
            }

            updateFurnaceMapping(uuid, furnaceLocs);
        }

        //open furnace
        Furnace furnace = (Furnace) furnaceLocs.get(index-1).getBlock().getState();
        furnace.setCustomName("PocketFurnace " + index);
        player.openInventory(furnace.getInventory());

    }

    private void updateFurnaceMapping(UUID uuid, List<Location> newFurnaceLocs){
        furnaceMapUsed.put(uuid, newFurnaceLocs);
        plugin.getDataHandler().setFurnaceLocations(uuid, newFurnaceLocs);
    }

}
