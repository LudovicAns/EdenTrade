package fr.edencraft.edentrade.trade;

import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Trade {

    private String name = null;

    private Set<ItemStack> requiredItems;
    private Map<String, Boolean> requiredPermissions;

    private Set<ItemStack> resultItems;
    private Map<String, Boolean> resultPermissions;

    private boolean init = false;

    public Trade(String name,
                 Set<ItemStack> requiredItems,
                 Map<String, Boolean> requiredPermissions,
                 Set<ItemStack> resultItems,
                 Map<String, Boolean> resultPermissions) {
        this.name = name;
        this.requiredItems = requiredItems;
        this.requiredPermissions = requiredPermissions;
        this.resultItems = resultItems;
        this.resultPermissions = resultPermissions;
    }

    public boolean isInit() {
        return init;
    }

    public String getName() {
        return name;
    }

    public Set<ItemStack> getRequiredItems() {
        return requiredItems;
    }

    public Map<String, Boolean> getRequiredPermissions() {
        return requiredPermissions;
    }

    public Set<ItemStack> getResultItems() {
        return resultItems;
    }

    public Map<String, Boolean> getResultPermissions() {
        return resultPermissions;
    }
}
