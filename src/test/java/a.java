import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class a {
    public static void main(String[] args) {
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new Gson().fromJson("{Tag: {RepairCost:0,Damage:0,display:{Name:'{\"text\":\"hello\"}'}}}", JsonObject.class)));
    }
}
