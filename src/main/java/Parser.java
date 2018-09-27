import data_objects.CranDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Parser
{
    static String PATH_TO_CRAN = "cran/cran.all.1400";

    private static String docsString = "";
    private static String[] docs;

    private static ArrayList<CranDocument> documents = new ArrayList<>();

    public static void main(String[] args){
        System.out.println("Test");

        readDocs();

        parseDocs();

        System.out.println(documents.get(10).Words);
    }

    private static void readDocs() {
        try {
            try (
                    Stream<String> stream = Files.lines(Paths.get(PATH_TO_CRAN))) {
                stream.forEach(new Consumer<String>() {
                    public void accept(String line) {
                        docsString = docsString.concat(line + "\n");
                    }
                });
            }

            docs = docsString.split(".I");

        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    private static void parseDocs(){
        for(String doc: docs){
            if(!doc.equals("")) {
                parseDoc(doc);
            }
        }
    }

    private static void parseDoc(String doc){
        CranDocument document = new CranDocument();

        LinkedList<String> docLines = new LinkedList<String>(Arrays.asList(doc.split("\n")));

        String docNum = docLines.remove(0);
        System.out.println(docNum);

        document.Index = Integer.parseInt(docNum.trim());

        String currentSection = "";

        for(String line: docLines){
            if(line.startsWith(".T")){
                currentSection = ".T";
                document.Title = line;
            }
            else if(line.startsWith(".A")){
                currentSection = ".A";
                document.Authors = line;
            }
            else if(line.startsWith(".B")){
                currentSection = ".B";
                document.Bib = line;
            }
            else if(line.startsWith(".W")){
                currentSection = ".W";
                document.Words = line;
            }
            else{
                addLineToSection(line, currentSection, document);
            }
        }
        documents.add(document);
    }

    private static void addLineToSection(String line, String section, CranDocument document){
        switch (section){
            case ".T":
                document.Title += "\n" + line;
            case ".A":
                document.Authors += "\n" + line;
            case ".B":
                document.Bib += "\n" + line;
            case ".W":
                document.Words += "\n" + line;
        }
    }
}