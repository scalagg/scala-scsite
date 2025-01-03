package gg.scala.site.cache.leaderboard;

import gg.scala.site.model.leaderboard.LeaderboardModel;
import redis.clients.jedis.Jedis;

public interface LeaderboardType {
    LeaderboardModel collect(Jedis redis);
}
