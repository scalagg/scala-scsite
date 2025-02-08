package gg.scala.site.advice;

import gg.scala.site.controller.NotificationController;
import gg.scala.site.model.leaderboard.LeaderboardModel;
import gg.scala.site.advice.item.NavItem;
import gg.scala.site.cache.CacheHandler;
import gg.scala.site.cache.impl.LeaderboardCache;
import gg.scala.site.model.profile.ProfileModel;
import gg.scala.site.util.SessionUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    public static final List<NavItem> NAV_ITEMS = List.of(
            new NavItem("Home", "/"),
            new NavItem("Forums", "/forums"),
            new NavItem("Leaderboard", "/leaderboard"),
            new NavItem("Support", "/support"),
            new NavItem("Staff", "/staff"),
            new NavItem("Store", "https://store.scala.gg")
    );

    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        ProfileModel profile = SessionUtil.getProfile(request);

        if (profile != null)
            model.addAttribute(
                    "unreadNotifications",
                    NotificationController.CACHE.getUnreadCount(profile.getUuid())
            );
    }

    @ModelAttribute("navItems")
    public List<NavItem> getNavItems() {
        return NAV_ITEMS;
    }

    @ModelAttribute("leaderboardNavItems")
    public List<NavItem> getLeaderboardNavItems() {
        List<NavItem> items = new ArrayList<>();
        List<LeaderboardModel> cachedData = CacheHandler.getCache(
                LeaderboardCache.class
        ).getCachedData();

        for (LeaderboardModel cachedDatum : cachedData) {
            String name = cachedDatum.getName();
            List<String> acceptedUrls = new ArrayList<>();

            acceptedUrls.add("/leaderboard/" + name);

            if (cachedDatum.getName().equalsIgnoreCase("mini"))
                acceptedUrls.add("/leaderboard");

            items.add(new NavItem(name, acceptedUrls));
        }

        return items;
    }

}