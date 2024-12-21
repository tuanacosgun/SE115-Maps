public class WayFinder {

    private CountryMap[] paths;  //Array to store the paths between cities

    public WayFinder(CountryMap[] paths) {
        this.paths = paths;  //Set the paths based on the input
    }

    //Method to find the shortest path and print the route with total time
    public String findShortestPath(City source, City destination, City[] cities) {
        int numCities = cities.length;
        int[][] graph = new int[numCities][numCities];  //Graph to store the travel times between cities
        String[] previousCity = new String[numCities];  //Array to track the previous city on the shortest path

        //Initialize the graph with maximum distances (infinity) for all pairs of cities
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                graph[i][j] = (i == j) ? 0 : Integer.MAX_VALUE;  //Set distance 0 for the same city and max value for others
            }
            previousCity[i] = null;  //Initialize the previous city array to null
        }

        //Fill the graph with travel times from the CountryMap
        for (CountryMap path : paths) {
            int sourceIndex = path.getSource().getOrder();  //Get the index of the source city
            int destIndex = path.getDestination().getOrder();  //Get the index of the destination city
            graph[sourceIndex][destIndex] = path.getTime();  //Set the travel time between source and destination
        }

        //Initialize Dijkstra's algorithm variables
        boolean[] visited = new boolean[numCities];  //Array to track visited cities
        int[] distances = new int[numCities];  //Array to store the shortest distance from source to each city

        //Set initial distances to maximum value (infinity) for all cities except the source city
        for (int i = 0; i < numCities; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        if(source == null || destination == null){
            System.out.println("Error: Start/end city does not exist.");
        }
        distances[source.getOrder()] = 0;  //Set the distance to the source city as 0

        //Implement Dijkstra's algorithm
        for (int i = 0; i < numCities; i++) {
            //Find the nearest unvisited city with the smallest distance
            int minDist = Integer.MAX_VALUE;
            int nearestCity = -1;
            for (int j = 0; j < numCities; j++) {
                if (!visited[j] && distances[j] < minDist) {
                    minDist = distances[j];  //Update the minimum distance
                    nearestCity = j;  //Mark the city as the nearest city
                }
            }

            //Mark the nearest city as visited
            visited[nearestCity] = true;

            //Update the distances
            for (int j = 0; j < numCities; j++) {
                if (!visited[j] && graph[nearestCity][j] != Integer.MAX_VALUE &&
                        distances[nearestCity] + graph[nearestCity][j] < distances[j]) {
                    //If the distance through the nearest city is smaller update the distance
                    distances[j] = distances[nearestCity] + graph[nearestCity][j];
                    previousCity[j] = cities[nearestCity].getName();  //Store the previous city to trace the path
                }
            }
        }

        //Reconstruct the path from source to destination
        StringBuilder path = new StringBuilder();  //StringBuilder to build the path string
        int totalTime = distances[destination.getOrder()];  //Get the total time for the destination

        //If the destination city is unreachable return a message
        if (totalTime == Integer.MAX_VALUE) {
            return "No path found.";  //Return a message indicating no path exists
        }

        //Start with the destination city and trace back to the source city using the previousCity array
        String currentCity = destination.getName();
        path.insert(0, currentCity);  //Insert the destination city at the beginning of the path

        //Trace back to the source and build the path string
        while (previousCity[destination.getOrder()] != null) {
            currentCity = previousCity[destination.getOrder()];  //Get the previous city in the path
            path.insert(0, currentCity + " -> ");  //Insert the previous city into the path
            destination = getCity(currentCity, cities);  //Get the City object for the previous city
        }

        //Return the path along with the total time
        return "Fastest Way: " + path + "\nTotal Time: " + totalTime + " min";
    }

    //Helper method to get City object based on city name
    private City getCity(String cityName, City[] cities) {
        for (City city : cities) {
            if (city.getName().equals(cityName)) {
                return city;
            }
        }
        return null;
    }
}



