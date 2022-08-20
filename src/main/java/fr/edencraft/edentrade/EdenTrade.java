package fr.edencraft.edentrade;

import co.aikar.commands.PaperCommandManager;
import fr.edencraft.edentrade.command.EdenTradeCommand;
import fr.edencraft.edentrade.manager.ConfigurationManager;
import fr.edencraft.edentrade.trade.Trade;
import fr.edencraft.edentrade.utils.CommandCompletionUtils;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class EdenTrade extends JavaPlugin {

    private static EdenTrade INSTANCE;
    private ConfigurationManager configurationManager;

    private List<Trade> trades = new ArrayList<>();
    private LuckPerms luckPermsAPI;

    @Override
    public void onEnable() {
        INSTANCE = this;
        configurationManager = new ConfigurationManager(this);
        configurationManager.setupFiles();

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPermsAPI = provider.getProvider();
        }

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new EdenTradeCommand());
        commandManager.getCommandCompletions().registerAsyncCompletion(
                "etreload",
                context -> {
                    CommandSender sender = context.getSender();
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
                    }
                    return CommandCompletionUtils.getConfigurationFilesName();
                }
        );
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

    public LuckPerms getLuckPermsAPI() {
        return luckPermsAPI;
    }
}
