package FileManagerNIO;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileManagerNIO {

    private String currentDirectory;

    public FileManagerNIO() {
        currentDirectory = "/";
    }

    public void executeCommands() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {System.out.println(currentDirectory);
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String[] commandSplit = line.trim().split("\\+s");
            if (commandSplit.length == 0) {
                continue;
            }

            String firstCommand = commandSplit[0];

            switch (firstCommand) {
                case "cd":
                    if (commandSplit.length > 1) {
                        String path = commandSplit[1];
                        changeDirectory(path);
                    } else {
                        System.out.println("Usage: cd <directory>");
                    }
                    break;
                case "cp":
                    if (commandSplit.length > 2) {
                        String source = commandSplit[0];
                        String target = commandSplit[1];
                        copyFiles(source, target);
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
        Path dir = Paths.get(currentDirectory);
        DirectoryStream<Path> stream = null;
        try {
            stream = Files.newDirectoryStream(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Path file : stream) {
            System.out.println(file.getFileName());
        }
    }

    private void copyFiles(String source, String target) {
        Path pathSource = Paths.get(source);
        Path pathTarget = Paths.get(target);
        try {
            Files.copy(pathSource, pathTarget);
            System.out.println("File copied successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void changeDirectory(String path) {
        String newPath;
        if (path.startsWith("/")) {
            newPath = path;
        } else {
            newPath = currentDirectory + "/" + path;
        }
        Path file = Paths.get(newPath);
        if (Files.isDirectory(file)) {
            currentDirectory = newPath;
        } else {
            System.out.println("Error! " + newPath + " is not a directory");
        }
    }
}
