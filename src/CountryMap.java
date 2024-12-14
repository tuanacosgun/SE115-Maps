public class CountryMap {
    private City source;
    private City destination;
    private int time;

    public CountryMap(City c1, City c2, int time){
       this.source=c1;
       this.destination=c2;
       this.time=time;
    }

    public City getSource() {
        return source;
    }

    public void setSource(City source) {
        this.source = source;
    }

    public City getDestination(){
        return destination;
    }

    public void setDestination(City destination){
        this.destination=destination;
    }

    public int getTime(){
        return time;
    }

    public void setTime(int time){
        this.time=time;
    }
}
