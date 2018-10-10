package ie.tcd.laveryhu.info_and_web;

public class IndexAndSearch {
    static Index index = new Index();
    static Search search = new Search();

    public static void main(String[] args) throws Exception {
        System.out.println("Indexing documents ... ");
        index.prepare();

        System.out.println("Finished indexing. Starting batch search");

        search.batchSearch();

        System.out.println("Batch search complete. Outputs written to results.txt in tre_eval directory");
    }
}
