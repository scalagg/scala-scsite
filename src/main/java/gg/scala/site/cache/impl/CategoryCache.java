package gg.scala.site.cache.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.scala.site.cache.type.RepeatingCache;
import gg.scala.site.model.forum.ForumCategory;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CategoryCache extends RepeatingCache<List<ForumCategory>> {

    public CategoryCache() {
        super(TimeUnit.SECONDS.toMillis(30));
    }

    @Override
    public void execute() {
        RequestResponse response = RequestHandler.get("forum/category");

        if (!response.wasSuccessful())
            return;

        List<ForumCategory> categories = new ArrayList<>();

        for (JsonElement element : response.asArray()) {
            JsonObject object = element.getAsJsonObject();
            categories.add(new ForumCategory(object));
        }

        categories.sort(ForumCategory.CATEGORY_COMPARATOR);
        cache(categories);
    }

    @Override
    public List<ForumCategory> getDefaultValue() {
        return new ArrayList<>();
    }

}