package com.inforetros.option;

import com.inforetros.app.Constants;
import com.inforetros.app.Sporch;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
public class PlaylistSearcher implements AutoCloseable{
    private final String playlistIndexDir = Sporch.R_INDEX_PATH;

    private final DirectoryReader indexReader;
    private final IndexSearcher indexSearcher;
    public QueryParser queryParser;

    public PlaylistSearcher() throws IOException {
        indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(playlistIndexDir)));
        indexSearcher = new IndexSearcher(indexReader);
    }

    public TopDocs search(String field, String searchQuery) throws IOException, ParseException {
        queryParser = new QueryParser(field, new StandardAnalyzer(EnglishAnalyzer.getDefaultStopSet()));
        Query query = queryParser.parse(searchQuery);
        return indexSearcher.search(query, Constants.MAX_SEARCH);
    }

    public List<String> searchByPid(String pid) throws IOException, ParseException {
        TopDocs docs = indexSearcher.search(new TermQuery(new Term(Constants.PLAYLIST_PID, pid)), 1);

        if (docs.totalHits.value > 0) {
            Document d = indexSearcher.doc(docs.scoreDocs[0].doc);
            return List.of(d.get(Constants.PLAYLIST_TRACK_URI_LIST).split(","));
        }
        return null;
    }

    public Document getDocument(ScoreDoc scoreDoc) throws IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }

    public void close() throws IOException {
        indexReader.close();
    }
}
