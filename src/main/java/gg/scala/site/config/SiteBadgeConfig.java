package gg.scala.site.config;

import gg.scala.site.badge.BadgeModel;
import gg.scala.site.badge.defaults.StaffBadgeModel;
import gg.scala.site.util.configuration.SiteConfiguration;
import lombok.Data;

import java.util.List;

@Data
public class SiteBadgeConfig implements SiteConfiguration {

    private List<BadgeModel> badges = List.of(
            new BadgeModel(),
            new StaffBadgeModel()
    );

}