package gg.scala.site.cache.impl;

import com.google.gson.JsonElement;
import gg.scala.site.cache.type.RepeatingCache;
import gg.scala.site.model.replies.ReplyModel;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author alfie
 * @project site
 * @date 21/08/2024 / 19:18
 */

public class ReplyCache extends RepeatingCache<List<ReplyModel>> {

    public ReplyCache() {
        super(TimeUnit.SECONDS.toMillis(10));
    }

    @Override
    public void execute() {
        RequestResponse response = RequestHandler.get("forum/replies");
        if (!response.wasSuccessful())
            return;

        List<ReplyModel> replies = new ArrayList<>();

        for (JsonElement element : response.asArray())
            replies.add(new ReplyModel(element.getAsJsonObject()));

        cache(replies);
    }

    @Override
    public List<ReplyModel> getDefaultValue() {
        return new ArrayList<>();
    }
}
