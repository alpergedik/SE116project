import java.util.*;

public class FSM {
    private Set<String> symbols = new HashSet<>();
    private Map<String, State> states = new HashMap<>();
    private State initialState;
    private Set<State> finalStates = new HashSet<>();
    private Map<String, Map<String, State>> transitions = new HashMap<>();

}


