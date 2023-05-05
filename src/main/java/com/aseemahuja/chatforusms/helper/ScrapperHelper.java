package com.aseemahuja.chatforusms.helper;

import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Controller
public class ScrapperHelper {
	
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36";
	
	public static List<Document> getPages(String url) throws IOException {
        List<Document> pages = new ArrayList<>();
        
        URL newUrl = new URL(url);

        // Get the first page
        Document document = Jsoup.parse(newUrl, 6000);

        // Get the first page
       
        pages.add(document);

        // Get all the links on the first page
        List<Element> links = document.select("a[href]");
        
        int linkCount = 0;

        // Iterate over the links and get the pages they point to
        for (Element link : links) {
            String linkUrl = link.attr("href");
            if (!linkUrl.startsWith("javascript:")
            		&& !linkUrl.startsWith("#")
            		&& !linkUrl.contains("facebook.com")
            		&& !linkUrl.contains("twitter.com")
            		&& !linkUrl.contains("linkedin.com")
            		&& !linkUrl.contains("instagram.com")
            		&& !linkUrl.startsWith("mailto:")) {
            	if(linkCount>60) {
            		break;
            	}
                pages.add(getPage(linkUrl));
                linkCount++;
            }
        }
        
        String socialMediaBody = "";
        
        for (Element link : links) {
            String linkUrl = link.attr("href");
            if (linkUrl.contains("facebook.com")
            		) {
            	socialMediaBody += " Facebook profile is " + linkUrl;
            } else  if (linkUrl.contains("twitter.com")
            		) {
            	socialMediaBody += " Twitter link is " + linkUrl;
            } else if (linkUrl.contains("linkedin.com")
            		) {
            	socialMediaBody += " LinkedIn link is " + linkUrl;
            } else if (linkUrl.contains("instagram.com")
            		) {
            	socialMediaBody += " Instagram link is " + linkUrl;
            } else if (linkUrl.startsWith("mailto:")
            		) {
            	socialMediaBody += " Email address is " + linkUrl;
            }
        }
        
        Document socialDoc = Jsoup.parse("<html><body>" + socialMediaBody + "</body></html>");
        
        
        pages.removeIf(Objects::isNull);
        pages.add(socialDoc);

        return pages;
    }

    private static Document getPage(String url) throws IOException {
    	
    	

        // Get the first page
        try {
        	URL newUrl = new URL(url);
			Document document = Jsoup.parse(newUrl, 6000);
			return Jsoup.parse(newUrl, 3000);
		} catch (Exception e) {
			return null;
		}
    }
    
    public static String getTitle(Document doc) {
    	
    	Elements metaTags = doc.getElementsByTag("meta");
		
		String result = "";
		  for(Element metaTag : metaTags){
		    //find URL inside meta-tag with property of "og:image"
		    if (metaTag.attr("property").equals("og:site_name")){
		      result = metaTag.attr("content");
		     
		      break;
		    }
		  }
		  
		  //Means website changed, things need to be fixed.
		  if (result.isEmpty()){
		    result = doc.title();
		  }
		  
		  if(!result.isEmpty() && result.contains("|")) {
			  result = result.split("\\|")[1];
		  }
		  
		  if(!result.isEmpty() && result.contains("»")) {
			  result = result.split("\\»")[0];
		  }
		  
		  return result.trim();
    	
    }
    
 public static String getIconUrl(Document doc) {
    	
    	Elements metaTags = doc.getElementsByTag("meta");
		
    	String baseUrl = "";
		
		try {
			String pageLink =doc.baseUri();
			URL pageUrl = new URL(pageLink);
			baseUrl = pageUrl.getProtocol()+"://" + pageUrl.getHost();
		} catch (Exception e) {
			baseUrl = "";
		}
		
		List<Element> links = doc.select("link");
		String iconUrl = "";
		
		for (Element link : links) {
            String linkUrl = link.attr("href");
            String rel = link.attr("rel");
            if (rel.equalsIgnoreCase("apple-touch-icon") || rel.equalsIgnoreCase("icon")) {
            	iconUrl = (linkUrl.startsWith("/"))?baseUrl+linkUrl : linkUrl;
            	break;
            }
        }
		
		if(iconUrl.isEmpty()) {
			  for(Element metaTag : metaTags){
			    //find URL inside meta-tag with property of "og:image"
			    if (metaTag.attr("property").equals("og:image")){
			    	iconUrl = metaTag.attr("content");
			     
			      break;
			    }
			  }
			
		}
		  
		  return iconUrl;
    	
    }
    
    


}
