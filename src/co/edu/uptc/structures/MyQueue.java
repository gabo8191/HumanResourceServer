package co.edu.uptc.structures;

public class MyQueue<T> {
  private MyList<T> list;

  public MyQueue() {
    list = new MyList<>();
  }

  public void push(T data) {
    list.add(data);
  }

  public T poll() {
    return list.remove(0);
  }

  public T peek() {
    return list.get(0);
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }

  public int size() {
    return list.size();
  }

  public Object[] toArray() {
    return list.toArray();
  }

}
