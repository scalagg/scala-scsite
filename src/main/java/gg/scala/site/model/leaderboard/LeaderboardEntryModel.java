package gg.scala.site.model.leaderboard;

import com.google.gson.JsonObject;
import gg.scala.site.util.uuid.UUIDCache;
import lombok.Data;

import java.util.UUID;

@Data
public class LeaderboardEntryModel {

    private final UUID uuid;
    private final String value;
    private final String webColor;

    private String resolvedName;

    public LeaderboardEntryModel(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.value = object.get("value").getAsString();
        this.resolvedName = object.get("resolvedName").getAsString();
        this.webColor = object.get("webColor").getAsString();

        if (resolvedName.equalsIgnoreCase("N/A"))
            resolvedName = UUIDCache.getName(uuid);
    }

}