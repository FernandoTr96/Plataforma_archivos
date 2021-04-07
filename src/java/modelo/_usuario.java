/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    Gson gson = new Gson();

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

    public JsonObject verificar_login() {

        int iddb = 0;
        String nombredb = "";
        String paternodb = "";
        String maternodb = "";
        String correodb = "";
        String roldb = "";
        String imgdb = "";
        String clavedb = "";
        String query = "SELECT * FROM usuarios WHERE correo='" + this.getCorreo() + "'";
        JsonObject json = new JsonObject();

        try {

            Statement st = conexion.createStatement();
            ResultSet result = st.executeQuery(query.trim());

            if (!result.isBeforeFirst()) {

                //en caso de que no exista el usuario regresamos en el map el estado 201
                json.addProperty("estado", 201);

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

                    json.addProperty("id", iddb);
                    json.addProperty("nombre", nombredb);
                    json.addProperty("paterno", paternodb);
                    json.addProperty("materno", maternodb);
                    json.addProperty("correo", correodb);
                    json.addProperty("rol", roldb);
                    json.addProperty("img", imgdb);
                    json.addProperty("estado", 200);

                } else {
                    //si no coincide mandare un estado 202
                    json.addProperty("estado", 202);
                }

            }

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);

        }

        return json;
    }

    public JsonObject registrar_usuario() {

        String query = "INSERT INTO usuarios(nombre,paterno,materno,correo,clave,rol,img,codigo_seguridad) VALUES(?,?,?,?,?,?,?,?)";
        JsonObject json = new JsonObject();
        
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
                json.addProperty("estado", 200);
            } else {
                json.addProperty("estado", 400);
            }

            conexion.close();

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);

            if (e.getErrorCode() == 1062) {
                json.addProperty("estado", 500);
            } else {
                json.addProperty("estado", e.getMessage());
            }
        }

        return json;
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

    public JsonObject asignarCodigo() {

        String query = "UPDATE usuarios set codigo_seguridad = ? WHERE correo = '" + this.getCorreo() + "'";
        JsonObject json = new JsonObject();
        
        try {

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, this.getCodigoSeguridad());
            int afectadas = ps.executeUpdate();

            if (afectadas != 0) {
                json.addProperty("estado", 200);
            } else {
                json.addProperty("estado", 400);
            }

            conexion.close();

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);
            json.addProperty("estado", e.getMessage());
        }

        return json;
    }

    public JsonObject verificarCodigodb() {

        String query = "SELECT * FROM usuarios WHERE codigo_seguridad='" + this.getCodigoSeguridad() + "'";
        JsonObject json = new JsonObject();
        
        try {

            Statement st = conexion.createStatement();
            ResultSet result = st.executeQuery(query.trim());

            if (!result.isBeforeFirst()) {

                //en caso de que no exista el usuario 
                json.addProperty("estado", 201);

            } else {

                json.addProperty("estado", 200);
            }

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);
            json.addProperty("estado", e.getMessage());

        }

        return json;
    }

    public JsonObject cambiarPwd() {

        String query = "UPDATE usuarios set clave = ? WHERE correo = '" + this.getCorreo() + "'";
        JsonObject json = new JsonObject();
        
        try {

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, this.getClave());
            int afectadas = ps.executeUpdate();

            if (afectadas != 0) {
                json.addProperty("estado", 200);
            } else {
                json.addProperty("estado", 400);
            }

            conexion.close();

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);
            json.addProperty("estado", e.getMessage());
        }

        return json;
    }

    public JsonObject updatePerfil() {

        String query = "UPDATE usuarios set correo = ? , clave = ? , img = ?  WHERE id =" + this.getId();
        JsonObject json = new JsonObject();
        
        try {

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, this.getCorreo());
            ps.setString(2, this.getClave());
            ps.setString(3, this.getImg());
            int afectadas = ps.executeUpdate();

            if (afectadas != 0) {
                json.addProperty("estado", 200);
            } else {
                json.addProperty("estado", 400);
            }

            conexion.close();

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);
            if (e.getErrorCode() == 1062) {
                json.addProperty("estado", 500);
            } else {
                json.addProperty("estado", e.getMessage());
            }
        }

        return json;
    }
    
    
    public JsonArray listarUsuarios() {
        
        JsonArray array = new JsonArray();
        String  query = "SELECT * FROM usuarios";
        JsonObject json = new JsonObject();
        
        try
        {
            
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(query);
           
            if (!rs.isBeforeFirst()) {

                //en caso de que no existan usuarios
                json.addProperty("estado", 201);
                array.add(json);

            }else{
                
                while (rs.next())
                {
                    JsonObject subObj = new JsonObject();
                    subObj.addProperty("id", rs.getInt("id"));
                    subObj.addProperty("nombre", rs.getString("nombre") +" "+rs.getString("paterno")+" "+rs.getString("materno"));
                    subObj.addProperty("correo", rs.getString("correo"));
                    subObj.addProperty("rol", rs.getString("rol"));
                    subObj.addProperty("img", rs.getString("img"));
                    array.add(subObj);
                }
                
                conexion.close();
            }
            
        }
        catch(SQLException e)
        {
            json.addProperty("estado", e.getMessage());
            array.add(json);
        }
        
        return array;
    }
    
    
    public JsonObject eliminarUsuario() {

        String query = "DELETE FROM usuarios  WHERE id = ?";
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

        } catch (SQLException e) {

            Logger.getLogger(_usuario.class.getName()).log(Level.SEVERE, null, e);
            json.addProperty("estado", e.getMessage());
        }

        return json;
    }
    
    public JsonObject getNombreyApellidos(){
        
        String  query = "SELECT nombre,paterno,materno FROM usuarios where id ="+this.getId();
        JsonObject json = new JsonObject();
        
        try
        {
            
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(query);
           
            if (!rs.isBeforeFirst()) {

                //en caso de que no existan usuarios
                json.addProperty("estado", 201);

            }else{
                
                while (rs.next())
                {
                    json.addProperty("nombre", rs.getString("nombre"));
                    json.addProperty("paterno", rs.getString("paterno"));
                    json.addProperty("materno", rs.getString("materno"));
                }
                
            }
            
        }
        catch(SQLException e)
        {
            json.addProperty("estado", e.getMessage());
        }
        
        return json;
    }
    
    public JsonArray buscarUsuario(String palabra) {
        
        JsonArray array = new JsonArray();
        String  query = "SELECT * FROM usuarios WHERE nombre LIKE '%"+palabra+"%' OR paterno LIKE '%"+palabra+"%' OR materno LIKE '%"+palabra+"%' OR correo LIKE '%"+palabra+"%'";
        JsonObject json = new JsonObject();
        
        try
        {
            
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(query);
           
            if (!rs.isBeforeFirst()) {

                //en caso de que no existan usuarios
                json.addProperty("estado", 201);
                array.add(json);

            }else{
                
                while (rs.next())
                {
                    JsonObject subObj = new JsonObject();
                    subObj.addProperty("id", rs.getInt("id"));
                    subObj.addProperty("nombre", rs.getString("nombre") +" "+rs.getString("paterno")+" "+rs.getString("materno"));
                    subObj.addProperty("correo", rs.getString("correo"));
                    subObj.addProperty("rol", rs.getString("rol"));
                    subObj.addProperty("img", rs.getString("img"));
                    array.add(subObj);
                }
                
                conexion.close();
            }
            
        }
        catch(SQLException e)
        {
            json.addProperty("estado", e.getMessage());
            array.add(json);
        }
        
        return array;
    }
}
