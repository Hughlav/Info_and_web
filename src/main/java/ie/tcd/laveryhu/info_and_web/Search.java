package ie.tcd.laveryhu.info_and_web;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import data_objects.CranDocument;
import document_parser.Parser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;
import Analyzer.MyAnalyzer;

public class Search {

    static String indexPath = "index/";
    static String queryPath = "cran/cran.qry";
    static String field = "contents";


//    public static void main(String[] args) throws Exception {
        public void batchSearch()throws Exception {
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