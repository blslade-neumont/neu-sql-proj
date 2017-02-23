package tooearly.neumont.edu.sqltaskmanager.Models;

public class Task {
    public Task() {
    }
    public Task(String name) {
        this.name = name;
    }

    public int id;
    public String name;
    public String description;
    public boolean completed;
    public int color;
    public int time_spent;
}
