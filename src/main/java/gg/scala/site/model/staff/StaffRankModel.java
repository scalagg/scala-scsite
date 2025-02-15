package gg.scala.site.model.staff;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.scala.site.util.ColorUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class StaffRankModel {

    private final UUID uuid;
    private final String name;
    private final String displayName;
    private final int weight;
    private final String webColor;
    private final List<StaffMember> members = new ArrayList<>();

    public StaffRankModel(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.name = object.get("name").getAsString();
        this.displayName = object.get("displayName").getAsString();
        this.weight = object.get("weight").getAsInt();
        this.webColor = ColorUtil.getHexFromMinecraft(object.get("color").getAsString());

        for (JsonElement element : object.get("members").getAsJsonArray()) {
            StaffMember member = new StaffMember(element.getAsJsonObject());

            if (members.contains(member)) {
                continue;
            }

            members.add(member);
        }
    }

}