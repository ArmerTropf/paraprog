package DistSystems.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by hm on 23.09.2017.
 */
public class JSONGraphBuilder {


    private void setupNeighboursJSON() {
        JSONObject nodesJSON = new JSONObject("");
        JSONArray nodesJSONArray = nodesJSON.getJSONArray("nodes");

        nodesJSONArray.forEach((nodeObj) -> {
            JSONObject node = (JSONObject) nodeObj;
            node.get("name");
            node.getBoolean("initiator");
            node.getJSONArray("neighbours");
        });
    }

}
