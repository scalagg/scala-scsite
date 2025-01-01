package gg.scala.site.controller;

import gg.scala.site.cache.CacheHandler;
import gg.scala.site.cache.impl.PlayerCountCache;
import gg.scala.site.cache.impl.StaffCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StaffController {

    public static final StaffCache CACHE
            = CacheHandler.getCache(StaffCache.class);

    public static final PlayerCountCache PLAYER_CACHE
            = CacheHandler.getCache(PlayerCountCache.class);

    @RequestMapping("/staff")
    public ModelAndView staff() {
        ModelAndView view = new ModelAndView("staff");
        view.addObject("ranks", CACHE.getCachedData());
        view.addObject("playerCount", PLAYER_CACHE.getCachedData());

        return view;
    }

}