
package travelagency;

/**
 * This class represents an order in TravelAgency.
 * 
 * @author Konrad
 * @version 1.0
 */
public class Travel {
    private Integer id;
    private String name;
    private String overview;
    private Integer distance;
    private String city;
    private double price;

    
   /** 
    * Class constructor.
    */    
    public Travel() {
    }

    /**
     * Class constructor specifying all of properties of objects to create.
     * @param name
     * @param overview
     * @param distance
     * @param city
     * @param price 
     */
    public Travel(String name, String overview, Integer distance, String city, double price) {
        this.name = name;
        this.overview = overview;
        this.distance = distance;
        this.city = city;
        this.price = price;
    }

    /**
     * Get the value of city
     *
     * @return the value of city
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the value of city
     *
     * @param city new value of city
     */     
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get the value of travel
     *
     * @return the value of travel
     */    
    public Integer getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */     
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */    
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */     
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of overview
     *
     * @return the value of overview
     */     
    public String getOverview() {
        return overview;
    }

    /**
     * Set the value of overview
     *
     * @param overview new value of overview
     */     
    public void setOverview(String overview) {
        this.overview = overview;
    }
  
    /**
     * Get the value of distance
     *
     * @return the value of distance
     */    
    public Integer getDistance() {
        return distance;
    }

    /**
     * Set the value of distance
     *
     * @param distance new value of distance
     */     
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    /**
     * Get the value of price
     *
     * @return the value of price
     */    
    public double getPrice() {
        return price;
    }

    /**
     * Set the value of price
     *
     * @param price new value of price
     */     
    public void setPrice(double price) {
        this.price = price;
    }
  
}
