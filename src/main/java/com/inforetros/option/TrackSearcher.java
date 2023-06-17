package com.inforetros.option;

import com.inforetros.app.Constants;
import com.inforetros.app.Sporch;
import com.inforetros.model.Track;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class TrackSearcher implements AutoCloseable{

    static String trackIndexDir = Sporch.R_INDEX_PATH + "\\track";

    private final DirectoryReader indexReader;
    private final IndexSearcher indexSearcher;

    public TrackSearcher() throws IOException {
        IndexSearcher indexSearcher1;
        DirectoryReader indexReader1;
        try {
            indexReader1 = DirectoryReader.open(FSDirectory.open(Paths.get(trackIndexDir)));
            indexSearcher1 = new IndexSearcher(indexReader1);
        } catch (IndexNotFoundException e) {
            System.out.println("index not found: " + trackIndexDir);
            indexReader1 = null;
            indexSearcher1 = null;
        }
        indexSearcher = indexSearcher1;
        indexReader = indexReader1;
    }

    public String searchPidListByUri(String trackUri) throws IOException, ParseException {
        if (indexReader == null) {
            return null;
        }

        TopDocs docs = indexSearcher.search(new TermQuery(new Term(Constants.TRACK_URI, trackUri)), 1);

        if (docs.totalHits.value > 0) {
            Document d = indexSearcher.doc(docs.scoreDocs[0].doc);
            return d.get(Constants.PLAYLIST_PID_LIST);
        }
        return null;
    }

    public Track searchTrackListByUri(String trackUri) {
        if (indexReader == null) {
            return null;
        }

        TopDocs docs;

        try {
            docs = indexSearcher.search(new TermQuery(new Term(Constants.TRACK_URI, trackUri)), 1);

            if (docs.totalHits.value > 0) {
                Document d = indexSearcher.doc(docs.scoreDocs[0].doc);
                return new Track(trackUri, d.get(Constants.TRACK_NAME), d.get(Constants.ALBUM_NAME), d.get(Constants.ARTIST_NAME));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void close() throws IOException {
        if (indexReader != null) {
            indexReader.close();
        }
    }

}
