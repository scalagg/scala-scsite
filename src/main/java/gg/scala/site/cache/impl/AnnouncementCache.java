package gg.scala.site.cache.impl;

import gg.scala.site.SiteConstant;
import gg.scala.site.cache.type.RepeatingCache;
import gg.scala.site.model.forum.ForumModel;
import gg.scala.site.model.forum.ForumThread;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AnnouncementCache extends RepeatingCache<List<ForumThread>> {

    public AnnouncementCache() {
        super(TimeUnit.SECONDS.toMillis(30));
    }

    @Override
    public void execute() {
        RequestResponse response = RequestHandler.get(
                "forum/forum/%s?page=1",
                SiteConstant.ANNOUNCEMENTS_FORUM
        );

        if (!response.wasSuccessful())
            return;

        ForumModel forumModel = new ForumModel(response.asObject());
        List<ForumThread> latestThreads = new ArrayList<>(forumModel.getThreads());

        latestThreads.sort((o1, o2) -> Long.compare(
                o2.getCreatedAt(), o1.getCreatedAt()
        ));


        cache(latestThreads);
    }

    @Override
    public List<ForumThread> getDefaultValue() {
        return new ArrayList<>();
    }
}