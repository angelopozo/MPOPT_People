package controller;

import model.entity.Person;
import model.entity.PersonException;
import model.dao.DAOArrayList;
import model.dao.DAOFile;
import model.dao.DAOFileSerializable;
import model.dao.DAOHashMap;
import model.dao.DAOJPA;
import model.dao.DAOSQL;
import model.dao.IDAO;
import start.Routes;
import view.DataStorageSelection;
import view.Delete;
import view.Insert;
import view.Menu;
import view.Read;
import view.ReadAll;
import view.Update;
import utils.Constants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.entity.User;
import org.jdatepicker.DateModel;
import utils.DataValidation;
import view.Login;

/**
 * This class starts the visual part of the application and programs and manages
 * all the events that it can receive from it. For each event received the
 * controller performs an action.
 *
 * @author Francesc Perez
 * @version 1.1.0
 */
public class ControllerImplementation implements IController, ActionListener {

    //Instance variables used so that both the visual and model parts can be 
    //accessed from the Controller.
    private final DataStorageSelection dSS;
    private DAOSQL userdb;
    private IDAO dao;
    private Menu menu;
    private Insert insert;
    private Read read;
    private Delete delete;
    private Update update;
    private ReadAll readAll;
    private Login login;

    public static String[] loggedUser;

    public static ArrayList<Person> s;

    /**
     * This constructor allows the controller to know which data storage option
     * the user has chosen.Schedule an event to deploy when the user has made
     * the selection.
     *
     * @param dSS
     */
    public ControllerImplementation(DataStorageSelection dSS) {
        this.dSS = dSS;
        ((JButton) (dSS.getAccept()[0])).addActionListener(this);
    }

    public ControllerImplementation(IDAO dao, Menu menu) {
        this.dao = dao;
        this.menu = menu;
        initView();
        initController();
        this.dSS = null;
    }

    private void initView() {
        menu.setVisible(true);
        updatePeopleCount();
    }

    private void initController() {
        menu.getCount().addActionListener(e -> updatePeopleCount());

        // Actualizar conteo después de operaciones CRUD
        menu.getInsert().addActionListener(e -> updatePeopleCount());
        menu.getDelete().addActionListener(e -> updatePeopleCount());
        menu.getDeleteAll().addActionListener(e -> updatePeopleCount());
        menu.getCount().addActionListener(e -> handleCountAction());
    }

