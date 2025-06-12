package systemdesign.ridehailing;

/**
 * Rider requesting rides.
 */
public class Rider {
    private final String name;

    public Rider(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
