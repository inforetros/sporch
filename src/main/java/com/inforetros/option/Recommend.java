package com.inforetros.option;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Recommend {
    // ex. 6I9VzXrHxO9rA9A5euc8Ak

    public static void recommendByTrackUri(String uri) throws IOException, ParseException {
        String pidListString;
        try(TrackSearcher trackSearcher = new TrackSearcher()) {
            pidListString = trackSearcher.searchPidListByUri(uri);
        }

        if (pidListString == null) {
            throw new RuntimeException("track not find");
        }

        List<String> pidList = List.of(pidListString.split(","));
        Map<String, Integer> songCounts = new HashMap<>();

        try (PlaylistSearcher playlistSearcher = new PlaylistSearcher()) {
            for (String pid: pidList) {
                List<String> trackUriList = playlistSearcher.searchByPid(pid);
                trackUriList.forEach(tackUri -> {
                    songCounts.merge(tackUri, 1, Integer::sum);
                });
            }
        }

        try (TrackSearcher trackSearcher = new TrackSearcher()) {
            songCounts.entrySet()
                    .stream()
                    .collect(Collectors.toMap(entry -> trackSearcher.searchTrackListByUri(entry.getKey()), Map.Entry::getValue))
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(10)
                    .forEach(System.out::println);
        }
    }
}