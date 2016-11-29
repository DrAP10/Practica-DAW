/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uhu.daw.practica5a;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author usuario
 */
@WebServlet(name = "Controlador", urlPatterns = {"/Controlador"})
public class Controlador extends HttpServlet {
    @Resource(name="javaWebApp1pool)")
    private DataSource javaWebApp1Pool;

    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        String method = request.getParameter("_method");
        switch(method){
            case "create":
                insert(request, response);
                break;
            case "delete":
                delete(request, response);
                break;
            case "read":
                read(request, response);
                break;
            case "edit":
                edit(request, response);
                break;
            case "update":
                update(request, response);
                break;
            default:
                    response.setContentType("text/html;charset=UTF-8");
                    try (PrintWriter out = response.getWriter()) {
                        /* TODO output your page here. You may use following sample code. */
                        out.println("<!DOCTYPE html>");
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<title>App Web Práctica 5.a)</title>");
                        out.println("</head>");
                        out.println("<body>");
                        out.println("<h1>DAW - Práctica 5.a) Servlets y Acceso a Datos mediante un Pool de conexiones</h1>");
                        out.println("<h2>Metodo no valido</h2>");
                        out.println("<p><a href=\"index.html\">Volver</a>");
                        out.println("</body>");
                        out.println("</html>");
                    }
                    break;
                    
        }      
    }

    private void insert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Guardar mensaje sobre estado del resultado
        String msg;
        // Crear un Objeto usuario
        Usuario user;
        // Creamos las variables para la conexión, la sentencia y el resultado y asignar sus campos con los valores leídos
        Connection conn;
        PreparedStatement ps;
        int filasAfectadas = 0;
 
        try {
           
            // Leer los parámetros enviados desde el formulario
            String dni = request.getParameter("dni");
            String nombre = request.getParameter("nombre");
            int edad = Integer.parseInt(request.getParameter("edad"));
            String apellidos = request.getParameter("apellidos");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("direccion");
 
            // Es necesario realizar un control sobre los valores válidos de los parámetros. Aquí un ejemplo
            if (edad<0) throw new NumberFormatException();
 
            user = new Usuario();
            user.setDni(dni);
            user.setNombre(nombre);
            user.setApellidos(apellidos);
            user.setEdad(edad);
            user.setDireccion(direccion);
            user.setEmail(email);
            user.setTelefono(telefono);
            
            // establecer la conexión
            Context c = new InitialContext();
            javaWebApp1Pool = (DataSource) c.lookup("jdbc/javaWebApp1");
            conn = javaWebApp1Pool.getConnection();
 
            // Preparar la sentencia SQL a realizar
            ps = conn.prepareStatement("INSERT INTO USUARIOS VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, user.getDni());
            ps.setString(2, user.getNombre());
            ps.setString(3, user.getApellidos());
            ps.setInt(4, user.getEdad());
            ps.setString(5, user.getDireccion());
            ps.setString(6, user.getTelefono());
            ps.setString(7, user.getEmail());
 
            // Ejecutar instrucción SQL y guardar resultado en msg
            filasAfectadas = ps.executeUpdate();
            System.out.println(ps);
            if (filasAfectadas > 0) {
                msg = "<p>OK: Inserción realizada correctamente</p>";
            } else {
                msg = "<p>ERROR: Ha fallado la Inserción</p>";
            }
 
            ps.close();
            conn.close();
 
        } catch (NamingException ex) {
            msg = "<p>ERROR: Recurso no disponible, "+ex+"</p>";
            System.out.println(ex);
        } catch (SQLException ex) {
            msg = "<p>ERROR: Base de Datos no disponible</p>";
            System.out.println(ex);
        }  catch (NumberFormatException ex) {
            msg = "<p>ERROR: Parámetros no Válidos</p>";
            System.out.println(ex);
        }
        // Implementar la respuesta HTML
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>App Web Práctica 5.a)</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>DAW - Práctica 5.a) Servlets y Acceso a Datos mediante un Pool de conexiones</h1>");
            out.println("<h2>Estado de la inserción</h2>");
            out.println(msg);
            out.println("<p><a href=\"index.html\">Volver</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Guardar mensaje sobre estado del resultado
        String msg;
        // Crear un Objeto usuario
        Usuario user;
        // Creamos las variables para la conexión, la sentencia y el resultado y asignar sus campos con los valores leídos
        Connection conn;
        PreparedStatement ps;
        int filasAfectadas = 0;
 
        try {
           
            // Leer los parámetros enviados desde el formulario
            // int id = Integer.parseInt(request.getParameter("id"));
            String dni = request.getParameter("dni");
 
            user = new Usuario();
            user.setDni(dni);
            
            // establecer la conexión
            Context c = new InitialContext();
            javaWebApp1Pool = (DataSource) c.lookup("jdbc/javaWebApp1");
            conn = javaWebApp1Pool.getConnection();
 
            // Preparar la sentencia SQL a realizar
            ps = conn.prepareStatement("DELETE FROM USUARIOS WHERE dni = ?");
            ps.setString(1, user.getDni());
 
            // Ejecutar instrucción SQL y guardar resultado en msg
            filasAfectadas = ps.executeUpdate();
            System.out.println(ps);
            if (filasAfectadas > 0) {
                msg = "<p>OK: Borrado realizada correctamente</p>";
            } else {
                msg = "<p>ERROR: Ha fallado el borrado</p>";
            }
 
            ps.close();
            conn.close();
 
        } catch (NamingException ex) {
            msg = "<p>ERROR: Recurso no disponible, "+ex+"</p>";
            System.out.println(ex);
        } catch (SQLException ex) {
            msg = "<p>ERROR: Base de Datos no disponible</p>";
            System.out.println(ex);
        }  catch (NumberFormatException ex) {
            msg = "<p>ERROR: Parámetros no Válidos</p>";
            System.out.println(ex);
        }
        // Implementar la respuesta HTML
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>App Web Práctica 5.a)</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>DAW - Práctica 5.a) Servlets y Acceso a Datos mediante un Pool de conexiones</h1>");
            out.println("<h2>Estado del borrado</h2>");
            out.println(msg);
            out.println("<p><a href=\"index.html\">Volver</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void read(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        // Guardar mensaje sobre estado del resultado
        String msg;
        // Crear un Objeto usuario
        Usuario user = null;
        // Creamos las variables para la conexión, la sentencia y el resultado y asignar sus campos con los valores leídos
        Connection conn;
        PreparedStatement ps;
        try {
           
            // Leer los parámetros enviados desde el formulario
            String dni = request.getParameter("dni");
 
            user = new Usuario();
            user.setDni(dni);
            
            // establecer la conexión
            Context c = new InitialContext();
            javaWebApp1Pool = (DataSource) c.lookup("jdbc/javaWebApp1");
            conn = javaWebApp1Pool.getConnection();
 
            // Preparar la sentencia SQL a realizar
            ps = conn.prepareStatement("SELECT * FROM USUARIOS WHERE dni = ?");
            ps.setString(1, user.getDni());
 
            // Ejecutar instrucción SQL y guardar resultado en msg
            ResultSet rs = ps.executeQuery();
            System.out.println(ps);
            if (rs.next()) {
                user.setNombre(rs.getString("nombre"));
                user.setApellidos(rs.getString("apellidos"));
                user.setEdad(rs.getInt("edad"));
                user.setDireccion(rs.getString("direccion"));
                user.setEmail(rs.getString("email"));
                user.setTelefono(rs.getString("telefono"));
                msg = "<p>Nombre: "+user.getNombre()+"<br>"
                        +"Apellidos: "+user.getApellidos()+"<br>"
                        +"Edad: "+user.getEdad()+"<br>"
                        +"Direccion: "+user.getDireccion()+"<br>"
                        +"Teléfono: "+user.getTelefono()+"<br>"
                        +"Email: "+user.getEmail()+"<br>"
                        +"</p>";
            } else {
                msg = "<p>ERROR: Ha fallado el ver el usuario "+user.getDni()+"</p>";
            }
 
            ps.close();
            conn.close();
 
        } catch (NamingException ex) {
            msg = "<p>ERROR: Recurso no disponible, "+ex+"</p>";
            System.out.println(ex);
        } catch (SQLException ex) {
            msg = "<p>ERROR: Base de Datos no disponible</p>";
            System.out.println(ex);
        }  catch (NumberFormatException ex) {
            msg = "<p>ERROR: Parámetros no Válidos</p>";
            System.out.println(ex);
        }
        // Implementar la respuesta HTML
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>App Web Práctica 5.a)</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>DAW - Práctica 5.a) Servlets y Acceso a Datos mediante un Pool de conexiones</h1>");
            out.println(msg);
            out.println("<p><a href=\"index.html\">Volver</a>");
            if(user!=null)
                out.println("<a href=\"Controlador?_method=edit&dni="+user.getDni()+"\">Actualizar</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void edit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        // Guardar mensaje sobre estado del resultado
        String msg;
        // Crear un Objeto usuario
        Usuario user = null;
        // Creamos las variables para la conexión, la sentencia y el resultado y asignar sus campos con los valores leídos
        Connection conn;
        PreparedStatement ps;
        try {
           
            // Leer los parámetros
            String dni = request.getParameter("dni");
            user = new Usuario();
            user.setDni(dni);
            
            // establecer la conexión
            Context c = new InitialContext();
            javaWebApp1Pool = (DataSource) c.lookup("jdbc/javaWebApp1");
            conn = javaWebApp1Pool.getConnection();
 
            // Preparar la sentencia SQL a realizar
            ps = conn.prepareStatement("SELECT * FROM USUARIOS WHERE dni = ?");
            ps.setString(1, user.getDni());
 
            // Ejecutar instrucción SQL y guardar resultado en msg
            ResultSet rs = ps.executeQuery();
            System.out.println(ps);
            if (rs.next()) {
                user.setNombre(rs.getString("nombre"));
                user.setApellidos(rs.getString("apellidos"));
                user.setEdad(rs.getInt("edad"));
                user.setDireccion(rs.getString("direccion"));
                user.setEmail(rs.getString("email"));
                user.setTelefono(rs.getString("telefono"));
                /*msg = "<p>Nombre: "+user.getNombre()+"<br>"
                        +"Apellidos: "+user.getApellidos()+"<br>"
                        +"Edad: "+user.getEdad()+"<br>"
                        +"Direccion: "+user.getDireccion()+"<br>"
                        +"Teléfono: "+user.getTelefono()+"<br>"
                        +"Email: "+user.getEmail()+"<br>"
                        +"</p>";*/
                msg = "<form id=\"form1\" action=\"Controlador\" method=\"post\" onsubmit=\"return validar(this);\">\n" +
"            <input type=\"hidden\" name=\"_method\" value=\"update\" />\n" +
"            <input type=\"hidden\" name=\"dni\" value=\""+user.getDni()+"\" />\n" +
"            <table>\n" +
"                <tr>\n" +
"                    <td><label for=\"nombre\">Nombre</label></td>\n" +
"                    <td><input id=\"nombre\" type=\"text\" name=\"nombre\" value=\""+user.getNombre()+"\" required /></td>                    \n" +
"                </tr>\n" +
"                <tr>\n" +
"                    <td><label for=\"apellidos\">Apellidos</label></td>\n" +
"                    <td><input id=\"apellidos\" type=\"text\" name=\"apellidos\" value=\""+user.getApellidos()+"\" required /></td>                    \n" +
"                </tr>\n" +
"                <tr>\n" +
"                    <td><label for=\"edad\">Edad</label></td>\n" +
"                    <td><input id=\"edad\" type=\"number\" name=\"edad\" value=\""+user.getEdad()+"\"/></td>\n" +
"                </tr>\n" +
"                <tr>\n" +
"                    <td><label for=\"direccion\">Direccion</label></td>\n" +
"                    <td><input id=\"direccion\" type=\"text\" name=\"direccion\" value=\""+user.getDireccion()+"\"/></td>                    \n" +
"                </tr>\n" +
"                <tr>\n" +
"                    <td><label for=\"email\">E-mail</label></td>\n" +
"                    <td><input id=\"email\" type=\"text\" name=\"email\" value=\""+user.getEmail()+"\"/></td>                    \n" +
"                </tr>\n" +
"                <tr>\n" +
"                    <td><label for=\"telefono\">Telefono</label></td>\n" +
"                    <td><input id=\"telefono\" type=\"text\" name=\"telefono\" value=\""+user.getTelefono()+"\"/></td>                    \n" +
"                </tr>\n" +
"                <tr>\n" +
"                    <td></td>\n" +
"                    <td><input type=\"submit\" value=\"Actualizar\" /></td>\n" +
"                </tr>\n" +
"            </table>\n" +
"        </form>";
            } else {
                msg = "<p>ERROR: Ha fallado al actualizar el usuario "+user.getDni()+"</p>";
            }
 
            ps.close();
            conn.close();
 
        } catch (NamingException ex) {
            msg = "<p>ERROR: Recurso no disponible, "+ex+"</p>";
            System.out.println(ex);
        } catch (SQLException ex) {
            msg = "<p>ERROR: Base de Datos no disponible</p>";
            System.out.println(ex);
        }  catch (NumberFormatException ex) {
            msg = "<p>ERROR: Parámetros no Válidos</p>";
            System.out.println(ex);
        }
        // Implementar la respuesta HTML
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>App Web Práctica 5.a)</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>DAW - Práctica 5.a) Servlets y Acceso a Datos mediante un Pool de conexiones</h1>");
            out.println(msg);
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Guardar mensaje sobre estado del resultado
        String msg;
        // Crear un Objeto usuario
        Usuario user;
        // Creamos las variables para la conexión, la sentencia y el resultado y asignar sus campos con los valores leídos
        Connection conn;
        PreparedStatement ps;
        int filasAfectadas = 0;
 
        try {
           
            // Leer los parámetros enviados desde el formulario
            String dni = request.getParameter("dni");
            String nombre = request.getParameter("nombre");
            int edad = Integer.parseInt(request.getParameter("edad"));
            String apellidos = request.getParameter("apellidos");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("direccion");
 
            // Es necesario realizar un control sobre los valores válidos de los parámetros. Aquí un ejemplo
            if (edad<0) throw new NumberFormatException();
 
            user = new Usuario();
            user.setDni(dni);
            user.setNombre(nombre);
            user.setApellidos(apellidos);
            user.setEdad(edad);
            user.setDireccion(direccion);
            user.setEmail(email);
            user.setTelefono(telefono);
            
            // establecer la conexión
            Context c = new InitialContext();
            javaWebApp1Pool = (DataSource) c.lookup("jdbc/javaWebApp1");
            conn = javaWebApp1Pool.getConnection();
 
            // Preparar la sentencia SQL a realizar
            ps = conn.prepareStatement("UPDATE USUARIOS SET nombre=?, "
                    +"apellidos=?, edad=?, direccion=?, telefono=?, email=? WHERE dni=?");
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getApellidos());
            ps.setInt(3, user.getEdad());
            ps.setString(4, user.getDireccion());
            ps.setString(5, user.getTelefono());
            ps.setString(6, user.getEmail());
            ps.setString(7, user.getDni());
 
            // Ejecutar instrucción SQL y guardar resultado en msg
            filasAfectadas = ps.executeUpdate();
            System.out.println(ps);
            if (filasAfectadas > 0) {
                msg = "<p>OK: Actualizacion realizada correctamente</p>";
            } else {
                msg = "<p>ERROR: Ha fallado la Inserción</p>";
            }
 
            ps.close();
            conn.close();
 
        } catch (NamingException ex) {
            msg = "<p>ERROR: Recurso no disponible, "+ex+"</p>";
            System.out.println(ex);
        } catch (SQLException ex) {
            msg = "<p>ERROR: Base de Datos no disponible</p>";
            System.out.println(ex);
        }  catch (NumberFormatException ex) {
            msg = "<p>ERROR: Parámetros no Válidos</p>";
            System.out.println(ex);
        }
        // Implementar la respuesta HTML
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>App Web Práctica 5.a)</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>DAW - Práctica 5.a) Servlets y Acceso a Datos mediante un Pool de conexiones</h1>");
            out.println("<h2>Estado de la actualización</h2>");
            out.println(msg);
            out.println("<p><a href=\"index.html\">Volver</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
