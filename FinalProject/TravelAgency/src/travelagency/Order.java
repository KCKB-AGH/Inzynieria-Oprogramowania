package travelagency;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class represents an order in TravelAgency.
 * 
 * @author Konrad
 * @version 1.0
 */

public class Order {
     private Integer id;
     private Travel travel = new Travel();
     private double price = 0.00;
     private Hotel hotel;
     private Transport transport;
     private Date startDate;
     private Date endDate;
     private double hotelPrice;
     ArrayList attractionsList = new ArrayList();

    /**
    * This method allow to compare dates in order to determine how or whether they differ.
    * @param date1 date of beginnig travel
    * @param date2 date of ending travel
    * @return difference between dates
    */ 
    public static int diff(Date date1, Date date2) {
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();

    c1.setTime(date1);
    c2.setTime(date2);
    int diffDay = 0;

    if (c1.before(c2)) {
      diffDay = countDiffDay(c1, c2);
    } else {
      diffDay = countDiffDay(c2, c1);
    }

    return diffDay;
  }  
    
    /**
    * This method display the difference between dates (in days).
    * @param date1 date of beginnig travel
    * @param date2 date of ending travel
    */ 
    public static void DateDiff(Date date1, Date date2) {
    int diffDay = diff(date1, date2);
    System.out.println("Different Day : " + diffDay);
  }
    
    /**
    * This method counts the difference between two dates (in days).
    * @param c1 date of beginnig travel
    * @param c2 date of ending travel
    * @return difference between dates
    */ 
    public static int countDiffDay(Calendar c1, Calendar c2) {
    int returnInt = 0;
    while (!c1.after(c2)) {
      c1.add(Calendar.DAY_OF_MONTH, 1);
      returnInt++;
    }

    if (returnInt > 0) {
      returnInt = returnInt - 1;
    }

    return (returnInt);
  } 
     
    /**
    * This method calculates the price with all of extras.
    */        
    public void priceUpdate(){
        
        double attractionPrices = 0;
        Attraction attraction;
        for(int i=0; i< getAttractionsList().size();i++)
        {
            attraction = (Attraction) getAttractionsList().get(i);
            attractionPrices += attraction.getPrice();
        }
        hotelPrice = diff(getEndDate(), getStartDate());
        price = getTravel().getPrice() + getHotel().getPrice()*(hotelPrice) 
                + getTransport().getPrice()*getTravel().getDistance()
                + attractionPrices;
    }
     
   /** 
    * Class constructor.
    */     
    public Order() {
    }

   /**
    * Class constructor specifying all of properties of objects to create.
    * @param id 
    * @param travel
    * @param price
    * @param hotel
    * @param transport 
    * @param startDate the date of begining
    * @param endDate the date of ending
    */    
    public Order(Integer id, Travel travel, double price, Hotel hotel, Transport transport, Date startDate, Date endDate) {
        this.id = id;
        this.travel = travel;
        this.price = price;
        this.hotel = hotel;
        this.transport = transport;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Get the value of travel
     *
     * @return the value of travel
     */
    public Travel getTravel() {
        return travel;
    }

    /**
     * Set the value of travel
     *
     * @param travel new value of travel
     */    
    public void setTravel(Travel travel) {
        this.travel = travel;
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
     * Set the value of price
     *
     * @param id new value of price
     */
    public void setId(Integer id) {
        this.id = id;
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

    /**
     * Get the value of hotel
     *
     * @return the value of hotel
     */    
    public Hotel getHotel() {
        return hotel;
    }

     /**
     * Set the value of hotel
     *
     * @param hotel new value of hotel
     */
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    /**
     * Get the value of transport
     *
     * @return the value of transport
     */    
    public Transport getTransport() {
        return transport;
    }

    /**
     * Set the value of transport
     *
     * @param transport new value of transport
     */
    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    /**
     * Get the value of startDate
     *
     * @return the value of startDate
     */    
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Set the value of startDate
     *
     * @param startDate new value of startDate
     */    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the value of endDate
     *
     * @return the value of endDate
     */     
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Set the value of endDate
     *
     * @param endDate new value of endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Get the value of attractionsList
     *
     * @return the value of attractionsList
     */    
    public ArrayList getAttractionsList() {
        return attractionsList;
    }

    /**
     * Set the value of attractionsList
     *
     * @param attractionsList new value of attractionsList
     */
    public void setAttractionsList(ArrayList attractionsList) {
        this.attractionsList = attractionsList;
    }  
}
