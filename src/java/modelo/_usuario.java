/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mysql_manager.mysql_manager;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author fernando
 */
public class _usuario {

    private int id;
    private String nombre;
    private String paterno;
    private String materno;
    private String correo;
    private String clave;
    private String rol;
    private String img;
    private String codigoSeguridad;

    Connection conexion = new mysql_manager().getConn();
    HashMap map = new HashMap();

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
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the paterno
     */
    public String getPaterno() {
        return paterno;
    }

    /**
     * @param paterno the paterno to set
     */
    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    /**
     * @return the materno
     */
    public String getMaterno() {
        return materno;
    }

    /**
     * @param materno the materno to set
     */
    public void setMaterno(String materno) {
        this.materno = materno;
    }

    /**
     * @return the corre
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param corre the corre to set
     */
    public void setCorreo(String corre) {
        this.correo = corre;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * @return the rol
     */
    public String getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return the img
     */
    public String getCodigoSeguridad() {
        return codigoSeguridad;
    }

    /**
     * @param codigoSeguridad the img to set
     */
    public void setCodigoSeguridad(String codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
    }

    public HashMap verificar_login() {

        int iddb = 0;
        String nombredb = "";
        String paternodb = "";
        String maternodb = "";
        String correodb = "";
        String roldb = "";
        String imgdb = "";
        String clavedb = "";
        String query = "SELECT * FROM usuarios WHERE correo='" + this.getCorreo() + "'";

        try {

            Statement st = conexion.createStatement();
            ResultSet result = st.executeQuery(query.trim());

            if (!result.isBeforeFirst()) {

                //en caso de que no exista el usuario regresamos en el map el estado 201
                map.put("estado", 201);

            } else {

                //como el usuario existe sacamos su contraseña guardada en la base de datos
                while (result.next()) {
                    iddb = result.getInt("id");
                    nombredb = result.getString("nombre");
                    paternodb = result.getString("paterno");
                    maternodb = result.getString("materno");
                    correodb = result.getString("correo");
                    clavedb = result.getString("clave");
                    roldb = result.getString("rol");
                    imgdb = result.getString("img");
                }

                conexion.close();

                //si existe el usuario comparamos las contraseñas la encriptada y la del formulario
                if (BCrypt.checkpw(this.getClave().trim(), clavedb.trim())) {

                    map.put("id", iddb);
                    map.put("nombre", nombredb);
                    map.put("paterno", paternodb);
                    map.put("materno", maternodb);
                    map.put("correo", correodb);
                    map.put("rol", roldb);
                    map.put("img", imgdb);

                } else {
                    //si no coincide mandare un estado 202
                    map.put("estado", 202);
                }

            }

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);

        }

        return map;
    }

    public HashMap registrar_usuario() {

        String query = "INSERT INTO usuarios(nombre,paterno,materno,correo,clave,rol,img,codigo_seguridad) VALUES(?,?,?,?,?,?,?,?)";

        try {

            PreparedStatement ps = conexion.prepareStatement(query);

            ps.setString(1, this.getNombre());
            ps.setString(2, this.getPaterno());
            ps.setString(3, this.getMaterno());
            ps.setString(4, this.getCorreo());
            ps.setString(5, this.getClave());
            ps.setString(6, this.getRol());
            ps.setString(7, this.getImg());
            ps.setString(8, this.getCodigoSeguridad());

            int afectadas = ps.executeUpdate();

            if (afectadas != 0) {
                map.put("estado", 200);
            } else {
                map.put("estado", 400);
            }

            conexion.close();

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);

            if (e.getErrorCode() == 1062) {
                map.put("estado", 500);
            } else {
                map.put("estado", e.getMessage());
            }
        }

        return map;
    }

    public boolean buscarNombreApellidos(String nombre, String paterno, String materno) {

        String query = "SELECT * FROM usuarios WHERE nombre='" + nombre + "'" + " AND paterno='" + paterno + "'" + " AND materno='" + materno + "'";
        boolean existe = false;

        try {

            Statement st = conexion.createStatement();
            ResultSet result = st.executeQuery(query.trim());

            if (!result.isBeforeFirst()) {

                //en caso de que no exista el usuario 
                existe = false;

            } else {

                //si existe
                existe = true;
            }

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);

        }

        return existe;
    }

    public boolean correoExiste() {

        String query = "SELECT * FROM usuarios WHERE correo='" + this.getCorreo() + "'";
        boolean existe = false;

        try {

            Statement st = conexion.createStatement();
            ResultSet result = st.executeQuery(query.trim());

            if (!result.isBeforeFirst()) {

                //en caso de que no exista el usuario 
                existe = false;

            } else {

                //si existe
                existe = true;
            }

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);

        }

        return existe;
    }

    public HashMap asignarCodigo() {

        String query = "UPDATE usuarios set codigo_seguridad = ? WHERE correo = '" + this.getCorreo() + "'";

        try {

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, this.getCodigoSeguridad());
            int afectadas = ps.executeUpdate();

            if (afectadas != 0) {
                map.put("estado", 200);
            } else {
                map.put("estado", 400);
            }

            conexion.close();

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);
            map.put("estado", e.getMessage());
        }

        return map;
    }

    public HashMap verificarCodigodb() {

        String query = "SELECT * FROM usuarios WHERE codigo_seguridad='" + this.getCodigoSeguridad() + "'";

        try {

            Statement st = conexion.createStatement();
            ResultSet result = st.executeQuery(query.trim());

            if (!result.isBeforeFirst()) {

                //en caso de que no exista el usuario 
                map.put("estado", 201);

            } else {

                map.put("estado", 200);
            }

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);
            map.put("estado", e.getMessage());

        }

        return map;
    }

    public HashMap cambiarPwd() {

        String query = "UPDATE usuarios set clave = ? WHERE correo = '" + this.getCorreo() + "'";
        
        
        try {

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, this.getClave());
            int afectadas = ps.executeUpdate();

            if (afectadas != 0) {
                map.put("estado", 200);
            } else {
                map.put("estado", 400);
            }

            conexion.close();

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);
            map.put("estado", e.getMessage());
        }

        return map;
    }
}
