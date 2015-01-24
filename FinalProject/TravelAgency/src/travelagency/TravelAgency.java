
package travelagency;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.exception.JDBCConnectionException;

/**
 * This class represents TravelAgency. It contains method independent of GUI.
 * 
 * @author Konrad
 * @version 1.0
 */
public class TravelAgency {
    Person user = new Person("kowalskij", "123", "Jan", "Kowalski", "konrad127@gmail.com",true);
    private static final Logger logger = Logger.getLogger(TravelAgency.class.getName()); 
    private ArrayList travelList = new ArrayList();
    private ArrayList hotelList = new ArrayList();
    private ArrayList transportList = new ArrayList();
    private ArrayList attractionsList = new ArrayList();
    private ArrayList historyList = new ArrayList();
    
    /**
     * Class constructor specifying user of objects to create.
     * @param user 
     */
    public TravelAgency(Person user){
        setUser(user);
    }
    
    /**
     * Get the value of user
     *
     * @return the value of user
     */    
    public Person getUser() {
        return user;
    }
    
    /**
     * Set the value of user
     *
     * @param user new value of user
     */    
    public void setUser(Person user) {
        this.user = user;
    }

    /**
     * Get the value of historyList
     *
     * @return the value of historyList
     */    
    public ArrayList getHistoryList() {
        return historyList;
    }

    /**
     * Set the value of historyList
     *
     * @param historyList new value of historyList
     */    
    public void setHistoryList(ArrayList historyList) {
        this.historyList = historyList;
    }

     /**
     * Get the value of travelList
     *
     * @return the value of travelList
     */      
    public ArrayList getTravelList() {
        return travelList;
    }

    /**
     * Set the value of travelList
     *
     * @param travelList new value of travelList
     */    
    public void setTravelList(ArrayList travelList) {
        this.travelList = travelList;
    }

     /**
     * Get the value of hotelList
     *
     * @return the value of hotelList
     */   
    public ArrayList getHotelList() {
        return hotelList;
    }

    /**
     * Set the value of hotelList
     *
     * @param hotelList new value of hotelList
     */    
    public void setHotelList(ArrayList hotelList) {
        this.hotelList = hotelList;
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

     /**
     * Get the value of transportList
     *
     * @return the value of transportList
     */   
    public ArrayList getTransportList() {
        return transportList;
    }

    /**
     * Set the value of transportList
     *
     * @param transportList new value of transportList
     */     
    public void setTransportList(ArrayList transportList) {
        this.transportList = transportList;
    }
    
    /**
     * This method updates list of avaliable travels.
     */
    public void updateTravelList(){
        Transaction tx = null;

        try{
            org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            travelList = (ArrayList) session.createQuery("FROM Travel").list();
            tx.commit();
            logger.info("TRAVEL LIST WAS UPDATED SUCCESSFULLY");
        }catch (JDBCConnectionException ex) {
            JOptionPane.showMessageDialog(null, "Lista wycieczek nie została zaimportowana!","Brak połączenia z bazą danych!",JOptionPane.ERROR_MESSAGE);
            logger.warn(ex);
        }catch(HibernateException e){
            if (tx!=null) tx.rollback();
            logger.warn(e);
            e.printStackTrace();
        }
    }    
    
    /**
     * Thist method updates list of travels which was bought by user.
     * @param id value of userId
     */
    public void updateHistoryList(Integer id){
        Transaction tx = null;

        try{
            org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            historyList = (ArrayList) session.createQuery("FROM History WHERE idPerson = ?").setString(0, Integer.toString(id)).list();
            tx.commit();
            logger.info("HISTORY LIST WAS UPDATED SUCCESSFULLY");
        }catch (JDBCConnectionException ex) {
            JOptionPane.showMessageDialog(null, "Lista wycieczek nie została zaimportowana!","Brak połączenia z bazą danych!",JOptionPane.ERROR_MESSAGE);
            logger.warn(ex);
        }catch(HibernateException e){
            if (tx!=null) tx.rollback();
            logger.warn(e);
            e.printStackTrace();
        }
    }    
    
    /**
     * This method updates avaliable hotels.
     * @param city value city.Travel
     */
    public void updateHotelList(String city){
        Transaction tx = null;

        try{
            org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            hotelList = (ArrayList) session.createQuery("FROM Hotel WHERE city = ?").setString(0, city).list();
            tx.commit();
            logger.info("HOTEL LIST WAS UPDATED SUCCESSFULLY");
        }catch (JDBCConnectionException ex) {
            JOptionPane.showMessageDialog(null, "Lista hoteli nie została zaimportowana!","Brak połączenia z bazą danych!",JOptionPane.ERROR_MESSAGE);
            logger.warn(ex);
        }catch(HibernateException e){
            if (tx!=null) tx.rollback();
            logger.warn(e);
            e.printStackTrace();
        }
    }
   
    /**
     * This method updates avaliable means of transport.
     */
    public void updateTransportList(){
        Transaction tx = null;

        try{
            org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            transportList = (ArrayList) session.createQuery("FROM Transport").list();
            tx.commit();
            logger.info("TRANSPORT LIST WAS UPDATED SUCCESSFULLY");
        }catch (JDBCConnectionException ex) {
            JOptionPane.showMessageDialog(null, "Lista środków transportu nie została zaimportowana!","Brak połączenia z bazą danych!",JOptionPane.ERROR_MESSAGE);
            logger.warn(ex);
        }catch(HibernateException e){
            if (tx!=null) tx.rollback();
            logger.warn(e);
            e.printStackTrace();
        }
    }
    
    /**
     * This method updates avaliable list of attractions.
     */
    public void updateAttractionsList(){
        Transaction tx = null;

        try{
            org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            attractionsList = (ArrayList) session.createQuery("FROM Attraction").list();
            tx.commit();
            logger.info("ATTRACTIONS LIST WAS UPDATED SUCCESSFULLY");
        }catch (JDBCConnectionException ex) {
            JOptionPane.showMessageDialog(null, "Lista atrakcji nie została zaimportowana!","Brak połączenia z bazą danych!",JOptionPane.ERROR_MESSAGE);
            logger.warn(ex);
        }catch(HibernateException e){
            if (tx!=null) tx.rollback();
            logger.warn(e);
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
