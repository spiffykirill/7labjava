package Lab7;

import java.net.*;


public class URLDepthPair {
    private int depth;
    private String url = "";

    URLDepthPair(String u, int d){
        url = u;
        depth = d;
    }
    URLDepthPair(String u){
        url = u;
        depth = 0;
    }

    public String getURL() {
        return url;
    }
    public int getDepth() {
        return depth;
    }
    public String toString() {
        return url + " : " + depth+"\n";
    }

    public String getDocPath() {
        try {
            URL turl = new URL(url);
            return turl.getPath();
        }
        catch (MalformedURLException e) {
            System.err.println("MalformedURLException in getDocPath(): " + e.getMessage());
            return null;
        }
    }

    public String getWebHost() {
        try {
            URL turl = new URL(url);
            return turl.getHost();
        }
        catch (MalformedURLException e) {
            System.err.println("MalformedURLException in getWebHost: " + e.getMessage());
            return null;
        }
    }
}
