import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.Scanner;

public class Main {

    // Method to read the file and check for format errors
    public static void readFile(String fileName) {
        Scanner reader = null;
        int lineNumber = 1;
        boolean isValid = true;

        String[] validCities = null;
        CountryMap[] paths = null;
        City[] cities = null;

        try {
            reader = new Scanner(Paths.get(fileName));

            // Read the number of cities (First line)
            if (reader.hasNextLine()) {
                String citiesCount = reader.nextLine().trim();
                if (!citiesCount.matches("\\d+")) {
                    System.out.println("Error Line: " + lineNumber + " Invalid number of cities.");
                    isValid = false;
                } else {
                    validCities = new String[Integer.parseInt(citiesCount)];
                    cities = new City[validCities.length]; // Create a City array based on the number of cities
                }
            }
            lineNumber++;

            // Read city labels (Second line)
            if (reader.hasNextLine()) {
                String citiesLine = reader.nextLine().trim();
                String[] cityLabels = citiesLine.split(" ");
                if (validCities != null && cityLabels.length != validCities.length) {
                    System.out.println("Error Line: " + lineNumber + " The number of city labels does not match the city count.");
                    isValid = false;
                } else {
                    for (int i = 0; i < cityLabels.length; i++) {
                        if (cityLabels[i].length() != 1) {
                            System.out.println("Error Line: " + lineNumber + " Invalid city label: " + cityLabels[i]);
                            isValid = false;
                        }
                        validCities[i] = cityLabels[i];
                        cities[i] = new City(validCities[i], i); // Create a City object for each city
                    }
                }
            }
            lineNumber++;

            // Read the number of routes (Third line)
            int expectedRouteCount = 0;
            if (reader.hasNextLine()) {
                String routesCountLine = reader.nextLine().trim();
                if (!routesCountLine.matches("\\d+")) {
                    System.out.println("Error Line: " + lineNumber + " Invalid number of routes.");
                    isValid = false;
                } else {
                    expectedRouteCount = Integer.parseInt(routesCountLine);
                }
            }
            lineNumber++;

            // Read the routes (From the fourth line onward)
            paths = new CountryMap[expectedRouteCount];
            for (int i = 0; i < expectedRouteCount; i++) {
                if (reader.hasNextLine()) {
                    String routeLine = reader.nextLine().trim();
                    String[] routeParts = routeLine.split(" ");
                    if (routeParts.length != 3) {
                        System.out.println("Error Line: " + lineNumber + " Incorrect route format. Expected '<City1> <City2> <Time>'.");
                        isValid = false;
                    } else {
                        City source = getCity(routeParts[0], cities);
                        City destination = getCity(routeParts[1], cities);
                        int time = Integer.parseInt(routeParts[2]);
                        paths[i] = new CountryMap(source, destination, time);
                    }
                }
                lineNumber++;
            }

            // Read starting and ending city (Last line)
            City start = null, end = null;
            if (reader.hasNextLine()) {
                String startEndCities = reader.nextLine().trim();
                String[] startEnd = startEndCities.split(" ");
                if (startEnd.length != 2) {
                    System.out.println("Error Line: " + lineNumber + " Incorrect start/end city format.");
                    isValid = false;
                } else {
                    start = getCity(startEnd[0], cities);
                    end = getCity(startEnd[1], cities);
                }
            }

            if (isValid) {
                System.out.println("File read is successful!"); // Print success message
                WayFinder wayFinder = new WayFinder(paths);
                String result = wayFinder.findShortestPath(start, end, cities);

                System.out.println(result);  // Print the fastest way and total time

                //Write output to a file
                try (FileWriter writer = new FileWriter("output.txt")) {
                    writer.write(result); //Write formatted to the file
                }catch (IOException e){
                    System.out.println("File write is unsuccessfull!");
                }
            }

        } catch (Exception e) {
           // e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    // Helper method to get City object based on city name
    public static City getCity(String cityName, City[] cities) {
        for (City city : cities) {
            if (city.getName().equals(cityName)) {
                return city;
            }
        }
        return null; // Return null if city not found
    }

    // Main method
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: No file name provided. Enter the text files name from the command line!");
            return;
        } else {
            readFile(args[0]);
            FileWriter writer = null; //Create filewriter

        }
    }
}
