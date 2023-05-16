package me.ismati5.map_odometry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashSet;

import static me.ismati5.map_odometry.DisplayOdometry.createTrace;

public class Command implements CommandExecutor {

    public static Location startLocA = new Location(Bukkit.getWorld("world"), -7.80D, 57.0D, -1.9D);
    public static Location startLocB = new Location(Bukkit.getWorld("world"), -7.80D, 57.0D, 26.0D);

    public Command(Main instance) {
        Main.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {

        if ((sender instanceof Player)) {

            Player p = (Player) sender;

            if ((cmd.getName().equalsIgnoreCase("mapodometry")) && (args.length > 0)) {

                if ((args[0].equalsIgnoreCase("setstart"))) {

                    double X = p.getLocation().getX();
                    double Y = p.getLocation().getY();
                    double Z = p.getLocation().getZ();
                    World world =  p.getLocation().getWorld();
                    Location location = new Location(world, X, Y, Z);
                    if ((args[1].equalsIgnoreCase("A"))) {
                        startLocA = location;
                        p.sendMessage("§8§l➤ §bStart A position set!");
                    } else {
                        startLocB = location;
                        p.sendMessage("§8§l➤ §bStart B position set!");
                    }

                    return true;

                }  else if ((args[0].equalsIgnoreCase("clear"))) {
                    for(Entity entity : Bukkit.getWorld("world").getEntities()) {
                        if(entity instanceof ArmorStand) {
                            entity.remove();
                        }
                    }
                    p.sendMessage("§8§l➤ §bMap was cleared!");
                    return true;
                } else if ((args[0].equalsIgnoreCase("show"))) {
                    if ((args[2].equalsIgnoreCase("A"))) {
                        createTrace(args[1], startLocA, args[2]);
                    } else {
                        createTrace(args[1], startLocB, args[2]);
                    }
                    return true;
                }
            }
        } return false;
    }

}
