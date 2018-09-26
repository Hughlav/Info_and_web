import data_objects.CranDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Parser
{
    static String PATH_TO_CRAN = "cran/cran.all.1400";

    private List<CranDocument> documents;

    public static void main(String[] args){
        System.out.println("Test");
        try {
            try(
            Stream<String> stream = Files.lines(Paths.get(PATH_TO_CRAN))){
                stream.forEach(System.out::println);
            }
        }
        catch (IOException e){
            System.out.println("Error");
        }
    }


}
