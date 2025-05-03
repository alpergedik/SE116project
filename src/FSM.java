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
    public void printTransitions() {
        System.out.println("TRANSITIONS:");
        for (String from : transitions.keySet()) {
            for (String symbol : transitions.get(from).keySet()) {
                System.out.println(symbol + " " + from + " " + transitions.get(from).get(symbol));
            }
        }
    }
    public void printFSM() {
        // SYMBOLS
        System.out.println("SYMBOLS {" + String.join(", ", symbols) + "}");

        // STATES
        System.out.print("STATES {");
        List<String> stateLines = new ArrayList<>();
        for (State s : states.values()) {
            StringBuilder sb = new StringBuilder(s.toString());
            if (s.equals(initialState)) sb.append(" (Initial)");
            if (finalStates.contains(s)) sb.append(" (Final)");
            stateLines.add(sb.toString());
        }
        System.out.println(String.join(", ", stateLines) + "}");

        // INITIAL STATE
        if (initialState != null)
            System.out.println("INITIAL STATE " + initialState);

        // FINAL STATES
        System.out.print("FINAL STATES {");
        List<String> finalStateNames = new ArrayList<>();
        for (State f : finalStates) {
            finalStateNames.add(f.toString());
        }
        System.out.println(String.join(", ", finalStateNames) + "}");

        // TRANSITIONS
        System.out.print("TRANSITIONS ");
        List<String> transitionStrings = new ArrayList<>();
        for (String from : transitions.keySet()) {
            for (String symbol : transitions.get(from).keySet()) {
                State to = transitions.get(from).get(symbol);
                transitionStrings.add(symbol + " " + from + " " + to);
            }
        }
        System.out.println(String.join(", ", transitionStrings));
    }

    public List<String> execute(String input) {
        List<String> trace = new ArrayList<>();
        if (initialState == null) {
            trace.add("NO");
            return trace;
        }

        State currentState = initialState;
        trace.add(currentState.toString());

        for (char c : input.toCharArray()) {
            String symbol = String.valueOf(c).toUpperCase();

            if (!symbols.contains(symbol)) {
                trace.add("Error: Invalid symbol '" + symbol + "'");
                trace.add("NO");
                return trace;
            }

            Map<String, State> currentTransitions = transitions.get(currentState.getName());
            if (currentTransitions == null || !currentTransitions.containsKey(symbol)) {
                trace.add("NO");
                return trace;
            }

            currentState = currentTransitions.get(symbol);
            trace.add(currentState.toString());
        }

        if (finalStates.contains(currentState)) {
            trace.add("YES");
        } else {
            trace.add("NO");
        }

        return trace;
    }
}

