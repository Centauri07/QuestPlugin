package me.centauri07.quest.quest;

import me.centauri07.quest.QuestPlugin;
import me.centauri07.quest.configuration.models.ItemStackModel;
import me.centauri07.quest.configuration.models.QuestModel;
import me.centauri07.quest.database.model.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Quest implements Listener {
    public final static List<Quest> QUESTS = new ArrayList<>();

    public final String name;
    public final String requestedName;

    public QuestModel questModel;
    public final QuestReward questReward;

    public Quest(@NotNull String name, @NotNull String requestedName) {
        this.name = name;
        this.requestedName = requestedName;

        this.questModel = QuestPlugin.getInstance().questsConfiguration.model.quests.stream().filter(
                model -> model.name != null && model.name.equals(this.name)
        ).findFirst().orElse(null);

        if (questModel == null) {
            questModel = new QuestModel();
            questModel.name = name;
            QuestPlugin.getInstance().questsConfiguration.model.quests.add(questModel);
        }

        questReward = new QuestReward()
                .ofItems(questModel.questReward.itemStacks.toArray(new ItemStackModel[0]))
                .ofLevel(questModel.questReward.level)
                .ofExp(questModel.questReward.exp);

        Bukkit.getPluginManager().registerEvents(this, QuestPlugin.getInstance());

        QUESTS.add(this);
    }

    public abstract void onQuestComplete(Player player);

    public void complete(Player player) {
        player.getInventory().addItem(questReward.items.toArray(new ItemStack[0]));
        player.giveExp(questReward.exp);
        player.giveExpLevels(questReward.level);
        PlayerModel.of(player).complete(name);
        onQuestComplete(player);
    }

    public static Quest of(String name) {
        return QUESTS.stream().filter(quest -> quest.name.equals(name)).findFirst().orElse(null);
    }
}