    private void updatePeopleCount() {
        try {
            int count = dao.count();
            menu.updatePeopleCount(count);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * With this method, the application is started, asking the user for the
     * chosen storage system.
     */
    @Override
    public void start() {
        try {
            Connection conn = DriverManager.getConnection(Routes.DB.getDbServerAddress() + Routes.DB.getDbServerComOpt(),
                    Routes.DB.getDbServerUser(), Routes.DB.getDbServerPassword());
            if (conn != null) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("create table if not exists " + Routes.DB.getDbServerDB() + "." + Routes.USERS.getDbServerTABLE() + "("
                            + "id int primary key auto_increment not null, "
                            + "username varchar(50), "
                            + "password varchar(50), "
                            + "role varchar(50) );");
                }
                conn.close();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dSS, "SQL-DDBB structure not created. Closing application.", "SQL_DDBB - People v1.1.0", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        userdb = new DAOSQL();
        dao = new DAOSQL();
        dSS.setVisible(true);
    }

    /**
     * This receives method handles the events of the visual part. Each event
     * has an associated action.
     *
     * @param e The event generated in the visual part
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dSS.getAccept()[0]) {
            handleDataStorageSelection();
        } else if (e.getSource() == login.getSignIn()) {
            try {
                handleSignInAction();
            } catch (Exception ex) {
                Logger.getLogger(ControllerImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == menu.getCount()) {
            handleCountAction();
        } else if (e.getSource() == menu.getInsert()) {
            handleInsertAction();
        } else if (insert != null && e.getSource() == insert.getInsert()) {
            handleInsertPerson();
        } else if (e.getSource() == menu.getRead()) {
            handleReadAction();
        } else if (read != null && e.getSource() == read.getRead()) {
            handleReadPerson();
        } else if (e.getSource() == menu.getDelete()) {
            handleDeleteAction();
        } else if (delete != null && e.getSource() == delete.getDelete()) {
            handleDeletePerson();
        } else if (e.getSource() == menu.getUpdate()) {
            handleUpdateAction();
        } else if (update != null && e.getSource() == update.getRead()) {
            handleReadForUpdate();
        } else if (update != null && e.getSource() == update.getUpdate()) {
            handleUpdatePerson();
        } else if (e.getSource() == menu.getReadAll()) {
            handleReadAll();
        } else if (e.getSource() == menu.getDeleteAll()) {
            handleDeleteAll();
        }
    }

    private void handleSignInAction() throws Exception {
        ArrayList<User> users = DAOSQL.class.cast(userdb).loadData();
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(login, "There are no registered users in the database. Please add a user to access the app.", "Login - People v1.1.0", JOptionPane.ERROR_MESSAGE);
        } else {
            boolean authenticated = false;
            for (User user : users) {
                if (login.getUsername().getText().equals(user.getUsername()) && login.getPassword().getText().equals(user.getPassword())) {
                    authenticated = true;
                    loggedUser = new String[]{user.getUsername(), user.getPassword(), user.getRole()};
                    JOptionPane.showMessageDialog(login, "You have successfully signed in. Welcome " + login.getUsername().getText() + "!", "Login", JOptionPane.INFORMATION_MESSAGE);
                    login.setVisible(false);
                    setupMenu();
                    break;
                }
            }
            if (!authenticated) {
                JOptionPane.showMessageDialog(login, "Invalid username or password. Please, try again.", "Login", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleCountAction() {
        try {
            int count = dao.count();
            JOptionPane.showMessageDialog(
                    menu,
                    "Número de personas registradas: " + count,
                    "Conteo",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    menu,
                    "Error al obtener el conteo: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleDataStorageSelection() {
        String daoSelected = ((javax.swing.JCheckBox) (dSS.getAccept()[1])).getText();
        dSS.dispose();
        switch (daoSelected) {
            case Constants.ARRAYLIST:
                dao = new DAOArrayList();
                break;
            case Constants.HASHMAP:
                dao = new DAOHashMap();
                break;
            case Constants.FILE:
                setupFileStorage();
                break;
            case Constants.FILE_SERIALIZATION:
                setupFileSerialization();
                break;
            case Constants.SQL_DATABASE:
                setupSQLDatabase();
                break;
            case Constants.JPA_DATABASE:
                setupJPADatabase();
                break;
        }
        setupLogin();
    }

    private void setupFileStorage() {
        File folderPath = new File(Routes.FILE.getFolderPath());
        File folderPhotos = new File(Routes.FILE.getFolderPhotos());
        File dataFile = new File(Routes.FILE.getDataFile());
        folderPath.mkdir();
        folderPhotos.mkdir();
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(dSS, "File structure not created. Closing application.", "File - People v1.1.0", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        dao = new DAOSQL();
    }

    private void setupFileSerialization() {
        File folderPath = new File(Routes.FILES.getFolderPath());
        File dataFile = new File(Routes.FILES.getDataFile());
        folderPath.mkdir();
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(dSS, "File structure not created. Closing application.", "FileSer - People v1.1.0", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        dao = new DAOFileSerializable();
    }

    private void setupSQLDatabase() {
        try {
            Connection conn = DriverManager.getConnection(Routes.DB.getDbServerAddress() + Routes.DB.getDbServerComOpt(),
                    Routes.DB.getDbServerUser(), Routes.DB.getDbServerPassword());
            if (conn != null) {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("create database if not exists " + Routes.DB.getDbServerDB() + ";");
                stmt.executeUpdate("create table if not exists " + Routes.DB.getDbServerDB() + "." + Routes.DB.getDbServerTABLE() + "("
                        + "nif varchar(9) primary key not null, "
                        + "name varchar(50), "
                        + "email varchar(25),"
                        + "phoneNumber varchar(50), "
                        + "postalCode varchar(25),"
                        + "dateOfBirth DATE, "
                        + "photo varchar(200) );");
                stmt.close();
                conn.close();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dSS, "SQL-DDBB structure not created. Closing application.", "SQL_DDBB - People v1.1.0", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void setupJPADatabase() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(Routes.DBO.getDbServerAddress());
            EntityManager em = emf.createEntityManager();
            em.close();
            emf.close();
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(dSS, "JPA_DDBB not created. Closing application.", "JPA_DDBB - People v1.1.0", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        dao = new DAOJPA();
    }

    private void setupMenu() {

        menu = new Menu(loggedUser);
        menu.setVisible(true);
        menu.getInsert().addActionListener(this);
        menu.getRead().addActionListener(this);
        menu.getUpdate().addActionListener(this);
        menu.getDelete().addActionListener(this);
        menu.getReadAll().addActionListener(this);
        menu.getDeleteAll().addActionListener(this);
        menu.getCount().addActionListener(this);
    }

    private void setupLogin() {
        login = new Login();
        login.getSignIn().addActionListener(this);
        login.setVisible(true);
    }

    private void handleInsertAction() {
        insert = new Insert(menu, true, loggedUser);
        insert.getInsert().addActionListener(this);
        insert.setVisible(true);
    }

    private void handleInsertPerson() {
       
    String rawPhone = insert.getPhoneNumber().getText();
String phone = rawPhone.replaceAll("[\\s().-]", "").trim();

if (!DataValidation.isValidPhoneNumber(phone)) {
    JOptionPane.showMessageDialog(insert, "Incorrect phone number (must be 9 digits, e.g., 612345678)", insert.getTitle(), JOptionPane.WARNING_MESSAGE);
    return;
}

Person p = new Person(insert.getNam().getText(), insert.getNif().getText(), insert.getEmail().getText(), phone, insert.getPostalCode().getText());
        

        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(insert.getEmail().getText());
       if (!matcher.matches()) {
            JOptionPane.showMessageDialog(insert, "Incorrect format for email  (E.g., p@gmail.com )", insert.getTitle(), JOptionPane.WARNING_MESSAGE);
            return;
        }
        

        String postalCodeRegex = "^(\\d{5})(?:[-\\s]?\\d{4})?$";
        Pattern pattern1 = Pattern.compile(postalCodeRegex);
        Matcher matcher1 = pattern1.matcher(insert.getPostalCode().getText().trim());

        if (!matcher1.matches()) {
            JOptionPane.showMessageDialog(insert,
                    "Incorrect format for postal code (E.g., 12345 or 12345-6789)",
                    insert.getTitle(),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (insert.getDateOfBirth().getModel().getValue() != null) {
            p.setDateOfBirth(((GregorianCalendar) insert.getDateOfBirth().getModel().getValue()).getTime());
        }
        if (insert.getPhoto().getIcon() != null) {
            p.setPhoto((ImageIcon) insert.getPhoto().getIcon());
        }
        insert(p);
        insert.getReset().doClick();
    }

    private void handleReadAction() {

        read = new Read(menu, true);
        read.getRead().addActionListener(this);
        read.setVisible(true);
    }

    private void handleReadPerson() {
        Person p = new Person(read.getNif().getText());
        Person pNew = read(p);
        if (pNew != null) {
            read.getNam().setText(pNew.getName());
            read.getEmail().setText(pNew.getEmail());
            read.getPostalCode().setText(pNew.getPostalCode());
            if (pNew.getDateOfBirth() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(pNew.getDateOfBirth());
                DateModel<Calendar> dateModel = (DateModel<Calendar>) read.getDateOfBirth().getModel();
                dateModel.setValue(calendar);
            }
             read.getPhoneNumber().setText(pNew.getPhoneNumber());
            //To avoid charging former images
            if (pNew.getPhoto() != null) {
                pNew.getPhoto().getImage().flush();
                read.getPhoto().setIcon(pNew.getPhoto());
            }
        } else {
            JOptionPane.showMessageDialog(read, p.getNif() + " doesn't exist.", read.getTitle(), JOptionPane.WARNING_MESSAGE);
            read.getReset().doClick();
        }
    }

    public void handleDeleteAction() {
        delete = new Delete(menu, true, loggedUser);
        delete.getDelete().addActionListener(this);
        delete.setVisible(true);
    }

    public void handleDeletePerson() {
        if (delete != null) {
            Person p = new Person(delete.getNif().getText());
            delete(p);
            delete.getReset().doClick();
        }
    }

    public void handleUpdateAction() {
        update = new Update(menu, true, loggedUser);
        update.getUpdate().addActionListener(this);
        update.getRead().addActionListener(this);
        update.setVisible(true);
    }

    public void handleReadForUpdate() {
        if (update != null) {
            Person p = new Person(update.getNif().getText());
            Person pNew = read(p);
            if (pNew != null) {
                update.getNam().setEnabled(true);
                update.getEmail().setEnabled(true);
                update.getPhoneNumber().setEnabled(true);
                update.getPostalCode().setEnabled(true);
                update.getDateOfBirth().setEnabled(true);
                update.getPhoto().setEnabled(true);
                update.getUpdate().setEnabled(true);
                update.getNam().setText(pNew.getName());
                update.getEmail().setText(pNew.getEmail());
                update.getPostalCode().setText(pNew.getPostalCode());
                 update.getPhoneNumber().setText(pNew.getPhoneNumber());
                if (pNew.getDateOfBirth() != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(pNew.getDateOfBirth());
                    DateModel<Calendar> dateModel = (DateModel<Calendar>) update.getDateOfBirth().getModel();
                    dateModel.setValue(calendar);
                }
                if (pNew.getPhoto() != null) {
                    pNew.getPhoto().getImage().flush();
                    update.getPhoto().setIcon(pNew.getPhoto());
                    update.getUpdate().setEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(update, p.getNif() + " doesn't exist.", update.getTitle(), JOptionPane.WARNING_MESSAGE);
                update.getReset().doClick();
            }
        }
    }

    public void handleUpdatePerson() {

String rawPhone = insert.getPhoneNumber().getText();
String phone = rawPhone.replaceAll("[\\s().-]", "").trim();

if (!DataValidation.isValidPhoneNumber(phone)) {
    JOptionPane.showMessageDialog(insert, "Incorrect phone number (must be 9 digits, e.g., 612345678)", insert.getTitle(), JOptionPane.WARNING_MESSAGE);
    return;
}

Person p = new Person(insert.getNam().getText(), insert.getNif().getText(), insert.getEmail().getText(), phone, insert.getPostalCode().getText());
       

            String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(update.getEmail().getText());
            if (!matcher.matches()) {
                JOptionPane.showMessageDialog(insert, "Incorrect format for email  (E.g., ejemplo@gmail.com )", insert.getTitle(), JOptionPane.WARNING_MESSAGE);
                return;
            }

            String postalCodeRegex = "^(\\d{5})(?:[-\\s]?\\d{4})?$";
            Pattern pattern1 = Pattern.compile(postalCodeRegex);
            Matcher matcher1 = pattern1.matcher(update.getPostalCode().getText().trim());

            if (!matcher1.matches()) {
                JOptionPane.showMessageDialog(update,
                        "Incorrect format for postal code (e.g., 12345 or 12345-6789)",
                        update.getTitle(),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if ((update.getDateOfBirth().getModel().getValue()) != null) {
                p.setDateOfBirth(((GregorianCalendar) update.getDateOfBirth().getModel().getValue()).getTime());
            }
            if ((ImageIcon) (update.getPhoto().getIcon()) != null) {
                p.setPhoto((ImageIcon) update.getPhoto().getIcon());
            }
            update(p);
            update.getReset().doClick();
        }
    

    public void handleReadAll() {
          ArrayList<Person> s = readAll();
    if (s.isEmpty()) {
        JOptionPane.showMessageDialog(menu, "There are no people registered yet.", "Read All - People v1.1.0", JOptionPane.WARNING_MESSAGE);
    } else {
        readAll = new ReadAll(menu, true);
        DefaultTableModel model = (DefaultTableModel) readAll.getTable().getModel();
        for (int i = 0; i < s.size(); i++) {
            model.addRow(new Object[model.getColumnCount()]);
            model.setValueAt(s.get(i).getNif(), i, 0);
            model.setValueAt(s.get(i).getName(), i, 1);
            model.setValueAt(s.get(i).getEmail(), i, 2);
            model.setValueAt(s.get(i).getPostalCode(), i, 3);
            if (s.get(i).getDateOfBirth() != null) {
                model.setValueAt(s.get(i).getDateOfBirth().toString(), i, 4);
            } else {
                model.setValueAt("", i, 4);
            }
            model.setValueAt(s.get(i).getPhoneNumber(), i, 5);
            if (s.get(i).getPhoto() != null) {
                model.setValueAt("yes", i, 6);
            } else {
                model.setValueAt("no", i, 6);
            }
        }
        readAll.setVisible(true);
    }
    }

    public void handleDeleteAll() {

        if (loggedUser[2].equalsIgnoreCase("employee")) {
            JOptionPane.showMessageDialog(menu, "Employees cannot access this option.");
            return;
        } else {
            Object[] options = {"Yes", "No"};
            int answer = JOptionPane.showOptionDialog(
                    menu,
                    "Are you sure you want to delete all registered people?",
                    "Delete All - People v1.1.0",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[1] // Default selection is "No"
            );

            if (answer == 0) {
                deleteAll();
            }
        }
    }

    /**
     * This function inserts the Person object with the requested NIF, if it
     * doesn't exist. If there is any access problem with the storage device,
     * the program stops.
     *
     * @param p Person to insert
     */
    @Override
    public void insert(Person p) {
        try {
            if (dao.read(p) == null) {
                dao.insert(p);
                JOptionPane.showMessageDialog(insert, "Person inserted succesfully!", insert.getTitle(), JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new PersonException(p.getNif() + " is registered and can not "
                        + "be INSERTED.");
            }
        } catch (Exception ex) {
            //Exceptions generated by file read/write access. If something goes 
            // wrong the application closes.
            if (ex instanceof FileNotFoundException || ex instanceof IOException
                    || ex instanceof ParseException || ex instanceof ClassNotFoundException
                    || ex instanceof SQLException || ex instanceof PersistenceException) {
                JOptionPane.showMessageDialog(insert, ex.getMessage() + ex.getClass() + " Closing application.", insert.getTitle(), JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if (ex instanceof PersonException) {
                JOptionPane.showMessageDialog(insert, ex.getMessage(), insert.getTitle(), JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * This function updates the Person object with the requested NIF, if it
     * doesn't exist. NIF can not be aupdated. If there is any access problem
     * with the storage device, the program stops.
     *
     * @param p Person to update
     */
    @Override
    public void update(Person p) {
        try {
            dao.update(p);
            JOptionPane.showMessageDialog(update, "Person updated succesfully!", update.getTitle(), JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            //Exceptions generated by file read/write access. If something goes 
            // wrong the application closes.
            if (ex instanceof FileNotFoundException || ex instanceof IOException
                    || ex instanceof ParseException || ex instanceof ClassNotFoundException
                    || ex instanceof SQLException || ex instanceof PersistenceException) {
                JOptionPane.showMessageDialog(update, ex.getMessage() + ex.getClass() + " Closing application.", update.getTitle(), JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    /**
     * This function deletes the Person object with the requested NIF, if it
     * exists. If there is any access problem with the storage device, the
     * program stops.
     *
     * @param p Person to read
     */
    @Override
    public void delete(Person p) {
        try {
            if (dao.read(p) != null) {
                dao.delete(p);
                int input = JOptionPane.showConfirmDialog(delete, "Are you sure you want to delete this person?", delete.getTitle(), JOptionPane.OK_OPTION);
                if (input == 0) {
                    dao.delete(p);
                    JOptionPane.showMessageDialog(delete, "Person deleted succesfully!", delete.getTitle(), JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                throw new PersonException(p.getNif() + " is not registered and can not "
                        + "be DELETED");
            }
        } catch (Exception ex) {
            //Exceptions generated by file, DDBB read/write access. If something  
            //goes wrong the application closes.
            if (ex instanceof FileNotFoundException || ex instanceof IOException
                    || ex instanceof ParseException || ex instanceof ClassNotFoundException
                    || ex instanceof SQLException || ex instanceof PersistenceException) {
                JOptionPane.showMessageDialog(read, ex.getMessage() + ex.getClass() + " Closing application.", "Insert - People v1.1.0", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if (ex instanceof PersonException) {
                JOptionPane.showMessageDialog(read, ex.getMessage(), "Delete - People v1.1.0", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * This function returns the Person object with the requested NIF, if it
     * exists. Otherwise it returns null. If there is any access problem with
     * the storage device, the program stops.
     *
     * @param p Person to read
     * @return Person or null
     */
    @Override
    public Person read(Person p) {
        try {
            Person pTR = dao.read(p);
            if (pTR != null) {
                return pTR;
            }
        } catch (Exception ex) {

            //Exceptions generated by file read access. If something goes wrong 
            //reading the file, the application closes.
            if (ex instanceof FileNotFoundException || ex instanceof IOException
                    || ex instanceof ParseException || ex instanceof ClassNotFoundException
                    || ex instanceof SQLException || ex instanceof PersistenceException) {
                JOptionPane.showMessageDialog(read, ex.getMessage() + " Closing application.", read.getTitle(), JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        return null;
    }

    /**
     * This function returns the people registered. If there is any access
     * problem with the storage device, the program stops.
     *
     * @return ArrayList
     */
    @Override
    public ArrayList<Person> readAll() {
        ArrayList<Person> people = new ArrayList<>();
        try {
            people = dao.readAll();
        } catch (Exception ex) {
            if (ex instanceof FileNotFoundException || ex instanceof IOException
                    || ex instanceof ParseException || ex instanceof ClassNotFoundException
                    || ex instanceof SQLException || ex instanceof PersistenceException) {
                JOptionPane.showMessageDialog(readAll, ex.getMessage() + " Closing application.", readAll.getTitle(), JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        return people;
    }

    /**
     * This function deletes all the people registered. If there is any access
     * problem with the storage device, the program stops.
     */
    @Override
    public void deleteAll() {
        try {
            dao.deleteAll();
        } catch (Exception ex) {
            if (ex instanceof FileNotFoundException || ex instanceof IOException
                    || ex instanceof ParseException || ex instanceof ClassNotFoundException
                    || ex instanceof SQLException || ex instanceof PersistenceException) {
                JOptionPane.showMessageDialog(menu, ex.getMessage() + " Closing application.", "Delete All - People v1.1.0", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    @Override
    public void count() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
