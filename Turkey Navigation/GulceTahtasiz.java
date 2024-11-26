import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Turkey Navigation
 * @author Gülce Tahtasız, Student ID: 2022400234
 * @since Date: 04.04.2024
 */


public class GulceTahtasiz {

    public static void main(String[] args) {

        // READING FILES.

        City[] cityCoordinates = new City[81]; // An array with 81(total line of that file) elements to hold the values from the city_coordinates.txt file.
        City2[] cityConnections = new City2[117]; // An array with 117(total line of that file) elements to hold the values from the city_coordinates.txt file.


        // Code segment for assigning elements from the files to the arrays above.
        try {
            File cityCoordinatesFile = new File("city_coordinates.txt");
            Scanner ccrs = new Scanner(cityCoordinatesFile);
            for (int i = 0; i < cityCoordinates.length; i++) {
                String line = ccrs.nextLine(); // Read what's written in line and store it as a string.
                String[] lineSplits = line.split(", "); // Split the String line with separator ", ".
                cityCoordinates[i] = new City(lineSplits[0], Integer.parseInt(lineSplits[1]), Integer.parseInt(lineSplits[2]));  // cityCoordinates list's i indexed element to be defined as a city with their x and y coordinates like  "Ankara", 1, 2.
            }


            File cityConnectionsFile = new File("city_connections.txt");
            Scanner ccns = new Scanner(cityConnectionsFile);
            for (int i = 0; i < cityConnections.length; i++) {
                String line = ccns.nextLine(); // Read what's written in line and store it as a string.
                String[] lineSplits = line.split(","); // Split the String line with separator ",".
                cityConnections[i] = new City2(lineSplits[0], lineSplits[1]); // cityConnections list's i indexed element to be defined as a neighbor cities like "Aksaray" and "Nevsehir".
            }

        } catch (FileNotFoundException e) { // ensures that the process terminates when the file is not found.
            e.printStackTrace();
            return;
        }




        // TAKING THE INPUT AND PRINTING THE CONSOLE OUTPUT.

        Scanner scanner = new Scanner(System.in); // Taking user inputs.
        String startCityName; //The start and destination cities defined for user input in the following code.
        String destinationCityName;

        while (true) { // A loop that continues until the user enters a valid starting city name
            System.out.print("Enter starting city: ");
            startCityName = scanner.next();
            if (findCitywithName(startCityName, cityCoordinates) == null) { // If the starting city name cannot found in the cityCoordinates list, it ensures that input is taken again.
                System.out.println("City named '" + startCityName + "' not found. Please enter a valid city name.");
            }else{ // If the starting city name found in the cityCoordinates list, then the input is in the correct format and the loop is finished.
                break;
            }
        }

        while (true) { // A loop that continues until the user enters a valid destination city name
            System.out.print("Enter destination city: ");
            destinationCityName = scanner.next();
            if (findCitywithName(destinationCityName, cityCoordinates) == null) { // If the destination city name cannot found in the cityCoordinates list, it ensures that input is taken again.
                System.out.println("City named '" + destinationCityName + "' not found. Please enter a valid city name.");
            }else{
                break; // If the destination city name found in the cityCoordinates list, then the input is in the correct format and the loop is finished.
            }
        }


        /* This code segment calls the findShortestPath function and takes startCityName,
        destinationCityName, cityCoordinates, cityConnections as parameters, and implements it into a String list with the path it founds with Dijkstra algorithm.
        This function gives us the shortest path as return.
         */
        List<String> shortestPath = findShortestPath(startCityName, destinationCityName, cityCoordinates, cityConnections);

        /* This code segment calls the totalDistance function and takes shortestPath list we implement above and cityCoordinates array as parameters,
         which returns the total value of the distance of the shortest path, and implements it to totalDistance variable.
         */
        double totalDistance = calculateTotalDistance(shortestPath, cityCoordinates);


        /* If the shortestPath list is empty, which means there is no path from the starting city to the destination city,
        it will be print "No path could be found." and no graphical output will be produced.
        Else the shortestPath list is not empty, indicating a valid path from the starting city to the destination city,
        the path will be printed, and graphical output will be drawn.*/

        if (shortestPath.isEmpty()){
            System.out.println("No path could be found.");

        }else{
            System.out.printf("Total Distance: %.2f. Path: ", totalDistance); // Prints the distance value rounded to two decimal places after the decimal point.
            for (int i = 0; i < shortestPath.size(); i++) {// Prints all the city names in the shortestPath list with the "->" sign between them.
                System.out.print(shortestPath.get(i));
                if (i < shortestPath.size() - 1) { // Stops to add "->" sign when the values are over.
                    System.out.print(" -> ");
                }
            }
            System.out.println();





            // DRAWING THE MAP AND THE PATH.

            StdDraw.setCanvasSize(2377, 1055); // width and height values of map.png to fit the canvas.
            StdDraw.setXscale(0, 2377); // Width scale
            StdDraw.setYscale(0, 1055); // Height scale
            StdDraw.picture(1188.5, 527.5, "map.png"); // Adding the map picture to standard draw.


            // This code segment prints the city names and their points with gray onto the map.
            for (int i = 0; i < cityCoordinates.length; i++) {
                StdDraw.setPenColor(StdDraw.GRAY);
                StdDraw.filledCircle(cityCoordinates[i].getX(), cityCoordinates[i].getY(), 4); // Takes all the cities x and y coordinates one by one and draws the little point on their locations.
                StdDraw.setPenRadius(0.03);
                StdDraw.text(cityCoordinates[i].getX(), cityCoordinates[i].getY() + 12, cityCoordinates[i].getName()); // Writes the city names slightly above the coordinates of the city.
            }


            // This code segment draws gray connection lines between neighboring cities.
            for (int i = 0; i < cityConnections.length; i++) {
                City city1 = findCitywithName(cityConnections[i].getName1(), cityCoordinates); // If the first city in the cityConnections array in the cityCoordinates array,implement its name and coordinates into city1 variable.
                City city2 = findCitywithName(cityConnections[i].getName2(), cityCoordinates); // If the second city in the cityConnections array in the cityCoordinates array,  implement its name and coordinates into city2 variable.
                StdDraw.setPenRadius(0.005);
                StdDraw.setPenColor(StdDraw.GRAY);
                StdDraw.line(city1.getX(), city1.getY(), city2.getX(), city2.getY()); // Draws a line between neighboring cities.
            }


            // This code segment prints the city names and their points with light blue onto the map if the cities are on the shortest path.
            for (int i = 0; i < shortestPath.size(); i++) {
                String cityName = shortestPath.get(i); // Implements the cities name one by one into the cityName variable.
                City city = findCitywithName(cityName, cityCoordinates); // Takes the cityName city with its coordinates and implements to city variable which is type city.
                StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
                StdDraw.filledCircle(city.getX(), city.getY(), 4); // Takes all the cities x and y coordinates one by one and draws the little light blue point on their locations (onto the gray ones).
                StdDraw.setPenRadius(0.03);
                StdDraw.text(city.getX(), city.getY() + 12, cityName); // Writes the city names on the path with light blue slightly above the coordinates of the city (onto the gray ones drawn before).
            }


            // This code segment draws light blue connection lines between the cities on path, which shows the path.
            for (int i = 0; i < shortestPath.size() - 1; i++) { // Because there should not be any other line after the destination city, ranges maximum value is shortestPath.size() - 1
                City city1 = findCitywithName(shortestPath.get(i), cityCoordinates); // The city variable representing the city from which the line will start.
                City city2 = findCitywithName(shortestPath.get(i + 1), cityCoordinates); // The city variable representing the city from which the line will end.
                StdDraw.setPenRadius(0.01); // A slightly thicker pen radius than the gray ones for better visibility.
                StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
                StdDraw.line(city1.getX(), city1.getY(), city2.getX(), city2.getY()); // Draws a line between cities on the map.
            }
        }
    }





