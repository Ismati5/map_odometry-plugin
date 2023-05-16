package me.ismati5.map_odometry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.EulerAngle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static me.ismati5.map_odometry.Main.plugin;
import static org.bukkit.Bukkit.getServer;

public class DisplayOdometry implements Listener {

    public static void createTrace(String name, Location loc, String map) {

        Double startX = loc.getX();
        Double startY = loc.getY();
        Double startZ = loc.getZ();

        for (Player p : getServer().getOnlinePlayers()){
            p.sendMessage("§8§l➤ §bStarting location: §f" + startZ + " " + startY + " " + startX);
        }

        // Get the path of the text file
        File file = new File(plugin.getDataFolder(), name);

        // Check if the file exists
        if (!file.exists()) {
            plugin.getLogger().warning("[" + plugin.getDescription().getName() + "] Log file does not exist.");
            return;
        }

        try {
            // Create a file reader
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");

            String line;
            List<String> lines = new ArrayList<>();

            int j = 0;
            // Read each line of the file
            while ((line = bufferedReader.readLine()) != null) {

                if (j % 5 == 0){

                    int xPos = line.indexOf("X:");
                    int yPos = line.indexOf("Y:");
                    int thPos = line.indexOf("TH:");
                    int vPos = line.indexOf("V:");

                    String xVal = line.substring(xPos + 2, yPos).trim();
                    String yVal = line.substring(yPos + 2, thPos).trim();
                    String thVal = line.substring(thPos + 3, vPos).trim();

                    if (!(Double.parseDouble(xVal) == 0.0 && Double.parseDouble(yVal) == 0.0 && Double.parseDouble(thVal) == -90)){
                        lines.add(line);
                    }

                }
                j++;
            }

            // Close the file reader
            bufferedReader.close();
            new BukkitRunnable(){
                ArmorStand armorStand;
                int i = 0;
                @Override
                public void run() {
                    if (i < lines.size()) {
                        String line = lines.get(i);
                        // Do something with the read line
                        int xPos = line.indexOf("X:");
                        int yPos = line.indexOf("Y:");
                        int thPos = line.indexOf("TH:");
                        int vPos = line.indexOf("V:");

                        String xVal = line.substring(xPos + 2, yPos).trim();
                        String yVal = line.substring(yPos + 2, thPos).trim();
                        String thVal = line.substring(thPos + 3, vPos).trim();

                        double radians = Math.toRadians(Double.parseDouble(thVal) - 225);
                        if (i > 0){
                            ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                            SkullMeta playerheadmeta = (SkullMeta) skullItem.getItemMeta();
                            armorStand.setHeadPose(new EulerAngle(0 , radians - 180, 0));


                            if (Objects.equals(map, "A")) {
                                playerheadmeta.setOwner("Lime");
                                skullItem.setItemMeta(playerheadmeta);
                                armorStand.setHelmet(skullItem);
                            } else {
                                playerheadmeta.setOwner("diablo3pk");
                                skullItem.setItemMeta(playerheadmeta);
                                armorStand.setHelmet(skullItem);
                            }
                        }

                        Location location = new Location(loc.getWorld(), startX + Double.parseDouble(yVal) / 10.0, startY - 1, startZ + Double.parseDouble(xVal) / 10.0);
                        armorStand = (ArmorStand) loc.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
                        armorStand.setVisible(false);
                        armorStand.setGravity(false);
                        ItemStack skullItem2 = new ItemStack(Material.SEA_LANTERN);
                        armorStand.setHelmet(skullItem2);
                        armorStand.setHeadPose(new EulerAngle(0 , radians - 225, 0));

                        for (Player p : getServer().getOnlinePlayers()) {
                            // p.sendMessage("loc: " + (startX + Double.parseDouble(yVal) / 10.0) + " | " + startY + " | " + (startZ + Double.parseDouble(xVal) / 10.0));
                            // p.playSound(p.getLocation(), Sound.ZOMBIE_WOOD, 1F, 1F);
                            p.sendMessage("§8§l➤ §bPosition: §eX: " +  decimalFormat.format(Float.parseFloat(xVal) / 10.0) + " §8| §eY: " +  decimalFormat.format(Float.parseFloat(yVal) / 10.0) + " §8| §eTH: " +  decimalFormat.format(Float.parseFloat(thVal)));
                        }
                        i++;
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 3L);

        } catch (IOException e) {
            plugin.getLogger().warning("[" + plugin.getDescription().getName() + "] Error while reading the log file: " + e.getMessage());
        }
    }

    @EventHandler
    public void displayOdometry(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if (p.getItemInHand().getType().equals(Material.STICK)) {
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                e.setCancelled(true);


                p.sendMessage("§bRobot starting location set!");

                createTrace("log-08h-32m-49s.txt", p.getTargetBlock((HashSet<Byte>) null, 10).getLocation(), "A");
            }
        }
    }
}
