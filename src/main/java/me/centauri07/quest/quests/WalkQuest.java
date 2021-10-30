package me.centauri07.quest.quests;

import me.centauri07.quest.QuestPlugin;
import me.centauri07.quest.quest.Quest;
import me.centauri07.quest.quest.QuestProgress;
import me.centauri07.quest.utility.Style;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class WalkQuest extends Quest {
    public WalkQuest() {
        super("walk-quest", "Walk Quest");
    }

    @Override
    public void onQuestComplete(Player player) {
        player.sendMessage(Style.main(
                "Quests",
                String.format("You have successfully completed %s, You've walked %d blocks!", requestedName, questModel.quantity)
        ));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(
                QuestPlugin.getInstance(),
                () -> {
                    if (!event.isCancelled())
                        if (questModel.enabled)
                            if (QuestProgress.of(event.getPlayer(), name) == QuestProgress.ProgressType.PENDING)
                                if (event.getFrom().getX() != event.getTo().getX() && event.getFrom().getZ() != event.getTo().getZ()) {
                                    Player player = event.getPlayer();

                                    QuestProgress.incrementProgress(player, name);

                                    if (QuestProgress.countOf(player, name) >= questModel.quantity)
                                        complete(player);
                                }
                }
        );
    }
}
