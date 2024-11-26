public class City {

    private String cityName;
    private int x;
    private int y;

    public City(String name, int x, int y) {
        this.cityName = name;
        this.x = x;
        this.y = y;

    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public String getName() {
        return cityName;
    }
    public double distanceTo(City other){ // calculates the distance between two cities
        return Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y- other.getY(), 2));
    }
}
