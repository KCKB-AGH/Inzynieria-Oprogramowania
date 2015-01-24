/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package travelagency;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.exception.JDBCConnectionException;

/**
 * This class represents one of GUI windows.
 *
 * @author Konrad
 * @version 1.0
 */
public class Register extends javax.swing.JFrame {

    private addProgress p1;
    ProgressBar b = new ProgressBar();
    ImageIcon icon = new ImageIcon("icon.png");
    private static final Logger logger = Logger.getLogger(Register.class.getName()); 
    ArrayList personList = new ArrayList();
    
    /**
     * Creates new form Register
     */
    public Register() {
        initComponents();
        updatePersonsList();
    }

    public ArrayList getPersonList() {
        return personList;
    }
    
    private void updatePersonsList(){
        Transaction tx = null;
        
        try{
            org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            personList = (ArrayList) session.createQuery("FROM Person").list();
            tx.commit();
            logger.info("PERSON LIST WAS UPDATED SUCCESSFULLY");
        }catch (JDBCConnectionException ex) {
            JOptionPane.showMessageDialog(null, "Lista użytkowników nie została zaimportowana!","Brak połączenia z bazą danych!",JOptionPane.ERROR_MESSAGE);
            logger.warn(ex);
        }catch(HibernateException e){
            if (tx!=null) tx.rollback();
            logger.warn(e);
            e.printStackTrace();
        }   
    }
    private void checkLogin(){
        if(regLoginTextField.getText().isEmpty())
       {
           errorLoginLabel.setForeground(Color.red);
           errorLoginLabel.setText("Podaj login!");
       }else
            { 
               boolean found = false;
               for(int i=0; i< getPersonList().size();i++)
                { 
                    Person person = (Person) getPersonList().get(i);
                    String name = person.getLogin();
                    String sample = regLoginTextField.getText();
                    
                    if(name.equalsIgnoreCase(sample))
                    {
                        found = true;
                        errorLoginLabel.setForeground(Color.red);
                        errorLoginLabel.setText("Login zajęty!");
                    }
                }
               if(!found)
               {
                errorLoginLabel.setText("");
               }
            }
    }
    private void checkPassword(){
       if(String.valueOf(regPasswordField.getPassword()).isEmpty())
       {
           errorPassLabel.setForeground(Color.red);
           errorPassLabel.setText("Podaj hasło!");
       }else
       {
           errorPassLabel.setText("");
       }
    }
    private void checkConfPassword(){
       if(String.valueOf(regConfPasswordField.getPassword()).isEmpty())
       {
           errorPassConfLabel.setForeground(Color.red);
           errorPassConfLabel.setText("Podaj ponownie hasło!"); 
       }else if(regPasswordField.getText() == null ? regConfPasswordField.getText() != null : !regPasswordField.getText().equals(regConfPasswordField.getText()))
       {
           errorPassConfLabel.setForeground(Color.red);
           errorPassConfLabel.setText("Hasła nie pasują do siebie!");
       }else
       {
           errorPassConfLabel.setText("");
       }
    }
    private void checkFirstName(){
        if(regFirstNameTextField.getText().isEmpty())
        {
            errorFirstNameLabel.setForeground(Color.red);
            errorFirstNameLabel.setText("Podaj imię!");
        }else
        {
            errorFirstNameLabel.setText("");
        }
    }
    private void checkLastName(){
       if(regLastNameTextField.getText().isEmpty())
        {
            errorLastNameLabel.setForeground(Color.red);
            errorLastNameLabel.setText("Podaj nazwisko!");
        }else
        {
            errorLastNameLabel.setText("");
        } 
    }
    private void checkEmail(){
      if(regEmailTextField.getText().isEmpty())
        {
            errorEmailLabel.setForeground(Color.red);
            errorEmailLabel.setText("Podaj adres E-mail!");
        }else if(!regEmailTextField.getText().contains("@")){
            errorEmailLabel.setForeground(Color.red);
            errorEmailLabel.setText("Adres E-mail nie poprawny!");
        }else if(!regEmailTextField.getText().contains(".")){
            errorEmailLabel.setForeground(Color.red);
            errorEmailLabel.setText("Adres E-mail nie poprawny!");
        }else
        {
            errorEmailLabel.setText("");
        }  
    }
    private Person getNewPerson(){
       Person newPerson = new Person();
       newPerson.setSeller(false);
       
       if(regLoginTextField.getText().isEmpty())
       {
           errorLoginLabel.setForeground(Color.red);
           errorLoginLabel.setText("Podaj login!");
       }else
            { 
               boolean found = false;
               for(int i=0; i< getPersonList().size();i++)
                { 
                    Person person = (Person) getPersonList().get(i);
                    String name = person.getLogin();
                    String sample = regLoginTextField.getText();
                    if(name.equalsIgnoreCase(sample))
                    {
                        found = true;
                        errorLoginLabel.setForeground(Color.red);
                        errorLoginLabel.setText("Login zajęty!");
                    }
                }
               if(!found)
               {
                newPerson.setLogin(regLoginTextField.getText());
                errorLoginLabel.setText("");
               }
            }
       
       if(regPasswordField.getText().isEmpty())
       {
           errorPassLabel.setForeground(Color.red);
           errorPassLabel.setText("Podaj hasło!");
       }
       if(regConfPasswordField.getText().isEmpty())
       {
           errorPassConfLabel.setForeground(Color.red);
           errorPassConfLabel.setText("Podaj ponownie hasło!");
       }
       if(regPasswordField.getText() == null ? regConfPasswordField.getText() != null : !regPasswordField.getText().equals(regConfPasswordField.getText()))
       {
           errorPassConfLabel.setForeground(Color.red);
           errorPassConfLabel.setText("Hasła nie pasują do siebie!");
       }
       if(String.valueOf(regPasswordField.getPassword()) !="" && String.valueOf(regConfPasswordField.getPassword()) != "")
       {
            if(regPasswordField.getText() == null ? regConfPasswordField.getText() == null : regPasswordField.getText().equals(regConfPasswordField.getText()))
             {
                 newPerson.setPassword(String.valueOf(regPasswordField.getPassword()));
                 errorPassLabel.setText("");
                 errorPassConfLabel.setText("");
             }
       }
       
       
       if(regFirstNameTextField.getText().isEmpty())
        {
            errorFirstNameLabel.setForeground(Color.red);
            errorFirstNameLabel.setText("Podaj imię!");
        }else
        {
            newPerson.setFirstName(regFirstNameTextField.getText());
            errorFirstNameLabel.setText("");
        }
       
       
       if(regLastNameTextField.getText().isEmpty())
        {
            errorLastNameLabel.setForeground(Color.red);
            errorLastNameLabel.setText("Podaj nazwisko!");
        }else
        {
            newPerson.setLastName(regLastNameTextField.getText());
            errorLastNameLabel.setText("");
        }
       
       
       if(regEmailTextField.getText().isEmpty())
        {
            errorEmailLabel.setForeground(Color.red);
            errorEmailLabel.setText("Podaj adres E-mail!");
        }else
        {
            newPerson.setEmail(regEmailTextField.getText());
            errorEmailLabel.setText("");
        }
       
       
       
       return newPerson;
    }
    private void addPerson(Person newPerson){

        Transaction tx = null;

            try{
                org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();
                session.save(newPerson);
                tx.commit();
                b.setVisible(false);
                JOptionPane.showMessageDialog(null,"Rejestracja przebieła pomyślnie!");
                logger.info("REGISTRATION SUCCESSFULLY - NEW PERSON => " + newPerson.getLogin() + " (NAME: " + newPerson.getFirstName() + " SURNAME: " + newPerson.getLastName() + ")");
                this.setVisible(false);
            }catch (JDBCConnectionException ex) {
                b.setVisible(false);
                logger.warn(ex);
                JOptionPane.showMessageDialog(null, "Nie zarejestrowano!","Brak połączenia z bazą danych!",JOptionPane.ERROR_MESSAGE);
            }catch(HibernateException e){
                logger.warn(e);
                b.setVisible(false);
                if (tx!=null) tx.rollback();
                e.printStackTrace();
            }
            
    }
    class addProgress extends SwingWorker <Void, Void>{
        @Override
        protected Void doInBackground(){
            b.setVisible(true);
            b.setProgressLabel("Rejestracja...");
            addPerson(getNewPerson());
            return null;
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        registerButton = new javax.swing.JButton();
        regLoginLabel = new javax.swing.JLabel();
        regLoginTextField = new javax.swing.JTextField();
        regPasswordLabel = new javax.swing.JLabel();
        regPasswordField = new javax.swing.JPasswordField();
        regConfPasswordLabel = new javax.swing.JLabel();
        regConfPasswordField = new javax.swing.JPasswordField();
        regFirstNameLabel = new javax.swing.JLabel();
        regFirstNameTextField = new javax.swing.JTextField();
        regLastNameLabel = new javax.swing.JLabel();
        regLastNameTextField = new javax.swing.JTextField();
        regEmailLabel = new javax.swing.JLabel();
        regEmailTextField = new javax.swing.JTextField();
        errorLoginLabel = new javax.swing.JLabel();
        errorPassLabel = new javax.swing.JLabel();
        errorPassConfLabel = new javax.swing.JLabel();
        errorFirstNameLabel = new javax.swing.JLabel();
        errorLastNameLabel = new javax.swing.JLabel();
        errorEmailLabel = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();

        setTitle("Rejestracja");
        setIconImage(icon.getImage());

        registerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/document_add.png"))); // NOI18N
        registerButton.setText("Rejestruj");
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        regLoginLabel.setText("Login:");

        regLoginTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                regLoginTextFieldKeyReleased(evt);
            }
        });

        regPasswordLabel.setText("Hasło:");

        regPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                regPasswordFieldKeyReleased(evt);
            }
        });

        regConfPasswordLabel.setText("Powtórz hasło:");

        regConfPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                regConfPasswordFieldKeyReleased(evt);
            }
        });

        regFirstNameLabel.setText("Imię:");

        regFirstNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                regFirstNameTextFieldKeyReleased(evt);
            }
        });

        regLastNameLabel.setText("Nazwisko:");

        regLastNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                checkLastNameHandler(evt);
            }
        });

        regEmailLabel.setText("E-mail:");

        regEmailTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                regEmailTextFieldKeyReleased(evt);
            }
        });

        backButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/back_alt.png"))); // NOI18N
        backButton.setText("Wróć");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(regEmailLabel)
                        .addComponent(regLastNameLabel)
                        .addComponent(regPasswordLabel)
                        .addComponent(regConfPasswordLabel)
                        .addComponent(regLoginLabel)
                        .addComponent(regFirstNameLabel))
                    .addComponent(backButton))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(regLastNameTextField)
                            .addComponent(regEmailTextField)
                            .addComponent(regPasswordField)
                            .addComponent(regFirstNameTextField)
                            .addComponent(regConfPasswordField)
                            .addComponent(regLoginTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(errorLoginLabel)
                            .addComponent(errorPassLabel)
                            .addComponent(errorPassConfLabel)
                            .addComponent(errorFirstNameLabel)
                            .addComponent(errorLastNameLabel)
                            .addComponent(errorEmailLabel)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(regLoginLabel)
                    .addComponent(regLoginTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(errorLoginLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(regPasswordLabel)
                    .addComponent(regPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(errorPassLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(regConfPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(regConfPasswordLabel)
                    .addComponent(errorPassConfLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(regFirstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(regFirstNameLabel)
                            .addComponent(errorFirstNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(regLastNameLabel)
                            .addComponent(regLastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(errorLastNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(regEmailLabel)
                            .addComponent(regEmailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(errorEmailLabel))
                        .addGap(39, 39, 39)
                        .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(backButton))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        // TODO add your handling code here:
        getNewPerson();
        if(errorLoginLabel.getText()=="" && errorPassLabel.getText()=="" && errorPassConfLabel.getText()=="" && errorFirstNameLabel.getText()=="" && errorLastNameLabel.getText()=="" && errorEmailLabel.getText()=="")
                {
                    p1 = new addProgress();
                    p1.execute();
                }else{
                    JOptionPane.showMessageDialog(null, "Nie dokonano rejestracji!", "Rejestracja",0);
                }
    }//GEN-LAST:event_registerButtonActionPerformed

    private void regLoginTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_regLoginTextFieldKeyReleased
        // TODO add your handling code here:
        checkLogin();
    }//GEN-LAST:event_regLoginTextFieldKeyReleased

    private void regPasswordFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_regPasswordFieldKeyReleased
        // TODO add your handling code here:
        checkPassword();
    }//GEN-LAST:event_regPasswordFieldKeyReleased

    private void regConfPasswordFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_regConfPasswordFieldKeyReleased
        // TODO add your handling code here:
        checkConfPassword();
    }//GEN-LAST:event_regConfPasswordFieldKeyReleased

    private void regFirstNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_regFirstNameTextFieldKeyReleased
        // TODO add your handling code here:
        checkFirstName();
    }//GEN-LAST:event_regFirstNameTextFieldKeyReleased

    private void regEmailTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_regEmailTextFieldKeyReleased
        // TODO add your handling code here:
        checkEmail();
    }//GEN-LAST:event_regEmailTextFieldKeyReleased

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_backButtonActionPerformed

    private void checkLastNameHandler(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkLastNameHandler
        // TODO add your handling code here:
        checkLastName();
    }//GEN-LAST:event_checkLastNameHandler

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
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Register().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JLabel errorEmailLabel;
    private javax.swing.JLabel errorFirstNameLabel;
    private javax.swing.JLabel errorLastNameLabel;
    private javax.swing.JLabel errorLoginLabel;
    private javax.swing.JLabel errorPassConfLabel;
    private javax.swing.JLabel errorPassLabel;
    private javax.swing.JPasswordField regConfPasswordField;
    private javax.swing.JLabel regConfPasswordLabel;
    private javax.swing.JLabel regEmailLabel;
    private javax.swing.JTextField regEmailTextField;
    private javax.swing.JLabel regFirstNameLabel;
    private javax.swing.JTextField regFirstNameTextField;
    private javax.swing.JLabel regLastNameLabel;
    private javax.swing.JTextField regLastNameTextField;
    private javax.swing.JLabel regLoginLabel;
    private javax.swing.JTextField regLoginTextField;
    private javax.swing.JPasswordField regPasswordField;
    private javax.swing.JLabel regPasswordLabel;
    private javax.swing.JButton registerButton;
    // End of variables declaration//GEN-END:variables
}