    // FUNCTIONS FOR THE ALGORITHM.

    public static List<String> findShortestPath(String startCityName, String destinationCityName, City[] cities, City2[] connections) {
        // Implementing Dijkstra's algorithm to find the shortest path between two cities.

        // Initializing the distance values of city names as infinity. Here, I used Double.MAX_VALUE to represent the biggest number I know.
        double[] valueOftheNode = new double[cities.length];

        for(int i = 0; i < cities.length;i++){
            valueOftheNode[i] = Double.MAX_VALUE;
        }
        valueOftheNode[findCityIndex(startCityName,cities)] = 0;

        // Array to mark visited cities.
        boolean[] isVisited = new boolean[cities.length];
        // Array to hold the previous cities names.
        String[] previousNode = new String[cities.length]; // the list that holds previous cities.

        // Finding the closest city and updating its distance value with infinity. Also, keeping track of its index.
        while (true) {

            int minIndex = -1; // Initializing the variable minIndex to -1 as a control to check if a minimum index has not been found during the loop.
            // If the loop cannot find a minimum index, the minIndex value remains as -1,
            // indicating that no minimum index has been found, and the loop exits without further processing.

            double minValueoftheNode = Double.MAX_VALUE; // Initializing the variable minValueoftheNode to Double.MAX_VALUE
            // ensures that it starts with the largest possible value for a double data type in Java.
            // This value is used to track the minimum distance found during the loop. By setting it initially to the maximum possible value,
            // any distance found during the iteration will be smaller than this value of the node,

                /*This code segment checks whether the value  of the current city node is less than the current minimum node.
                If it is, updates the minValueoftheNode variable to the distance of the current node of the city,
                and we also update the minIndex variable to the index of the current city i. */
            for (int i = 0; i < cities.length; i++) {
                if (!isVisited[i] && (valueOftheNode[i] < minValueoftheNode)) {
                    minValueoftheNode = valueOftheNode[i];
                    minIndex = i;
                }
            }

                /*If minIndex is still -1, it means that no city satisfying the conditions was found during the loop iteration.
                In other words, all cities have been visited, or there are no reachable cities from the starting city.
                In this case, the break statement is executed, causing the loop to terminate early.
                This is because there are no more cities to explore, and the algorithm should stop. */
            if (minIndex == -1) {
                break;
            }

            isVisited[minIndex] = true; // It marks the city as visited, indicating that its shortest path from the starting city has been determined.
            // This prevents the algorithm from revisiting this city later on during the traversal.


            // This loop iterates over each City2 object in the connections array.
            for (City2 connection : connections) {
                int city1Index = findCityIndex(connection.getName1(), cities); // Represents the starting cities index.
                int city2Index = findCityIndex(connection.getName2(), cities); // Represents the destination cities index.


                    /* This condition checks if the index of the first city in the connection (cityIndex1)
                    is equal to minIndex (which represents the index of the current closest city found) and if the destination city
                    has not been visited yet. If both conditions are met,
                    it calculates the new distance from the starting city to the destination city vith the current closest city (minIndex).
                    if this new value of the node is less than the previously recorded value to the destination city (valueOftheNode[cityIndex2]),
                    it updates the value to the destination city with this new shorter value and marks the current closest city as the previous node for the destination city (previousNode[cityIndex2]) to create the path later. */
                if ((city1Index == minIndex) && !isVisited[city2Index]) {
                    double newValueoftheNode = valueOftheNode[minIndex] + cities[minIndex].distanceTo(cities[city2Index]);
                    if (newValueoftheNode < valueOftheNode[city2Index]) {
                        valueOftheNode[city2Index] = newValueoftheNode;
                        previousNode[city2Index] = cities[minIndex].getName();
                    }
                        /*This code segment performs a similar operation to the one above,
                    However, it checks the file to determine if the starting city is listed as the second value in the city_connections.txt file.
                    If so, it executes the same logic. */
                } if ((city2Index == minIndex) && !isVisited[city1Index]) {
                    double newValueoftheNode = valueOftheNode[minIndex] + cities[minIndex].distanceTo(cities[city1Index]);
                    if (newValueoftheNode < valueOftheNode[city1Index]) {
                        valueOftheNode[city1Index] = newValueoftheNode;
                        previousNode[city1Index] = cities[minIndex].getName();
                    }
                }
            }
        }




        // CREATING THE PATH

        List<String> path = new ArrayList<>(); // This list stores the names of cities along the shortest path.

        int destinationIndex = findCityIndex(destinationCityName, cities);
        if (valueOftheNode[destinationIndex] != Double.MAX_VALUE) { // If the value of the node of the destination city is not infinite, indicating that a path exists.
            while (destinationIndex != -1) {
                path.add(cities[destinationIndex].getName()); // Add the name of the current city to the path list.
                destinationIndex = findCityIndex(previousNode[destinationIndex], cities); // Update the destination index to the previous node, continuing until the starting city is reached.
            }

            // Since we constructed the path list from end to start, we need to reverse it for the printed output.
            List<String> reversedPath = new ArrayList<>();
            // Reverse the path list to prepare the output.
            for (int i = path.size() - 1; i >= 0; i--) {
                reversedPath.add(path.get(i));
            }
            path = reversedPath; // Upload the file with its reversed version.
        }
        return path; // When this findShortestPath ends, it will return the shortest path.
    }




