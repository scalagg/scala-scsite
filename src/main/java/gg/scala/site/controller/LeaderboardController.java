package gg.scala.site.controller;

import gg.scala.site.cache.CacheHandler;
import gg.scala.site.cache.impl.LeaderboardCache;
import gg.scala.site.cache.impl.PlayerCountCache;
import gg.scala.site.model.leaderboard.LeaderboardModel;
import gg.scala.site.util.ErrorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/leaderboard")
public class LeaderboardController {

    public static final LeaderboardCache CACHE
            = CacheHandler.getCache(LeaderboardCache.class);

    public static final PlayerCountCache PLAYER_CACHE
            = CacheHandler.getCache(PlayerCountCache.class);

    @RequestMapping
    public ModelAndView defaultBoard() {
        return fillData("mini");
    }

    @RequestMapping("/{id}")
    public ModelAndView leaderboard(@PathVariable(name = "id") String leaderboardId) {
        return fillData(leaderboardId);
    }

    public ModelAndView fillData(String leaderboardId) {
        if (CACHE.getCachedData().isEmpty())
            return ErrorUtil.create(500, "Leaderboard data is currently unavailable.");

        LeaderboardModel leaderboard = CACHE.getLeaderboard(leaderboardId);
        if (leaderboard == null)
            return new ModelAndView("redirect:/leaderboard");

        ModelAndView view = new ModelAndView("leaderboard");
        view.addObject("vertContainer", true);
        view.addObject("playerCount", PLAYER_CACHE.getCachedData());

        view.addObject("leaderboardName", leaderboard.getName());
        view.addObject("leaderboardEntries", leaderboard.getEntries());

        return view;
    }

}