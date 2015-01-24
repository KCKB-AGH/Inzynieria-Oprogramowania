
package travelagency;

/**
 * This class represents a hotel.
 *
 * @author Konrad
 * @version 1.0
 */
public class Hotel {
    private Integer id;
    private String name;
    private String city;
    private double price;

   /** 
    * Class constructor.
    */    
    public Hotel() {
    }
    
   /**
    * Class constructor specifying name, city and price of objects to create.
    */
    public Hotel(String name, String city, double price) {
        this.name = name;
        this.city = city;
        this.price = price;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
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
