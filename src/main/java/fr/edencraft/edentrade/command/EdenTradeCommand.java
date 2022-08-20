package fr.edencraft.edentrade.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lone.itemsadder.api.CustomStack;
import fr.edencraft.edentrade.EdenTrade;
import fr.edencraft.edentrade.manager.ConfigurationManager;
import fr.edencraft.edentrade.trade.Trade;
import fr.edencraft.edentrade.trade.TradeBuildException;
import fr.edencraft.edentrade.trade.TradeBuilder;
import fr.edencraft.edentrade.utils.ColoredText;
import fr.edencraft.edentrade.utils.LuckPermsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

@CommandAlias("edentrade")
public class EdenTradeCommand extends BaseCommand {

    private static final String basePermission = "edentrade.command";
    private static final ConfigurationManager cm = EdenTrade.getINSTANCE().getConfigurationManager();

    @Default
    @CommandPermission(basePermission)
    public static void onCommand(CommandSender commandSender) {
        commandSender.sendMessage(getHelpMessage());
    }

    @Subcommand("reload|rl")
    @Syntax("[fileName]")
    @CommandCompletion("@etreload")
    @CommandPermission(basePermission + ".reload")
    public static void onReload(CommandSender sender, @Optional String fileName){
        FileConfiguration lang = cm.getConfigurationFile("Fr.yml");
        if (fileName != null && !fileName.isEmpty()) {
            if (cm.getConfigurationFile(fileName) != null) {
                cm.reloadFile(fileName);
                sender.sendMessage(new ColoredText(
                        lang.getString("reload-file")
                                .replaceAll("\\{filename}", fileName))
                        .treat());
            } else {
                sender.sendMessage(new ColoredText(
                        lang.getString("unknown-file")
                                .replaceAll("\\{filename}", fileName))
                        .treat());
            }
        } else {
            cm.reloadFiles();
            sender.sendMessage(new ColoredText(lang.getString("reload-all")).treat());
        }
    }

    @Subcommand("file")
    @CommandPermission(basePermission + ".file")
    @CommandCompletion("@players @ettradesfile")
    public static void onCustomTrade(CommandSender sender, String playerName, String fileName) {
        FileConfiguration cfg = cm.getConfigurationFile(fileName);
        FileConfiguration lang = cm.getConfigurationFile("Fr.yml");
        if (cfg == null) {
            sender.sendMessage(
                    new ColoredText(lang.getString("unknown-trade-file").replaceAll("\\{0}", fileName)).treat()
            );
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

        if (!playerHadRequiredPermissions(trade.getRequiredPermissions(), player)) {
            player.sendMessage(
                    new ColoredText(lang.getString("missing-trade-permissions")).treat()
            );
            return;
        }

        if (!playerHadRequiredItems(trade.getRequiredItems(), player)) {
            player.sendMessage(
                    new ColoredText(lang.getString("missing-trade-items")).treat()
            );
            return;
        }

        if (!playerHadEnoughPlace(trade.getResultItems().size() + trade.getExtraSlotNeeded(), player.getInventory())) {
            player.sendMessage(
                    new ColoredText(lang.getString("missing-inventory-space")).treat()
            );
            return;
        }
        removeRequiredItemsFromPlayer(trade.getRequiredItems(), player);
        giveResultItemToPlayer(trade.getResultItems(), trade.getResultCommands(), player);
        giveResultPermissionToPlayer(trade.getResultPermissions(), player);
    }

    private static void giveResultPermissionToPlayer(Map<String, Boolean> resultPermissions, Player player) {
        resultPermissions.forEach((key, value) -> {
            if (value && !player.hasPermission(key)) LuckPermsUtils.addPlayerPermission(player, key);
            else LuckPermsUtils.removePlayerPermission(player, key);
        });
    }

    private static void giveResultItemToPlayer(Set<ItemStack> resultItems, Set<String> resultCommands, Player player) {
        resultItems.forEach(itemStack -> player.getInventory().addItem(itemStack));
        resultCommands.forEach(command -> Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(), command.replaceAll("\\{player}", player.getName())
        ));
    }

