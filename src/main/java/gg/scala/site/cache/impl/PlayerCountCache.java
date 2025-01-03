package gg.scala.site.cache.impl;

import gg.scala.site.SiteApplication;
import gg.scala.site.cache.type.RepeatingCache;
import gg.scala.site.redis.RedisCommand;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

public class PlayerCountCache extends RepeatingCache<Integer> {

    public PlayerCountCache() {
        super(TimeUnit.SECONDS.toMillis(5));
    }

    @Override
    public void execute() {
        String globalPlayerCount = SiteApplication.INSTANCE.getRedisService().executeCommand(
                redis -> redis.get("global-player-count")
        );

        cache(Integer.valueOf(globalPlayerCount));
    }

    @Override
    public Integer getDefaultValue() {
        return 0;
    }

}