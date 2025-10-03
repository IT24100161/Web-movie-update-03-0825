package movies;

public class UpcomingMovie {
    private int id;
    private String title;
    private String releaseDate;

    public UpcomingMovie() {}

    public UpcomingMovie(int id, String title, String releaseDate) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}
