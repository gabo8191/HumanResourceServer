package co.edu.uptc.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

import co.edu.uptc.model.Request;
import co.edu.uptc.structures.MyQueue;

public class QueueDeserializer implements JsonDeserializer<MyQueue<Request>> {
  @Override
  public MyQueue<Request> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    MyQueue<Request> queue = new MyQueue<>();
    JsonArray jsonArray = json.getAsJsonArray();
    for (JsonElement element : jsonArray) {
      Request request = context.deserialize(element, Request.class);
      queue.push(request);
    }
    return queue;
  }
}
