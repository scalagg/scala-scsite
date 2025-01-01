package gg.scala.site.cache.impl;

import gg.scala.site.cache.type.TimedMultiCache;
import gg.scala.site.model.profile.ProfileModel;

import java.util.concurrent.TimeUnit;

public class ProfileCache extends TimedMultiCache<ProfileModel> {

    public ProfileCache() {
        super(30, TimeUnit.SECONDS);
    }

}