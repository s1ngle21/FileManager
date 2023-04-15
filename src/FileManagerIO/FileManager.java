package FileManagerIO;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileManager {
    private static final Logger logger = Logger.getLogger(Logger.class.getName());
    private String currentDirectory;

    public FileManager() {
        currentDirectory = "/";
    }

    public void start() {

        while (true) {
            logger.log(Level.INFO, currentDirectory);
            String line = null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                line = reader.readLine();
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
                            logger.log(Level.INFO, "Usage: cd <directory>");
                        }
                        break;
                    case "cp":
                        if (commandSplit.length > 2) {
                            String source = commandSplit[1];
                            String destination = commandSplit[2];
                            copyFile(source, destination);
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

        private void printWorkingDirectory () {
            logger.log(Level.INFO, currentDirectory);
        }

        private void listFiles () {
            File file = new File(currentDirectory);
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    logger.log(Level.INFO, f.getName());
                }
            }
        }

        private void changeDirectory (String path){
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
                logger.log(Level.INFO,"Error! " + newPath + " is not a directory");
            }
        }

        private void copyFile (String source, String destination){
            File sourceFile = new File(source);
            File destinatoinFile = new File(destination);

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile));
                 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(destinatoinFile))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    bufferedWriter.write(line);
                    logger.log(Level.INFO,"File copied successfully.");
                }
            } catch (IOException e) {
                throw new IOError(e);
            }
        }
    }
