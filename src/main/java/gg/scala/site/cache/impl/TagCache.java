package gg.scala.site.cache.impl;

import com.google.gson.JsonElement;
import gg.scala.site.cache.type.RepeatingCache;
import gg.scala.site.model.TagModel;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TagCache extends RepeatingCache<List<TagModel>> {

    public TagCache() {
        super(TimeUnit.MINUTES.toMillis(1));
    }

    @Override
    public void execute() {
        RequestResponse response = RequestHandler.get("tag");
        if (!response.wasSuccessful())
            return;

        List<TagModel> tags = new ArrayList<>();

        for (JsonElement rankElement : response.asArray())
            tags.add(new TagModel(
                    rankElement.getAsJsonObject()
            ));

        cache(tags);
    }

    @Override
    public List<TagModel> getDefaultValue() {
        return new ArrayList<>();
    }

}