    /*This function searches for a City object in an array of City objects by its name.
    If a matching city is found, it returns that city, else, it returns null */
    public static City findCitywithName(String cityName, City[] cities) {
        for (City city : cities) { // Each loop iterates through each City object in the cities array.
            if (city.getName().equals(cityName)) { // Checks if the given city in the cities array.
                return city;
            }
        }
        return null;
    }



    /* This function calculates the total distance along the provided path between the starting city and the destination city.
    It iterates through the path list, which contains the names of cities in the order of traversal.
    For each consecutive pair of cities in the path, it calculates the distance between them using the city class list.
    The algorithm finds the distance between the previous city and the next city, updates the city indices, and accumulates the distances.
    It continues this process until reaching the end of the path list. */
    public static double calculateTotalDistance(List<String> path, City[] cities) {
        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            City city1 = findCitywithName(path.get(i), cities); // Retrieves the previous city on the path
            City city2 = findCitywithName(path.get(i + 1), cities); // Retrieves the next city on the path
            totalDistance += city1.distanceTo(city2); // Calculates the distance between the previous and the next city
        }
        return totalDistance;
    }



    /* This function searches for a City object in an array of City objects by its name.
    If a matching city is found, it returns that cities index. Else it returns the -1 value. */
    public static int findCityIndex(String cityName, City[] cities) {
        for (int i = 0; i < cities.length; i++) {
            if (cities[i].getName().equals(cityName)) { // Each loop iterates through each City object in the cities array. If the given city is in the cities array, return its index.
                return i;
            }
        }
        return -1; // The initial control value of minIndex
    }

}
