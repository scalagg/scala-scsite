package gg.scala.site.cache.thread;

import gg.scala.site.cache.type.RepeatingCache;
import gg.scala.site.util.thread.SiteThread;
import gg.scala.site.cache.CacheHandler;
import gg.scala.site.cache.AbstractCache;

public class CacheThread extends SiteThread {

    public CacheThread() {
        super("Site - Cache Thread", 500L);
    }

    @Override
    public void execute() {
        for (AbstractCache<?> abstractCache : CacheHandler.CACHE_MAP.values()) {
            if (!(abstractCache instanceof RepeatingCache<?> cache))
                continue;

            long lastCache = cache.getLastCache();
            long delay = cache.getDelay();

            if (lastCache + delay <= System.currentTimeMillis()) {
                cache.  execute();
                cache.setLastCache(System.currentTimeMillis());
            }
        }
    }

}