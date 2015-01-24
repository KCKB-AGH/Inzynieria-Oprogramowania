
package travelagency;

import java.awt.CardLayout;
import java.awt.Color;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.exception.JDBCConnectionException;


/**
 * This class is the main class of project. It contains GUI.
 * 
 * @author Konrad
 * @version 1.0
 */
public class Main extends javax.swing.JFrame {
    TravelAgency travelAgency;
    Person user = new Person("kowalskij", "123", "Jan", "Kowalski", "konrad127@gmail.com",true);
    private static final Logger logger = Logger.getLogger(Login.class.getName()); 
    DecimalFormat df = new DecimalFormat();
    TableModel travelModel, hotelModel, transportModel, attractionModel, travelsModel;
    Object travelRowData[][],hotelRowData[][],transportRowData[][],attractionRowData[][],travelsRowData[][];
    String travelColumnNames[]={"ID","Nazwa","Opis","Miasto","Odległość","Cena"};
    String hotelColumnNames[]={"ID","Nazwa","Miasto","Cena"};
    String transportColumnNames[]={"ID","Nazwa","Cena"};
    String attractionColumnNames[]={"ID","Nazwa","Cena"};
    String travelsColumnNames[]={"ID","Nazwa","Miasto", "Cena"};
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    ProgressBar b = new ProgressBar();
    public Main(Person user) {
        setUser(user);
        travelAgency = new TravelAgency(user);
        initComponents();
        currentDate();
       
        
        logger.info("TRAVEL LIST - " + travelAgency.getTravelList().size());
        logger.info("HOTEL LIST - " + travelAgency.getHotelList().size());
        logger.info("TANSPORT LIST - " + travelAgency.getTransportList().size());
        logger.info("ATTRACTIONS LIST - " + travelAgency.getAttractionsList().size());        
        
        
        cardPanel.add(mainPanel, "mainPanel");
        cardPanel.add(transportPanel, "transportPanel");
        cardPanel.add(travelPanel, "travelPanel");
        cardPanel.add(attractionPanel, "attractionPanel");
        cardPanel.add(hotelPanel, "hotelPanel");
        cardPanel.add(editProfilePanel, "editProfilePanel");
        cardPanel.add(datePanel, "datePanel");
        cardPanel.add(submitPanel, "submitPanel");
        cardPanel.add(travelsPanel, "travelsPanel");
    }

