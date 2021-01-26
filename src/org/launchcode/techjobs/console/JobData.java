package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    public static ArrayList<HashMap<String, String>> findAll () {

        // load data, if not already loaded
        loadData();
        ArrayList<HashMap<String, String>> newList = new ArrayList<>(allJobs);
        return newList;
    }

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll ( String field ) {
        // load data, if not already loaded
        loadData();
        ArrayList<String> values = new ArrayList<>();
        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);
            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }
        return values;
    }

    //  new method findByValue
    public static ArrayList<HashMap<String, String>> findByValue ( String value ) {
        // load data, if not already loaded
        loadData();
        ArrayList<HashMap<String, String>> jobsSearch = new ArrayList<>();
        for (HashMap<String, String> job : allJobs) {
                for (Map.Entry<String, String> entry : job.entrySet()) {
                    String hello = entry.getValue().toLowerCase();
                    if (hello.contains(value)) {
                        jobsSearch.add(job);
                }
            }
        }
        return jobsSearch;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     * <p>
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column Column that should be searched.
     * @param value  Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue ( String column, String value ) {
        // load data, if not already loaded
        loadData();
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(column).toLowerCase();
            if (aValue.contains(value)) {
                jobs.add(row);
            }
        }
        return jobs;
    }

    static Map<String, String> sortByValue ( HashMap<String, String> job ) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, String>> list =
                new LinkedList<Map.Entry<String, String >>(job.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String > o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, String > sortedMap = new LinkedHashMap<String, String >();
        for (Map.Entry<String, String > entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/


        return sortedMap;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData () {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
