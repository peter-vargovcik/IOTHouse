/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vargovcik.peter.iot.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Peter Vargovcik
 */
@XmlRootElement(name = "TempData")
@XmlType(propOrder = {"zone","temperature", "timeStamp", "date", "timeStampInMillis"})
public class TempData {
    
    private float temperature;
    private String zone,date;
    private Date timeStamp;
    private long timeStampInMillis;
    private SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    public TempData(){
    }
    
     public TempData(String zone, float temperature) {
        this.temperature = temperature;
        this.zone = zone;
    }

    public TempData(String zone, float temperature, Date timeStamp) {
        this.temperature = temperature;
        this.zone = zone;
        this.timeStamp = timeStamp;
    }
    
    
     
     
    @XmlElement(name="temperature")
    public float getTemperature() {
        return temperature;
    }


    @XmlElement(name="zone")
    public String getZone() {
        return zone;
    }

    @XmlElement(name="timeStamp")
    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

   

    public void setZone(String zone) {
        this.zone = zone;
    }
    
    @XmlElement(name="date")
    public String getDate() {
        date = dt.format(timeStamp);
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @XmlElement(name="timeStampInMillis")
    public long getTimeStampInMillis() {
        return timeStamp.getTime();
    }

    public void setTimeStampInMillis(long timeStampInMillis) {
        this.timeStampInMillis = timeStampInMillis;
    }   
    

    @Override
    public String toString() {
        return "TempData{" + "temperature=" + temperature + ", zone=" + zone + ", date=" + date + ", timeStamp=" + timeStamp + ", timeStampInMillis=" + timeStampInMillis + '}';
    }
 
         
    
}
