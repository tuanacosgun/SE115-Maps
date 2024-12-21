import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    //Method to read the file and check for format errors
    public static void readFile(String inputFileName, String outputFileName) {
        Scanner reader = null;
        int lineNumber = 1;
        boolean isValid = true;

        String[] validCities = null;  //Array to store valid city names
        CountryMap[] paths = null;  //Array to store the routes
        City[] cities = null;  //Array to store city objects

        try {
            reader = new Scanner(Paths.get(inputFileName));  //Open the input file for reading

            //Read the number of cities (First line)
            if (reader.hasNextLine()) {
                String citiesCount = reader.nextLine().trim();  //Get the number of cities
                if (!citiesCount.matches("\\d+")) {  //Check if it's a valid number
                    System.out.println("Error Line: " + lineNumber + " Invalid number of cities.");
                    isValid = false;  //Mark as invalid if the number is not a valid integer
                } else {
                    validCities = new String[Integer.parseInt(citiesCount)];  //Initialize city names array
                    cities = new City[validCities.length];  //Initialize city objects array
                }
            }
            lineNumber++;  //Move to the next line

            //Read city labels (Second line)
            if (reader.hasNextLine()) {
                String citiesLine = reader.nextLine().trim();  //Get the line with city labels
                String[] cityLabels = citiesLine.split(" ");  //Split the labels by spaces
                if (validCities != null && cityLabels.length != validCities.length) {  //Check if the count matches
                    System.out.println("Error Line: " + lineNumber + " The number of city labels does not match the city count.");
                    isValid = false;
                } else {
                    //Validate each city label and initialize City objects
                    for (int i = 0; i < cityLabels.length; i++) {
                        if (cityLabels[i].length() != 1) {  //Check if city label is a single character
                            System.out.println("Error Line: " + lineNumber + " Invalid city label: " + cityLabels[i]);
                            isValid = false;
                        }
                        validCities[i] = cityLabels[i];  //Store city name
                        cities[i] = new City(validCities[i], i);  //Create a City object
                    }
                }
            }
            lineNumber++;  //Move to the next line

            //Read the number of routes (Third line)
            int expectedRouteCount = 0;  //Variable to store the number of routes
            if (reader.hasNextLine()) {
                String routesCountLine = reader.nextLine().trim();  //Get the number of routes
                if (!routesCountLine.matches("\\d+")) {  //Check if it's a valid number
                    System.out.println("Error Line: " + lineNumber + " Invalid number of routes.");
                    isValid = false;  //Mark as invalid if the number is not a valid integer
                } else {
                    expectedRouteCount = Integer.parseInt(routesCountLine);  //Store the number of routes
                }
            }
            lineNumber++;

            //Read the routes (From line 4 onwards)
            paths = new CountryMap[expectedRouteCount];  //Initialize the array of paths (routes)
            for (int i = 0; i < expectedRouteCount; i++) {
                if (reader.hasNextLine()) {
                    String routeLine = reader.nextLine().trim();  //Get the route line
                    String[] routeParts = routeLine.split(" ");  //Split the route by spaces
                    if (routeParts.length != 3) {  //Validate the format of the route
                        System.out.println("Error Line: " + lineNumber + " Incorrect route format. Expected '<City1> <City2> <Time>'");
                        isValid = false;
                    } else {
                        City source = getCity(routeParts[0], cities);  //Get the source city object
                        City destination = getCity(routeParts[1], cities);  //Get the destination city object
                        int time;
                        try {
                            time = Integer.parseInt(routeParts[2]);  //Parse the travel time
                        } catch (NumberFormatException e) {
                            System.out.println("Error Line: " + lineNumber + " Invalid time value.");
                            isValid = false;  //Mark as invalid if the time is not a valid integer
                            continue;
                        }
                        paths[i] = new CountryMap(source, destination, time);  //Create a CountryMap object for the route
                    }
                }
                lineNumber++;
            }

            //Read starting and ending city
            City start = null, end = null;
            if (reader.hasNextLine()) {
                String startEndCities = reader.nextLine().trim();  //Get the start and end cities
                String[] startEnd = startEndCities.split(" ");  //Split the cities
                if (startEnd.length != 2) {  // Validate the format
                    System.out.println("Error Line: " + lineNumber + " Incorrect start/end city format.");
                    isValid = false;
                } else {
                    start = getCity(startEnd[0], cities);  //Get the start city object
                    end = getCity(startEnd[1], cities);  //Get the end city object
                }
            }

            //If the file format is valid proceed to find the shortest path
            if (isValid) {
                System.out.println("File read is successful!");  //Confirm that the file was read successfully
                WayFinder wayFinder = new WayFinder(paths);  //Create a WayFinder object with the paths
                String result = wayFinder.findShortestPath(start, end, cities);  //Find the shortest path
                System.out.println(result);

                // Write output to a file
                try (FileWriter writer = new FileWriter(outputFileName)) {
                    writer.write(result);  //Write the result to the output file
                    System.out.println("Results successfully written to: " + outputFileName);  //Confirm that the file has written
                } catch (IOException e) {
                    System.out.println("Error: Could not write to the output file. Please check permissions or disk space.");
                }
            }



        } catch (IOException e) {
            System.out.println("Error: Could not read the input file. Please ensure the file exists and is accessible.");
        }catch (Exception e) {
            System.out.println("Error: An unexpected error occurred while processing the file.");
        } finally {
            if (reader != null) {
                reader.close();  //Close the reader
            }
        }
    }

    //Helper method to get City object based on city name
    public static City getCity(String cityName, City[] cities) {
        for (City city : cities) {
            if (city.getName().equals(cityName)) {
                return city;  //Return the city object if found
            }
        }
        return null;  //Return null if city is not found
    }

    //Main method
    public static void main(String[] args) {
        //Checking for input and output file names from command line arguments
        if (args.length < 2) {
            System.out.println("Error: Please provide input and output file names as command line arguments.");
            return;  //Exit if args are missing
        } else {
            readFile(args[0], args[1]);  //Call the readFile method with the provided args
        }
    }
}
