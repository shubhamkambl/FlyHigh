package com.acc.group4.FlyHigh.Bo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.acc.group4.FlyHigh.UIRepresentation.FlightDetailsTable;
import com.acc.group4.FlyHigh.Utils.Constants;
import com.acc.group4.FlyHigh.crawler.WebCrwaler;
import com.acc.group4.FlyHigh.pojo.FlightDetails;

public class FlyHighBO {
	private static FlyHighBO flyHighInstance = null;
	private WebCrwaler webCrawler = WebCrwaler.getInstance();
	private static Logger logger ;  
	public static FlyHighBO getInstance() {
		flyHighInstance = flyHighInstance == null ? new FlyHighBO() : flyHighInstance;
		return flyHighInstance;
	}
  
	private Scanner sc = new Scanner(System.in);

	public void getFlightDetails() {
		System.out.println(">>>Entering getFlightDetails method<<<");
		String choice;
		do {
			getFlightDetailsOperation();
			System.out.println("Want to continue search for other date or city?(Y/N): ");
			choice = sc.next();
		} while (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("yes"));
		System.out.println(">>>Exiting getFlightDetails method<<<");
	}

	private void getFlightDetailsOperation() {
		System.out.println(">>>Entering getFlightDetailsOperation method<<<");
		System.out.println("Enter the source: ");
		String source = sc.next();
		System.out.println("Enter the Destination: ");
		String destination = sc.next();
		System.out.println("Enter the Date(yyyy-MM-dd): ");
		String date = sc.next();
		System.out.println("Enter the Number of travellers: ");
		String numberOfPeople = sc.next();
		ArrayList<FlightDetails> flightDetails = new ArrayList<FlightDetails>();
		System.out.println("How many url's to be crawled :: ");
		int numberOfURLSToCrawl = sc.nextInt();
		String Url = numberOfPeople.equalsIgnoreCase("1")?Constants.KAYAK_INITIAL_URL_BEFORE_PARAM+source+"-"+destination+"/" + date :
			Constants.KAYAK_INITIAL_URL_BEFORE_PARAM+source+"-"+destination+"/" + date+"/"+numberOfPeople+"adults";
		System.out.println("Select the travelling class(Business : B/b, Economy: E/e, First Class: F/f, Premium Economy: P/p): ");
		String travelClass = sc.next();
		String travellClassAppender = travelClass.equalsIgnoreCase("B")?"business":travelClass.equalsIgnoreCase("P")?"premium":travelClass.equalsIgnoreCase("F")?
			"first":null;
		Url = Url+"/"+travellClassAppender;
		HashSet<String> urlForFlightPages = webCrawler.crawlFlightSites("https://www.kayak.com", numberOfURLSToCrawl);
		try {
			//String Url = Constants.KAYAK_INITIAL_URL_BEFORE_PARAM + date + Constants.KAYAK_INITIAL_URL_AFTER_PARAM;
			Document doc = Jsoup.parse(webCrawler.WebScrapper(Url));
			Elements flightDethtml = doc.getElementsByClass(Constants.KAYAK_FLIGHT_DETAIL_ROOT);
			//System.out.println("Test1 :: "+flightDethtml);
			for (Element element : flightDethtml) {
				FlightDetails flightInfo = new FlightDetails();
				flightInfo.setFlightPrice(element.getElementsByClass(Constants.KAYAK_FLIGHT_PRICE_HTML_CLASS).text());
				flightInfo.setTimeToReach(
						element.getElementsByClass(Constants.KAYAK_FLIGHT_TIME_HTML_CLASS).textNodes().get(0).text());
				flightInfo.setFlightName(
						element.getElementsByClass(Constants.KAYAK_FLIGHT_OPERATOR_HTML_CLASS).textNodes().get(0).text());
				flightInfo.setStops(element.getElementsByClass(Constants.KAYAK_FLIGHT_STOPS_HTML_CLASS).text());
				flightDetails.add(flightInfo);
			}
			System.out.println("Flights :: " + flightDetails);
			Consumer<FlightDetails> method = (fD)->{System.out.println("Flight price"+fD.getFlightPrice());};
			flightDetails.forEach(method);
			FlightDetailsTable.showFlights(flightDetails);
			System.out.println("Cheapest Flight on KAYAK : Price "+
			doc.getElementsByClass("Hv20-value").text());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(">>>Exiting getFlightDetailsOperation method<<<");
	}
	

}
