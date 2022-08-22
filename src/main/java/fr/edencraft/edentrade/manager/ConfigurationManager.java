package fr.edencraft.edentrade.manager;

import fr.edencraft.edentrade.EdenTrade;
import fr.edencraft.edentrade.content.lang.Fr;
import fr.edencraft.edentrade.content.trade.CustomTrade;
import fr.edencraft.edentrade.content.trade.VanillaTrade;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public class ConfigurationManager {

    private final Plugin plugin;
    private final Map<File, FileConfiguration> filesMap = new HashMap<>();

    public ConfigurationManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void setupFiles() {
        if (!plugin.getDataFolder().exists()) {
            if (plugin.getDataFolder().mkdir()) {
                log(Level.INFO, "✅ DataFolder has been created.");
            } else {
                log(Level.SEVERE, "⚠️  DataFolder can't be created.");
            }
        } else {
            log(Level.INFO, "✅ DataFolder loaded successfully.");
        }
        initNewFile("lang", Fr.class.getSimpleName() + ".yml", Fr.CONTENT);
        initNewFile("trades", VanillaTrade.class.getSimpleName() + ".yml", VanillaTrade.CONTENT);
        initNewFile("trades", CustomTrade.class.getSimpleName() + ".yml", CustomTrade.CONTENT);
        loadTradeFiles();
    }


    public void loadTradeFiles() {
        char sep = File.separatorChar;
        File file = new File(EdenTrade.getINSTANCE().getDataFolder().getAbsolutePath() + sep + "trades");
        Arrays.stream(file.listFiles()).forEach(f -> {
            YamlConfiguration fileCFG = YamlConfiguration.loadConfiguration(f);
            filesMap.put(f, fileCFG);
        });
    }


    /**
     * @param directory   the directory to put the new file. If the directory does not exist it will be
     *                    automatically create. Just put "." if you want to add it directly inside the data-folder.
     * @param fileName    the name of your new file.
     * @param fileContent the content oh the file.
     */
    public void initNewFile(String directory, String fileName, String fileContent) {
        char sep = File.separatorChar;
        File directoryFile = new File(plugin.getDataFolder().getAbsolutePath() + sep + directory);
        if (directoryFile.mkdirs()) {
            log(Level.INFO, "✅ The directory " + directory + " has been created successfully.");
        } else {
            log(Level.INFO, "✅ The directory " + directory + " has been loaded successfully.");
        }


        File file = new File(directoryFile.getAbsolutePath() + sep + fileName);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    log(Level.INFO, "✅ The configuration file " + fileName + " has been created successfully.");
                } else {
                    log(Level.INFO, "✅ The configuration file " + fileName + " has been loaded successfully.");
                }
            } catch (IOException ignored) {
            }

            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                writer.write(fileContent);
                writer.flush();
            } catch (IOException ignored) {
            }
        } else {
            log(Level.INFO, "✅ The configuration file " + fileName + " has been loaded successfully.");
        }

        YamlConfiguration fileCFG = YamlConfiguration.loadConfiguration(file);
        filesMap.put(file, fileCFG);
    }

    public void saveFiles() {
        filesMap.forEach((file, fileConfiguration) -> {
            try {
                fileConfiguration.save(file);
            } catch (IOException ignored) {
            }
        });
    }

    public void reloadFile(String name) {
        if (getConfigurationFile(name) == null) return;
        filesMap.forEach((file, fileConfiguration) -> {
            if (file.getName().equalsIgnoreCase(name)) {
                try {
                    fileConfiguration.load(file);
                } catch (IOException | InvalidConfigurationException ignored) {
                }
            }
        });
    }

    public FileConfiguration getConfigurationFile(String name) {
        AtomicReference<FileConfiguration> fileCFG = new AtomicReference<>(null);
        filesMap.forEach((file, fileConfiguration) -> {
            if (file.getName().equalsIgnoreCase(name)) {
                fileCFG.set(fileConfiguration);
            }
        });
        return fileCFG.get();
    }

    private void log(Level level, String message) {
        EdenTrade.getINSTANCE().log(level, message);
    }

    public Map<File, FileConfiguration> getFilesMap() {
        return filesMap;
    }

    public void reloadFiles() {
        filesMap.forEach((file, fileConfiguration) -> {
            try {
                fileConfiguration.load(file);
            } catch (IOException | InvalidConfigurationException ignored) {
            }
        });
    }
}

