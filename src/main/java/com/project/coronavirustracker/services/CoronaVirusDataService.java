package com.project.coronavirustracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.project.coronavirustracker.models.LocationStats;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CoronaVirusDataService {

   private final String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

   private List<LocationStats> allStats = new ArrayList<>();

   /**
    * 
    * @PostConstruct: when spring application starts, run this function OR when you
    *                 construct this class, automatically run this function after
    *                 the class gets constructed.
    *
    * @Scheduled: we want to run this method on daily basis, so we use this
    *             annotation. The 'cron' param will take a regular expression.
    *             First '*' means seconds, next means minutes, next is for hours,
    *             etc. Here we have scheduled the function to run evey second. So
    *             now (cron = "* * * * * *") means run every second. Now we have
    *             made it to run every day instead of every second. By doing this
    *             it will run at 1st hour of each day. The expression for the same
    *             will be (cron = "* * 1 * * *")
    * 
    * @throws IOException
    * @throws InterruptedException
    */
   @PostConstruct
   @Scheduled(cron = "* * 1 * * *")
   public void fetchVirusData() throws IOException, InterruptedException {
      List<LocationStats> newStats = new ArrayList<>();
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      // System.out.println(response.body());

      // StringReader coverts the 'String' to 'Reader' type. Here 'response.body()'
      // returns a string of csv file.
      StringReader csvReaderBody = new StringReader(response.body());

      // we use 'CSVRecord' class here to say that the first line of the csv file is
      // header.
      Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReaderBody);

      // iterating through the csv file to save the records in the class and making a
      // collection of that class to pass it to the view.
      for (CSVRecord record : records) {
         LocationStats locationStat = new LocationStats();
         locationStat.setState(record.get("Province/State"));
         locationStat.setCountry(record.get("Country/Region"));
         int latestCases = Integer.parseInt(record.get(record.size() - 1));
         int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
         locationStat.setLatestTotalCases(latestCases);
         locationStat.setDiffFromPreviousDay(latestCases - prevDayCases);
         // System.out.println(locationStat);
         newStats.add(locationStat);
      }
      this.allStats = newStats;
   }

   public List<LocationStats> getAllStats() {
      return allStats;
   }
}
