import java.util.*;

public class FSM {
    private Set<String> symbols = new HashSet<>();
    private Map<String, State> states = new HashMap<>();
    private State initialState;
    private Set<State> finalStates = new HashSet<>();
    private Map<String, Map<String, State>> transitions = new HashMap<>();

    public boolean addSymbol(String s) {
        return symbols.add(s.toUpperCase());
    }

    public boolean addState(String name) {
        String key = name.toUpperCase();
        if (!states.containsKey(key)) {
            State newState = new State(key);
            states.put(key, newState);
            if (initialState == null) {
                initialState = newState;
            }
            return true;
        }
        return false;
    }

    public void printSymbols() {
        System.out.println("SYMBOLS: " + String.join(", ", symbols));
    }

    public void printStates() {
        System.out.print("STATES: ");
        for (State s : states.values()) {
            System.out.print(s);
            if (s.equals(initialState)) System.out.print(" (Initial)");
            if (finalStates.contains(s)) System.out.print(" (Final)");
            System.out.print(", ");
        }
        System.out.println();
    }

    public boolean setInitialState(String name) {
        String key = name.toUpperCase();
        State s = states.get(key);
        if (s == null) {
            s = new State(key);
            states.put(key, s);
            initialState = s;
            return false; // warning için
        }
        initialState = s;
        return true;
    }

    public boolean addFinalState(String name) {
        String key = name.toUpperCase();
        State s = states.get(key);
        if (s == null) {
            s = new State(key);
            states.put(key, s);
            finalStates.add(s);
            return false; // warning için
        }
        return finalStates.add(s);
    }
    public boolean isSymbolDefined(String s) {
        return symbols.contains(s.toUpperCase());
    }

    public boolean isStateDefined(String s) {
        return states.containsKey(s.toUpperCase());
    }
    public void addTransition(String symbol, String from, String to) {
        symbol = symbol.toUpperCase();
        from = from.toUpperCase();
        to = to.toUpperCase();

        State fromState = states.get(from);
        State toState = states.get(to);

        transitions.putIfAbsent(from, new HashMap<>());

        Map<String, State> symbolMap = transitions.get(from);

        if (symbolMap.containsKey(symbol)) {
            System.out.println("Warning: Overriding previous transition for <" + symbol + ", " + from + ">");
        }

        symbolMap.put(symbol, toState);
    }
}

