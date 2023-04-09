package FileManagerIO;
import java.io.*;


public class FileManager {

    private String currentDirectory;

    public FileManager() {
        currentDirectory = "/";
    }

    public void executeCommands()  {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println(currentDirectory);
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] commandSplit = line.trim().split("\\s+");
            if (commandSplit.length == 0) {
                continue;
            }
            String firstCommand = commandSplit[0];

            switch (firstCommand) {
                case "cd":
                    if (commandSplit.length > 1) {
                        String target = commandSplit[1];
                        changeDirectory(target);
                    } else {
                        System.out.println("Usage: cd <directory>");
                    }
                    break;
                case "cp":
                    if (commandSplit.length > 2) {
                        String source = commandSplit[1];
                        String destination = commandSplit[2];
                        copyFile(source, destination);
                    } else {
                        System.out.println("Usage: cd <source> <target>");
                    }
                    break;
                case "ls":
                    listFiles();
                    break;
                case "pdw":
                    printWorkingDirectory();
                    break;
                default:
                    System.out.println("Unknown command " + firstCommand);
                    break;
            }
        }
    }

    private void printWorkingDirectory() {
        System.out.println(currentDirectory);
    }

    private void listFiles() {
        File file = new File(currentDirectory);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                System.out.println(f.getName());
            }
        }
    }

    private void changeDirectory(String path) {
        String newPath;
        if (path.startsWith("/")) {
            newPath = path;
        } else {
            newPath = currentDirectory + "/" + path;
        }
        File file = new File(newPath);
        if (file.isDirectory()) {
            currentDirectory = newPath;
        } else {
            System.out.println("Error! " + newPath + " is not a directory");
        }
    }

    private void copyFile(String source, String destination) {
        File sourceFile = new File(source);
        File destinatoinFile = new File(destination);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(destinatoinFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line);
                System.out.println("File copied successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
