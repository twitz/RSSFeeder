package ml223vz.dv606.rssfeeder;

import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * TODO: Maybe create bean "channel" that is iterable with posts
 */
public class RSSHandler extends DefaultHandler{
    /*
    EXAMPLE OF DATA

    <channel>
        <title>SweClockers.com</title>
        <link>http://www.sweclockers.com</link>
        <description>Nya artiklar postade på sweclockers.com</description>
        <language>sv</language>
        <lastBuildDate>Sat, 17 Oct 2015 22:08:05 +0200</lastBuildDate>
        <ttl>10</ttl>
        <item>
            <title>Fredagspanelen 102: Radeon Gemini i höst, nya Surface från Microsoft, gäster i studion</title>
            <link>http://www.sweclockers.com/artikel/21195-fredagspanelen-102-radeon-gemini-i-host-nya-surface-fran-microsoft-gaster-i-studion</link>
            <description><![CDATA[Det vankas ett riktigt specialavsnitt av SweClockers fredagspanel när en hel hög gäster besöker studion. På agendan hamnar allt från hårdvara till spelhöstens höjdpunkter.]]></description>
            <guid isPermaLink="true">http://www.sweclockers.com/artikel/21195</guid>
            <pubDate>Fri, 16 Oct 2015 19:38:20 +0200</pubDate>
            <comments>http://www.sweclockers.com/forum/trad/1393212</comments>
        </item>
    */

    private static final String TAG = "Feedr.Handler";

    private int CURRENT_ELEMENT;

    private static final int IN_TITLE = 1;
    private static final int IN_DESCRIPTION = 2;
    private static final int IN_LINK = 3;

    private String ITEM = "item";
    private String TITLE = "title";
    private String DESCRIPTION = "description";
    private String LINK = "link";

    private boolean IN_ITEM = false;

    private PostData currentPost;
    private ArrayList<PostData> results;

    // TODO: Send PostData bean to ContentProvider
    public static ArrayList<PostData> parseStream(URL url){
        RSSHandler handler = new RSSHandler();
        try {
            URLConnection connection = url.openConnection();
            InputStream input = connection.getInputStream();
            Xml.parse(input, Xml.Encoding.UTF_8, handler);
        } catch (Exception e){
            Log.e(TAG, "Exception caught in parseStream method");
            e.printStackTrace();
        }
        return handler.getResult();
    }

    private ArrayList<PostData> getResult() { return results; }

    private StringBuilder builder = new StringBuilder();

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(IN_ITEM) switch (CURRENT_ELEMENT) {
            case IN_TITLE:
                builder.append(ch, start, length);
                break;
            case IN_DESCRIPTION:
                builder.append(ch, start, length);
                break;
            case IN_LINK:
                builder.append(ch, start, length);
                break;
            default:
                break;
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        results = new ArrayList<PostData>();
        currentPost = new PostData();
        Log.i(TAG, "Document started");
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        Log.i(TAG, "Document ended");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equals(ITEM)){
            IN_ITEM = true;
        } else if (localName.equals(TITLE)){
            CURRENT_ELEMENT = IN_TITLE;
        } else if (localName.equals(DESCRIPTION)){
            CURRENT_ELEMENT = IN_DESCRIPTION;
        } else if (localName.equals(LINK)){
            CURRENT_ELEMENT = IN_LINK;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.equals(ITEM)){
            IN_ITEM = false;
            results.add(currentPost);
            currentPost = new PostData();
        } else if (localName.equals(TITLE)){
            CURRENT_ELEMENT = 0;
            currentPost.setTitle(builder.toString());
            builder.setLength(0);
        } else if (localName.equals(DESCRIPTION)){
            CURRENT_ELEMENT = 0;
            currentPost.setDescription(builder.toString());
            builder.setLength(0);
        } else if (localName.equals(LINK)){
            CURRENT_ELEMENT = 0;
            URL url = null;

            try{
                url = new URL(builder.toString());
            } catch (IOException ioe){
                Log.i(TAG, "Error when parsing link, trace inc.");
                ioe.printStackTrace();
            }

            if(url != null){
                currentPost.setLink(url);
            }

            builder.setLength(0);
        }
    }
}
