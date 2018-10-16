package ie.tcd.laveryhu.info_and_web;

import data_objects.CranDocument;
import document_parser.Parser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import Analyzer.MyAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

public class Index
{
    static String PATH_TO_CRAN = "cran/cran.all.1400";
    static String indexPath = "index/";
    static Analyzer analyzer = new MyAnalyzer();
//    static Analyzer analyzer = new EnglishAnalyzer();
    static IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
    static Parser myParser = new Parser();

//    public static void main(String [ ] args){
//        prepare();
//    }

    public void prepare(){
        try {
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            iwc.setOpenMode(OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(dir, iwc);
            indexDocs(writer);
//            writer.forceMerge(1);
            writer.close();
            dir.close();
        }
        catch (IOException e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
            }
    }


    static void indexDocs(IndexWriter writer) throws IOException{
        myParser.createDocs(PATH_TO_CRAN);
        for (CranDocument doc: myParser.documents){
            indexDoc(writer, doc);
        }
    }

    static void indexDoc(IndexWriter writer, CranDocument document) throws IOException {
        Document doc = new Document();

        doc.add(new StringField("index", Integer.toString(document.Index), Field.Store.YES));

        doc.add(new TextField("contents", document.Words + " " + document.Title, Field.Store.YES));

        doc.add(new StringField("authors", document.Authors, Field.Store.YES));

        doc.add(new StringField("title", document.Title, Field.Store.YES));

        doc.add(new StringField("bib", document.Bib, Field.Store.YES));

        writer.addDocument(doc);
    }
}
