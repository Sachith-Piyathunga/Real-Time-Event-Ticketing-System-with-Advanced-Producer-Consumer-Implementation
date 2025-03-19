package main.models;

public class SalesLog {
    private int id; // Unique identifier for the sales log entry
    private String timeAndDate; // Timestamp of when the log entry was created
    private String log; // Description or details of the sales log entry

    //Constructor to initialize a SalesLog object with the specified details
    public SalesLog(int id, String timeAndDate, String log) {
        this.id = id;
        this.timeAndDate = timeAndDate;
        this.log = log;
    }
    // Retrieves the timestamp of the sales log entry
    public String getTimeAndDate() {
        return timeAndDate;
    }

    // Retrieves the description or details of the sales log entry
    public String getLog() {
        return log;
    }

}