    /**
     * Get the value of user
     *
     * @return the value of user
     */    
    public Person getUser(){
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
     * This method is responsible for the display of the date in the main window.
     */
    public void currentDate(){  
        Thread clock = new Thread()
        {
            public void run()
            {
                for(;;)
                {
                    Calendar cal = new GregorianCalendar();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    dateLabel.setText(year + "-" + (month+1) +"-"+ day );

                    int second = cal.get(Calendar.SECOND);
                    int minute = cal.get(Calendar.MINUTE);
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    timeLabel.setText(hour + ":" + (minute) + ":" + second);

                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {

                    }
                }
            }
        };
        
        clock.start();
    
    }
    
    /**
     * This method is responsible for editing user profile in program.
     */
    public void editProfile(){
        Transaction tx = null;
        try{
                org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();
                int id = user.getId();
                List personss =  session.createQuery("FROM Person WHERE id = ?").setInteger(0, id).list();;
                Person sample = new Person();
                
                if(personss.size()>0)
                {
                    sample = (Person)personss.get(0);
                }
                
                if(!firstNameProflTextField.getText().isEmpty()) sample.setFirstName(firstNameProflTextField.getText());
                if(!lastNameProfilTextField.getText().isEmpty()) sample.setLastName(lastNameProfilTextField.getText());
                if(!emailProfilTextField.getText().isEmpty()) sample.setEmail(emailProfilTextField.getText());
                
                if(sample.getPassword().equals(String.valueOf(oldProfilPasswordField.getPassword())))
                {
                    if( String.valueOf(newPass1PasswordField.getPassword()).equals(String.valueOf(newPass2PasswordField.getPassword())))
                    {
                        if(!newPass1PasswordField.getText().isEmpty()) sample.setPassword(String.valueOf(newPass1PasswordField.getPassword()));
                    }
                }
                
                
                session.saveOrUpdate(sample);
                setUser(sample);
                nameLabel.setText(getUser().getFirstName()+" " + getUser().getLastName());
                tx.commit();
                JOptionPane.showMessageDialog(null,"Profil został uaktualniony!" ,"Edycja",JOptionPane.INFORMATION_MESSAGE);
                logger.warn("POFILE UPDATED");
            }catch (JDBCConnectionException ex) {
                logger.warn(ex);
                if (tx!=null) tx.rollback();
                JOptionPane.showMessageDialog(null,"Brak połączenia z bazą danych!" ,"Błąd!",JOptionPane.ERROR_MESSAGE);
            }catch(HibernateException e){
                e.printStackTrace();
                tx.rollback();
                logger.warn(e);
            }
        
    }
    
    /**
     * This method is responsible for updating travelListTable.
     */
    private void updateTravelTable() {
        travelRowData = new Object[travelAgency.getTravelList().size()][travelColumnNames.length];
        df.getMaximumFractionDigits();
        Travel travel;
        for(int i=0; i< travelAgency.getTravelList().size();i++)
        {
            travel =  (Travel) travelAgency.getTravelList().get(i);
            travelRowData[i][0]=travel.getId();
            travelRowData[i][1]=travel.getName();
            travelRowData[i][2]=travel.getOverview();
            travelRowData[i][3]=travel.getCity();
            travelRowData[i][4]=travel.getDistance();
            travelRowData[i][5]=travel.getPrice();
        }
                 
        travelModel = new AbstractTableModel() {
            public int getColumnCount() {
                return travelColumnNames.length;
            }
            
            @Override
            public String getColumnName(int column) {
                return travelColumnNames[column];
            }
            
            public int getRowCount() {
                return travelRowData.length;
            }
            
            public Object getValueAt(int row, int column) {
                return travelRowData[row][column];
            }
            
            @Override
            public Class getColumnClass(int column) 
                    
            {
                Class c = getValueAt(0, column).getClass();
                return c;
            }
        };
        travelListTable.setModel(travelModel);
        travelListTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        travelListTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        travelListTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        travelListTable.getColumnModel().getColumn(3).setPreferredWidth(5);
        travelListTable.getColumnModel().getColumn(4).setPreferredWidth(10);
        travelListTable.getColumnModel().getColumn(5).setPreferredWidth(10);
    }
    
    /**
     * This method is responsible for updating hotelListTable.
     */    
    private void updateHotelTable() {
        hotelRowData = new Object[travelAgency.getHotelList().size()][hotelColumnNames.length];
        df.getMaximumFractionDigits();
        Hotel hotel;
        for(int i=0; i< travelAgency.getHotelList().size();i++)
        {
            hotel = (Hotel) travelAgency.getHotelList().get(i);
            hotelRowData[i][0]=hotel.getId();
            hotelRowData[i][1]=hotel.getName();
            hotelRowData[i][2]=hotel.getCity();
            hotelRowData[i][3]=hotel.getPrice();
        }
                 
        hotelModel = new AbstractTableModel() {
            public int getColumnCount() {
                return hotelColumnNames.length;
            }
            
            @Override
            public String getColumnName(int column) {
                return hotelColumnNames[column];
            }
            
            public int getRowCount() {
                return hotelRowData.length;
            }
            
            public Object getValueAt(int row, int column) {
                return hotelRowData[row][column];
            }
            
            @Override
            public Class getColumnClass(int column) 
                    
            {
                Class c = getValueAt(0, column).getClass();
                return c;
            }
        };
        hotelListTable.setModel(hotelModel);
        hotelListTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        hotelListTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        hotelListTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        hotelListTable.getColumnModel().getColumn(3).setPreferredWidth(10);
    }
    
    /**
     * This method is responsible for updating transportListTable.
     */    
    private void updateTransportTable() {
        transportRowData = new Object[travelAgency.getTransportList().size()][transportColumnNames.length];
        df.getMaximumFractionDigits();
        Transport transport;
        for(int i=0; i< travelAgency.getTransportList().size();i++)
        {
            transport = (Transport) travelAgency.getTransportList().get(i);
            transportRowData[i][0]=transport.getId();
            transportRowData[i][1]=transport.getName();
            transportRowData[i][2]=transport.getPrice();
        }
                 
        transportModel = new AbstractTableModel() {
            public int getColumnCount() {
                return transportColumnNames.length;
            }
            
            @Override
            public String getColumnName(int column) {
                return transportColumnNames[column];
            }
            
            public int getRowCount() {
                return transportRowData.length;
            }
            
            public Object getValueAt(int row, int column) {
                return transportRowData[row][column];
            }
            
            @Override
            public Class getColumnClass(int column) 
                    
            {
                Class c = getValueAt(0, column).getClass();
                return c;
            }
        };
        transportListTable.setModel(transportModel);
        transportListTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        transportListTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        transportListTable.getColumnModel().getColumn(2).setPreferredWidth(10);
    }
    
    /**
     * This method is responsible for updating attractionListTable.
     */
    private void updateAttractionTable() {
        attractionRowData = new Object[travelAgency.getAttractionsList().size()][attractionColumnNames.length];
        df.getMaximumFractionDigits();
        Attraction attraction;
        for(int i=0; i< travelAgency.getAttractionsList().size();i++)
        {
            attraction = (Attraction) travelAgency.getAttractionsList().get(i);
            attractionRowData[i][0]=attraction.getId();
            attractionRowData[i][1]=attraction.getName();
            attractionRowData[i][2]=attraction.getPrice();
        }
                 
        attractionModel = new AbstractTableModel() {
            public int getColumnCount() {
                return attractionColumnNames.length;
            }
            
            @Override
            public String getColumnName(int column) {
                return attractionColumnNames[column];
            }
            
            public int getRowCount() {
                return attractionRowData.length;
            }
            
            public Object getValueAt(int row, int column) {
                return attractionRowData[row][column];
            }
            
            @Override
            public Class getColumnClass(int column) 
                    
            {
                Class c = getValueAt(0, column).getClass();
                return c;
            }
        };
        attractionListTable.setModel(attractionModel);
        attractionListTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        attractionListTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        attractionListTable.getColumnModel().getColumn(2).setPreferredWidth(10);
    }
    
    /**
     * This method is responsible for updating attractionListTable1.
     */    
    private void updateAttractionOrderTable() {
        attractionRowData = new Object[getUser().getOrder().getAttractionsList().size()][attractionColumnNames.length];
        df.getMaximumFractionDigits();
        Attraction attraction;
        for(int i=0; i< getUser().getOrder().getAttractionsList().size();i++)
        {
            attraction = (Attraction) getUser().getOrder().getAttractionsList().get(i);
            attractionRowData[i][0]=attraction.getId();
            attractionRowData[i][1]=attraction.getName();
            attractionRowData[i][2]=attraction.getPrice();
        }
                 
        attractionModel = new AbstractTableModel() {
            public int getColumnCount() {
                return attractionColumnNames.length;
            }
            
            @Override
            public String getColumnName(int column) {
                return attractionColumnNames[column];
            }
            
            public int getRowCount() {
                return attractionRowData.length;
            }
            
            public Object getValueAt(int row, int column) {
                return attractionRowData[row][column];
            }
            
            @Override
            public Class getColumnClass(int column) 
                    
            {
                Class c = getValueAt(0, column).getClass();
                return c;
            }
        };
        attractionListTable1.setModel(attractionModel);
        attractionListTable1.getColumnModel().getColumn(0).setPreferredWidth(5);
        attractionListTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
        attractionListTable1.getColumnModel().getColumn(2).setPreferredWidth(10);
    }
    
    /**
     * This method is responsible for updating submitAttractionTable.
     */    
    private void updateSubmitAttractionTable(){
        attractionRowData = new Object[getUser().getOrder().getAttractionsList().size()][attractionColumnNames.length];
        df.getMaximumFractionDigits();
        Attraction attraction;
        for(int i=0; i< getUser().getOrder().getAttractionsList().size();i++)
        {
            attraction = (Attraction) getUser().getOrder().getAttractionsList().get(i);
            attractionRowData[i][0]=attraction.getId();
            attractionRowData[i][1]=attraction.getName();
            attractionRowData[i][2]=attraction.getPrice();
        }
                 
        attractionModel = new AbstractTableModel() {
            public int getColumnCount() {
                return attractionColumnNames.length;
            }
            
            @Override
            public String getColumnName(int column) {
                return attractionColumnNames[column];
            }
            
            public int getRowCount() {
                return attractionRowData.length;
            }
            
            public Object getValueAt(int row, int column) {
                return attractionRowData[row][column];
            }
            
            @Override
            public Class getColumnClass(int column) 
                    
            {
                Class c = getValueAt(0, column).getClass();
                return c;
            }
        };
        submitAttractionTable.setModel(attractionModel);
        submitAttractionTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        submitAttractionTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        submitAttractionTable.getColumnModel().getColumn(2).setPreferredWidth(10);  
        
    }
    
    /**
     * This method is responsible for updating travelsTable.
     */    
    private void updateTravelsTable() {
        travelsRowData = new Object[travelAgency.getHistoryList().size()][travelsColumnNames.length];
        df.getMaximumFractionDigits();
        History history;
        for(int i=0; i< travelAgency.getHistoryList().size();i++)
        {
            history =  (History) travelAgency.getHistoryList().get(i);
            travelsRowData[i][0]=history.getId();
            travelsRowData[i][1]=history.getName();
            travelsRowData[i][2]=history.getCity();
            travelsRowData[i][3]=history.getPrice();
        }
                 
        travelsModel = new AbstractTableModel() {
            public int getColumnCount() {
                return travelsColumnNames.length;
            }
            
            @Override
            public String getColumnName(int column) {
                return travelsColumnNames[column];
            }
            
            public int getRowCount() {
                return travelsRowData.length;
            }
            
            public Object getValueAt(int row, int column) {
                return travelsRowData[row][column];
            }
            
            @Override
            public Class getColumnClass(int column) 
                    
            {
                Class c = getValueAt(0, column).getClass();
                return c;
            }
        };
        travelsTable.setModel(travelsModel);
        travelsTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        travelsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        travelsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        travelsTable.getColumnModel().getColumn(3).setPreferredWidth(10);

    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headPanel = new javax.swing.JPanel();
        timeLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        helloLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        cardPanel = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        editProfilePanel = new javax.swing.JPanel();
        loginProfileLabel = new javax.swing.JLabel();
        firstNameProfilLabel = new javax.swing.JLabel();
        LastNameProfilLabel = new javax.swing.JLabel();
        EmailProfilLabel = new javax.swing.JLabel();
        changePassProfilLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        oldPassProfilLabel = new javax.swing.JLabel();
        newPass1ProfilLabel = new javax.swing.JLabel();
        newPass2ProfilLabel = new javax.swing.JLabel();
        loginProfileTextField = new javax.swing.JTextField();
        firstNameProflTextField = new javax.swing.JTextField();
        lastNameProfilTextField = new javax.swing.JTextField();
        emailProfilTextField = new javax.swing.JTextField();
        oldProfilPasswordField = new javax.swing.JPasswordField();
        newPass1PasswordField = new javax.swing.JPasswordField();
        newPass2PasswordField = new javax.swing.JPasswordField();
        editProfileButton = new javax.swing.JButton();
        passErrorProfilLabel = new javax.swing.JLabel();
        travelsPanel = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        travelsTable = new javax.swing.JTable();
        editTravelsPanel = new javax.swing.JPanel();
        travelPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        travelListTable = new javax.swing.JTable();
        travelNextButton = new javax.swing.JButton();
        progressLabel = new javax.swing.JLabel();
        hotelPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        hotelListTable = new javax.swing.JTable();
        hotelNextButton = new javax.swing.JButton();
        hotelBackButton = new javax.swing.JButton();
        progressLabel1 = new javax.swing.JLabel();
        transportPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        transportListTable = new javax.swing.JTable();
        transportBackButton = new javax.swing.JButton();
        transportNextButton = new javax.swing.JButton();
        progressLabel2 = new javax.swing.JLabel();
        attractionPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        attractionListTable = new javax.swing.JTable();
        attractionBackButton = new javax.swing.JButton();
        attractionNextButton = new javax.swing.JButton();
        progressLabel3 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        attractionListTable1 = new javax.swing.JTable();
        addAttractionButton = new javax.swing.JButton();
        datePanel = new javax.swing.JPanel();
        startCalendar = new com.toedter.calendar.JCalendar();
        endCalendar = new com.toedter.calendar.JCalendar();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dateNextButton = new javax.swing.JButton();
        dateBackButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        submitPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        submitAttractionTable = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        submitBackButton = new javax.swing.JButton();
        submitBuyButton = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        MenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        logoutMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        profilMenu = new javax.swing.JMenu();
        editProfileMenuItem = new javax.swing.JMenuItem();
        tavelMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        timeLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        timeLabel.setText("TIME");

        dateLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        dateLabel.setText("DATE");

        helloLabel.setText("Witaj:");

        nameLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        nameLabel.setText(getUser().getFirstName()+" " + getUser().getLastName());

        javax.swing.GroupLayout headPanelLayout = new javax.swing.GroupLayout(headPanel);
        headPanel.setLayout(headPanelLayout);
        headPanelLayout.setHorizontalGroup(
            headPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headPanelLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(helloLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameLabel)
                .addContainerGap())
        );
        headPanelLayout.setVerticalGroup(
            headPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(dateLabel)
                .addComponent(timeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(helloLabel)
                .addComponent(nameLabel))
        );

        timeLabel.getAccessibleContext().setAccessibleName("timeLabel");
        dateLabel.getAccessibleContext().setAccessibleName("dateLabel");

        cardPanel.setLayout(new java.awt.CardLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(jTable1);

        jButton1.setText("Edytuj");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        cardPanel.add(mainPanel, "card3");

        loginProfileLabel.setText("Login:");

        firstNameProfilLabel.setText("Imię:");

        LastNameProfilLabel.setText("Nazwisko:");

        EmailProfilLabel.setText("Email:");

        changePassProfilLabel.setText("Zmień hasło:");

        oldPassProfilLabel.setText("Stare hasło:");

        newPass1ProfilLabel.setText("Nowe hasło:");

        newPass2ProfilLabel.setText("Powtórz nowe hasło:");

        loginProfileTextField.setEnabled(false);

        newPass1PasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                newPass1PasswordFieldKeyReleased(evt);
            }
        });

        newPass2PasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                newPass2PasswordFieldKeyReleased(evt);
            }
        });

        editProfileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Pencil-icon.png"))); // NOI18N
        editProfileButton.setText("Edytuj profil");
        editProfileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProfileButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editProfilePanelLayout = new javax.swing.GroupLayout(editProfilePanel);
        editProfilePanel.setLayout(editProfilePanelLayout);
        editProfilePanelLayout.setHorizontalGroup(
            editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editProfilePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editProfilePanelLayout.createSequentialGroup()
                        .addComponent(changePassProfilLabel)
                        .addGap(342, 342, 342))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editProfilePanelLayout.createSequentialGroup()
                        .addGroup(editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(newPass1ProfilLabel)
                            .addComponent(oldPassProfilLabel)
                            .addComponent(newPass2ProfilLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(oldProfilPasswordField)
                            .addComponent(newPass1PasswordField)
                            .addComponent(newPass2PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editProfilePanelLayout.createSequentialGroup()
                        .addGroup(editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(firstNameProfilLabel)
                            .addComponent(loginProfileLabel)
                            .addComponent(LastNameProfilLabel)
                            .addComponent(EmailProfilLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(loginProfileTextField)
                            .addComponent(firstNameProflTextField)
                            .addComponent(lastNameProfilTextField)
                            .addComponent(emailProfilTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passErrorProfilLabel)
                .addContainerGap(277, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editProfilePanelLayout.createSequentialGroup()
                .addGap(61, 518, Short.MAX_VALUE)
                .addComponent(editProfileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        editProfilePanelLayout.setVerticalGroup(
            editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editProfilePanelLayout.createSequentialGroup()
                .addContainerGap(103, Short.MAX_VALUE)
                .addGroup(editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loginProfileLabel)
                    .addComponent(loginProfileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstNameProflTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstNameProfilLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LastNameProfilLabel)
                    .addComponent(lastNameProfilTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EmailProfilLabel)
                    .addComponent(emailProfilTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(changePassProfilLabel)
                .addGap(2, 2, 2)
                .addGroup(editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oldPassProfilLabel)
                    .addComponent(oldProfilPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newPass1ProfilLabel)
                    .addComponent(newPass1PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newPass2ProfilLabel)
                    .addComponent(newPass2PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passErrorProfilLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(editProfileButton)
                .addContainerGap())
        );

        cardPanel.add(editProfilePanel, "card8");

        travelsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(travelsTable);

        javax.swing.GroupLayout travelsPanelLayout = new javax.swing.GroupLayout(travelsPanel);
        travelsPanel.setLayout(travelsPanelLayout);
        travelsPanelLayout.setHorizontalGroup(
            travelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(travelsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                .addContainerGap())
        );
        travelsPanelLayout.setVerticalGroup(
            travelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(travelsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                .addContainerGap())
        );

        cardPanel.add(travelsPanel, "card10");

        javax.swing.GroupLayout editTravelsPanelLayout = new javax.swing.GroupLayout(editTravelsPanel);
        editTravelsPanel.setLayout(editTravelsPanelLayout);
        editTravelsPanelLayout.setHorizontalGroup(
            editTravelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        editTravelsPanelLayout.setVerticalGroup(
            editTravelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        cardPanel.add(editTravelsPanel, "card11");

        travelListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(travelListTable);

        travelNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add1.png"))); // NOI18N
        travelNextButton.setText("Dalej");
        travelNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                travelNextButtonActionPerformed(evt);
            }
        });

        progressLabel.setText("WYBÓR WYCIECZKI...");

        javax.swing.GroupLayout travelPanelLayout = new javax.swing.GroupLayout(travelPanel);
        travelPanel.setLayout(travelPanelLayout);
        travelPanelLayout.setHorizontalGroup(
            travelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, travelPanelLayout.createSequentialGroup()
                .addGroup(travelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, travelPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(progressLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(travelPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(travelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(travelPanelLayout.createSequentialGroup()
                                .addGap(0, 568, Short.MAX_VALUE)
                                .addComponent(travelNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        travelPanelLayout.setVerticalGroup(
            travelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(travelPanelLayout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(progressLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(travelNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        cardPanel.add(travelPanel, "card4");

        hotelListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(hotelListTable);

        hotelNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add1.png"))); // NOI18N
        hotelNextButton.setText("Dalej");
        hotelNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hotelNextButtonActionPerformed(evt);
            }
        });

        hotelBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/back_alt.png"))); // NOI18N
        hotelBackButton.setText("Wstecz");
        hotelBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hotelBackButtonActionPerformed(evt);
            }
        });

        progressLabel1.setText("WYBÓR WYCIECZKI ==> WYBÓR HOTELU");

        javax.swing.GroupLayout hotelPanelLayout = new javax.swing.GroupLayout(hotelPanel);
        hotelPanel.setLayout(hotelPanelLayout);
        hotelPanelLayout.setHorizontalGroup(
            hotelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hotelPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(hotelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addGroup(hotelPanelLayout.createSequentialGroup()
                        .addComponent(hotelBackButton)
                        .addGap(456, 456, 456)
                        .addComponent(hotelNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(hotelPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(progressLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        hotelPanelLayout.setVerticalGroup(
            hotelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hotelPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(progressLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addGroup(hotelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hotelBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hotelNextButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        cardPanel.add(hotelPanel, "card5");

        transportListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(transportListTable);

        transportBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/back_alt.png"))); // NOI18N
        transportBackButton.setText("Wstecz");
        transportBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportBackButtonActionPerformed(evt);
            }
        });

        transportNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add1.png"))); // NOI18N
        transportNextButton.setText("Dalej");
        transportNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportNextButtonActionPerformed(evt);
            }
        });

        progressLabel2.setText("WYBÓR WYCIECZKI ==> WYBÓR HOTELU ==> WYBÓR ŚRODKA TRANSPORTU");

        javax.swing.GroupLayout transportPanelLayout = new javax.swing.GroupLayout(transportPanel);
        transportPanel.setLayout(transportPanelLayout);
        transportPanelLayout.setHorizontalGroup(
            transportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transportPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(transportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                    .addGroup(transportPanelLayout.createSequentialGroup()
                        .addComponent(transportBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(transportNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(transportPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(progressLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        transportPanelLayout.setVerticalGroup(
            transportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transportPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(progressLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(transportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(transportBackButton, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(transportNextButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        cardPanel.add(transportPanel, "card6");

        attractionListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(attractionListTable);

        attractionBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/back_alt.png"))); // NOI18N
        attractionBackButton.setText("Wstecz");
        attractionBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attractionBackButtonActionPerformed(evt);
            }
        });

        attractionNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add1.png"))); // NOI18N
        attractionNextButton.setText("Dalej");
        attractionNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attractionNextButtonActionPerformed(evt);
            }
        });

        progressLabel3.setText("WYBÓR WYCIECZKI ==> WYBÓR HOTELU ==> WYBÓR ŚRODKA TRANSPORTU ==> WYBÓR DODATKOWYCH ATRAKCJI");

        attractionListTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(attractionListTable1);

        addAttractionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/plus_orange (1).png"))); // NOI18N
        addAttractionButton.setText("Dodaj atrakcję...");
        addAttractionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAttractionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout attractionPanelLayout = new javax.swing.GroupLayout(attractionPanel);
        attractionPanel.setLayout(attractionPanelLayout);
        attractionPanelLayout.setHorizontalGroup(
            attractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(attractionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(attractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(attractionPanelLayout.createSequentialGroup()
                        .addGroup(attractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                            .addGroup(attractionPanelLayout.createSequentialGroup()
                                .addComponent(attractionBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(attractionNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, attractionPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(attractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, attractionPanelLayout.createSequentialGroup()
                                .addComponent(progressLabel3)
                                .addGap(35, 35, 35))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, attractionPanelLayout.createSequentialGroup()
                                .addComponent(addAttractionButton)
                                .addContainerGap())))))
        );
        attractionPanelLayout.setVerticalGroup(
            attractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(attractionPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(progressLabel3)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(addAttractionButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addGroup(attractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(attractionNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(attractionBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        cardPanel.add(attractionPanel, "card7");

        jLabel1.setText("Data wyjazdu:");

        jLabel2.setText("Data powrtotu:");

        dateNextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Shopping-handshake-icon.png"))); // NOI18N
        dateNextButton.setText("Podsumowanie");
        dateNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateNextButtonActionPerformed(evt);
            }
        });

        dateBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/back_alt.png"))); // NOI18N
        dateBackButton.setText("Wstecz");
        dateBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateBackButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("WYBÓR WYCIECZKI ==> WYBÓR HOTELU ==> WYBÓR ŚRODKA TRANSPORTU");

        jLabel4.setText("==> WYBÓR DODATKOWYCH ATRAKCJI ==> WYBÓR DATY");

        javax.swing.GroupLayout datePanelLayout = new javax.swing.GroupLayout(datePanel);
        datePanel.setLayout(datePanelLayout);
        datePanelLayout.setHorizontalGroup(
            datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, datePanelLayout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addGroup(datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(endCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(124, 124, 124))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, datePanelLayout.createSequentialGroup()
                .addComponent(dateBackButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dateNextButton)
                .addContainerGap())
            .addGroup(datePanelLayout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(169, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, datePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        datePanelLayout.setVerticalGroup(
            datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datePanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addGroup(datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(endCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                .addGroup(datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateNextButton)
                    .addComponent(dateBackButton))
                .addContainerGap())
        );

        cardPanel.add(datePanel, "card8");

        jLabel5.setText("NAZWA WYCIECZKI:");

        jLabel6.setText("HOTEL:");

        jLabel7.setText("TRANSPORT:");

        jLabel8.setText("ATRAKCJE:");

        submitAttractionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(submitAttractionTable);

        jLabel9.setText("DATA WYJAZDU:");

        jLabel10.setText("DATA POWROTU");

        jLabel11.setText("ŁĄCZNY KOSZT:");

        jLabel12.setText("name");

        jLabel13.setText("hotel");
        jLabel13.setToolTipText("");

        jLabel14.setText("transport");

        jLabel15.setText("startDate");

        jLabel16.setText("endDate");

        jLabel17.setText("cost");

        submitBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/back_alt.png"))); // NOI18N
        submitBackButton.setText("Wstecz");
        submitBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitBackButtonActionPerformed(evt);
            }
        });

        submitBuyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/coins.png"))); // NOI18N
        submitBuyButton.setText("KUP");
        submitBuyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitBuyButtonActionPerformed(evt);
            }
        });

        jLabel18.setText("WYBÓR WYCIECZKI ==> WYBÓR HOTELU ==> WYBÓR ŚRODKA TRANSPORTU");

        jLabel19.setText("==> WYBÓR DODATKOWYCH ATRAKCJI ==> WYBÓR DATY ==> PODSUMOWANIE");

        javax.swing.GroupLayout submitPanelLayout = new javax.swing.GroupLayout(submitPanel);
        submitPanel.setLayout(submitPanelLayout);
        submitPanelLayout.setHorizontalGroup(
            submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(submitPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(submitPanelLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15))
                    .addGroup(submitPanelLayout.createSequentialGroup()
                        .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16))))
                .addGap(0, 539, Short.MAX_VALUE))
            .addGroup(submitPanelLayout.createSequentialGroup()
                .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(submitPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(submitPanelLayout.createSequentialGroup()
                                .addComponent(submitBackButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(submitBuyButton))
                            .addGroup(submitPanelLayout.createSequentialGroup()
                                .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(submitPanelLayout.createSequentialGroup()
                                        .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel7))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel14)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel12)))
                                    .addGroup(submitPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(28, 28, 28)
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(submitPanelLayout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel18))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        submitPanelLayout.setVerticalGroup(
            submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(submitPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addGap(45, 45, 45)
                .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                .addGroup(submitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(submitBackButton)
                    .addComponent(submitBuyButton))
                .addContainerGap())
        );

        cardPanel.add(submitPanel, "card9");

        fileMenu.setText("Plik");

        logoutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        logoutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/gnome_application_exit.png"))); // NOI18N
        logoutMenuItem.setText("Wyloguj");
        logoutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(logoutMenuItem);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit.png"))); // NOI18N
        exitMenuItem.setText("Wyjście");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        MenuBar.add(fileMenu);

        profilMenu.setText("Profil");

        editProfileMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/documents-yellow-edit-icon.png"))); // NOI18N
        editProfileMenuItem.setText("Edytuj");
        editProfileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProfileMenuItemActionPerformed(evt);
            }
        });
        profilMenu.add(editProfileMenuItem);

        MenuBar.add(profilMenu);

        tavelMenu.setText("Wycieczka");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/plus_orange.png"))); // NOI18N
        jMenuItem1.setText("Nowa");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        tavelMenu.add(jMenuItem1);

        jMenuItem2.setText("Lista zakupionych wycieczek");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        tavelMenu.add(jMenuItem2);

        MenuBar.add(tavelMenu);

        helpMenu.setText("Pomoc");

        aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Categories-system-help-icon.png"))); // NOI18N
        aboutMenuItem.setText("O programie");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        MenuBar.add(helpMenu);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newPass1PasswordFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newPass1PasswordFieldKeyReleased
        // TODO add your handling code here:
        if(! String.valueOf(newPass1PasswordField.getPassword()).equals(String.valueOf(newPass2PasswordField.getPassword())))
        {
            passErrorProfilLabel.setForeground(Color.red);
            passErrorProfilLabel.setText("Hasła nie pasują do siebie!");
        }else{
            passErrorProfilLabel.setText("");
        }
    }//GEN-LAST:event_newPass1PasswordFieldKeyReleased

    private void newPass2PasswordFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newPass2PasswordFieldKeyReleased
        // TODO add your handling code here:
        if(! String.valueOf(newPass1PasswordField.getPassword()).equals(String.valueOf(newPass2PasswordField.getPassword())))
        {
            passErrorProfilLabel.setForeground(Color.red);
            passErrorProfilLabel.setText("Hasła nie pasują do siebie!");
        }else{
            passErrorProfilLabel.setText("");
        }

    }//GEN-LAST:event_newPass2PasswordFieldKeyReleased

    private void editProfileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProfileButtonActionPerformed
        // TODO add your handling code here:

        if(! firstNameProflTextField.getText().isEmpty() && !lastNameProfilTextField.getText().isEmpty() && !emailProfilTextField.getText().isEmpty() )
        {
            editProfile();
        }else
        {
            JOptionPane.showMessageDialog(null,"Wypełnij wszystkie pola!" ,"Błąd edycji!",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_editProfileButtonActionPerformed

    private void logoutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutMenuItemActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        new Login().setVisible(true);
    }//GEN-LAST:event_logoutMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void editProfileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProfileMenuItemActionPerformed
        // TODO add your handling code here:
        loginProfileTextField.setText(user.getLogin());
        firstNameProflTextField.setText(user.getFirstName());
        lastNameProfilTextField.setText(user.getLastName());
        emailProfilTextField.setText(user.getEmail());
        ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "editProfilePanel");
    }//GEN-LAST:event_editProfileMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        // TODO add your handling code here:
        new About().setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        travelAgency.updateTravelList();
        updateTravelTable();
        ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "travelPanel");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void travelNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_travelNextButtonActionPerformed
       travelAgency.updateHotelList(getUser().getOrder().getTravel().getName()); 
        try{
            if(!travelListTable.isRowSelected(-1))
            {
                        int index = travelListTable.getSelectedRow();
                        Travel travel;
                        travel = (Travel) travelAgency.getTravelList().get(index);
                        getUser().getOrder().setTravel(travel);
                travelAgency.updateHotelList(getUser().getOrder().getTravel().getCity());
                updateHotelTable();        
                ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "hotelPanel");
            }
        }catch(Exception e){
            logger.warn(e);
            JOptionPane.showMessageDialog(this, "Nie wybrano wycieczki...","Wybierz wyczieczke!", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_travelNextButtonActionPerformed

    private void hotelNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hotelNextButtonActionPerformed
        try{
            if(!hotelListTable.isRowSelected(-1))
            {
                        int index = hotelListTable.getSelectedRow();
                        Hotel hotel;
                        hotel = (Hotel) travelAgency.getHotelList().get(index);
                        getUser().getOrder().setHotel(hotel);
                
                travelAgency.updateTransportList();
                updateTransportTable();        
                ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "transportPanel");
            }
        }catch(Exception e){
            logger.warn(e);
            JOptionPane.showMessageDialog(this, "Nie wybrano hotelu...","Wybierz hotel!", JOptionPane.WARNING_MESSAGE);
        } 
    }//GEN-LAST:event_hotelNextButtonActionPerformed

    private void hotelBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hotelBackButtonActionPerformed
        ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "travelPanel");
    }//GEN-LAST:event_hotelBackButtonActionPerformed

    private void transportBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportBackButtonActionPerformed
        ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "hotelPanel");
    }//GEN-LAST:event_transportBackButtonActionPerformed

    private void transportNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportNextButtonActionPerformed
        try{
            if(!transportListTable.isRowSelected(-1))
            {
                        int index = transportListTable.getSelectedRow();
                        Transport transport;
                        transport = (Transport) travelAgency.getTransportList().get(index);
                        getUser().getOrder().setTransport(transport);
                        
                travelAgency.updateAttractionsList();    
                updateAttractionTable();        
                ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "attractionPanel");
            }
        }catch(Exception e){
            logger.warn(e);
            JOptionPane.showMessageDialog(this, "Nie wybrano środka transportu...","Wybierz środek transportu!", JOptionPane.WARNING_MESSAGE);
        } 
    }//GEN-LAST:event_transportNextButtonActionPerformed

    private void attractionBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attractionBackButtonActionPerformed
        ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "transportPanel");
    }//GEN-LAST:event_attractionBackButtonActionPerformed

    private void attractionNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attractionNextButtonActionPerformed
        ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "datePanel");
    }//GEN-LAST:event_attractionNextButtonActionPerformed

    private void addAttractionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAttractionButtonActionPerformed
        // TODO add your handling code here:
        try{
            if(!attractionListTable.isRowSelected(-1))
            {
                        int index = attractionListTable.getSelectedRow();
                        Attraction attraction;
                        attraction = (Attraction) travelAgency.getAttractionsList().get(index);
                        getUser().getOrder().getAttractionsList().add(attraction);
                
                        updateAttractionOrderTable();
            }
        }catch(Exception e){
            logger.warn(e);
        } 
        
    }//GEN-LAST:event_addAttractionButtonActionPerformed

    private void dateNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateNextButtonActionPerformed
        // TODO add your handling code here:
        
        
        
