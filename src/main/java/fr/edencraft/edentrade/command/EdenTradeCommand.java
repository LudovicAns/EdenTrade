package fr.edencraft.edentrade.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.edencraft.edentrade.EdenTrade;
import fr.edencraft.edentrade.content.lang.Fr;
import fr.edencraft.edentrade.manager.ConfigurationManager;
import fr.edencraft.edentrade.trade.Trade;
import fr.edencraft.edentrade.trade.TradeBuildException;
import fr.edencraft.edentrade.trade.TradeBuilder;
import fr.edencraft.edentrade.utils.ColoredText;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;

@CommandAlias("edentrade")
public class EdenTradeCommand extends BaseCommand {

    private static final String basePermission = "edentrade.command";
    private static final ConfigurationManager cm = EdenTrade.getINSTANCE().getConfigurationManager();

    @Default
    @CommandPermission(basePermission)
    public static void onCommand(CommandSender commandSender) {
        commandSender.sendMessage(getHelpMessage());
    }

    @Subcommand("file")
    @CommandPermission(basePermission + ".file")
    @CommandCompletion("@players VanillaTrade.yml")
    public static void onCustomTrade(CommandSender sender, String playerName, String fileName) {
        FileConfiguration cfg = cm.getConfigurationFile(fileName);
        if (cfg == null) {
            sender.sendMessage("Error. Need to be added in " + Fr.class);

            cm.getFilesMap().forEach((file, fileConfiguration) -> {
                System.out.println(file.getName() + "|" + fileConfiguration.getName());
            });
            return;
        }

        TradeBuilder tradeBuilder = new TradeBuilder(cfg);
        Trade trade;

        try {
            trade = tradeBuilder.build();
        } catch (TradeBuildException e) {
            sender.sendMessage(e.getMessage());
            e.printStackTrace();
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        if (!offlinePlayer.isOnline()) return;

        Player player = (Player) offlinePlayer;

        // Todo: Check if player had required permission
        if (!playerHasRequiredPermissions(trade.getRequiredPermissions(), player)) {
            // Need perm
            player.sendMessage("No perm");
            return;
        }

        // Todo: Check if player had required item

        // Todo: Check if player had enough place in his inventory to give result items

        // Todo: Take all required items from player inventory

        // Todo: Give all result items to player inventory

        // Todo: Give result permissions to player

        // Todo: Well done !
    }

    private static boolean playerHasRequiredPermissions(Map<String, Boolean> requiredPermissions, Player player) {
        for (Map.Entry<String, Boolean> perm : requiredPermissions.entrySet()) {
            if (perm.getValue()) {
                if (!player.hasPermission(perm.getKey())) return false;
            } else {
                if (player.hasPermission(perm.getKey())) return false;
            }
        }
        return true;
    }

    private static String getHelpMessage() {
        FileConfiguration cfg = cm.getConfigurationFile("Fr.yml");
        StringBuilder builder = new StringBuilder();

        for (String line : cfg.getStringList("help-message")) {
            builder.append(new ColoredText(line).treat() + "\n");
        }

        return builder.toString();
    }

}
