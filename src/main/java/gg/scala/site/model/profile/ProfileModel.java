package gg.scala.site.model.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.scala.site.SiteApplication;
import gg.scala.site.badge.BadgeModel;
import gg.scala.site.cache.CacheHandler;
import gg.scala.site.cache.impl.OnlinePlayerCache;
import gg.scala.site.model.RankModel;
import gg.scala.site.model.anticheat.AnticheatModel;
import gg.scala.site.model.forum.ForumThread;
import gg.scala.site.model.grants.GrantsModel;
import gg.scala.site.model.profile.data.*;
import gg.scala.site.model.profile.log.LogModel;
import gg.scala.site.model.profile.log.Note;
import gg.scala.site.model.punishment.PunishmentModel;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.util.TimeUtils;
import gg.scala.site.util.uuid.UUIDHolder;
import lombok.Getter;

import java.util.*;

@Getter
public class ProfileModel extends UUIDHolder {

    private static final OnlinePlayerCache CACHE = CacheHandler.getCache(OnlinePlayerCache.class);

    private final UUID uuid;
    private final String name;

    private final long firstJoin;
    private final long lastSeen;
    private final long playTime;
    private final String lastServer;
    private final String currentServer;

    private final RankModel rank;

    private final PunishmentModel activeBan;
    private final PunishmentModel activeMute;

    private final List<StatsModel> stats = new ArrayList<>();
    private final List<ForumThread> threads = new ArrayList<>();
    private final List<FriendModel> friends = new ArrayList<>();
    private final List<CommentModel> comments = new ArrayList<>();
    private final Map<String, Boolean> permissions = new HashMap<>();

    private final List<LogModel> logs = new ArrayList<>();
    private final List<PunishmentModel> punishments = new ArrayList<>();
    private final List<GrantsModel> grants = new ArrayList<>();
    private final List<Note> notes = new ArrayList<>();
    private final List<BadgeModel> badges = new ArrayList<>();

    private final Map<String, String> settings = new HashMap<>();
    private final boolean trusted;

    public ProfileModel(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uniqueId").getAsString());
        this.name = object.get("name").getAsString();
        this.firstJoin = object.getAsJsonObject("metadata").getAsJsonObject("first-connection").get("value").getAsLong();

        this.lastSeen = object.getAsJsonObject("metadata").getAsJsonObject("last-connection").get("value").getAsLong();

        this.playTime = object.has("playTime")
                ? object.get("playTime").getAsLong()
                : -1;

        this.lastServer = object.getAsJsonObject("metadata").getAsJsonObject("last-connected-server-id").get("value").getAsString();
//        this.currentServer = object.has("currentServer")
//                ? object.get("currentServer").getAsString()
//                : null;


        if (CACHE.getCachedData().contains(uuid.toString())) {
            this.currentServer = lastServer;
        } else {
            this.currentServer = null;
        }

        this.rank = new RankModel(object.get("rank").getAsJsonObject());

        this.activeBan = object.has("activeBan") && !object.get("activeBan").isJsonNull()
                ? new PunishmentModel(object.get("activeBan").getAsJsonObject())
                : null;

        this.activeMute = object.has("activeMute") && !object.get("activeMute").isJsonNull()
                ? new PunishmentModel(object.get("activeMute").getAsJsonObject())
                : null;

        if (object.has("effectivePermissions")) {
            JsonArray jsonArray = object.get("effectivePermissions").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                String permission = jsonElement.getAsString();
                //if (!jsonObject.get(permission).getAsString().equals("true"))
                //    continue;

                // TODO: tf this bro on gangy
                this.permissions.put(permission, true);
            }
        }

        if (object.has("logs"))
            for (JsonElement element : object.get("logs").getAsJsonArray())
                logs.add(new LogModel(element.getAsJsonObject()));
        if (object.has("notes"))
            for (JsonElement element : object.get("notes").getAsJsonArray())
                notes.add(new Note(element.getAsJsonObject()));


        if (object.has("badges") && !object.get("badges").isJsonNull())
            for (JsonElement element : object.get("badges").getAsJsonArray()) {
                BadgeModel badge = SiteApplication.INSTANCE
                        .getBadgeHandler()
                        .getBadge(element.getAsString());

                if (badge != null)
                    badges.add(badge);
            }


        if (object.has("webSettings") && !object.get("webSettings").isJsonNull()) {
            JsonObject settingsObject = object.get("webSettings").getAsJsonObject();
            settingsObject.keySet().forEach(key -> settings.put(
                    key, settingsObject.get(key).getAsString()
            ));
        }