    private static void removeRequiredItemsFromPlayer(Set<ItemStack> requiredItems, Player player) {
        for (ItemStack item : requiredItems) {
            boolean isItemsAdderItem = CustomStack.byItemStack(item) != null;
            int amountNeeded = item.getAmount();
            @Nullable ItemStack[] contents = player.getInventory().getStorageContents();

            for (ItemStack inventoryItem : contents) {
                if (amountNeeded <= 0) break;
                if (inventoryItem == null || inventoryItem.getType().equals(Material.AIR)) continue;

                if (isItemsAdderItem && CustomStack.byItemStack(inventoryItem) != null) {
                    if (isEqual(CustomStack.byItemStack(item), CustomStack.byItemStack(inventoryItem))) {
                        int amount = inventoryItem.getAmount();
                        if (amount < amountNeeded) inventoryItem.setAmount(0);
                        else inventoryItem.setAmount(amount - amountNeeded);
                        amountNeeded -= amount;
                    }
                } else if (!isItemsAdderItem && CustomStack.byItemStack(inventoryItem) == null) {
                    if (isEqual(item, inventoryItem)) {
                        int amount = inventoryItem.getAmount();
                        if (amount < amountNeeded) inventoryItem.setAmount(0);
                        else inventoryItem.setAmount(amount - amountNeeded);
                        amountNeeded -= amount;
                    }
                }
            }
        }
    }

    private static boolean playerHadEnoughPlace(int slotNeeded, PlayerInventory playerInventory) {
        return calculateFreeSlot(playerInventory) >= slotNeeded;
    }

    private static int calculateFreeSlot(PlayerInventory playerInventory) {
        int count = 0;
        for (ItemStack item : playerInventory.getStorageContents()) {
            if (item == null) count++;
        }
        return count;
    }

    private static boolean playerHadRequiredItems(Set<ItemStack> requireditems, Player player) {
        for (ItemStack item : requireditems) {
            boolean isItemsAdderItem = CustomStack.byItemStack(item) != null;
            int amountNeeded = item.getAmount();
            @Nullable ItemStack[] contents = player.getInventory().getStorageContents();

            for (ItemStack inventoryItem : contents) {
                if (amountNeeded <= 0) break;
                if (inventoryItem == null || inventoryItem.getType().equals(Material.AIR)) continue;

                if (isItemsAdderItem && CustomStack.byItemStack(inventoryItem) != null) {
                    if (isEqual(CustomStack.byItemStack(item), CustomStack.byItemStack(inventoryItem))) {
                        int amount = inventoryItem.getAmount();
                        amountNeeded -= amount;
                    }
                } else if (!isItemsAdderItem && CustomStack.byItemStack(inventoryItem) == null) {
                    if (isEqual(item, inventoryItem)) {
                        int amount = inventoryItem.getAmount();
                        amountNeeded -= amount;
                    }
                }
            }

            if (amountNeeded > 0) return false;
        }
        return true;
    }

    private static boolean isEqual(ItemStack item1, ItemStack item2) {
        if (!item1.getType().equals(item2.getType())) return false;
        if (item1.getItemMeta().hasCustomModelData() && item2.getItemMeta().hasCustomModelData()) {
            return item1.getItemMeta().getCustomModelData() == item2.getItemMeta().getCustomModelData();
        } else return !item1.getItemMeta().hasCustomModelData() && !item2.getItemMeta().hasCustomModelData();
        // Add more comparaison later (like displayname, lore, etc).
    }

    private static boolean isEqual(CustomStack item1, CustomStack item2) {
        return item1.getId().equals(item2.getId());
    }

    private static boolean playerHadRequiredPermissions(Map<String, Boolean> requiredPermissions, Player player) {
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
            builder.append(new ColoredText(line).treat()).append("\n");
        }

        return builder.toString();
    }

}
