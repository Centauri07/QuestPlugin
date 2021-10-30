package me.centauri07.quest.listeners;

import me.centauri07.quest.database.model.PlayerModel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerModel playerModel = PlayerModel.create(event.getPlayer());
        if (playerModel != null) {
            playerModel.quests.forEach(
                    questModel -> playerModel.start(questModel.questName)
            );
        }
    }
}
