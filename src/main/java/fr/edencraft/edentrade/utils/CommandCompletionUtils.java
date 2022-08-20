package fr.edencraft.edentrade.utils;

import fr.edencraft.edentrade.EdenTrade;
import fr.edencraft.edentrade.manager.ConfigurationManager;

import java.util.ArrayList;
import java.util.List;

public class CommandCompletionUtils {

    public static List<String> getConfigurationFilesName() {
        List<String> cfgList = new ArrayList<>();
        ConfigurationManager configurationManager = EdenTrade.getINSTANCE().getConfigurationManager();
        configurationManager.getFilesMap().forEach((file, fileConfiguration) -> cfgList.add(file.getName()));
        return cfgList;
    }

    public static List<String> getTradesFile() {
        List<String> tradesFileList = new ArrayList<>();
        ConfigurationManager cm = EdenTrade.getINSTANCE().getConfigurationManager();
        cm.getFilesMap().forEach((file, fileConfiguration) -> {
            if (fileConfiguration.contains("result")) {
                tradesFileList.add(file.getName());
            }
        });
        return tradesFileList;
    }

}
