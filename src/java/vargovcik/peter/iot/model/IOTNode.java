/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vargovcik.peter.iot.model;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Peter Vargovcik
 */
@XmlRootElement(name = "IOTNode")
@XmlType(propOrder = {"id","ipAddress","name","description","payload"})
public class IOTNode {
    
    private int id;
    private String ipAddress, name, description;
    private Map<String, String> payload;
    
     public IOTNode(int id, String ipAddress, String name) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.name = name;
    }

    public IOTNode(int id, String ipAddress, String name, String description) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.name = name;
        this.description = description;
    }

    public IOTNode() {
    }
    
    @XmlElement(name="id")
    public int getId() {
        return id;
    }

    @XmlElement(name="ipAddress")
    public String getIpAddress() {
        return ipAddress;
    }

    @XmlElement(name="name")
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @XmlElement(name="description")
    public String getDescription() {
        return description;
    }
        
    @XmlJavaTypeAdapter(MapAdapter.class)
    public Map<String, String> getPayload() {
        if(payload == null){
            payload = new HashMap<>(); 
        }
        return payload;
    }

    public void setPayload(Map<String, String> map) {
        this.payload = map;
    }

    @Override
    public String toString() {
        return "IOTNode{" + "id=" + id + ", ipAddress=" + ipAddress + ", name=" + name + ", description=" + description + ", payload=" + payload + '}';
    }       
       
}
