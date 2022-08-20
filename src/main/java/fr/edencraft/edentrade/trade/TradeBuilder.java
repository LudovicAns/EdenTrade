package fr.edencraft.edentrade.trade;

import dev.lone.itemsadder.api.CustomStack;
import fr.edencraft.edentrade.EdenTrade;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.*;

public class TradeBuilder {

    private final FileConfiguration cfg;
    private final String name;

    private final List<ItemStack> requiredItems = new ArrayList<>();
    private final Map<String, Boolean> requiredPermissions = new HashMap<>();

    private final List<ItemStack> resultItems = new ArrayList<>();
    private final Map<String, Boolean> resultPermissions = new HashMap<>();

    private final List<String> resultCommands = new ArrayList<>();
    private int extraSlotNeeded = 0;

    public TradeBuilder(FileConfiguration cfg) {
        this.cfg = cfg;
        this.name = cfg.getName();
    }

    public Trade build() throws TradeBuildException {
        // Check valid CFG.
        if (cfg == null) {
            throw new TradeBuildException("Configuration file is null.", this);
        }

        // Check valid name.
        if (!isUsableName()) {
            throw new TradeBuildException("Name already used: " + this.name, this);
        }

        for (String key : cfg.getConfigurationSection("requierments.items").getKeys(false)) {
            ConfigurationSection section = cfg.getConfigurationSection("requierments.items." + key);
            ItemStack item = getItemByConfigurationSection(section);
            if (item == null) {
                throw new TradeBuildException("Items " + key + " can't be loaded.", this);
            }
            requiredItems.add(item);
        }

        if (cfg.contains("requierments.permissions")) {
            List<String> permissions = cfg.getStringList("requierments.permissions");
            for (String permission : permissions) {
                Map.Entry<String, Boolean> entry = parsePermission(permission);
                if (entry.getKey() != null) requiredPermissions.put(entry.getKey(), entry.getValue());
            }
        }

        for (String key : cfg.getConfigurationSection("result.items").getKeys(false)) {
            ConfigurationSection section = cfg.getConfigurationSection("result.items." + key);
            if (section.contains("command")) {
                resultCommands.add(section.getString("command"));
                extraSlotNeeded += section.getInt("amount");
            } else {
                ItemStack item = getItemByConfigurationSection(section);
                if (item == null) {
                    throw new TradeBuildException("Items " + key + " can't be loaded.", this);
                }
                resultItems.add(item);
            }
        }

        if (cfg.contains("result.permissions")) {
            List<String> permissions = cfg.getStringList("result.permissions");
            for (String permission : permissions) {
                Map.Entry<String, Boolean> entry = parsePermission(permission);
                if (entry.getKey() != null) resultPermissions.put(entry.getKey(), entry.getValue());
            }
        }

        return new Trade(
                this.name,
                Set.copyOf(this.requiredItems),
                new HashMap<>(this.requiredPermissions),
                Set.copyOf(this.resultItems),
                new HashMap<>(this.resultPermissions),
                Set.copyOf(this.resultCommands),
                extraSlotNeeded
        );
    }

    private Map.Entry<String, Boolean> parsePermission(String permission) {
        String key;
        boolean value = true;

        if (permission.startsWith("!")) {
            value = false;
            key = permission.substring(1);
        } else {
            key = permission;
        }

        return new AbstractMap.SimpleEntry<>(key, value);
    }

    private boolean isUsableName() {
        List<Trade> trades = EdenTrade.getINSTANCE().getTrades();
        return trades.stream().noneMatch(trade -> trade.getName().equalsIgnoreCase(name));
    }

    /*
    Section pattern:

        Case of classic/textured item.
        item: 'IGNORED'
        material: leather
        model-id: 10000
        amount: 16

        Case of plugin custom item like itemsadder:
        item: 'itemsadder:namespace__itemname'
        material: 'IGNORED'
        model-id: 'IGNORED'
        amount: 16
     */
    private @Nullable ItemStack getItemByConfigurationSection(ConfigurationSection section) {
        if (isCustomItem(section)) return buildCustomItem(section);
        return buildClassicItem(section);
    }

    private boolean isCustomItem(ConfigurationSection section) {
        return section.contains("item") && !section.getString("item").isEmpty();
    }

    private ItemStack buildClassicItem(ConfigurationSection section) {
        String materialName = section.getString("material", Material.STONE.name());
        int modelId = section.getInt("model-id");
        int amount = section.getInt("amount", 1);

        Material material = Material.getMaterial(materialName.toUpperCase());
        material = material == null ? Material.STONE : material;

        ItemStack itemStack = new ItemStack(material, amount);
        if (modelId != 0) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(modelId);
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    private @Nullable ItemStack buildCustomItem(ConfigurationSection section) {
        String itemPath = section.getString("item");

        // Because we only use itemsadder item.
        if (!itemPath.split(":")[0].equalsIgnoreCase("itemsadder")) {
            // Throw error ? Invalid itemPath.
            return null;
        }

        // Parse itemPath.
        String namespaceName = itemPath.split(":")[1].split("__")[0];
        String itemName = itemPath.split(":")[1].split("__")[1];

        if (CustomStack.getInstance(namespaceName + ":" + itemName).getItemStack() == null) {
            // Throw error ? Invalid custom item.
            return null;
        }

        CustomStack customStack = CustomStack.getInstance(namespaceName + ":" + itemName);

        return customStack.getItemStack();
    }

}
