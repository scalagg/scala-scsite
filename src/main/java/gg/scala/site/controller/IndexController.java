package gg.scala.site.controller;

import gg.scala.site.cache.CacheHandler;
import gg.scala.site.cache.impl.AnnouncementCache;
import gg.scala.site.cache.impl.PlayerCountCache;
import gg.scala.site.model.forum.ForumThread;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    public static final AnnouncementCache CACHE
            = CacheHandler.getCache(AnnouncementCache.class);

    public static final PlayerCountCache PLAYER_CACHE
            = CacheHandler.getCache(PlayerCountCache.class);

    @RequestMapping({"/", "home"})
    public ModelAndView showIndex(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        List<ForumThread> announcements = new ArrayList<>(CACHE.getCachedData());

        view.addObject("playerCount", PLAYER_CACHE.getCachedData());

        if (!announcements.isEmpty()) {
            view.addObject("latestAnnouncement", announcements.get(0));
            announcements.remove(0);

            if (!announcements.isEmpty()) {
                int endIndex = Math.min(2, announcements.size());
                view.addObject("announcements", announcements.subList(0, endIndex));
            }
        }

        return view;
    }

}
