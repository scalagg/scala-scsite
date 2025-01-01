package gg.scala.site.config.badge;

import gg.scala.site.badge.defaults.StaffBadgeModel;
import lombok.Data;
import gg.scala.site.badge.BadgeModel;
import gg.scala.site.badge.defaults.*;
import gg.scala.site.util.configuration.SiteConfiguration;

import java.util.List;

@Data
public class SiteBadgeConfig implements SiteConfiguration {

    private List<BadgeModel> badges = List.of(
            new BadgeModel(),
            new StaffBadgeModel()
    );

}