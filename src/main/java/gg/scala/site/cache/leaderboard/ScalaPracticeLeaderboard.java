package gg.scala.site.cache.leaderboard;

import gg.scala.site.model.leaderboard.LeaderboardEntryModel;
import gg.scala.site.model.leaderboard.LeaderboardModel;
import gg.scala.site.util.ColorUtil;
import gg.scala.site.util.uuid.UUIDCache;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScalaPracticeLeaderboard implements LeaderboardType {
    private static final String BASE_KEY = "tropicpractice:leaderboards:";
    private static final String END_KEY = ":global:final";

    private static final String[] STAT_TYPES = new String[] {
            "RankedWins",
            "RankedWinStreak",
            "UnrankedWinStreak",
            "ELO",
            "UnrankedWins"
    };

    @Override
    public LeaderboardModel collect(Jedis redis) {
        var entries = new HashMap<String, List<LeaderboardEntryModel>>();

        for (var statType : STAT_TYPES) {
            List<LeaderboardEntryModel> entryModels = new ArrayList<>();

            var key = BASE_KEY + statType + END_KEY;
            var values = redis.zrangeWithScores(key, 0, -1);

            int i = 0;
            for (var entry : values) {
                if (i >= 10)
                    break;

                entryModels.add(new LeaderboardEntryModel(
                        UUID.fromString(entry.getElement()),
                        entry.getScore() + "",
                        ColorUtil.getHexFromMinecraft("&c"), // TODO: do this!
                        UUIDCache.getName(UUID.fromString(entry.getElement()))
                ));
                i++;
            }

            entries.put(statType, entryModels);
        }

        return new LeaderboardModel("Practice", entries);
    }
}
