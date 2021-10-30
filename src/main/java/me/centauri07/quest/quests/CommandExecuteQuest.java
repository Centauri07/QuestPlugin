package me.centauri07.quest.quests;

import me.centauri07.quest.QuestPlugin;
import me.centauri07.quest.quest.Quest;
import me.centauri07.quest.quest.QuestProgress;
import me.centauri07.quest.utility.Style;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandExecuteQuest extends Quest {
    public CommandExecuteQuest() {
        super("command-execute-quest", "Command Execute Quest");
    }

    @Override
    public void onQuestComplete(Player player) {
        player.sendMessage(Style.main(
                "Quests",
                String.format("You have successfully completed %s, You've executed commands %d times!", requestedName, questModel.quantity)
        ));
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(
                QuestPlugin.getInstance(),
                () -> {
                    if (!event.isCancelled())
                        if (questModel.enabled)
                            if (QuestProgress.of(event.getPlayer(), name) == QuestProgress.ProgressType.PENDING) {
                                Player player = event.getPlayer();

                                QuestProgress.incrementProgress(player, name);

                                if (QuestProgress.countOf(player, name) >= questModel.quantity)
                                    complete(player);
                            }
                }
        );
    }
}
