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

import org.apache.lucene.document.Document;
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


        public void batchSearch()throws Exception {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new ClassicSimilarity());

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

            System.out.println("Index = " + cranQuery.Index);
            Query query = parser.parse(line);
            System.out.println("Searching for: " + query.toString(field));

            TopDocs results = searcher.search(query, 1400);

            ScoreDoc[] hits = results.scoreDocs;
            for(ScoreDoc doc: hits){

                Document currDoc = searcher.doc(doc.doc);

                lines.add(Integer.toString(queryID) + " Q0 " + Integer.toString(doc.doc + 1) + " " + Integer.toString(0) + " " + doc.score + " STANDARD");

            }
            queryID++;
        }

        Path file = Paths.get("trec_eval.9.0/results.txt");
        Files.write(file, lines, Charset.forName("UTF-8"));

        reader.close();
    }
}