package documentparser;
import data_objects.CranDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Parser
{
    static String pathToFile = "";
    private static String docsString = "";
    private static String[] docs;

    public ArrayList<CranDocument> documents = new ArrayList<>();

    public void createDocs(String path){
        pathToFile = path;
        readDocs();
        parseDocs();
    }

    private static void readDocs() {
        try {
            try (
                    Stream<String> stream = Files.lines(Paths.get(pathToFile))) {
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

    private void parseDocs(){
        for(String doc: docs){
            if(!doc.equals("")) {
                parseDoc(doc);
            }
        }
    }

    private void parseDoc(String doc){
        CranDocument document = new CranDocument();

        LinkedList<String> docLines = new LinkedList<String>(Arrays.asList(doc.split("\n")));

        String docNum = docLines.remove(0);
        System.out.println(docNum);

        document.Index = Integer.parseInt(docNum.trim());

        String currentSection = "";

        for(String line: docLines){
            if(line.startsWith(".T")){
                line = line.substring(2);
                currentSection = ".T";
                document.Title += "\n" + line;
            }
            else if(line.startsWith(".A")){
                line = line.substring(2);
                currentSection = ".A";
                document.Authors += "\n" + line;
            }
            else if(line.startsWith(".B")){
                line = line.substring(2);
                currentSection = ".B";
                document.Bib += "\n" + line;
            }
            else if(line.startsWith(".W")){
                line = line.substring(2);
                currentSection = ".W";
                document.Words += "\n" + line;
            }
            else{
                addLineToSection(line, currentSection, document);
            }
        }
        documents.add(document);
    }

    private void addLineToSection(String line, String section, CranDocument document){
        switch (section){
            case ".T":
                document.Title += " " + line;
                break;
            case ".A":
                document.Authors += " " + line;
                break;
            case ".B":
                document.Bib += " " + line;
                break;
            case ".W":
                document.Words += " " + line;
                break;
        }
    }
}