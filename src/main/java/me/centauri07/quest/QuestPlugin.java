package me.centauri07.quest;

import me.centauri07.quest.configuration.Configuration;
import me.centauri07.quest.configuration.models.DatabaseModel;
import me.centauri07.quest.configuration.models.QuestsModel;
import me.centauri07.quest.database.Database;
import me.centauri07.quest.listeners.PlayerJoinListener;
import me.centauri07.quest.quests.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class QuestPlugin extends JavaPlugin {
    private static QuestPlugin instance;

    public Configuration<QuestsModel> questsConfiguration;
    public Configuration<DatabaseModel> databaseConfiguration;

    public Database database;

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists()) getDataFolder().mkdirs();

        databaseConfiguration =
                new Configuration<>(getDataFolder(), new DatabaseModel(), "database-config");

        questsConfiguration =
                new Configuration<>(getDataFolder(), new QuestsModel(), "quests-config");

        new BlockBreakQuest();
        new BlockPlaceQuest();
        new CommandExecuteQuest();
        new KillEntityQuest();
        new WalkQuest();

        if (databaseConfiguration.model.username == null || databaseConfiguration.model.password == null) {
            Bukkit.getLogger().severe("Quest Plugin | Database username or password is null. Please change it in \"database.json\"");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        database = new Database();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(
                this, () -> Configuration.configurations.forEach(Configuration::save), 0, 5 * 20
        );

        Bukkit.getScheduler().runTaskTimerAsynchronously(
                this, () -> database.save(), 0, 5 * 20
        );
    }

    public static QuestPlugin getInstance() {
        return instance;
    }
}