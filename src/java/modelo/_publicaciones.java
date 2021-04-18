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
import mysql_manager.mysql_manager;

/**
 *
 * @author fernando
 */
public class _publicaciones {

    private int id;
    private int id_usuario;
    private String titulo;
    private String descripcion;
    private String fecha_alta;
    private String fecha_inicio;
    private String fecha_final;
    private String prioridad;
    private String carpeta;
    private String estado;
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
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the fecha_alta
     */
    public String getFecha_alta() {
        return fecha_alta;
    }

    /**
     * @param fecha_alta the fecha_alta to set
     */
    public void setFecha_alta(String fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    /**
     * @return the fecha_inicio
     */
    public String getFecha_inicio() {
        return fecha_inicio;
    }

    /**
     * @param fecha_inicio the fecha_inicio to set
     */
    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    /**
     * @return the fecha_final
     */
    public String getFecha_final() {
        return fecha_final;
    }

    /**
     * @param fecha_final the fecha_final to set
     */
    public void setFecha_final(String fecha_final) {
        this.fecha_final = fecha_final;
    }

    /**
     * @return the prioridad
     */
    public String getPrioridad() {
        return prioridad;
    }

    /**
     * @param prioridad the prioridad to set
     */
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    /**
     * @return the carpeta
     */
    public String getCarpeta() {
        return carpeta;
    }

    /**
     * @param carpeta the carpeta to set
     */
    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public JsonObject guardarPublicacion() {

        String query = "INSERT INTO publicaciones(id_usuario,titulo,descripcion,fecha_alta,fecha_inicio,fecha_final,prioridad,carpeta,estado) VALUES(?,?,?,CURDATE(),?,?,?,?,?)";
        JsonObject json = new JsonObject();

        try {

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, this.getId_usuario());
            ps.setString(2, this.getTitulo());
            ps.setString(3, this.getDescripcion());
            ps.setString(4, this.getFecha_inicio());
            ps.setString(5, this.getFecha_final());
            ps.setString(6, this.getPrioridad());
            ps.setString(7, this.getCarpeta());
            ps.setString(8, this.getEstado());

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

    public JsonArray listarPublicaciones() {

        JsonArray array = new JsonArray();
        JsonObject json = new JsonObject();
        String query = "SELECT \n"
                + "p.id,\n"
                + "CONCAT(u.nombre,' ',u.paterno,' ',u.materno) as admin, \n"
                + "p.titulo,\n"
                + "p.fecha_inicio,\n"
                + "p.fecha_final,\n"
                + "p.prioridad,\n"
                + "p.estado\n"
                + "FROM publicaciones p LEFT JOIN usuarios u ON p.id_usuario = u.id\n"
                + "ORDER BY p.fecha_alta DESC,p.id DESC";

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
                    subObj.addProperty("admin", rs.getString("admin"));
                    subObj.addProperty("titulo", rs.getString("titulo"));
                    subObj.addProperty("fecha_inicio", rs.getString("fecha_inicio"));
                    subObj.addProperty("fecha_final", rs.getString("fecha_final"));
                    subObj.addProperty("prioridad", rs.getString("prioridad"));
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

    public JsonObject eliminarPulicacion() {

        String query = "DELETE FROM publicaciones WHERE id = ?";
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

    public JsonObject getPublicacion() {

        JsonObject json = new JsonObject();
        String query = "SELECT "
        + "titulo,"
        + "prioridad,"
        + "fecha_inicio,"
        + "fecha_final,"
        + "descripcion,"
        + "CONCAT(DAY(fecha_inicio),' de ',ELT( MONTH(fecha_inicio), 'Enero','Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'),' del ',YEAR(fecha_inicio)) as fecha_inicio_format,"
        + "CONCAT(DAY(fecha_final),' de ',ELT( MONTH(fecha_final), 'Enero','Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'),' del ',YEAR(fecha_final)) as fecha_final_format,"
        + "estado"
        + " FROM publicaciones WHERE id =" + this.getId();

        try {

            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (!rs.isBeforeFirst()) {

                //en caso de que no existan usuarios
                json.addProperty("estado", 201);

            } else {

                while (rs.next()) {
                    json.addProperty("titulo", rs.getString("titulo"));
                    json.addProperty("prioridad", rs.getString("prioridad"));
                    json.addProperty("fecha_inicio", rs.getString("fecha_inicio"));
                    json.addProperty("fecha_final", rs.getString("fecha_final"));
                    json.addProperty("descripcion", rs.getString("descripcion"));
                    json.addProperty("fecha_inicio_format", rs.getString("fecha_inicio_format"));
                    json.addProperty("fecha_final_format", rs.getString("fecha_final_format"));
                    json.addProperty("estado", rs.getString("estado"));
                }

                conexion.close();
            }

        } catch (SQLException e) {
            json.addProperty("estado", e.getMessage());
        }

        return json;
    }

    public JsonObject updatePublicacion() {

        String query = "UPDATE  publicaciones set titulo = ?, descripcion = ? , prioridad = ? , fecha_inicio = ? , fecha_final = ? , carpeta = ?  WHERE id =" + this.getId();
        JsonObject json = new JsonObject();

        try {

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, this.getTitulo());
            ps.setString(2, this.getDescripcion());
            ps.setString(3, this.getPrioridad());
            ps.setString(4, this.getFecha_inicio());
            ps.setString(5, this.getFecha_final());
            ps.setString(6, this.getCarpeta());

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

    public boolean buscarTitulo(String titulo) {

        boolean existe;
        String query = "SELECT * FROM publicaciones WHERE titulo ='" + titulo + "'";

        try {

            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (!rs.isBeforeFirst()) {

                existe = false;

            } else {

                existe = true;
                conexion.close();
            }

        } catch (SQLException e) {
            existe = false;
        }

        return existe;

    }

    public JsonObject obtenerTitulo(int id) {

        JsonObject json = new JsonObject();
        String query = "SELECT titulo FROM publicaciones WHERE id =" + id;

        try {

            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (!rs.isBeforeFirst()) {

                json.addProperty("estado", 201);

            } else {

                while (rs.next()) {
                    json.addProperty("titulo", rs.getString("titulo"));
                }

                json.addProperty("estado", 200);

            }

        } catch (SQLException e) {
            json.addProperty("estado", e.getMessage());
        }

        return json;

    }

    public JsonArray buscadorPublicaciones(String palabra) {

        JsonArray array = new JsonArray();
        JsonObject json = new JsonObject();
        String query = "SELECT \n"
                + "p.id,\n"
                + "CONCAT(u.nombre,' ',u.paterno,' ',u.materno) as admin, \n"
                + "p.titulo,\n"
                + "p.fecha_inicio,\n"
                + "p.fecha_final,\n"
                + "p.prioridad,\n"
                + "p.estado\n"
                + "FROM publicaciones p LEFT JOIN usuarios u ON p.id_usuario = u.id\n"
                + "WHERE p.titulo LIKE '%" + palabra + "%'  OR  p.descripcion LIKE '%" + palabra + "%' OR  p.prioridad  LIKE '%" + palabra + "%'"
                + " OR u.nombre LIKE '%" + palabra + "%' OR  u.materno LIKE '%" + palabra + "%'  OR  u.paterno LIKE '%" + palabra + "%'"
                + " ORDER BY p.fecha_alta DESC,p.id DESC";

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
                    subObj.addProperty("admin", rs.getString("admin"));
                    subObj.addProperty("titulo", rs.getString("titulo"));
                    subObj.addProperty("fecha_inicio", rs.getString("fecha_inicio"));
                    subObj.addProperty("fecha_final", rs.getString("fecha_final"));
                    subObj.addProperty("prioridad", rs.getString("prioridad"));
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

    public JsonArray listarPublicacionesHome(int id_usuario, int index, int cantidad) {

        String query = "SELECT \n"
                + "p.id,\n"
                + "u.img,\n"
                + "CONCAT(u.nombre,' ',u.paterno,' ',u.materno) as nombre,\n"
                + "CONCAT(' el ',DAY(p.fecha_alta),' de ',ELT( MONTH(p.fecha_alta), 'Enero','Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'),' del ',YEAR(p.fecha_alta)) as fecha_alta,\n"
                + "p.titulo,\n"
                + "p.descripcion,\n"
                + "p.estado,\n"
                + "IF(a.id_usuario = " + id_usuario + ",true,false) as marcada\n"
                + "FROM publicaciones p \n"
                + "LEFT JOIN usuarios u ON p.id_usuario = u.id \n"
                + "LEFT JOIN acceso_directo a ON p.id = a.id_publicacion\n"
                + "ORDER BY p.fecha_alta DESC,p.id DESC LIMIT " + index + "," + cantidad;

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
                    subObj.addProperty("img", rs.getString("img"));
                    subObj.addProperty("nombre", rs.getString("nombre"));
                    subObj.addProperty("fecha_alta", rs.getString("fecha_alta"));
                    subObj.addProperty("titulo", rs.getString("titulo"));
                    subObj.addProperty("descripcion", rs.getString("descripcion"));
                    subObj.addProperty("estatus", rs.getString("estado"));
                    subObj.addProperty("marcada", rs.getBoolean("marcada"));
                    subObj.addProperty("id_usuario", id_usuario);
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

    public JsonArray buscarPublicacionesHome(int id_usuario, String palabra) {

        String query = "SELECT \n"
                + "p.id,\n"
                + "u.img,\n"
                + "CONCAT(u.nombre,' ',u.paterno,' ',u.materno) as nombre,\n"
                + "CONCAT(' el ',DAY(p.fecha_alta),' de ',ELT( MONTH(p.fecha_alta), 'Enero','Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'),' del ',YEAR(p.fecha_alta)) as fecha_alta,\n"
                + "p.titulo,\n"
                + "p.descripcion,\n"
                + "p.estado,\n"
                + "IF(a.id_usuario = " + id_usuario + ",true,false) as marcada\n"
                + "FROM publicaciones p \n"
                + "LEFT JOIN usuarios u ON p.id_usuario = u.id \n"
                + "LEFT JOIN acceso_directo a ON p.id = a.id_publicacion\n"
                + "WHERE p.titulo LIKE '%" + palabra + "%' OR p.descripcion LIKE '%" + palabra + "%' OR  p.estado LIKE '%" + palabra + "%' OR u.nombre LIKE '%" + palabra + "%' OR u.paterno LIKE '%" + palabra + "%' OR u.materno LIKE '%" + palabra + "%' "
                + "ORDER BY p.fecha_alta DESC,p.id DESC";

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
                    subObj.addProperty("img", rs.getString("img"));
                    subObj.addProperty("nombre", rs.getString("nombre"));
                    subObj.addProperty("fecha_alta", rs.getString("fecha_alta"));
                    subObj.addProperty("titulo", rs.getString("titulo"));
                    subObj.addProperty("descripcion", rs.getString("descripcion"));
                    subObj.addProperty("estatus", rs.getString("estado"));
                    subObj.addProperty("marcada", rs.getBoolean("marcada"));
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

    public JsonArray filtrarPublicacionesHome(int id_usuario, String dato) {

        String condicion = "";

        switch (dato) {
            case "normal":
                condicion = "WHERE p.prioridad = 'normal' ORDER BY p.fecha_alta DESC,p.id DESC";
                break;

            case "urgente":
                condicion = "WHERE p.prioridad = 'urgente' ORDER BY p.fecha_alta DESC,p.id DESC";
                break;

            case "abierta":
                condicion = "WHERE p.estado = 'abierta' ORDER BY p.fecha_alta DESC,p.id DESC";
                break;

            case "cerrada":
                condicion = "WHERE p.estado = 'cerrada' ORDER BY p.fecha_alta DESC,p.id DESC";
                break;

            case "programada":
                condicion = "WHERE p.estado = 'programada' ORDER BY p.fecha_alta DESC,p.id DESC";
                break;

            case "ASC":
                condicion = "ORDER BY p.fecha_alta ASC,p.id ASC";
                break;

            case "DESC":
                condicion = "ORDER BY p.fecha_alta DESC,p.id DESC";
                break;

            default:
                condicion = "WHERE p.fecha_alta = '" + dato + "'";
                break;
        }

        String query = "SELECT \n"
                + "p.id,\n"
                + "u.img,\n"
                + "CONCAT(u.nombre,' ',u.paterno,' ',u.materno) as nombre,\n"
                + "CONCAT(' el ',DAY(p.fecha_alta),' de ',ELT( MONTH(p.fecha_alta), 'Enero','Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'),' del ',YEAR(p.fecha_alta)) as fecha_alta,\n"
                + "p.titulo,\n"
                + "p.descripcion,\n"
                + "p.estado,\n"
                + "IF(a.id_usuario = " + id_usuario + ",true,false) as marcada\n"
                + "FROM publicaciones p \n"
                + "LEFT JOIN usuarios u ON p.id_usuario = u.id \n"
                + "LEFT JOIN acceso_directo a ON p.id = a.id_publicacion\n"
                + condicion;

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
                    subObj.addProperty("img", rs.getString("img"));
                    subObj.addProperty("nombre", rs.getString("nombre"));
                    subObj.addProperty("fecha_alta", rs.getString("fecha_alta"));
                    subObj.addProperty("titulo", rs.getString("titulo"));
                    subObj.addProperty("descripcion", rs.getString("descripcion"));
                    subObj.addProperty("estatus", rs.getString("estado"));
                    subObj.addProperty("marcada", rs.getBoolean("marcada"));
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

    public JsonObject buscarPublicacionPorId(int id) {

        String query = "";

        JsonObject json = new JsonObject();

        try {

            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (!rs.isBeforeFirst()) {

                //en caso de que no existan usuarios
                json.addProperty("estado", 201);

            } else {

                while (rs.next()) {
                    json.addProperty("id", rs.getInt("id"));
                    json.addProperty("img", rs.getString("img"));
                    json.addProperty("nombre", rs.getString("nombre"));
                    json.addProperty("fecha_alta", rs.getString("fecha_alta"));
                    json.addProperty("titulo", rs.getString("titulo"));
                    json.addProperty("descripcion", rs.getString("descripcion"));
                    json.addProperty("estatus", rs.getString("estado"));
                    json.addProperty("marcada", rs.getBoolean("marcada"));
                }

                conexion.close();
            }

        } catch (SQLException e) {
            json.addProperty("estado", e.getMessage());
        }

        return json;
    }
    
    public JsonObject cerrarPublicacion() {

        String query = "UPDATE  publicaciones set estado = ?  WHERE id =" + this.getId();
        JsonObject json = new JsonObject();

        try {

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, this.getEstado());

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