//        if(getUser().getOrder().getStartDate() != null && getUser().getOrder().getEndDate() != null )
//        {
//            if(getUser().getOrder().getStartDate() != getUser().getOrder().getEndDate()){
                getUser().getOrder().setStartDate(startCalendar.getDate());
                getUser().getOrder().setEndDate(endCalendar.getDate());    
                getUser().getOrder().priceUpdate();
                 updateSubmitAttractionTable();
                 jLabel12.setText(getUser().getOrder().getTravel().getName());
                 jLabel13.setText(getUser().getOrder().getHotel().getName());
                 jLabel14.setText(getUser().getOrder().getTransport().getName());
                 jLabel15.setText(dateFormat.format(getUser().getOrder().getStartDate()));
                 jLabel16.setText(dateFormat.format(getUser().getOrder().getEndDate()));
                 jLabel17.setText(String.valueOf(getUser().getOrder().getPrice()));
                ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "submitPanel");
//            }else
//            {
//                JOptionPane.showMessageDialog(this, "Data powrotu pokrywa sie z data wyjazdu...","Wybierz date powrotu!", JOptionPane.WARNING_MESSAGE);
//            }
//            
//        }else{
//            JOptionPane.showMessageDialog(this, "Nie wybrano dat...","Wybierz daty!", JOptionPane.WARNING_MESSAGE);
//        }
//                
    }//GEN-LAST:event_dateNextButtonActionPerformed

    private void dateBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateBackButtonActionPerformed
        ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "attractionPanel");
    }//GEN-LAST:event_dateBackButtonActionPerformed

    private void submitBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitBackButtonActionPerformed
        ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "datePanel");
    }//GEN-LAST:event_submitBackButtonActionPerformed

    private void submitBuyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitBuyButtonActionPerformed
        // TODO add your handling code here:
        Transaction tx = null;
        History history = new History();
        history.setIdPerson(getUser().getId());
        history.setIdTravel(getUser().getOrder().getTravel().getId());
        history.setIdHotel(getUser().getOrder().getHotel().getId());
        history.setIdTransport(getUser().getOrder().getTransport().getId());
        history.setIdAttraction(1);
