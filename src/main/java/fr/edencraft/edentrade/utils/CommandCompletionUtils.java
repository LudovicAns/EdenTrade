package fr.edencraft.edentrade.utils;

import fr.edencraft.edentrade.EdenTrade;
import fr.edencraft.edentrade.manager.ConfigurationManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
        char sep = File.separatorChar;
        File file = new File(EdenTrade.getINSTANCE().getDataFolder().getAbsolutePath() + sep + "trades");
        Arrays.stream(file.listFiles()).forEach(f -> tradesFileList.add(f.getName()));
        return tradesFileList;
    }

}
