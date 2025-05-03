import java.io.*;

public class Logger {
    private BufferedWriter writer = null;
    private boolean isActive = false;

    public void startLogging(String filename) {
        try {
            if (isActive) stopLogging();
            writer = new BufferedWriter(new FileWriter(filename));
            isActive = true;
        } catch (IOException e) {
            System.out.println("Error: Could not start logging to file: " + filename);
        }
    }

    public void stopLogging() {
        try {
            if (writer != null) {
                writer.close();
            }
            isActive = false;
        } catch (IOException e) {
            System.out.println("Error: Could not stop logging");
        }
    }

    public void log(String line) {
        if (!isActive || writer == null) return;
        try {
            writer.write(line);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error: Failed to write to log file");
        }
    }

    public boolean isLogging() {
        return isActive;
    }
}