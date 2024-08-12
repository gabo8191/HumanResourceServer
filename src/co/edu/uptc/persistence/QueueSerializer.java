package co.edu.uptc.persistence;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import co.edu.uptc.model.Request;
import co.edu.uptc.structures.MyQueue;

public class QueueSerializer implements JsonSerializer<MyQueue<Request>> {

  public JsonElement serialize(MyQueue<Request> src, Type typeOfSrc, JsonSerializationContext context) {
    JsonArray jsonArray = new JsonArray();
    return jsonArray;
  }
}
