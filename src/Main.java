import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    // Method to read the file and check for format errors
    public static void readFile(String fileName) {
        Scanner reader = null; // Scanner object to read the file
        int lineNumber = 1; // Variable to track the line number
        boolean isValid = true; // Flag to determine if the file is valid

        // Array to store valid city labels
        String[] validCities = null;

        try {
            reader = new Scanner(Paths.get(fileName));

            // Read the number of cities (First line)
            if (reader.hasNextLine()) {
                String citiesCount = reader.nextLine().trim();
                if (!citiesCount.matches("\\d+")) { // Check if the city count is a valid number
                    System.out.println("Error Line: " + lineNumber + " Invalid number of cities.");
                    isValid = false; // Mark the file as invalid if the count is incorrect
                } else {
                    validCities = new String[Integer.parseInt(citiesCount)]; // Create an array to store city labels
                }
            }
            lineNumber++; // Move to the next line

            // Read city labels (Second line)
            if (reader.hasNextLine()) {
                String citiesLine = reader.nextLine().trim();
                String[] cityLabels = citiesLine.split(" "); // Split the city labels by spaces
                if (validCities != null && cityLabels.length != validCities.length) {
                    System.out.println("Error Line: " + lineNumber + " The number of city labels does not match the city count.");
                    isValid = false;
                } else {
                    for (int i = 0; i < cityLabels.length; i++) {
                        if (cityLabels[i].length() != 1) { // Check if each label is a single character
                            System.out.println("Error Line: " + lineNumber + " Invalid city label: " + cityLabels[i]);
                            isValid = false;
                        }
                        validCities[i] = cityLabels[i]; // Add the label to the array
                    }
                }
            }
            lineNumber++;

            // Read the number of routes (Third line)
            int expectedRouteCount = 0;
            if (reader.hasNextLine()) {
                String routesCountLine = reader.nextLine().trim();
                if (!routesCountLine.matches("\\d+")) { // Check if the route count is a valid number
                    System.out.println("Error Line: " + lineNumber + " Invalid number of routes.");
                    isValid = false;
                } else {
                    expectedRouteCount = Integer.parseInt(routesCountLine); // Store the expected route count
                }
            }
            lineNumber++;

            // Read the routes (From the fourth line onward)
            for (int i = 0; i < expectedRouteCount; i++) {
                if (reader.hasNextLine()) {
                    String routeLine = reader.nextLine().trim();
                    String[] routeParts = routeLine.split(" "); // Split the route by spaces
                    if (routeParts.length != 3) {
                        System.out.println("Error Line: " + lineNumber + " Incorrect route format. Expected '<City1> <City2> <Time>'.");
                        isValid = false;
                    } else {
                        if (!isCityValid(routeParts[0], validCities) || !isCityValid(routeParts[1], validCities)) {
                            System.out.println("Error Line: " + lineNumber + " Invalid city in route: " + routeLine);
                            isValid = false;
                        }
                        try {
                            int time = Integer.parseInt(routeParts[2]); // Check if the time is a valid number
                            if (time < 0) { // Check if the time is negative
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
            e.printStackTrace(); // Print file reading errors
        } finally {
            if (reader != null) {
                reader.close(); // Close the file reading process
            }
        }

        // If no errors found, print success message
        if (isValid) {
            System.out.println("File read is successful!");
        }
    }

    // Method to check if a city is valid
    public static boolean isCityValid(String city, String[] validCities) {
        if (validCities == null) return false; // Return false if validCities array is null
        for (String validCity : validCities) {
            if (validCity.equals(city)) {
                return true; // Return true if the city is valid
            }
        }
        return false; // Return false if the city is not valid
    }

    // Main method
    public static void main(String[] args) {
        if (args.length == 0) { // If no file name is provided from the command line
            System.out.println("Error: No file name provided.");
            return;
        } else {
            readFile(args[0]); // Pass the file name to the `readFile` method
        }
    }
}


