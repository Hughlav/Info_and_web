import data_objects.CranDocument;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import java.io.*;

public class Index
{
    Analyzer analyzer = new StandardAnalyzer();
    IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

    static void indexDoc(IndexWriter writer, CranDocument document) throws IOException {
        Document doc = new Document();

        doc.add(new StringField("index", Integer.toString(document.Index), Field.Store.YES));

        doc.add(new TextField("contents", new StringReader(document.Words)));

        doc.add(new TextField("authors", new StringReader(document.Authors)));

        doc.add(new TextField("title", new StringReader(document.Title)));

        doc.add(new TextField("bib", new StringReader(document.Bib)));

        System.out.println("adding doc");
        writer.addDocument(doc);
    }
}
