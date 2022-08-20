package fr.edencraft.edentrade.content.lang;

public class Fr {

    public static final String CONTENT = """
    # Version of the configuration file. Useless for now but in future that will allow you to add automatically missing
    # sections based on a specific version.
    # This is not linked to the plugin version.
    config-version: 1.0.0
    
    help-message:
      - "&3========== &6EdenTrade Aide &3=========="
      - "&f- &b/edentrade file <joueur> <trade.yml> &7: &eRéalise le trade 'trade.yml' pour le joueur donné."
      - "&3========== &6EdenTrade Aide &3=========="
      
    unknown-trade-file: "&cLe fichier de trade {0} n'existe pas."
    missing-trade-permissions: "&cVous n'avez pas les permissions pour réaliser cet échange."
    missing-trade-items: "&cIl vous manque des items pour réaliser cet échange."
    missing-inventory-space: "&cVous n'avez pas assez de place dans votre inventaire pour réaliser cet échange."
    """;

}
