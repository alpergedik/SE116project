import java.io.*;
import java.util.*;

public class CommandParser {
    private FSM fsm;
    private Logger logger;

    public CommandParser(FSM fsm) {
        this.fsm = fsm;
    }

    public CommandParser(FSM fsm, Logger logger) {
        this.fsm = fsm;
        this.logger = logger;
    }

    public boolean parseAndExecute(String rawInput) {
        String cleanInput = removeComments(rawInput).trim();

        if (logger.isLogging()) logger.log("> " + rawInput.replaceAll("\n", " "));
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

            case "INITIAL-STATE":
                handleInitialState(Arrays.copyOfRange(tokens, 1, tokens.length));
                break;

            case "FINAL-STATES":
                handleFinalStates(Arrays.copyOfRange(tokens, 1, tokens.length));
                break;

            case "TRANSITIONS":
                handleTransitions(cleanInput.substring("TRANSITIONS".length()).trim());
                break;
            case "PRINT":
                fsm.printFSM();
                break;
            case "EXECUTE":
                handleExecute(Arrays.copyOfRange(tokens, 1, tokens.length));
                break;
            case "CLEAR":
                fsm.clearAll();
                System.out.println("FSM CLEARED.");
                break;
            case "LOG":
                if (tokens.length == 1) {
                    if (logger.isLogging()) {
                        logger.stopLogging();
                        System.out.println("STOPPED LOGGING");
                    } else {
                        System.out.println("LOGGING was not enabled");
                    }
                } else {
                    String filename = tokens[1];
                    logger.startLogging(filename);
                    System.out.println("STARTED LOGGING to " + filename);
                }
                break;
            case "COMPILE":
                if (tokens.length < 2) {
                    System.out.println("Error: COMPILE requires a filename.");
                    break;
                }
                handleCompile(tokens[1]);
                break;
            case "LOAD":
                if (tokens.length < 2) {
                    System.out.println("Error: LOAD requires a filename.");
                    break;
                }
                handleLoad(tokens[1]);
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
    private void handleInitialState(String[] args) {
        if (args.length == 0) {
            System.out.println("Warning: No initial state provided");
            return;
        }
        String stateName = args[0];
        if (!stateName.matches("[A-Za-z0-9]+")) {
            System.out.println("Warning: Invalid state '" + stateName + "'");
            return;
        }
        boolean result = fsm.setInitialState(stateName);
        if (!result) {
            System.out.println("Warning: State '" + stateName + "' was not previously declared. Declaring it now.");
        }
    }

    private void handleFinalStates(String[] args) {
        if (args.length == 0) {
            System.out.println("Warning: No final states provided");
            return;
        }

        for (String stateName : args) {
            if (!stateName.matches("[A-Za-z0-9]+")) {
                System.out.println("Warning: Invalid state '" + stateName + "'");
                continue;
            }
            boolean result = fsm.addFinalState(stateName);
            if (!result) {
                System.out.println("Warning: Final state '" + stateName + "' was already declared as final");
            }
        }
    }
    private void handleTransitions(String input) {
        String[] transitionParts = input.split(",");

        for (String part : transitionParts) {
            String[] tokens = part.trim().split("\s+");

            if (tokens.length != 3) {
                System.out.println("Error: Each transition must have exactly 3 parts (symbol, fromState, toState)");
                continue;
            }

            String symbol = tokens[0].toUpperCase();
            String from = tokens[1].toUpperCase();
            String to = tokens[2].toUpperCase();

            if (!fsm.isSymbolDefined(symbol)) {
                System.out.println("Error: Invalid symbol '" + symbol + "'");
                continue;
            }
            if (!fsm.isStateDefined(from)) {
                System.out.println("Error: Invalid state '" + from + "'");
                continue;
            }
            if (!fsm.isStateDefined(to)) {
                System.out.println("Error: Invalid state '" + to + "'");
                continue;
            }

            fsm.addTransition(symbol, from, to);
        }
    }
    private void handleExecute(String[] args) {
        if (args.length != 1) {
            System.out.println("Error: EXECUTE requires a single alphanumeric input string.");
            return;
        }

        String input = args[0].toUpperCase();
        if (!input.matches("[A-Z0-9]+")) {
            System.out.println("Error: Invalid characters in input string.");
            return;
        }

        List<String> stateTrace = fsm.execute(input);
        System.out.println(String.join(" ", stateTrace));
    }
    private void handleCompile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(fsm);
            System.out.println("Compile successful");
            if (logger.isLogging()) logger.log("Compiled to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error: Could not write to file " + filename);
            e.printStackTrace();
        }
    }
    private void handleLoad(String filename) {
        if (filename.endsWith(".fsm")) {
            // Load FSM from binary file
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
                FSM loadedFSM = (FSM) in.readObject();
                this.fsm.copyFrom(loadedFSM);
                System.out.println("FSM loaded successfully from " + filename);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error: Failed to load FSM from binary file.");
            }
        } else {
            // Text file: simulate running commands
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    parseAndExecute(line.trim());
                }
                System.out.println("Commands loaded and executed from text file.");
            } catch (IOException e) {
                System.out.println("Error: Could not read file " + filename);
            }
        }
    }
}
