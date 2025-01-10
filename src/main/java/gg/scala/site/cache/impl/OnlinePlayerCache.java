package gg.scala.site.cache.impl;

import com.google.gson.JsonObject;
import gg.scala.site.SiteApplication;
import gg.scala.site.SiteConstant;
import gg.scala.site.cache.AbstractCache;
import gg.scala.site.cache.type.RepeatingCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OnlinePlayerCache extends RepeatingCache<List<String>> {

    public OnlinePlayerCache() {
        super(TimeUnit.SECONDS.toMillis(10));
    }

    @Override
    public void execute() {
        List<String> onlinePlayers = new ArrayList<>();
        SiteApplication.INSTANCE.getRedisService().executeCommand(jedis -> {
            jedis.hgetAll("serversync:serversv2").forEach((server, data) -> {
                JsonObject object = SiteConstant.GSON.fromJson(data, JsonObject.class);

                object.getAsJsonObject("properties")
                        .getAsJsonArray("metadata")
                        .forEach(jsonElement -> {
                            JsonObject metadata = jsonElement.getAsJsonObject();
                            if (metadata.get("first").getAsString().equals("server:online-list")) {
                                var values = metadata.getAsJsonObject("second").getAsJsonArray("value");
                                values.forEach(jsonElement1 -> onlinePlayers.add(jsonElement1.getAsString()));
                            }
                        });
            });

            return null;
        });

        cache(onlinePlayers);
    }

    @Override
    public List<String> getDefaultValue() {
        return List.of();
    }
}
