package be.intecbrussel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainApp {

    public static void main(String[] args) {

        File folder = new File("c:/data/unsorted");
        sortMyFiles(folder);


        printReport(new File("c:/data/sorted"));
    }

    /**
     * the getExtension will take the path and extract the name.
     * after the name extraction the code will take everything after the . and return the result as string
     *
     * @param file address of the file
     * @return the extention of the file name after extraction.
     */
    public static String getExtension(File file) {
        String extension = null;
        try {
            if (file != null) {
                String name = file.getName().toString();
                extension = name.substring(name.lastIndexOf(".") + 1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return extension;
    }

    /**
     * create a folder with the name of the extension of the file.
     * if a folder exist then no new one will be created.
     *
     * @param extenstion here need to be given an extension as folder name
     */
    public static void makeDir(String extenstion) {
        File dir = new File("c:/data/sorted/" + extenstion);
        //check if folder doesn't exist than create new one else do nothing
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * make a copy of the given file and add it to the new directory that will have the same name as the file extension
     * @param address is the starting path of the file
     * @param extension is the extension of the file so it will be added to the correct directory
     * @param filename is the new path of the copied file.
     */
    public static void copyTosortedFolder(String address, String extension, String filename) {
        try {
            Files.copy(Paths.get(address), Paths.get("c:/data/sorted/" + extension + "/" + filename));
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }

    /**
     * this method will check all files in the given directory
     * if a other directory is detected the methode will restart all over into the new found directory as well
     * @param dir
     */
    public static void sortMyFiles(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                sortMyFiles(file);
            } else {
                makeDir(getExtension(file));
                copyTosortedFolder(file.toString(),getExtension(file),file.getName().toString());
            }
        }
    }

    /**
     * this method print all the files name and their read and write ability. it also show the files in all subfolder of the given directory
     * @param dir
     */
    public static void printReport(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                printToFile("\n--------\n");
                printToFile( file.getName()+":");
                printToFile("\n--------\n");
                printReport(file);
            } else {
                printToFile(String.format("%-50s|%13s|%13s|%n", file.getName(),(file.canRead()?"x      ":"/       "),(file.canWrite()?"x       ":"/       ")));
            }
        }
    }

    public static void printToFile(String text){
        checkIfExist();
        try(FileWriter log = new FileWriter("log.txt",true)){



            log.write(text);


        }catch (IOException io){
            System.out.println(io.getCause());
        }

    }

    public static void checkIfExist() {
        if (!new File("log.txt").exists()) {
            try (FileWriter log = new FileWriter("log.txt", true)) {
                log.write("\nname                                              |  readable   |  writeable  |\n");

            } catch (IOException io) {
                System.out.println(io.getCause());
            }
        }
    }
}
