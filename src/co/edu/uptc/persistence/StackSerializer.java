package co.edu.uptc.persistence;

import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import co.edu.uptc.model.Notification;
import co.edu.uptc.structures.MyStack;

public class StackSerializer implements JsonSerializer<MyStack<Notification>> {

  @Override
  public JsonElement serialize(MyStack<Notification> src, Type typeOfSrc, JsonSerializationContext context) {
    JsonArray jsonArray = new JsonArray();
    return jsonArray;
  }
}
