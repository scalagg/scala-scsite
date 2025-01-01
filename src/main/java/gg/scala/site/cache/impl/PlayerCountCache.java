package gg.scala.site.cache.impl;

import gg.scala.site.cache.type.RepeatingCache;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;

import java.util.concurrent.TimeUnit;

public class PlayerCountCache extends RepeatingCache<Integer> {

    public PlayerCountCache() {
        super(TimeUnit.SECONDS.toMillis(5));
    }

    @Override
    public void execute() {
        RequestResponse response = RequestHandler.get("global-count");
        if (!response.wasSuccessful()) {
            cache(0);
            return;
        }

        cache(response.asObject().get("online").getAsInt());
    }

    @Override
    public Integer getDefaultValue() {
        return 0;
    }

}