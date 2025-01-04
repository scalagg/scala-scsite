package gg.scala.site.model.anticheat;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class AnticheatModel {
    private final String checkName;
    private final int count;

    public AnticheatModel(JsonObject object) {
        this.checkName = object.get("checkName").getAsString();
        this.count = object.get("count").getAsInt();
    }
}
