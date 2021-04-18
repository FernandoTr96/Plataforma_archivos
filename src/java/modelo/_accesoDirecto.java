/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import mysql_manager.mysql_manager;

/**
 *
 * @author fernando
 */
public class _accesoDirecto {

    private int id;
    private int id_usuario;
    private int id_publicacion;
    Connection conexion = new mysql_manager().getConn();

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the id_usuario
     */
    public int getId_usuario() {
        return id_usuario;
    }

    /**
     * @param id_usuario the id_usuario to set
     */
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    /**
     * @return the id_publicacion
     */
    public int getId_publicacion() {
        return id_publicacion;
    }

    /**
     * @param id_publicacion the id_publicacion to set
     */
    public void setId_publicacion(int id_publicacion) {
        this.id_publicacion = id_publicacion;
    }
    

    public JsonObject marcar() {

        String query = "INSERT INTO acceso_directo(id,id_usuario,id_publicacion) VALUES(NULL,?,?)";
        JsonObject json = new JsonObject();

        try {

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, this.getId_usuario());
            ps.setInt(2, this.getId_publicacion());

            int afectadas = ps.executeUpdate();

            if (afectadas != 0) {
                json.addProperty("estado", 200);
            } else {
                json.addProperty("estado", 400);
            }

            conexion.close();

        } catch (Exception e) {
            json.addProperty("estado", e.getMessage());
        }

        return json;
    }

    
    public JsonObject desmarcar() {

        String query = "DELETE FROM acceso_directo WHERE id_usuario = ? AND id_publicacion = ?";
        JsonObject json = new JsonObject();

        try {

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, this.getId_usuario());
            ps.setInt(2, this.getId_publicacion());

            int afectadas = ps.executeUpdate();

            if (afectadas != 0) {
                json.addProperty("estado", 200);
            } else {
                json.addProperty("estado", 400);
            }

            conexion.close();

        } catch (Exception e) {
            json.addProperty("estado", e.getMessage());
        }

        return json;
    }
    
    
    public JsonArray listarAccesos() {

        String query = "SELECT \n" +
        "a.id, \n" +
        "p.id as id_publicacion,\n" +
        "p.titulo,\n" +
        "p.descripcion,\n" +
        "p.fecha_alta,\n" +
        "p.estado\n" +
        "FROM acceso_directo a LEFT JOIN publicaciones p \n" +
        "ON a.id_publicacion = p.id  WHERE a.id_usuario = "+this.getId_usuario()+" ORDER BY a.id DESC";

        JsonArray array = new JsonArray();
        JsonObject json = new JsonObject();

        try {

            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (!rs.isBeforeFirst()) {

                //en caso de que no existan usuarios
                json.addProperty("estado", 201);
                array.add(json);

            } else {

                while (rs.next()) {
                    JsonObject subObj = new JsonObject();
                    subObj.addProperty("id", rs.getInt("id"));
                    subObj.addProperty("id_publicacion", rs.getInt("id_publicacion"));
                    subObj.addProperty("titulo", rs.getString("titulo"));
                    subObj.addProperty("descripcion", rs.getString("descripcion"));
                    subObj.addProperty("fecha_alta", rs.getString("fecha_alta"));
                    subObj.addProperty("estatus", rs.getString("estado"));
                    array.add(subObj);
                }

                conexion.close();
            }

        } catch (SQLException e) {
            json.addProperty("estado", e.getMessage());
            array.add(json);
        }

        return array;
    }
    
    
    public JsonObject borrarAccesoDirecto() {

        String query = "DELETE FROM acceso_directo WHERE id = ?";
        JsonObject json = new JsonObject();

        try {

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, this.getId());

            int afectadas = ps.executeUpdate();

            if (afectadas != 0) {
                json.addProperty("estado", 200);
            } else {
                json.addProperty("estado", 400);
            }

            conexion.close();

        } catch (Exception e) {
            json.addProperty("estado", e.getMessage());
        }

        return json;
    }

}
