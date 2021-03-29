/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mysql_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fernando
 */

public class mysql_manager {

    private final String url;
    private final String username;
    private final String password;
    private Connection conn;

    public mysql_manager() {
        this.url = "jdbc:mysql://localhost:3306/plataforma_archivos";
        this.username = "root";
        this.password = "";
        this.conn = null;
    }

    public Connection getConn() {
       
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(this.url, this.username, this.password);

        } catch (ClassNotFoundException | SQLException e) {
            
            Logger.getLogger(mysql_manager.class.getName()).log(Level.SEVERE,null,e);
            
        }
        
        return this.conn;
    }
    

}
