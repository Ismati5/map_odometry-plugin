package me.ismati5.map_odometry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main extends JavaPlugin implements Listener {

    public static Plugin plugin;
    public static Main instance;

    @Override
    public void onEnable(){
        plugin = this;
        instance = this;

        System.out.print("[" + plugin.getDescription().getName() + "] Enabled succesfully.");
        System.out.print("[" + plugin.getDescription().getName() + "] Version: " + plugin.getDescription().getVersion());
        System.out.print("[" + plugin.getDescription().getName() + "] Author: " + plugin.getDescription().getAuthors());

        this.getCommand("mapodometry").setExecutor(new Command(this));

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new DisplayOdometry(),this);
    }

    @Override
    public void onDisable() {
        System.out.print("[" + plugin.getDescription().getName() + "] Disabled succesfully.");
    }

}
