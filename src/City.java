public class City {
    private String name;
    private int area;
    public int population;
    public City(String name, int area, int population){
        this.name=name;
        this.area=area;
        this.population=population;

    }

    public int getArea() {
        return area;
    }

    public int getPopulation() {
        return population;
    }

    public String getName() {
        return name;
    }
}