//        history.setStartDate( new java.sql.Date(getUser().getOrder().getStartDate().getTime()));
//        history.setEndDate( new java.sql.Date(getUser().getOrder().getEndDate().getTime()));
        history.setName(getUser().getOrder().getTravel().getName());
        history.setCity(getUser().getOrder().getTravel().getCity());
        history.setPrice(getUser().getOrder().getPrice());
        try{
            org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(history);
            tx.commit();
            b.setVisible(false);
            JOptionPane.showMessageDialog(this,"Zakupiono wycieczkę!");
            getUser().updateOrderList();
            logger.info("BUY TRAVEL - SUCCESSFUL");
            ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "mainPanel");

        }catch (JDBCConnectionException ex) {
            logger.warn(ex);
            b.setVisible(false);
            JOptionPane.showMessageDialog(null, "Nie zakupiono wycieczki!","Brak połączenia z bazą danych!",JOptionPane.ERROR_MESSAGE);
        }catch(HibernateException e){
            logger.warn(e);
            b.setVisible(false);
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
        
        
        getUser().getOrderList().add(getUser().getOrder());
    }//GEN-LAST:event_submitBuyButtonActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        travelAgency.updateHistoryList(getUser().getId());
        updateTravelsTable();
        ((CardLayout)this.cardPanel.getLayout()).show(cardPanel, "travelsPanel");
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel EmailProfilLabel;
    private javax.swing.JLabel LastNameProfilLabel;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton addAttractionButton;
    private javax.swing.JButton attractionBackButton;
    private javax.swing.JTable attractionListTable;
    private javax.swing.JTable attractionListTable1;
    private javax.swing.JButton attractionNextButton;
    private javax.swing.JPanel attractionPanel;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JLabel changePassProfilLabel;
    private javax.swing.JButton dateBackButton;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton dateNextButton;
    private javax.swing.JPanel datePanel;
    private javax.swing.JButton editProfileButton;
    private javax.swing.JMenuItem editProfileMenuItem;
    private javax.swing.JPanel editProfilePanel;
    private javax.swing.JPanel editTravelsPanel;
    private javax.swing.JTextField emailProfilTextField;
    private com.toedter.calendar.JCalendar endCalendar;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel firstNameProfilLabel;
    private javax.swing.JTextField firstNameProflTextField;
    private javax.swing.JPanel headPanel;
    private javax.swing.JLabel helloLabel;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JButton hotelBackButton;
    private javax.swing.JTable hotelListTable;
    private javax.swing.JButton hotelNextButton;
    private javax.swing.JPanel hotelPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField lastNameProfilTextField;
    private javax.swing.JLabel loginProfileLabel;
    private javax.swing.JTextField loginProfileTextField;
    private javax.swing.JMenuItem logoutMenuItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPasswordField newPass1PasswordField;
    private javax.swing.JLabel newPass1ProfilLabel;
    private javax.swing.JPasswordField newPass2PasswordField;
    private javax.swing.JLabel newPass2ProfilLabel;
    private javax.swing.JLabel oldPassProfilLabel;
    private javax.swing.JPasswordField oldProfilPasswordField;
    private javax.swing.JLabel passErrorProfilLabel;
    private javax.swing.JMenu profilMenu;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JLabel progressLabel1;
    private javax.swing.JLabel progressLabel2;
    private javax.swing.JLabel progressLabel3;
    private com.toedter.calendar.JCalendar startCalendar;
    private javax.swing.JTable submitAttractionTable;
    private javax.swing.JButton submitBackButton;
    private javax.swing.JButton submitBuyButton;
    private javax.swing.JPanel submitPanel;
    private javax.swing.JMenu tavelMenu;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JButton transportBackButton;
    private javax.swing.JTable transportListTable;
    private javax.swing.JButton transportNextButton;
    private javax.swing.JPanel transportPanel;
    private javax.swing.JTable travelListTable;
    private javax.swing.JButton travelNextButton;
    private javax.swing.JPanel travelPanel;
    private javax.swing.JPanel travelsPanel;
    private javax.swing.JTable travelsTable;
    // End of variables declaration//GEN-END:variables
}
