package fr.edencraft.edentrade.utils;

import fr.edencraft.edentrade.EdenTrade;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class LuckPermsUtils {

    private static final LuckPerms luckPermsAPI = EdenTrade.getINSTANCE().getLuckPermsAPI();

    public static void addPlayerPermission(Player player, String permission) {
        Node permissionNode = Node.builder(permission).build();

        luckPermsAPI.getUserManager().modifyUser(player.getUniqueId(), (User user) -> {
            DataMutateResult result = user.data().add(permissionNode);
            if (!result.wasSuccessful()) {
                Bukkit.getLogger().log(Level.WARNING, user.getUsername() + " à déjà la permission: " + permission);
            }
        });
    }

    public static void removePlayerPermission(Player player, String permission) {
        Node permissionNode = Node.builder(permission).build();
        luckPermsAPI.getUserManager().modifyUser(player.getUniqueId(), user -> user.data().remove(permissionNode));
    }

}
