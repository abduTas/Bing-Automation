import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.client.HttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TranslatorAPI
{

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException 
	
	{
		
		//WebDriver driver = new FirefoxDriver();
		
		//getting the JSON response
		String clientId="abdulla";
		String clientSecret="ULmCt0a5kjI0kmaedKpZ8r07hYTbFqpRbjV74qFTWTQ=";
		clientId= URLEncoder.encode(clientId,"UTF-8");
	    clientSecret= URLEncoder.encode(clientSecret,"UTF-8");
		
		URL url = new URL("https://datamarket.accesscontrol.windows.net/v2/OAuth2-13");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
	
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
		String string= "grant_type=client_credentials&client_id="+clientId+"&client_secret="+clientSecret+"&scope=http://api.microsofttranslator.com";
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(string);
		wr.flush();
		wr.close();
		
		//extracting the token from JSON Response
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String tok = response.toString();
		
		JSONParser parsor= new JSONParser();
		JSONObject object=(JSONObject) parsor.parse(tok);
	    String accessToken= (String)object.get("access_token");
		
	    System.out.println(accessToken);
	    
	    //Sending The GET request for translating the text
	    
	    BufferedReader br = new BufferedReader(new FileReader("/home/abdullatasleem/Documents/bing.csv"));
		String line=null;
		String [] country;
		
		while ((line = br.readLine()) != null) 
		{
		country = line.split(",");
		URL url2= new URL("http://api.microsofttranslator.com/V2/Http.svc/Translate?text="+URLEncoder.encode(country[2],"UTF-8")+"&from="+country[0]+"&to="+country[1]);
		HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
		conn2.setRequestProperty("Authorization", "Bearer "+accessToken);
		conn2.setRequestMethod("GET");
	    conn2.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		int responseCode1 = conn2.getResponseCode();
		BufferedReader in1 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
		String inputLine1;
		StringBuffer response1 = new StringBuffer();
		System.out.println(line);
		while ((inputLine1 = in1.readLine()) != null) 
		{
			 response1.append(inputLine1);
		     String sss=response1.toString();
			 sss = sss.substring(sss.indexOf(">") + 1);
			 sss = sss.substring(0, sss.indexOf("<"));		
			 System.out.println(sss);
		}
		in1.close();
		}
		
		

	
	}
}
