package gui;

import index.DirectoryIndex;
import index.Index;
import index.context.ContextIndex;
import index.context.DirectoryContextIndex;
import search.ContextSearchEngine;
import search.RankedRetrievalSearchEngine;
import search.SearchEngine;

import java.nio.file.Paths;

public class MainProgram {

    public static void main(String[] args) {
        SearchEngine searchEngine;
        if (args.length == 0) {
            usage();
            return;
        } else if (args.length == 1) {
            searchEngine = getRankedRetrievalSearchEngine (args);
        } else {
            searchEngine = getContextSearchEngine (args);
        }

        SearchWindowController searchWindowController = new SearchWindowController(searchEngine);

        searchWindowController.displayFrame();
    }

    private static SearchEngine getRankedRetrievalSearchEngine(String[] args) {
        Index index = new DirectoryIndex(Paths.get (args[0]));
        return new RankedRetrievalSearchEngine (index);
    }

    private static SearchEngine getContextSearchEngine(String[] args) {
        Index index = new DirectoryIndex(Paths.get (args[0]));
        ContextIndex contextIndex = new DirectoryContextIndex(Paths.get(args[1]));
        return new ContextSearchEngine (index, contextIndex);
    }


    private static void usage() {
        System.out.println("Usage: java gui.MainProgram index-dir [context-index-dir]");
    }
}
