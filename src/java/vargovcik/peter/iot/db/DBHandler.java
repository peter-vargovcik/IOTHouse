/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vargovcik.peter.iot.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import vargovcik.peter.iot.model.IOTNode;
import vargovcik.peter.iot.model.TempData;

/**
 *
 * @author Peter Vargovcik
 */
public class DBHandler {

    private static DBHandler instance;
    public enum LOG {INFO, WARNING, ERROR, FATAL};

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;

    static String url = "jdbc:mysql://localhost:3306/IOTHouse";
    static String user = "monitor";
    static String password = "monitor1";

    private DBHandler() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static DBHandler getInstance() {
        if (instance == null) {
            instance = new DBHandler();
        }
        return instance;
    }

    public String testDB() {

        String res = "";

        try {

            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                System.out.println(rs.getString(1));
                res = rs.getString(1);
            }

        } catch (SQLException ex) {
            System.err.println(ex.toString());
             log(ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                System.err.println(ex.toString());
                 log(ex.getMessage(), ex);
            }
        }
        return res;
    }

    public synchronized ArrayList<IOTNode> getNodeList() {
        ArrayList<IOTNode> nodes = new ArrayList<>();

        try {

            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT tdate, ttime, zone, temperature FROM tempdat;");

            while (rs.next()) {
                IOTNode node = new IOTNode(rs.getInt("id"), rs.getString("ipAddress"), rs.getString("name"));
                nodes.add(node);
            }

        } catch (SQLException ex) {
            System.err.println(ex.toString());
             log(ex.getMessage(), ex);

        } finally {
            closeSQLConnection();
        }

        return nodes;
    }

    public synchronized ArrayList<TempData> getTempDataList() {
        ArrayList<TempData> nodes = new ArrayList<>();

        try {

            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT temperatureDate, temperatureZone, temperature FROM temperatures;");

            while (rs.next()) {
                TempData tempData = new TempData(rs.getString("temperatureZone"), rs.getFloat("temperature"), new Date(rs.getTimestamp("temperatureDate").getTime()));
                nodes.add(tempData);
            }

        } catch (SQLException ex) {
            System.err.println(ex.toString());
             log(ex.getMessage(), ex);

        } finally {
            closeSQLConnection();
        }

        return nodes;
    }

    public synchronized ArrayList<TempData> getCurentTempDataList() {
        ArrayList<TempData> nodes = new ArrayList<>();

        try {

            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("CALL getLastTemperatures();");

            while (rs.next()) {
                TempData tempData = new TempData(rs.getString("temperatureZone"), rs.getFloat("temperature"), new Date(rs.getTimestamp("temperatureDate").getTime()));
                nodes.add(tempData);
            }

        } catch (SQLException ex) {
            System.err.println(ex.toString());
             log(ex.getMessage(), ex);

        } finally {
            closeSQLConnection();
        }

        return nodes;
    }

    public synchronized ArrayList<TempData> getTemperatures(Date from, Date to) {
        ArrayList<TempData> nodes = new ArrayList<>();

        Calendar calStart = new GregorianCalendar();
        calStart.setTime(from);
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        from = calStart.getTime();
        
        calStart.setTime(to);
        calStart.set(Calendar.HOUR_OF_DAY, 24);
        calStart.set(Calendar.MINUTE, 60);
        calStart.set(Calendar.SECOND, 60);
        to = calStart.getTime();

        java.sql.Date sqlDateFrom = new java.sql.Date(from.getTime());
        java.sql.Date sqlDateTo = new java.sql.Date(to.getTime());

        try {

            con = DriverManager.getConnection(url, user, password);

            String sql = "CALL getTemperaturesFromTo(?,?);";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDate(1, sqlDateFrom);
            preparedStatement.setDate(2, sqlDateTo);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                TempData tempData = new TempData(rs.getString("temperatureZone"), rs.getFloat("temperature"), new Date(rs.getTimestamp("temperatureDate").getTime()));
                nodes.add(tempData);
            }

        } catch (SQLException ex) {
             log(ex.getMessage(), ex);

        } finally {
            closeSQLConnection();
        }

        return nodes;
    }
    

    public synchronized String addTempData(String zone, float temperature) {

        try {

            con = DriverManager.getConnection(url, user, password);

            String sql = "CALL `IOTHouse`.`inputTemperature`(?, ?);";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, zone);
            preparedStatement.setFloat(2, temperature);
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
             log(ex.getMessage(), ex);
            return ex.toString();

        } finally {
            closeSQLConnection();
        }
        return "ok";

    }

    private void closeSQLConnection() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (SQLException ex) {
            log(ex.getMessage(), ex);
        }
    }
    
    public synchronized IOTNode addIOTNode(IOTNode node){
        
        int nodeId = node.getId();
        
        try {
            con = DriverManager.getConnection(url, user, password);
            
            String sql = "CALL `IOTHouse`.`addIOTNode`(?,?,?);";
            log( node.getIpAddress());
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, node.getName());
            preparedStatement.setString(2, node.getIpAddress());
            preparedStatement.setString(3, node.getDescription());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            log(ex.getMessage(), ex);

        } finally {
            closeSQLConnection();
        }
        
        
        return node;
    }
    
    public IOTNode getIOTNode(IOTNode node){
        
        int nodeId = node.getId();
                
        return getIOTNodeByID(nodeId);
    }
    
    public boolean nodeExists(int nodeId){      
                
        return getIOTNodeByID(nodeId) !=null;
    }
    
    
    
    private synchronized IOTNode getIOTNodeByID(int nodeId){
        IOTNode resultNode = null;
        
        try {
            con = DriverManager.getConnection(url, user, password);
            
            String verifySql = "CALL `IOTHouse`.`getIOTNode`(?);";

            PreparedStatement preparedStatement = con.prepareStatement(verifySql);
            preparedStatement.setInt(1, nodeId);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                resultNode = new IOTNode(rs.getInt("id"), rs.getString("ipAddress"), rs.getString("name"), rs.getString("description"));
            }

        } catch (SQLException ex) {
             log(ex.getMessage(), ex);

        } finally {
            closeSQLConnection();
        }
        
        return resultNode;
    }
    
    public void log(String message){
        savelog(LOG.INFO,message,"");
    }
    
    public void log(LOG logLevel, String message){
        savelog(logLevel,message,"");
    }
    
    public void log(String message, Exception e){       
        savelog(LOG.ERROR,message,getStackTraceToString(e));
    }
    
    public void logFatal(String message, Exception e){
        savelog(LOG.FATAL,message,getStackTraceToString(e));
    }
    
    private String getStackTraceToString(Exception e){
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i<e.getStackTrace().length; i++){
            StackTraceElement el = e.getStackTrace()[i];
            sb.append(el.toString());
        }
        return sb.toString();
    }
    
    private synchronized void savelog(LOG logLevel, String message, String stockTrace){
        try {
            con = DriverManager.getConnection(url, user, password);
            
            String sql = "CALL `IOTHouse`.`saveLog`(?,?,?);";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            
            preparedStatement.setString(1,logLevel.toString());
            preparedStatement.setString(2, message);
            preparedStatement.setString(3, stockTrace);
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
             log(ex.getMessage(), ex);

        } finally {
            closeSQLConnection();
        }        
    }
}


