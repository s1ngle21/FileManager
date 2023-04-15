package FileManagerNIO;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileManagerNIO {
    private static final Logger logger = Logger.getLogger(Logger.class.getName());
    private String currentDirectory;

    public FileManagerNIO() {
        currentDirectory = "/";
    }

    public void start() {

        while (true) {

            logger.log(Level.INFO,currentDirectory);
            String line = null;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                line = reader.readLine();
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
                            logger.log(Level.INFO,"Usage: cd <directory>");
                        }
                        break;
                    case "cp":
                        if (commandSplit.length > 2) {
                            String source = commandSplit[0];
                            String target = commandSplit[1];
                            copyFiles(source, target);
                        } else {
                            logger.log(Level.INFO,"Usage: cd <source> <target>");
                        }
                        break;
                    case "ls":
                        listFiles();
                        break;
                    case "pwd":
                        printWorkingDirectory();
                        break;
                    default:
                        logger.log(Level.INFO,"Unknown command " + firstCommand);
                        break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void printWorkingDirectory() {
        logger.log(Level.INFO,currentDirectory);
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
            logger.log(Level.INFO, String.valueOf(file.getFileName()));
        }
    }

    private void copyFiles(String source, String target) {
        Path pathSource = Paths.get(source);
        Path pathTarget = Paths.get(target);
        try {
            Files.copy(pathSource, pathTarget);
            logger.log(Level.INFO,"File copied successfully.");
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
            logger.log(Level.INFO,"Error! " + newPath + " is not a directory");
        }
    }
}
