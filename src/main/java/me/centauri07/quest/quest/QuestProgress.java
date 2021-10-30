package me.centauri07.quest.quest;

import me.centauri07.quest.database.model.PlayerModel;
import org.bukkit.entity.Player;

public class QuestProgress {
    public static void incrementProgress(Player player, String questName) {
        PlayerModel.of(player).getQuest(questName).progress += 1;
    }

    public static int countOf(Player player, String questName) {
        return PlayerModel.of(player).getQuest(questName).progress;
    }

    public static ProgressType of(Player player, String questName) {
        return PlayerModel.of(player).getQuest(questName).status;
    }

    public enum ProgressType {
        NOT_STARTED, PENDING, COMPLETED
    }
}
