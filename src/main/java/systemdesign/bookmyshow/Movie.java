package systemdesign.bookmyshow;

/**
 * Movie entity.
 */
public class Movie {
    private final String name;

    public Movie(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