        this.trusted = object.has("isTrusted")
                && object.get("isTrusted").getAsBoolean();

        if (object.has("comments") && !object.get("comments").isJsonNull()) {
            for (JsonElement element : object.get("comments").getAsJsonArray())
                comments.add(new CommentModel(element.getAsJsonObject()));

            comments.sort(CommentModel.COMPARATOR);
        }

        if (object.has("threads") && !object.get("threads").isJsonNull()) {
            for (JsonElement element : object.get("threads").getAsJsonArray())
                threads.add(new ForumThread(element.getAsJsonObject()));
        }

        if (object.has("friends") && !object.get("friends").isJsonNull()) {
            for (JsonElement element : object.get("friends").getAsJsonArray())
                friends.add(new FriendModel(element.getAsJsonObject()));
        }

        if (object.has("stats") && !object.get("stats").isJsonNull()) {
            for (JsonElement element : object.get("stats").getAsJsonArray())
                stats.add(new StatsModel(element.getAsJsonObject()));
        }
    }


    public String getSocial(SocialModel social) {
        return settings.getOrDefault(social.getName(), "");
    }

    public String formatSeenAgo() {
        return TimeUtils.formatTimeAgo(lastSeen);
    }

    public String formatFirstJoin() {
        return TimeUtils.formatCalendarString(firstJoin, false);
    }

    public String formatPlaytime() {
        return TimeUtils.formatTimeShort(playTime);
    }

    public boolean isOnline() {
        if (currentServer != null)
            return !currentServer.equalsIgnoreCase("N/A");

        return false;
    }

    public boolean hasPermission(String permission) {
        if (permission.equalsIgnoreCase("trusted"))
            return isTrusted();

        boolean superPerm = hasSuperPerm();

        if (permission.equals("*"))
            return superPerm;

        if (superPerm)
            return true;

        return permissions.getOrDefault(
                permission.toLowerCase(),
                false
        );
    }

    public boolean hasSuperPerm() {
        if (trusted)
            return true;

        return permissions.containsKey("*") && permissions.get("*");
    }

    public boolean hasBadge(String id) {
        for (BadgeModel badge : badges) {
            if (badge.getId().equals(id))
                return true;
        }

        return false;
    }

    public List<SocialModel> getActiveSocials() {
        List<SocialModel> toReturn = new ArrayList<>();

        for (SocialModel value : SocialModel.values()) {
            if (!settings.containsKey(value.getName()) || getSocial(value).isEmpty())
                continue;

            toReturn.add(value);
        }

        return toReturn;
    }

    public PrivacyModel getStaffPageStatus() {
        return PrivacyModel.valueOf(settings.getOrDefault(
                "STAFF_PAGE_STATUS",
                "YES"
        ));
    }


    public PrivacyModel getCommentStatus() {
        return PrivacyModel.valueOf(settings.getOrDefault(
                "COMMENT_STATUS",
                "EVERYONE"
        ));
    }

    public PrivacyModel getOnlineStatus() {
        return PrivacyModel.valueOf(settings.getOrDefault(
                "ONLINE_STATUS",
                "EVERYONE"
        ));
    }

    public boolean hasFriend(ProfileModel profile) {
        if (profile == null)
            return false;

        for (FriendModel friend : friends)
            if (friend.contains(profile.getUuid()))
                return true;

        return false;
    }

    public PunishmentModel getActiveMute() {
        if (activeMute == null || !activeMute.isActive())
            return null;

        return activeMute;
    }

//    public NotificationModel getNotification(UUID uuid) {
//        for (NotificationModel notification : notifications) {
//            if (notification.getUuid().equals(uuid))
//                return notification;
//        }
//
//        return null;
//    }
//
//    public int countUnreadNotifications() {
//        int count = 0;
//
//        for (NotificationModel notification : notifications) {
//            if (!notification.isRead())
//                count++;
//        }
//
//        return count;
//    }

    /**
     * This is lazily loaded due to it being fairly expensive on the backend
     * so we don't need to load it every time a profile is loaded, only when
     * someone specifically requests it.
     */
    public List<AnticheatModel> getAnticheatLogs() {
        var response = RequestHandler.get("anticheat/" + uuid.toString());
        if (!response.wasSuccessful()) {
            return new ArrayList<>();
        }

        JsonArray results = response.asArray();
        List<AnticheatModel> logs = new ArrayList<>();

        for (JsonElement element : results) {
            logs.add(new AnticheatModel(element.getAsJsonObject()));
        }

        return logs;
    }

}