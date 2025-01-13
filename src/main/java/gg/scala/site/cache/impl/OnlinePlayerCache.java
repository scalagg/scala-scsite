package gg.scala.site.cache.impl;

import com.google.gson.JsonObject;
import gg.scala.site.SiteApplication;
import gg.scala.site.SiteConstant;
import gg.scala.site.cache.type.RepeatingCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OnlinePlayerCache extends RepeatingCache<Map<String, String>> {

    public OnlinePlayerCache() {
        super(TimeUnit.SECONDS.toMillis(10));
    }

    @Override
    public void execute() {
        Map<String, String> onlinePlayers = new HashMap<>();
        SiteApplication.INSTANCE.getRedisService().executeCommand(jedis -> {
            Map<String, String> heartbeats = jedis.hgetAll("serversync:heartbeats");

            jedis.hgetAll("serversync:serversv2").forEach((server, data) -> {
                JsonObject object = SiteConstant.GSON.fromJson(data, JsonObject.class);

                if (heartbeats.containsKey(server) && System.currentTimeMillis() > Long.parseLong(heartbeats.get(server))) {
                    return;
                }

                object.getAsJsonObject("properties")
                        .getAsJsonArray("metadata")
                        .forEach(jsonElement -> {
                            JsonObject metadata = jsonElement.getAsJsonObject();
                            if (metadata.get("first").getAsString().equals("server:online-list")) {
                                var values = metadata.getAsJsonObject("second").getAsJsonArray("value");
                                values.forEach(jsonElement1 -> {
                                    onlinePlayers.put(jsonElement1.getAsString(), server);
                                });
                            }
                        });
            });

            return null;
        });

        cache(onlinePlayers);
    }

    @Override
    public Map<String, String> getDefaultValue() {
        return new HashMap<>();
    }
}
