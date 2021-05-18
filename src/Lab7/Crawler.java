package Lab7;

import java.util.*;
import java.io.*;
import java.net.*;


public class Crawler {
    public static final int max_depth = 1;

    public static void main(String[] args) {

        LinkedList<URLDepthPair> pendingURLs = new LinkedList<URLDepthPair>();
        LinkedList<URLDepthPair> processedURLs = new LinkedList<URLDepthPair>();

        URLDepthPair currentDepthPair = new URLDepthPair("http://vvfmtuci.ru/sveden/document/");
        pendingURLs.add(currentDepthPair);

        ArrayList<String> seenURLs = new ArrayList<String>();
        seenURLs.add(currentDepthPair.getURL());

        while (pendingURLs.size() != 0) {
            URLDepthPair depthPair = pendingURLs.pop();
            processedURLs.add(depthPair);
            int curDepth = depthPair.getDepth();
            LinkedList<String> linksList = new LinkedList<String>();
            linksList = Crawler.getAllLinks(depthPair);

            if (curDepth < max_depth) {
                for (int i=0;i<linksList.size();i++) {
                    String newURL = linksList.get(i);

                    if (seenURLs.contains(newURL)) {
                        continue;
                    } else {
                        URLDepthPair newDepthPair = new URLDepthPair(newURL, curDepth + 1);
                        pendingURLs.add(newDepthPair);
                        seenURLs.add(newURL);
                    }
                }
            }
        }

        System.out.println(String.valueOf(processedURLs));
    }

    private static LinkedList<String> getAllLinks(URLDepthPair myDepthPair) {

        LinkedList<String> URLs = new LinkedList<String>();
        Socket sock;

        try {
            sock = new Socket(myDepthPair.getWebHost(), 80);
        }
        catch (UnknownHostException e) {
            System.err.println("UnknownHostException: " + e.getMessage());
            return URLs;
        }
        catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return URLs;
        }

        try {
            sock.setSoTimeout(1000);
        }
        catch (SocketException exc) {
            System.err.println("SocketException: " + exc.getMessage());
            return URLs;
        }

        String docPath = myDepthPair.getDocPath();
        String webHost = myDepthPair.getWebHost();

        OutputStream outStream;

        try {
            outStream = sock.getOutputStream();
        }
        catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            return URLs;
        }

        PrintWriter myWriter = new PrintWriter(outStream, true);
        System.out.println(":::>"+webHost+docPath+"<:::");
        myWriter.println("GET " + docPath + " HTTP/1.1");
        myWriter.println("Host: " + webHost);
        myWriter.println("Connection: close");
        myWriter.println();

        InputStream inStream;
        try {
            inStream = sock.getInputStream();
        }
        catch (IOException e){
            System.err.println("IOException: " + e.getMessage());
            return URLs;
        }
        InputStreamReader inStreamReader = new InputStreamReader(inStream);
        BufferedReader BuffReader = new BufferedReader(inStreamReader);

        // Try to read line from Buffered reader.
        while (true) {
            String line;
            try {
                line = BuffReader.readLine();
            }
            catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                return URLs;
            }
            //System.out.println(line);
            if (line == null)
                break;
            int beginIndex = 0;
            int endIndex = 0;
            int index = 0;

            while (true) {
                String URL_INDICATOR = "a href=\"";
                String END_URL = "\"";

                index = line.indexOf(URL_INDICATOR, index);
                if (index == -1)
                    break;

                index += URL_INDICATOR.length();
                beginIndex = index;

                endIndex = line.indexOf(END_URL, index);
                index = endIndex;

                try {
                    String newLink = line.substring(beginIndex, endIndex);
                    if(URLs.contains(newLink))
                        continue;

                    if(newLink.startsWith("http"))
                        URLs.add(newLink);
                    else if(newLink.startsWith("tel")) {

                    }
                    else
                        URLs.add("http://"+webHost+""+newLink);
                }catch(Exception e) {
                    System.err.println("error while substringing: "+beginIndex + " " + endIndex);
                    break;
                }

            }

        }
        return URLs;
    }
}
