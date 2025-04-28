import java.util.*;

public class CommandParser {
    private FSM fsm;

    public CommandParser(FSM fsm) {
        this.fsm = fsm;
    }

    public boolean parseAndExecute(String rawInput) {
        String cleanInput = removeComments(rawInput).trim();

        if (cleanInput.isEmpty()) return false;

        String[] tokens = cleanInput.split("\\s+");
        String command = tokens[0].toUpperCase();

        switch (command) {
            case "EXIT":
                return true;

            case "SYMBOLS":
                handleSymbols(Arrays.copyOfRange(tokens, 1, tokens.length));
                break;

            case "STATES":
                handleStates(Arrays.copyOfRange(tokens, 1, tokens.length));
                break;

            default:
                System.out.println("Warning: Unknown command");
        }

        return false;
    }

    private String removeComments(String input) {
        String[] lines = input.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            int semicolonIndex = line.indexOf(';');
            if (semicolonIndex != -1) {
                line = line.substring(0, semicolonIndex);
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private void handleSymbols(String[] args) {
        if (args.length == 0) {
            fsm.printSymbols();
            return;
        }

        for (String symbol : args) {
            if (!symbol.matches("[A-Za-z0-9]")) {
                System.out.println("Warning: Invalid symbol '" + symbol + "'");
                continue;
            }
            boolean added = fsm.addSymbol(symbol);
            if (!added) {
                System.out.println("Warning: Symbol '" + symbol + "' already declared");
            }
        }
    }

    private void handleStates(String[] args) {
        if (args.length == 0) {
            fsm.printStates();
            return;
        }

        for (String state : args) {
            if (!state.matches("[A-Za-z0-9]+")) {
                System.out.println("Warning: Invalid state '" + state + "'");
                continue;
            }
            boolean added = fsm.addState(state);
            if (!added) {
                System.out.println("Warning: State '" + state + "' already declared");
            }
        }
    }
}
