package fr.edencraft.edentrade;

import co.aikar.commands.PaperCommandManager;
import fr.edencraft.edentrade.command.EdenTradeCommand;
import fr.edencraft.edentrade.manager.ConfigurationManager;
import fr.edencraft.edentrade.trade.Trade;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class EdenTrade extends JavaPlugin {

    private static EdenTrade INSTANCE;
    private ConfigurationManager configurationManager;

    private List<Trade> trades = new ArrayList<>();

    @Override
    public void onEnable() {
        INSTANCE = this;
        configurationManager = new ConfigurationManager(this);
        configurationManager.setupFiles();

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new EdenTradeCommand());
    }

    @Override
    public void onDisable() {
        configurationManager.saveFiles();
    }

    public static EdenTrade getINSTANCE() {
        return INSTANCE;
    }

    public void log(Level level, String message) {
        Bukkit.getLogger().log(level, message);
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public List<Trade> getTrades() {
        return trades;
    }
}
