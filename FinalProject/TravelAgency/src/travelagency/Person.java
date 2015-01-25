package travelagency;


import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.exception.JDBCConnectionException;




/**
 * This class represents user in TravelAgency.
 * 
 * @author Konrad
 * @version 1.0
 */

public class Person  implements java.io.Serializable {

    ArrayList orderList = new ArrayList();
    Order order = new Order();
    
     private Integer id;
     private String login;
     private String password;
     private String firstName;
     private String lastName;
     private String email;
     private boolean seller;
     private static final Logger logger = Logger.getLogger(Login.class.getName()); 
     
    /**
    * This method updates the list of bought travels.
    */ 
    public void updateOrderList(){
        Transaction tx = null;

        try{
            org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            orderList = (ArrayList) session.createQuery("FROM TravelOrder WHERE id = ?").setString(0, Integer.toString(getId())).list();
            tx.commit();
            logger.info("ORDERS LIST WAS UPDATED SUCCESSFULLY");
        }catch (JDBCConnectionException ex) {
            JOptionPane.showMessageDialog(null, "Lista zakupionych wycieczek nie została zaimportowana!","Brak połączenia z bazą danych!",JOptionPane.ERROR_MESSAGE);
            logger.warn(ex);
        }catch(HibernateException e){
            if (tx!=null) tx.rollback();
            logger.warn(e);
            e.printStackTrace();
        }
    }
     
   /** 
    * Class constructor.
    */     
    public Person() {
    }

    /**
     * Get the value of orderList
     *
     * @return the value of orderList
     */    
    public ArrayList getOrderList() {
        return orderList;
    }

    /**
     * Set the value of orderList
     *
     * @param orderList new value of orderList
     */      
    public void setorderList(ArrayList orderList) {
        this.orderList = orderList;
    }

    /**
     * Get the value of order
     *
     * @return the value of order
     */ 
    public Order getOrder() {
        return order;
    }

    /**
     * Set the value of order
     *
     * @param order new value of order
     */      
    public void setOrder(Order order) {
        this.order = order;
    }

   /**
    * Class constructor specifying all properties of objects to create.
    * @param login
    * @param password
    * @param firstName
    * @param lastName
    * @param email
    * @param seller
    */    
    public Person(String login, String password, String firstName, String lastName, String email, boolean seller) {
       this.login = login;
       this.password = password;
       this.firstName = firstName;
       this.lastName = lastName;
       this.email = email;
       this.seller = seller;
    }

   /**
    * Class constructor specifying seller flag as false of objects to create.
    * @param login
    * @param password
    * @param firstName
    * @param lastName
    * @param email
    */    
    public Person(String login, String password, String firstName, String lastName, String email) {
       this.login = login;
       this.password = password;
       this.firstName = firstName;
       this.lastName = lastName;
       this.email = email;
       this.seller = false;
    }    
    
    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public Integer getId() {
        return this.id;
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
     * Get the value of login
     *
     * @return the value of login
     */ 
    public String getLogin() {
        return this.login;
    }

    /**
     * Set the value of login
     *
     * @param login new value of login
     */    
    public void setLogin(String login) {
        this.login = login;
    }
    
    /**
     * Get the value of password
     *
     * @return the value of password
     */ 
    public String getPassword() {
        return this.password;
    }
    
    /**
     * Set the value of password
     *
     * @param password new value of password
     */    
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Get the value of firstName
     *
     * @return the value of firstName
     */      
    public String getFirstName() {
        return this.firstName;
    }
    
    /**
     * Set the value of firstName
     *
     * @param firstName new value of firstName
     */     
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * Get the value of lastName
     *
     * @return the value of lastName
     */     
    public String getLastName() {
        return this.lastName;
    }
    
    /**
     * Set the value of lastName
     *
     * @param lastName new value of lastName
     */    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Get the value of email
     *
     * @return the value of email
     */     
    public String getEmail() {
        return this.email;
    }
 
    /**
     * Set the value of email
     *
     * @param email new value of email
     */    
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Get the value of seller
     *
     * @return the value of seller
     */ 
    public boolean isSeller() {
        return this.seller;
    }
    
    /**
     * Set the value of seller
     *
     * @param seller new value of seller
     */
    public void setSeller(boolean seller) {
        this.seller = seller;
    }

    /**
     * Get the value of orderList
     *
     * @return the value of orderList
     */ 
    public ArrayList getCartList() {
        return orderList;
    }

    /**
     * This method throws unsupported operation exception
     * @param b 
     */
    void isSeller(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}


