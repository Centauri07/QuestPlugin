package me.centauri07.quest.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.centauri07.quest.QuestPlugin;
import me.centauri07.quest.database.models.PlayerModel;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Database {
    public Database() {
        ConnectionString connectionString = new ConnectionString(String.format("mongodb+srv://%s:%s@cluster0.z7fbg.mongodb.net/myFirstDatabase?retryWrites=true&w=majority",
                QuestPlugin.getInstance().databaseConfiguration.model.username, QuestPlugin.getInstance().databaseConfiguration.model.password));

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                .codecRegistry(
                        CodecRegistries.fromRegistries(
                                MongoClientSettings.getDefaultCodecRegistry(),
                                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
                        )
                )
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("player");

        playerModelCollection = getOrCreateCollection("player-model", PlayerModel.class);
    }

    public final MongoDatabase database;

    public final MongoCollection<PlayerModel> playerModelCollection;

    public <T> MongoCollection<T> getOrCreateCollection(String name, Class<T> type) {
        if (!database.listCollectionNames().into(new ArrayList<>()).contains(name)) {
            database.createCollection(name);
        }

        return database.getCollection(name, type);
    }

    public void save() {
        if (PlayerModel.PLAYER_MODELS.isEmpty()) return;

        List<UUID> toRemove = new ArrayList<>();
        
        PlayerModel.PLAYER_MODELS.forEach(
                pm -> {
                    playerModelCollection.replaceOne(Filters.eq("_id", pm.id), PlayerModel.of(pm.id));
                    if (Bukkit.getPlayer(pm.id) == null) toRemove.add(pm.id);
                }
        );
        
        toRemove.forEach(
                uuid -> PlayerModel.PLAYER_MODELS.remove(PlayerModel.of(uuid))
        );
    }
}