import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import data_objects.CranDocument;
import documentparser.Parser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.TermStats;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import Analyzer.MyAnalyzer;

public class Search {

    private Search() {
    }

    static String indexPath = "index/";
    static String queryPath = "cran/cran.qry";
    static String field = "contents";

    /**
     * Simple command-line based search demo.
     */
    public static void main(String[] args) throws Exception {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new BM25Similarity());
//        searcher.setSimilarity(new ClassicSimilarity());


        Analyzer analyzer = new MyAnalyzer();
        QueryParser parser = new QueryParser(field, analyzer);

        parser.setAllowLeadingWildcard(true);

        Parser fileParser = new Parser();
        fileParser.createDocs(queryPath);
        List<CranDocument> cranQueries = fileParser.documents;

        int queryID = 1;

        List<String> lines = new ArrayList<>();

        for (CranDocument cranQuery : cranQueries) {
            String line = cranQuery.Words;

            Query query = parser.parse(line);
            System.out.println("Searching for: " + query.toString(field));

            TopDocs results = searcher.search(query, 1400);

            ScoreDoc[] hits = results.scoreDocs;
            int rank = 0;
            for(ScoreDoc doc: hits){
                lines.add(Integer.toString(queryID) + " Q0 " + Integer.toString(doc.doc) + " " + Integer.toString(rank) + " " + doc.score + " standard");

                rank++;
            }
            queryID++;
        }

        Path file = Paths.get("trec_eval.8.1/results.txt");
        Files.write(file, lines, Charset.forName("UTF-8"));

        reader.close();
    }
}