package gg.scala.site.util;

import gg.scala.site.cache.CacheHandler;
import gg.scala.site.cache.impl.SessionCache;
import gg.scala.site.model.profile.ProfileModel;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class SessionUtil {

    public static final SessionCache CACHE = CacheHandler.getCache(SessionCache.class);

    public static void storeSession(HttpServletRequest request, UUID uuid) {
        request.getSession().setAttribute("uuid", uuid);
    }

    public static ProfileModel getProfile(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute("uuid");

        if (attribute == null)
            return null;

        UUID uuid = (UUID) attribute;

        ProfileModel profileModel = CACHE.get(uuid);
        if (profileModel != null)
            return profileModel;

        RequestResponse response = RequestHandler.get(
                "profile/%s?webResolved=true&includePermissions=true",
                uuid.toString()
        );

        if (!response.wasSuccessful())
            return null;

        profileModel = new ProfileModel(response.asObject());
        CACHE.update(profileModel);

        return profileModel;
    }

}