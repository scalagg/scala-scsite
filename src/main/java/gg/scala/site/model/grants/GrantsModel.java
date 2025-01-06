package gg.scala.site.model.grants;

import com.google.gson.JsonObject;
import gg.scala.site.util.TimeUtils;
import gg.scala.site.util.uuid.UUIDCache;
import lombok.Getter;

import java.util.Comparator;
import java.util.UUID;

@Getter
public class GrantsModel {

    public static final Comparator<GrantsModel> COMPARATOR
            = (o1, o2) -> (int) (o2.getAddedAt() - o1.getAddedAt());


    private final UUID id;
    private final UUID uuid;
    private final long duration;

    private final String addedBy;
    private final long addedAt;

    private final String addedReason;
    private final String rankId;

    private final String server;
    private final boolean removed;
    private final String removedBy;
    private final long removedAt;
    private final String removedReason;

    private final String addedByName;
    private final String addedByWebColor;

    private final String removerByName;
    private final String removerByWebColor;

    public GrantsModel(JsonObject object) {
        this.id = UUID.fromString(object.get("uuid").getAsString());
        this.uuid = UUID.fromString(object.get("target").getAsString());

        this.duration = object.get("duration").getAsLong();

        if (object.get("addedBy").isJsonNull()) {
            this.addedBy = "00000000-0000-0000-0000-000000000000";
        } else {
            this.addedBy = object.get("addedBy").getAsString();
        }

        this.addedAt = object.get("addedAt").getAsLong();
        this.addedReason = object.get("addedReason").getAsString();
        this.rankId = object.get("rankId").getAsString();

        this.server = object.get("addedOn").getAsString();

        if (object.get("removedAt").getAsLong() != -1) {
            this.removed = true;

            if (object.get("removedBy").isJsonNull()) {
                this.removedBy = "00000000-0000-0000-0000-000000000000";
            } else {
                this.removedBy = object.get("removedBy").getAsString();
            }

            this.removedAt = object.get("removedAt").getAsLong();
            this.removedReason = object.get("removedReason").getAsString();
        } else {
            this.removed = false;
            this.removedBy = "N/A";
            this.removedAt = -1;
            this.removedReason = "N/A";
        }


        if (UUID.fromString(addedBy).getMostSignificantBits() == 0 && UUID.fromString(addedBy).getLeastSignificantBits() == 0) {
            this.addedByName = "Console";
            this.addedByWebColor = "#cc0000";
        } else {
            if (object.has("addedByName"))
                this.addedByName = object.get("addedByName").getAsString();
            else this.addedByName = UUIDCache.getName(UUID.fromString(addedBy));

            if (object.has("addedByWebColor"))
                this.addedByWebColor = object.get("addedByWebColor").getAsString();
            else this.addedByWebColor = "#212529";
        }

        if (object.has("removerByName"))
            this.removerByName = object.get("removerByName").getAsString();
        else this.removerByName = (removedBy.equals("N/A") ? null : UUIDCache.getName(UUID.fromString(removedBy)));

        if (object.has("removerByWebColor"))
            this.removerByWebColor = object.get("removerByWebColor").getAsString();
        else this.removerByWebColor = "#212529";
    }

    public String getAddedAgo() {
        return TimeUtils.formatTimeAgo(addedAt);
    }

    public String getRemovedAgo() {
        return TimeUtils.formatTimeAgo(removedAt);
    }

    public String formatDuration() {
        return TimeUtils.formatDetailed(duration);
    }

    public String formatStatus() {
        if (removed)
            return "Removed";

        if (isActive())
            return "Active";

        return "Expired";
    }

    public boolean isRemoved() {
        return !this.removedBy.equals("N/A");
    }

    public long getEnd() {
        return duration == Long.MAX_VALUE ? -1 : addedAt + duration;
    }

    public boolean isActive() {
        if (removed)
            return false;

        long end = getEnd();
        if (end == -1)
            return true;

        return end >= System.currentTimeMillis();
    }
}