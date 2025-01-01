package gg.scala.site.cache.impl;

import gg.scala.site.cache.type.TimedMultiCache;
import gg.scala.site.model.profile.ProfileModel;

import java.util.concurrent.TimeUnit;

public class SessionCache extends TimedMultiCache<ProfileModel> {

    public SessionCache() {
        super(30, TimeUnit.SECONDS);
    }

}