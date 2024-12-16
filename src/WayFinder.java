public class WayFinder {
    private CountryMap[] paths;

    public WayFinder(CountryMap[] paths) {
        this.paths = paths;
    }

    public CountryMap[] getPaths() {
        return paths;
    }

    public void setPaths(CountryMap[] paths) {
        this.paths = paths;
    }

    // Method to find the shortest path between two cities
    public int findShortestPath(City source, City destination, City[] cities) {
        int numCities = cities.length;
        int[][] graph = new int[numCities][numCities];

        // Initialize graph with maximum distances
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                graph[i][j] = (i == j) ? 0 : Integer.MAX_VALUE; // 0 for same city, MAX_VALUE for others
            }
        }

        // Fill the graph with path times from CountryMap
        for (CountryMap path : paths) {
            int sourceIndex = path.getSource().getOrder();
            int destIndex = path.getDestination().getOrder();
            graph[sourceIndex][destIndex] = path.getTime();
        }

        // Dijkstra's Algorithm
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
                }
            }
        }

        // Return the shortest distance to the destination
        return distances[destination.getOrder()] == Integer.MAX_VALUE ? -1 : distances[destination.getOrder()];
    }
}

