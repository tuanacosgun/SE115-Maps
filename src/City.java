public class City {
    private String name;
    private int order;
    public City(String name, int order){
        this.name=name;
        this.order=order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String toString(){
        System.out.println(name);
        return name;
    }
}
