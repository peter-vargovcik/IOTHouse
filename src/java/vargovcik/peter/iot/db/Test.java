/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vargovcik.peter.iot.db;

/**
 *
 * @author Peter Vargovcik
 */
public class Test {
    
    public static void main(String[] args){
        DBHandler dBHandler = DBHandler.getInstance();
        System.out.println(dBHandler.addTempData("livingRoom",18));
        System.out.println(dBHandler.getTempDataList().toString());
        
    }
    
}
