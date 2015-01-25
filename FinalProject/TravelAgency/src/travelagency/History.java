package travelagency;

import java.sql.Date;

/**
 * This class represents a bought travels history.
 *
 * @author Konrad
 * @version 1.0
 */
public class History {

    private Integer id;
    private Integer idPerson;
    private Integer idTravel;
    private Integer idHotel;
    private Integer idTransport;
    private Integer idAttraction;
    private Date startDate;
    private Date endDate;
    private String name;
    private String city;
    private double price;

    /**
     * Class constructor.
     */
    public History() {
    }
    
   /**
    * Class constructor specifying all properties of objects to create.
    * @param idPerson
    * @param idTravel
    * @param idHotel
    * @param idTransport
    * @param idAttraction
    * @param startDate
    * @param endDate
    * @param name
    * @param city
    * @param price
    */   
    public History(Integer idPerson, Integer idTravel, Integer idHotel, Integer idTransport, Integer idAttraction, Date startDate, Date endDate, String name, String city, double price) {
        this.idPerson = idPerson;
        this.idTravel = idTravel;
        this.idHotel = idHotel;
        this.idTransport = idTransport;
        this.idAttraction = idAttraction;
        this.startDate = startDate;
        this.endDate = endDate;
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
     * Get the value of idPerson
     *
     * @return the value of idPerson
     */
    public Integer getIdPerson() {
        return idPerson;
    }

    /**
     * Set the value of idPerson
     *
     * @param idPerson new value of idPerson
     */
    public void setIdPerson(Integer idPerson) {
        this.idPerson = idPerson;
    }

    /**
     * Get the value of idTravel
     *
     * @return the value of idTravel
     */
    public Integer getIdTravel() {
        return idTravel;
    }

    /**
     * Set the value of idTravel
     *
     * @param idTravel new value of idTravel
     */
    public void setIdTravel(Integer idTravel) {
        this.idTravel = idTravel;
    }

    /**
     * Get the value of idHotel
     *
     * @return the value of idHotel
     */
    public Integer getIdHotel() {
        return idHotel;
    }

    /**
     * Set the value of idHotel
     *
     * @param idHotel new value of idHotel
     */
    public void setIdHotel(Integer idHotel) {
        this.idHotel = idHotel;
    }

    /**
     * Get the value of idTransport
     *
     * @return the value of idTransport
     */
    public Integer getIdTransport() {
        return idTransport;
    }

    /**
     * Set the value of idTransport
     *
     * @param idTransport new value of idTransport
     */
    public void setIdTransport(Integer idTransport) {
        this.idTransport = idTransport;
    }

    /**
     * Get the value of idAttraction
     *
     * @return the value of idAttraction
     */
    public Integer getIdAttraction() {
        return idAttraction;
    }

    /**
     * Set the value of idAttraction
     *
     * @param idAttraction new value of idAttraction
     */
    public void setIdAttraction(Integer idAttraction) {
        this.idAttraction = idAttraction;
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

}
