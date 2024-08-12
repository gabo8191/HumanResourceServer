package co.edu.uptc.persistence;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import co.edu.uptc.model.Notification;
import co.edu.uptc.structures.MyStack;

public class StackDeserializer implements JsonDeserializer<MyStack<Notification>> {
  @Override
  public MyStack<Notification> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    MyStack<Notification> stack = new MyStack<>();
    JsonArray jsonArray = json.getAsJsonArray();
    for (JsonElement element : jsonArray) {
      Notification notification = context.deserialize(element, Notification.class);
      stack.push(notification);
    }
    return stack;
  }
}
