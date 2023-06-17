package com.inforetros.command;

import com.inforetros.app.Sporch;
import com.inforetros.option.*;
import org.apache.lucene.queryparser.classic.ParseException;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
@CommandLine.Command(name = "sporch", mixinStandardHelpOptions = true, version = "sporch 1.0", description = "Searches for the given query in Spotify playlists")
public class SporchCommand implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "query string to search or a track URI to provide recommendation")
    String parameter;

    @CommandLine.Option(names = { "-n" }, description = "number of searching results", defaultValue = "10", paramLabel = "num-of-results")
    public static Integer NUMOFRESULTS = 10;

    @CommandLine.Option(names = { "-tn", "--track_name" }, description = "search query within the track_name")
    private boolean isTrackName = false;

    @CommandLine.Option(names = { "-arn", "--artist_name" }, description = "search query within the artist_name")
    private boolean isArtistName = false;

    @CommandLine.Option(names = { "-aln", "--album_name" }, description = "search query within the album_name")
    private boolean isAlbumName = false;

    @CommandLine.Option(names = { "-a", "--all" },description = "search query within all fields of the track")
    private boolean isAll = false;

    @CommandLine.Option(names = { "-r", "-–recommend" }, description = "utilize the recommendation feature by providing a track URI")
    private boolean isRecommend = false;

    @CommandLine.Option(names = { "-h", "--help" }, usageHelp = true, description = "display a help message")
    private boolean helpRequested = false;

    @Override
    public Integer call() throws Exception {
        String fieldName;

        // 1. Check index
        File file = new File(Sporch.INDEX_PATH);
        if (file.exists()) {
            System.out.println("The index file has been found");
        } else {
            // 1.1. If the corpus is not preprocessed, build index
            System.out.println("The index file could not be found. The data will be indexed please wait...");
            BuildIndexer indexer = new BuildIndexer();
            indexer.run();
            System.out.println("The data has been indexed.");
        }

        // 2. Check recommendation index
        file = new File(Sporch.R_INDEX_PATH);
        if (file.exists()) {
            System.out.println("The recommendation index file has been found");
        } else {
            // 2.1. If the corpus is not preprocessed, build index
            System.out.println("The recommendation index file could not be found. The data will be indexed please wait...");
            try (PlaylistIndexer indexFiles = new PlaylistIndexer()) {
                long startTime = System.currentTimeMillis();
                indexFiles.createIndex(Sporch.JSON_FOLDER);
                long endTime = System.currentTimeMillis();
                System.out.println("All files indexed, time taken: " + (endTime - startTime) + " ms");
            }
        }

        // 3. Check recommendation index
        file = new File(Sporch.R_INDEX_PATH + "\\track");
        if (file.exists()) {
            System.out.println("The recommendation track index file has been found");
        } else {
            // 3.1. If the corpus is not preprocessed, build index
            System.out.println("The recommendation track index file could not be found. The data will be indexed please wait...");
            try (TrackIndexer TrackIndexer = new TrackIndexer()) {
                long startTime = System.currentTimeMillis();
                TrackIndexer.createIndex(Sporch.JSON_FOLDER);
                long endTime = System.currentTimeMillis();
                System.out.println("All files indexed, time taken: " + (endTime - startTime) + " ms");
            }
        }

        // 4. Give query string and perform the searching
        if(isTrackName) {
            fieldName = "track_name";
            System.out.println(parameter + " is being searching on track_name...");
            Searcher searcher = new Searcher(fieldName, parameter);
            searcher.run();
        }
        else if (isAlbumName) {
            fieldName = "album_name";
            System.out.println(parameter + " is being searching on album_name...");
            Searcher searcher = new Searcher(fieldName, parameter);
            searcher.run();
        }
        else if (isArtistName) {
            fieldName = "artist_name";
            System.out.println(parameter + " is being searching on artist_name...");
            Searcher searcher = new Searcher(fieldName, parameter);
            searcher.run();
        }
        else if (isAll) {
            fieldName = "all";
            System.out.println(parameter + " is being searching on artist_name...");
            Searcher searcher = new Searcher(fieldName, parameter);
            searcher.run();
        }
        else if (isRecommend) {
            try {
                Recommend.recommendByTrackUri(parameter);
            } catch (IOException e) {
                throw new RuntimeException("io", e);
            } catch (ParseException e) {
                throw new RuntimeException("parse", e);
            }
        }
        else {
            // help
        }

        return null;
    }

}
