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
            String nombre = request.getParameter("nombre");
            int edad = Integer.parseInt(request.getParameter("edad"));
 
            // Es necesario realizar un control sobre los valores válidos de los parámetros. Aquí un ejemplo
            if (edad<0) throw new NumberFormatException();
 
            user = new Usuario();
           // user.setId(id);
            user.setNombre(nombre);
            user.setEdad(edad);
            
            // establecer la conexión
            Context c = new InitialContext();
            javaWebApp1Pool = (DataSource) c.lookup("jdbc/javaWebApp1");
            conn = javaWebApp1Pool.getConnection();
 
            // Preparar la sentencia SQL a realizar
            ps = conn.prepareStatement("INSERT INTO USUARIOS (nombre, edad) VALUES (?, ?)");
            //ps.setInt(1, user.getId());
            ps.setString(1, user.getNombre());
            ps.setInt(2, user.getEdad());
 
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
