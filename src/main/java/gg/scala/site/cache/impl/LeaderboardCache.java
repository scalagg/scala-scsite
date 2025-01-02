package gg.scala.site.cache.impl;

import com.google.gson.JsonElement;
import gg.scala.site.SiteApplication;
import gg.scala.site.cache.leaderboard.LeaderboardType;
import gg.scala.site.cache.leaderboard.ScalaPracticeLeaderboard;
import gg.scala.site.cache.type.RepeatingCache;
import gg.scala.site.model.leaderboard.LeaderboardModel;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LeaderboardCache extends RepeatingCache<List<LeaderboardModel>> {
    private final List<LeaderboardType> leaderboards = List.of(
            new ScalaPracticeLeaderboard()
    );

    public LeaderboardCache() {
        super(TimeUnit.SECONDS.toMillis(10));
    }

    @Override
    public void execute() {
        List<LeaderboardModel> models = new ArrayList<>();

        for (LeaderboardType leaderboard : leaderboards) {
            SiteApplication.INSTANCE.getRedisService().executeCommand(jedis -> models.add(leaderboard.collect(jedis)));

            System.out.println("Got leaderboard: " + leaderboard.getClass().getSimpleName());
        }

        cache(models);
    }

    public LeaderboardModel getLeaderboard(String name) {
        for (LeaderboardModel model : getCachedData()) {
            if (model.getName().equalsIgnoreCase(name))
                return model;
        }

        return null;
    }

    @Override
    public List<LeaderboardModel> getDefaultValue() {
        return new ArrayList<>();
    }

}