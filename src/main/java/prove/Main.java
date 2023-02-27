package prove;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {


    public static void main(String args[]) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", "ciccio").put("b", "buffo");
        System.out.println(jsonObject.toString());

        jsonArray.put(0,new JSONObject().put("a","ciccio")).put(1,new JSONObject().put("b", "buffo"));
        System.out.println(jsonArray.toString());
    }

}
