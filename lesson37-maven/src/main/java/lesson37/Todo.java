package lesson37;

// Maps JSON fields from the API (Gson matches names automatically)
public class Todo {
    public int userId;
    public int id;
    public String title;
    public boolean completed;

    @Override
    public String toString() {
        return "Todo{id=" + id + ", title='" + title + "', completed=" + completed + "}";
    }
}
