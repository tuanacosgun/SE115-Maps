import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    private static boolean contains(String[] a, String city) {
        for (String s : a) {
            if (s.equals(city)) {
                return true;
            }
        }
        return false;
    }

    public static void readFile(String inputFileName, String outputFileName) {
        Scanner reader = null;
        int lineNumber = 1;
        boolean isValid = true;

        String[] validCities = null;
        CountryMap[] paths = null;
        City[] cities = null;

        try {
            reader = new Scanner(Paths.get(inputFileName));

            // Read the number of cities
            if (reader.hasNextLine()) {
                String citiesCount = reader.nextLine().trim();
                if (!citiesCount.matches("\\d+")) {
                    System.out.println("Error Line: " + lineNumber + " Invalid number of cities.");
                    isValid = false;
                } else {
                    validCities = new String[Integer.parseInt(citiesCount)];
                    cities = new City[validCities.length];
                }
            }
            lineNumber++;

            // Read city labels
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
                        cities[i] = new City(validCities[i], i);
                    }
                }
            }
            lineNumber++;

            // Read the number of routes
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

            // Read the routes
            paths = new CountryMap[expectedRouteCount];
            for (int i = 0; i < expectedRouteCount; i++) {
                if (reader.hasNextLine()) {
                    String routeLine = reader.nextLine().trim();
                    String[] routeParts = routeLine.split(" ");
                    if (routeParts.length != 3) {
                        System.out.println("Error Line: " + lineNumber + " Incorrect route format. Expected '<City1> <City2> <Time>'");
                        isValid = false;
                        lineNumber++;
                        continue;
                    }
                    City source = getCity(routeParts[0], cities);
                    City destination = getCity(routeParts[1], cities);
                    if (!contains(validCities, routeParts[0]) || !contains(validCities, routeParts[1])) {
                        System.out.println("Error Line: " + lineNumber + " Invalid city selected.");
                        isValid = false;
                        lineNumber++;
                        continue;
                    }
                    int time;
                    try {
                        time = Integer.parseInt(routeParts[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("Error Line: " + lineNumber + " Invalid time value.");
                        isValid = false;
                        lineNumber++;
                        continue;
                    }
                    paths[i] = new CountryMap(source, destination, time);
                }
                lineNumber++;
            }

            // Read starting and ending city
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

            // If the file format is valid, find the shortest path
            if (isValid) {
                WayFinder wayFinder = new WayFinder(paths);
                String result = wayFinder.findShortestPath(start, end, cities);
                System.out.println("File read is successful!");
                System.out.println(result);

                // Write output to file
                try (FileWriter writer = new FileWriter(outputFileName)) {
                    writer.write(result);
                    System.out.println("Results successfully written to: " + outputFileName);
                } catch (IOException e) {
                    System.out.println("Error: Could not write to the output file. Please check permissions or disk space.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error: Could not read the input file. Please ensure the file exists and is accessible.");
        } catch (Exception e) {
            System.out.println("Error: An unexpected error occurred while processing the file.");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static City getCity(String cityName, City[] cities) {
        for (City city : cities) {
            if (city.getName().equals(cityName)) {
                return city;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: Please provide input and output file names as command line arguments.");
        } else {
            readFile(args[0], args[1]);
        }
    }
}

