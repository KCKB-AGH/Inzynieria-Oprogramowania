
package travelagency;

/**
 * This class represents a transport.
 *
 * @author Konrad
 * @version 1.0
 */
public class Transport {
    private Integer id;
    private String name;
    private double price;

    
   /** 
    * Class constructor.
    */    
    public Transport() {
    }
   
   /**
    * Class constructor specifying name and price of objects to create.
    * @param name the name of Transport
    * @param price the price of transport
    */    
    public Transport(String name, double price) {
        this.name = name;
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
