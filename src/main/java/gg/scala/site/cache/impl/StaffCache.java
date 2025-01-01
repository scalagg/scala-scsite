package gg.scala.site.cache.impl;

import com.google.gson.JsonElement;
import gg.scala.site.cache.type.RepeatingCache;
import gg.scala.site.model.staff.StaffRankModel;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StaffCache extends RepeatingCache<List<StaffRankModel>> {

    public StaffCache() {
        super(TimeUnit.MINUTES.toMillis(1));


    }

    @Override
    public void execute() {
        RequestResponse response = RequestHandler.get("staffList?website=true");
        if (!response.wasSuccessful())
            return;

        List<StaffRankModel> ranks = new ArrayList<>();
        for (JsonElement rankElement : response.asArray())
            ranks.add(new StaffRankModel(
                    rankElement.getAsJsonObject()
            ));

        ranks.sort((o1, o2) -> o2.getWeight() - o1.getWeight());
        cache(ranks);
    }

    @Override
    public List<StaffRankModel> getDefaultValue() {
        return new ArrayList<>();
    }

}