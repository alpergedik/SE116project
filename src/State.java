import java.io.Serializable;

public class State implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;

    public State(String name) {
        this.name = name.toUpperCase();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof State && name.equals(((State) o).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}