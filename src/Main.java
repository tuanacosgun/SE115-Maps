import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    // Method to read the file and check for format errors
    public static void readFile(String fileName) {
        Scanner reader = null;
        int lineNumber = 1;
        boolean isValid = true;

        // Array to store valid city labels
        String[] validCities = null;

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

            // Read routes (Next lines)
            for (int i = 0; i < expectedRouteCount; i++) {
                if (reader.hasNextLine()) {
                    String routeLine = reader.nextLine().trim();
                    String[] routeParts = routeLine.split(" ");
                    if (routeParts.length != 3) {
                        System.out.println("Error Line: " + lineNumber + " Incorrect route format. Expected '<City1> <City2> <Time>'.");
                        isValid = false;
                    } else {
                        if (!isCityValid(routeParts[0], validCities) || !isCityValid(routeParts[1], validCities)) {
                            System.out.println("Error Line: " + lineNumber + " Invalid city in route: " + routeLine);
                            isValid = false;
                        }
                        try {
                            int time = Integer.parseInt(routeParts[2]);
                            if (time < 0) {
                                System.out.println("Error Line: " + lineNumber + " Invalid time value. Time cannot be negative.");
                                isValid = false;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error Line: " + lineNumber + " Invalid time value. Expected a number.");
                            isValid = false;
                        }
                    }
                } else {
                    System.out.println("Error Line: " + lineNumber + " Missing route definition.");
                    isValid = false;
                }
                lineNumber++;
            }

            // Read starting and ending city (Last line)
            if (reader.hasNextLine()) {
                String startEndCities = reader.nextLine().trim();
                String[] startEnd = startEndCities.split(" ");
                if (startEnd.length != 2) {
                    System.out.println("Error Line: " + lineNumber + " Incorrect start/end city format.");
                    isValid = false;
                } else {
                    if (!isCityValid(startEnd[0], validCities) || !isCityValid(startEnd[1], validCities)) {
                        System.out.println("Error Line: " + lineNumber + " Invalid start or end city.");
                        isValid = false;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        // If no errors found, print success message
        if (isValid) {
            System.out.println("File read is successful!");
        }
    }

    // Method to check if a city is valid
    public static boolean isCityValid(String city, String[] validCities) {
        if (validCities == null) return false;
        for (String validCity : validCities) {
            if (validCity.equals(city)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: No file name provided.");
            return;
        }
        readFile(args[0]);
    }
}


