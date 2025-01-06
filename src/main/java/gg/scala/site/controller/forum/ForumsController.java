package gg.scala.site.controller.forum;

import com.google.gson.JsonObject;
import gg.scala.site.util.ErrorUtil;
import gg.scala.site.util.PopupUtil;
import gg.scala.site.util.SessionUtil;
import gg.scala.site.util.StringUtil;
import gg.scala.site.cache.CacheHandler;
import gg.scala.site.cache.impl.CategoryCache;
import gg.scala.site.model.profile.ProfileModel;
import gg.scala.site.model.forum.ForumCategory;
import gg.scala.site.model.forum.ForumModel;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;
import gg.scala.site.util.pagination.ViewPaginationModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ForumsController {

    public static final CategoryCache CATEGORY_CACHE
            = CacheHandler.getCache(CategoryCache.class);

    @RequestMapping("/forums")
    public ModelAndView forums(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("forum/forums");
        List<ForumCategory> cachedData = new ArrayList<>(CATEGORY_CACHE.getCachedData());

        ProfileModel profile = SessionUtil.getProfile(request);

        for (ForumCategory category : CATEGORY_CACHE.getCachedData()) {
            if (category.hasPermission(profile))
                continue;

            cachedData.remove(category);
        }

        view.addObject("categories", cachedData);

        return view;
    }

    @RequestMapping("/forum/{id}")
    public ModelAndView viewForum(HttpServletRequest request,
                                  @PathVariable("id") String id,
                                  @RequestParam(value = "page", required = false) Integer page) {
        RequestResponse response = RequestHandler.get("forum/forum/%s?page=1", id.replace(" ", "-"));
        if (!response.wasSuccessful())
            return ErrorUtil.create(response);

        JsonObject object = response.asObject();
        ForumModel model = new ForumModel(object);
        ModelAndView view = new ModelAndView("forum/forum");

        ProfileModel profile = SessionUtil.getProfile(request);
        String permission = model.getPermission();

        if ((permission != null && !permission.isEmpty()) && (profile == null || !profile.hasPermission(permission))) {
            if (profile == null)
                return ErrorUtil.loginRedirect("/forum/" + id);

            PopupUtil.error(request.getSession(), "You don't have permission to view this forum");
            return new ModelAndView("redirect:/forums");
        }

        new ViewPaginationModel<>(
                page == null ? 1 : page,
                10,
                model.getThreads(),
                "threads"
        ).applyTo(view, "/forum/" + id + "?page={page}");

        view.addObject("forum", model);

        return view;
    }

    @PostMapping("/forums/create/{id}")
    public ModelAndView createForum(HttpServletRequest request,
                                    @PathVariable("id") String id,
                                    @RequestParam("name") String name,
                                    @RequestParam("weight") Integer weight,
                                    @RequestParam("description") String description,
                                    @RequestParam("permission") String permission,
                                    @RequestParam(value = "locked", required = false) boolean locked) {
        ProfileModel profile = SessionUtil.getProfile(request);

        if (profile == null)
            return ErrorUtil.loginRedirect("/forums");

        if (!profile.hasPermission("website.forum.manage"))
            return new ModelAndView("redirect:/forums");

        if (name.isBlank()) {
            PopupUtil.error(request.getSession(), "The title cannot be empty.");
            return new ModelAndView("redirect:/forums");
        }

        JsonObject body = new JsonObject();
        body.addProperty("category", id);
        body.addProperty("id", StringUtil.generateId(5));
        body.addProperty("name", name.trim());
        body.addProperty("weight", weight);
        body.addProperty("description", description);
        body.addProperty("locked", locked);

        if (permission != null)
            body.addProperty("permission", permission.isEmpty() ? "" : permission);

        RequestResponse response = RequestHandler.post("forum/forum", body);
        if (!response.wasSuccessful())
            return ErrorUtil.create(response);

        CATEGORY_CACHE.forceExecute();
        return new ModelAndView("redirect:/forums");
    }

    @PostMapping("/forums/edit/{id}")
    public ModelAndView editForum(HttpServletRequest request,
                                  @PathVariable("id") String id,
                                  @RequestParam("name") String name,
                                  @RequestParam("weight") Integer weight,
                                  @RequestParam("description") String description,
                                  @RequestParam("permission") String permission,
                                  @RequestParam(value = "locked", required = false) boolean locked) {
        ProfileModel profile = SessionUtil.getProfile(request);

        if (profile == null)
            return ErrorUtil.loginRedirect("/forums");

        if (!profile.hasPermission("website.forum.manage"))
            return new ModelAndView("redirect:/forums");

        if (name.isBlank()) {
            PopupUtil.error(request.getSession(), "The title cannot be empty.");
            return new ModelAndView("redirect:/forums");
        }

        JsonObject body = new JsonObject();
        body.addProperty("name", name.trim());
        body.addProperty("weight", weight);
        body.addProperty("description", description);
        body.addProperty("locked", locked);


        if (permission != null && !permission.isEmpty())
            body.addProperty("permission", permission);

        RequestResponse response = RequestHandler.put("forum/forum/%s", body, id);
        if (!response.wasSuccessful())
            return ErrorUtil.create(response);

        CATEGORY_CACHE.forceExecute();
        return new ModelAndView("redirect:/forums");
    }

    @PostMapping("/forums/delete/{id}")
    public ModelAndView deleteForum(HttpServletRequest request,
                                    @PathVariable("id") String id) {
        ProfileModel profile = SessionUtil.getProfile(request);

        if (profile == null)
            return ErrorUtil.loginRedirect("/forums");

        if (!profile.hasPermission("website.forum.manage"))
            return new ModelAndView("redirect:/forums");

        RequestResponse response = RequestHandler.delete("forum/forum/%s", id);
        if (!response.wasSuccessful())
            return ErrorUtil.create(response);

        CATEGORY_CACHE.forceExecute();
        return new ModelAndView("redirect:/forums");
    }

}