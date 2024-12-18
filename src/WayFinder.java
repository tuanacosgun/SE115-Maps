public class WayFinder {

    private CountryMap[] paths;

    public WayFinder(CountryMap[] paths) {
        this.paths = paths;
    }

    // Method to find the shortest path and print the route with total time
    public String findShortestPath(City source, City destination, City[] cities) {
        int numCities = cities.length;
        int[][] graph = new int[numCities][numCities];
        String[] previousCity = new String[numCities];  // Store previous city to trace the path

        // Initialize graph with maximum distances
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                graph[i][j] = (i == j) ? 0 : Integer.MAX_VALUE; // 0 for same city, MAX_VALUE for others
            }
            previousCity[i] = null;  // Initialize previousCity
        }

        // Fill the graph with path times from CountryMap
        for (CountryMap path : paths) {
            int sourceIndex = path.getSource().getOrder();
            int destIndex = path.getDestination().getOrder();
            graph[sourceIndex][destIndex] = path.getTime();
        }

        // Dijkstra's algorithm
        boolean[] visited = new boolean[numCities];
        int[] distances = new int[numCities];

        // Initialize distances
        for (int i = 0; i < numCities; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        distances[source.getOrder()] = 0;

        for (int i = 0; i < numCities; i++) {
            // Find the nearest unvisited city
            int minDist = Integer.MAX_VALUE;
            int nearestCity = -1;
            for (int j = 0; j < numCities; j++) {
                if (!visited[j] && distances[j] < minDist) {
                    minDist = distances[j];
                    nearestCity = j;
                }
            }

            // Mark the nearest city as visited
            visited[nearestCity] = true;

            // Update distances of adjacent cities
            for (int j = 0; j < numCities; j++) {
                if (!visited[j] && graph[nearestCity][j] != Integer.MAX_VALUE &&
                        distances[nearestCity] + graph[nearestCity][j] < distances[j]) {
                    distances[j] = distances[nearestCity] + graph[nearestCity][j];
                    previousCity[j] = cities[nearestCity].getName();  // Record the previous city to trace the path
                }
            }
        }

        // Construct the path from source to destination
        StringBuilder path = new StringBuilder();
        int totalTime = distances[destination.getOrder()];
        if (totalTime == Integer.MAX_VALUE) {
            return "No path found.";
        }

        // Reconstruct path
        String currentCity = destination.getName();
        path.insert(0, currentCity);

        // Trace back to the source and build the path string
        while (previousCity[destination.getOrder()] != null) {
            currentCity = previousCity[destination.getOrder()];
            path.insert(0, currentCity + " -> ");
            destination = getCity(currentCity, cities);
        }

        return "Fastest Way: " + path + "\nTotal Time: " + totalTime + " min";
    }

    // Helper method to get City object based on city name
    private City getCity(String cityName, City[] cities) {
        for (City city : cities) {
            if (city.getName().equals(cityName)) {
                return city;
            }
        }
        return null; // Return null if city not found
    }
}



