package me.centauri07.quest.database.model;

import com.mongodb.client.model.Filters;
import me.centauri07.quest.QuestPlugin;
import me.centauri07.quest.quest.Quest;
import me.centauri07.quest.quest.QuestProgress;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerModel {
    @BsonIgnore
    public static final List<PlayerModel> PLAYER_MODELS = new ArrayList<>();

    @BsonId
    public UUID id;

    public List<QuestModel> quests = new ArrayList<>();

    public static PlayerModel of(Player player) {
        return of(player.getUniqueId());
    }

    public static PlayerModel of(UUID uuid) {
        return PLAYER_MODELS.stream().filter(playerModel -> playerModel.id.equals(uuid)).findFirst().orElse(null);
    }

    public static PlayerModel create(Player player) {
        return create(player.getUniqueId());
    }

    public static PlayerModel create(UUID uuid) {
        PlayerModel pm = load(uuid);

        if (pm == null) {
            pm = new PlayerModel();
            pm.id = uuid;
            PlayerModel finalPm = pm;
            Quest.QUESTS.forEach(
                    quest -> {
                        QuestModel questModel = new QuestModel();
                        questModel.questName = quest.name;
                        questModel.progress = 0;
                        questModel.status = QuestProgress.ProgressType.PENDING;

                        finalPm.quests.add(questModel);
                    }
            );

            QuestPlugin.getInstance().database.playerModelCollection.insertOne(pm);

            PLAYER_MODELS.add(pm);
        }

        return pm;
    }

    public static PlayerModel load(Player player) {
        return load(player.getUniqueId());
    }

    public static PlayerModel load(UUID uuid) {
        PlayerModel playerModel = QuestPlugin.getInstance().database.playerModelCollection.find(
                Filters.eq("_id", uuid)
        ).first();

        if (playerModel != null)
            if (!PLAYER_MODELS.contains(playerModel)) PLAYER_MODELS.add(playerModel);

        return playerModel;
    }

    public QuestModel getQuest(String name) {
        return quests.stream().filter(questModel -> questModel.questName.equals(name)).findFirst().orElse(null);
    }

    public void complete(String questName) {
        getQuest(questName).status = QuestProgress.ProgressType.COMPLETED;
    }

    public void start(String questName) {
        if (getQuest(questName).status != QuestProgress.ProgressType.COMPLETED && getQuest(questName).status != QuestProgress.ProgressType.PENDING)
            getQuest(questName).status = QuestProgress.ProgressType.PENDING;

    }
}