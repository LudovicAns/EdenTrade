package fr.edencraft.edentrade.trade;

import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Trade {

    private String name = null;

    private Set<ItemStack> requiredItems;
    private Map<String, Boolean> requiredPermissions;

    private Set<ItemStack> resultItems;
    private Map<String, Boolean> resultPermissions;

    private Set<String> resultCommands;
    private int extraSlotNeeded;

    private boolean init = false;

    public Trade(String name,
                 Set<ItemStack> requiredItems,
                 Map<String, Boolean> requiredPermissions,
                 Set<ItemStack> resultItems,
                 Map<String, Boolean> resultPermissions,
                 Set<String> resultCommands,
                 int extraSlotNeeded) {
        this.name = name;
        this.requiredItems = requiredItems;
        this.requiredPermissions = requiredPermissions;
        this.resultItems = resultItems;
        this.resultPermissions = resultPermissions;
        this.resultCommands = resultCommands;
        this.extraSlotNeeded = extraSlotNeeded;
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

    public Set<String> getResultCommands() {
        return resultCommands;
    }

    public int getExtraSlotNeeded() {
        return extraSlotNeeded;
    }
}
