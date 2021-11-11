package locb.both.guildbattles.data;

import locb.both.guildbattles.GuildBattles;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YAML {

    private File file;
    private FileConfiguration config;

    public YAML(String filename) {
        file = new File(GuildBattles.getInstance().getDataFolder(), filename);
        try {
            if( !file.exists() && file.createNewFile() ) throw new IOException();
        } catch (IOException e){
            throw new RuntimeException("Failed to create file", e);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void  save(){
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }
}
