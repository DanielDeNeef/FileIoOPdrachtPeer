package be.intecbrussel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * File Io opdracht opgemaakt in peer.
 * Hannes en Daniel
 */
//TODO: Ik heb een .gitignore gemaakt met daarin de nodige excludes voor user/project specific files.
// Deze zal de getrackte files niet hiden. Maar dan hebben jullie een template voor het volgende project.

public class MainApp {

    //TODO: Jullie hebben de main rechtstreeks in deze klasse gemaakt.
    // Probeer deze in een apparte klasse te maken, Dan kon je van deze klasse een util klasse maken.

    public static void main(String[] args) {

        //TODO: Hier hadden jullie de 2 strings in final variabelen kunnen plaatsen [source, destination]

        File folder = new File("C:/data/unsorted");
        sortMyFiles(folder);


        printReport(new File("C:/data/sorted"));
    }

    //TODO: Zeer goed dat jullie gebruik maken van Javadoc!

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
                //TODO: toString hier aanroepen is redundant, getName returned namelijk een String
                String name = file.getName().toString();

                //TODO: Wanneer je zou gebruiken maken van '.'
                // ga je de vluggere lastIndexOf(char c) methode gebruiken
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
        //TODO: Hier had je opnieuw gebruik kunnen maken van de final destination variabele
        File dir = new File("C:/data/sorted/" + extenstion);
        //check if folder doesn't exist than create new one else do nothing
        //TODO: Doet hetzelfde: Files.createDirectories(dir.toPath());
        if (!dir.exists()) {
            dir.mkdir();  //TODO: Feedback hier kan handig zijn, return value gaat nu verloren.
        }
    }

    /**
     * make a copy of the given file and add it to the new directory that will have the same name as the file extension
     *
     * @param address   is the starting path of the file
     * @param extension is the extension of the file so it will be added to the correct directory
     * @param filename  is the new path of the copied file.
     */
    //TODO: copyToSortedFolder, probeer de Java conventions te volgen in je benamingen.
    public static void copyTosortedFolder(String address, String extension, String filename) {
        try {
            Files.copy(Paths.get(address), Paths.get("C:/data/sorted/" + extension + "/" + filename));
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }

    /**
     * this method will check all files in the given directory
     * if a other directory is detected the method will restart all over into the new found directory as well
     *
     * @param dir //TODO: missing param description.
     */
    public static void sortMyFiles(File dir) {
        File[] files = dir.listFiles();

        //TODO: NullPointer kan hier gebeuren wanneer de map leeg is,
        // beter om hier een null check te doen.
        for (File file : files) {
            if (file.isDirectory()) {
                sortMyFiles(file);
            } else {
                makeDir(getExtension(file));
                //TODO: toString is hier redundant, getName returned een String
                copyTosortedFolder(file.toString(), getExtension(file), file.getName().toString());
            }
        }
    }

    /**
     * this method print all the files name and their read and write ability.
     * it also show the files in all subfolder of the given directory
     *
     * @param dir //TODO: missing param description.
     */
    public static void printReport(File dir) {
        File[] files = dir.listFiles();

        //TODO: Ik heb op deze plaats een null check gedaan, wegens het krijgen van nullpointer exception.
        if (files != null) {

            for (File file : files) {
                if (file.isDirectory()) {
                    //TODO: final String layout = "\n--------\n"
                    // Wanneer we een literal meer dan 1 keer gebruiken,
                    // is het verstandig deze in een variabele op te slaan
                    printToFile("\n--------\n");
                    printToFile(file.getName() + ":");
                    printToFile("\n--------\n");
                    printReport(file);
                } else {
                    printToFile(String.format("%-50s|%13s|%13s|%n", file.getName(), (file.canRead() ? "x      " : "/       "),
                            //TODO: Wanneer een statement te lang word kan je deze best opsplitsen.
                            (file.canWrite() ? "x       " : "/       ")));
                }
            }
        }
    }

    //TODO: Deze methode kan beter opgesplitst worden in verschillende kleinere methoden

    public static void printToFile(String text) {
        //TODO: Deze methode wordt eigenlijk maar 1 keer echt gebruikt, maar wordt elke iteratie opgeroepen in methode
        // hierboven.
        checkIfExist();
        //TODO: log.txt wordt op 3 verschillende plaatsen gebruikt, beter hier een constante van maken.
        try (FileWriter log = new FileWriter("log.txt", true)) {

            log.write(text);


        } catch (IOException io) {
            //TODO: Op deze manier verlies je de informatie van de omringende exception.
            System.out.println(io.getCause());
        }

    }

    public static void checkIfExist() {
        if (!new File("log.txt").exists()) {
            try (FileWriter log = new FileWriter("log.txt", true)) {
                //TODO: Het gebruik van \t had hier duidelijker geweest.
                log.write("\nname                                              |  readable   |  writeable  |\n");

            } catch (IOException io) {
                //TODO: Op deze manier verlies je de informatie van de omringende exception.
                System.out.println(io.getCause());
            }
        }
    }
}
