package me.centauri07.quest.quests;

import me.centauri07.quest.QuestPlugin;
import me.centauri07.quest.quest.Quest;
import me.centauri07.quest.quest.QuestProgress;
import me.centauri07.quest.utility.Style;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillEntityQuest extends Quest {
    public KillEntityQuest() {
        super("kill-entity-quest", "Kill Entity Quest");
    }

    @Override
    public void onQuestComplete(Player player) {
        player.sendMessage(Style.main(
                "Quests",
                String.format("You have successfully completed %s, You've killed %d entities!", requestedName, questModel.quantity)
        ));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(
                QuestPlugin.getInstance(),
                () -> {
                    if (questModel.enabled)
                        if (event.getEntity().getKiller() != null)
                            if (QuestProgress.of(event.getEntity().getKiller(), name) == QuestProgress.ProgressType.PENDING) {
                                Player player = event.getEntity().getKiller();

                                QuestProgress.incrementProgress(player, name);

                                if (QuestProgress.countOf(player, name) >= questModel.quantity)
                                    complete(player);
                            }
                }
        );
    }
}
