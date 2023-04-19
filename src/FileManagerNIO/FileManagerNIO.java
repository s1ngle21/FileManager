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

        BufferedReader reader = null;

        try {
            while (true) {
                logger.info(currentDirectory);
                String line;
                reader = new BufferedReader(new InputStreamReader(System.in));
                line = reader.readLine();
                String[] commandSplit = line.trim().split("\\s+");
                if (commandSplit.length == 0) {
                    continue;
                }

                String firstCommand = commandSplit[0];

                switch (firstCommand) {
                    case "cd" -> changeDirectory(commandSplit);
                    case "cp" -> copy(commandSplit);
                    case "ls" -> listFiles();
                    case "pwd" -> printWorkingDirectory();
                    default -> logger.info("Unknown command " + firstCommand);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE,"Error has occurred while closing BufferedReader", e);
                }
            }
        }
    }

    private void changeDirectory(String[] commandSplit) {
        if (commandSplit.length > 1) {
            String target = commandSplit[1];
            changeDirectory(target);
        } else {
            logger.info("Usage: cd <directory>");
        }
    }

    private void copy(String[] commandSplit) {
        if (commandSplit.length > 2) {
            String source = commandSplit[1];
            String destination = commandSplit[2];
            copyFiles(source, destination);
        } else {
            logger.info("Usage: cp <source> <target>");
        }
    }

    private void printWorkingDirectory() {
        logger.info(currentDirectory);
    }

    private void listFiles() {
        Path dir = Paths.get(currentDirectory);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)){
            for (Path file : stream) {
                logger.info(String.valueOf(file.getFileName()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void copyFiles(String source, String target) {

        try {
            Path pathSource = Paths.get(source);
            Path pathTarget = Paths.get(target);
            Files.copy(pathSource, pathTarget);
            logger.info("File copied successfully.");
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
            logger.severe("Error! " + newPath + " is not a directory");
        }
    }
}
