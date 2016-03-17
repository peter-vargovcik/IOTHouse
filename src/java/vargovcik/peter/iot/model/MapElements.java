/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vargovcik.peter.iot.model;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Peter Vargovcik
 */
public class MapElements {
  @XmlElement public String  key;
  @XmlElement public String value;

  private MapElements() {} //Required by JAXB

  public MapElements(String key, String value)
  {
    this.key   = key;
    this.value = value;
  }
}