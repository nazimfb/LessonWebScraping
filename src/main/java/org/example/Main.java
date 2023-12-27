package org.example;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

public class Main {
    static String baseUrl;
    static HashSet<String> hashset = new HashSet<>();

    public static void main(String[] args) throws IOException {
        baseUrl = "https://code.edu.az";
        hashset.add(baseUrl);
        getLinksFromPage("https://code.edu.az");
    }

    public static void getLinksFromPage(String url) throws IOException {
        try {
            Document page = Jsoup.connect(url).get();
            Elements links = page.select("a[href]");

            links.stream()
                    .map(l -> l.attr("href"))
                    .filter(l -> l.startsWith(baseUrl) || l.startsWith("/"))
                    .map(l -> l.startsWith("/") ? baseUrl + l : l)
                    .filter(l -> !hashset.contains(l))
                    .distinct()
                    .peek(l -> hashset.add(l))
                    .peek(l -> System.out.print("[" + url + "]: "))
                    .peek(System.out::println)
                    .forEach(url1 -> {
                        try {
                            getLinksFromPage(url1);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            System.out.print("");
        }
    }
}