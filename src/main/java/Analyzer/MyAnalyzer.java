package Analyzer;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.tartarus.snowball.ext.PorterStemmer;

import java.util.Arrays;
import java.util.List;

public final class MyAnalyzer extends StopwordAnalyzerBase {
    public static final CharArraySet MY_STOP_WORD_SET;

    public MyAnalyzer() {
        this(MY_STOP_WORD_SET);
    }

    public MyAnalyzer(CharArraySet stopwords) {
        super(stopwords);
    }

    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer source = new StandardTokenizer();

        TokenStream result = new LowerCaseFilter(source);

        result = new StopFilter(result, this.stopwords);

        result = new KStemFilter(result);

        result = new SnowballFilter(result, new PorterStemmer());
        return new TokenStreamComponents(source, result);
    }

    protected TokenStream normalize(String fieldName, TokenStream in) {
        return new LowerCaseFilter(in);
    }

    static {
        List<String> stopWords = Arrays.asList("a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with", "flow", "what", "boundary", "pressure", "layer", "number");
        CharArraySet stopSet = new CharArraySet(stopWords, false);
        MY_STOP_WORD_SET = CharArraySet.unmodifiableSet(stopSet);
    }
}
