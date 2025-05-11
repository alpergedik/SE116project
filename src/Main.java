
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("FSM DESIGNER 1.0 " + java.time.LocalDateTime.now());
        Scanner scanner = new Scanner(System.in);
        FSM fsm = new FSM();
        Logger logger = new Logger();
        CommandParser parser = new CommandParser(fsm, logger);

        while (true) {
            System.out.print("? ");
            StringBuilder input = new StringBuilder();
            String line;
            do {
                line = scanner.nextLine();
                input.append(line).append("\n");
            } while (!line.contains(";"));

            if (parser.parseAndExecute(input.toString().trim())) {
                break;
            }
        }
        System.out.println("TERMINATED BY USER");
    }
}
