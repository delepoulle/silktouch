package fr.theobessel.silkspawner;

/**
 * Mining Class listnerer
 */

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;

public class SpawnerMining implements Listener {

    private Plugin plugin;

    boolean cheat = false;

    public static String METADATA_TAG = "spawntype";
    private double breakProba;

    public SpawnerMining(Plugin plugin, boolean cheat){
        this.plugin = plugin;
        this.cheat = cheat;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (block.getType() == Material.SPAWNER) {
                CreatureSpawner diggedSpawner = (CreatureSpawner) block.getState();


                ItemStack handItem = player.getInventory().getItemInMainHand();

                NamespacedKey key = new NamespacedKey(plugin, "type");
                ItemMeta itemMeta = handItem.getItemMeta();
                itemMeta.getCustomTagContainer().setCustomTag(key, ItemTagType.STRING, diggedSpawner.getSpawnedType().name());
                handItem.setItemMeta(itemMeta);

                double rand = Math.random()*100.0;
                System.out.println(rand + " vs " + breakProba);

                if ( handItem == null || rand < breakProba ) return;
                if ( handItem.getType() == Material.DIAMOND_PICKAXE | handItem.getType() == Material.IRON_PICKAXE) {
                    if (handItem.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                        ItemStack givedSpawner = new ItemStack(Material.SPAWNER, 1);
                        BlockStateMeta blockStateMeta = (BlockStateMeta) givedSpawner.getItemMeta();
                        BlockState blockState = blockStateMeta.getBlockState();
                        CreatureSpawner spawner = (CreatureSpawner) blockState;
                        spawner.setSpawnedType(diggedSpawner.getSpawnedType());

                        spawner.update();
                        blockState.update();
                        blockStateMeta.setBlockState(blockState);


                        if (cheat) {
                            blockStateMeta.setDisplayName(diggedSpawner.getSpawnedType().name());
                        }

                        givedSpawner.setItemMeta(blockStateMeta);

                        ItemMeta meta = handItem.getItemMeta();
                        //meta.setLore(Arrays.asList(diggedSpawner.getSpawnedType().name()));
                        givedSpawner.setItemMeta(meta);

                        event.setExpToDrop(0);

                        player.getInventory().addItem(givedSpawner);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

            Block block = event.getBlock();
            Player player = event.getPlayer();
            if (block.getType() == Material.SPAWNER) {
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                ItemStack spawnerItem = player.getInventory().getItemInMainHand();

                NamespacedKey key = new NamespacedKey(plugin, "type");
                ItemMeta itemMeta = spawnerItem.getItemMeta();
                CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();


                /*
                List<String> lores = new ArrayList<String>();
                if (itemMeta.getLore() != null){
                    lores = itemMeta.getLore();
                }

                System.out.println("lore" + lores);
                */

                ItemMeta spawnerItemMeta = spawnerItem.getItemMeta();

                String entityType = "";



                /*
                if(cheat) {
                    System.out.println("cheat");
                    if (spawnerItemMeta.getDisplayName().contains("§8")) {
                        entityType = spawnerItemMeta.getDisplayName().replaceAll("§8", "");
                    }
                } else {
                    System.out.println("pas cheat");
                    //entityType = spawnerItemMeta.getDisplayName();
                }
                */

                /*
                System.out.println(entityType);

                if (block.hasMetadata(METADATA_TAG)){
                    System.out.println( "on devrait spawner des " + block.getMetadata(METADATA_TAG).get(0).value() );
                }*/

                //spawner.setCreatureTypeByName(entityType);
                if(tagContainer.hasCustomTag(key , ItemTagType.STRING)) {
                    String foundValue = tagContainer.getCustomTag(key, ItemTagType.STRING);
                    System.out.println(foundValue);
                    spawner.setCreatureTypeByName(foundValue);
                }

                if(cheat){
                    entityType = spawnerItemMeta.getDisplayName();
                    spawner.setCreatureTypeByName(entityType);
                }

                spawner.update();

                System.out.println(spawner.getSpawnedType().getKey() + " spawner placed");
            }

    }

    public void setBreakProbability(double breakProba) {
        this.breakProba = breakProba;
    }
}
