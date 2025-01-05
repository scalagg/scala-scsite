package gg.scala.site;

import gg.scala.site.cache.impl.*;
import gg.scala.site.cache.impl.*;
import gg.scala.site.config.RedisConfig;
import gg.scala.site.config.SiteBadgeConfig;
import gg.scala.site.redis.RedisService;
import lombok.Getter;
import gg.scala.site.badge.BadgeHandler;
import gg.scala.site.cache.CacheHandler;
import gg.scala.site.cache.thread.CacheThread;
import gg.scala.site.util.configuration.ConfigurationHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@Getter
@SpringBootApplication
public class SiteApplication {

    public static SiteApplication INSTANCE;

    private final CacheThread cacheThread;
    private final BadgeHandler badgeHandler;
    private final SiteBadgeConfig badgeConfig;
    private final RedisConfig redisConfig;
    private final RedisService redisService;

    public SiteApplication() {
        INSTANCE = this;

        this.badgeConfig = ConfigurationHandler.INSTANCE.loadConfiguration(
                SiteBadgeConfig.class,
                new File("config/badges.json")
        );

        this.redisConfig = ConfigurationHandler.INSTANCE.loadConfiguration(
                RedisConfig.class,
                new File("config/redis.json")
        );

        this.cacheThread = new CacheThread();
        this.badgeHandler = new BadgeHandler();
        this.redisService = new RedisService(this.redisConfig, "core");

        CacheHandler.registerAll(
                new StaffCache(),
                new CategoryCache(),
                new LeaderboardCache(),
                new PlayerCountCache(),
                new AnnouncementCache(),
                new StaffSupportCache(),
                new ApplicationSupportCache(),
                new ProfileCache(),
                new SessionCache(),
                new TagCache(),
                new ReplyCache(),
                new NotificationCache(),
                new OnlinePlayerCache()
        );

        Runtime.getRuntime().addShutdownHook(new Thread(
                cacheThread::interrupt,
                "Site -  Shutdown"
        ));
    }

    public static void main(String[] args) {
        SpringApplication.run(SiteApplication.class);
    }

}