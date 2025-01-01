package gg.scala.site.model.replies;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.scala.site.model.profile.ProfileModel;
import gg.scala.site.util.ColorUtil;
import gg.scala.site.util.TimeUtils;
import gg.scala.site.util.uuid.UUIDCache;
import gg.scala.site.util.uuid.UUIDHolder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.*;

/**
 * @author alfie
 * @project site
 * @date 21/08/2024 / 19:04
 */

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class ReplyModel extends UUIDHolder {

    private final UUID uuid;
    private final UUID author;
    private final String authorWebColor;
    private final String thread;
    private String comment;

    private long createdAt;
    private long lastEditedAt;

    private UUID parentReply;

    private List<ReplyModel> replies = new ArrayList<>();
    private transient Map<ReplyModel, Integer> allSubReplies = new HashMap<>();

    public ReplyModel(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.author = UUID.fromString(object.get("author").getAsString());
        this.thread = object.get("thread").getAsString();
        this.comment = object.get("comment").getAsString();
        this.createdAt = object.get("createdAt").getAsLong();
        this.authorWebColor = ColorUtil.getHexFromMinecraft(object.get("authorColor").getAsString());

        if (object.has("parentReplyId") && !object.get("parentReplyId").isJsonNull())
            this.parentReply = UUID.fromString(object.get("parentReplyId").getAsString());

        if (object.has("replies")) {
            for (JsonElement element : object.get("replies").getAsJsonArray())
                replies.add(new ReplyModel(element.getAsJsonObject()));
        }

        if (object.has("lastEditedAt") && object.get("lastEditedAt").getAsLong() != 0)
            this.lastEditedAt = object.get("lastEditedAt").getAsLong();

        this.allSubReplies = fetchSubReplies(1);
    }

    public String getAuthorName() {
        return UUIDCache.getName(author);
    }

    public String getPostedAgo() {
        return TimeUtils.formatTimeAgo(createdAt);
    }

    public boolean canDelete(ProfileModel profileModel) {
        if (profileModel == null)
            return false;

        return author.equals(profileModel.getUuid())
                || profileModel.hasPermission("website.reply.delete.*");
    }

    public Map<ReplyModel, Integer> fetchSubReplies(int step) {
        Map<ReplyModel, Integer> replyMap = new LinkedHashMap<>();

        for (ReplyModel reply : getSortedReplies()) {
            replyMap.put(reply, Math.min(step * 5, 15));

            Map<ReplyModel, Integer> subReplies = reply.fetchSubReplies(step + 1);
            replyMap.putAll(subReplies);
        }

        return replyMap;
    }

    public List<ReplyModel> getSortedReplies() {
        List<ReplyModel> sortedReplies = new ArrayList<>(replies);
        sortedReplies.sort(Comparator.comparingLong(ReplyModel::getCreatedAt));

        return sortedReplies;
    }

}
