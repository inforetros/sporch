package com.inforetros.option;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.inforetros.adapter.DateLongFormatTypeAdapter;
import com.inforetros.app.Sporch;
import com.inforetros.model.Playlist;
import com.inforetros.model.Track;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
public class BuildIndexer {
    public void run() {

        try {
            // Check data set existence
            File file = new File(Sporch.JSON_FOLDER);
            if (!file.exists()) {
                System.out.println("JSON folder (Spotify Playlist Dataset) could not be found");
                System.exit(1);
            }

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateLongFormatTypeAdapter())
                    .create();

            Directory directory = FSDirectory.open(Paths.get(Sporch.INDEX_PATH));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);

            // Note that IndexWriterConfig.OpenMode.CREATE will override the original index in the folder
            config.setOpenMode( IndexWriterConfig.OpenMode.CREATE );

            // Lucene's default BM25Similarity stores document field length using a "low-precision" method.
            config.setSimilarity( new BM25Similarity() );

            IndexWriter writer = new IndexWriter(directory, config);

            // This is the field setting for metadata field (no tokenization, searchable, and stored).
            FieldType fieldTypeMetadata = new FieldType();
            fieldTypeMetadata.setOmitNorms( true );
            // Only documents are indexed: term frequencies and positions are omitted.
            // Phrase and other positional queries on the field will throw an exception,
            // and scoring will behave as if any term in the document appears only once
            fieldTypeMetadata.setIndexOptions( IndexOptions.DOCS );
            fieldTypeMetadata.setStored( true );
            fieldTypeMetadata.setTokenized( false );
            fieldTypeMetadata.freeze();

            // This is the field setting for normal text field (tokenized, searchable, store document vectors)
            FieldType fieldTypeText = new FieldType();
            // Indexes documents, frequencies and positions.
            // This is a typical default for full-text search:
            // full scoring is enabled and positional queries are supported.
            fieldTypeText.setIndexOptions( IndexOptions.DOCS_AND_FREQS_AND_POSITIONS );
            fieldTypeText.setStoreTermVectors( true );
            fieldTypeText.setStoreTermVectorPositions( true );
            fieldTypeText.setTokenized( true );
            fieldTypeText.setStored( true );
            fieldTypeText.freeze();

            // Iterate over JSON files in the folder
            Files.walk(Paths.get(Sporch.JSON_FOLDER))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(jsonFile -> {
                        try {
                            // Parse JSON and extract content
                            String jsonContent = new String(Files.readAllBytes(jsonFile));
                            JsonObject data = new JsonParser().parse(jsonContent).getAsJsonObject();
                            JsonArray playlistsJson = data.getAsJsonArray("playlists");
                            Type listType = new TypeToken<List<Playlist>>() {}.getType();
                            List<Playlist> playlists = gson.fromJson(playlistsJson, listType);

                            for (Playlist playlist :
                                    playlists) {

                                for (Track track :
                                        playlist.getTracks()) {

                                    // Create Lucene Document
                                    Document document = new Document();
                                    // Playlist is metadata
                                    // Track name, album name and artist name are token
                                    document.add(new Field("track_name", track.getTrack_name(), fieldTypeText));
                                    document.add(new Field("album_name", track.getAlbum_name(), fieldTypeText));
                                    document.add(new Field("artist_name", track.getArtist_name(), fieldTypeText));
                                    document.add(new Field("playlist_name", playlist.getName(), fieldTypeMetadata));

                                    // Add Document to IndexWriter
                                    System.out.println( "[" + jsonFile.getFileName() + "] indexing track " + track.getTrack_name() + " in playlist " + playlist.getName() );
                                    writer.addDocument(document);
                                }

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            // Close the IndexWriter
            writer.close();
            directory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
