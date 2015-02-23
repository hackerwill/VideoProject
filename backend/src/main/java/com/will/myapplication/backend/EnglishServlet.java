package com.will.myapplication.backend;

import java.io.*;
import java.sql.*;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.google.appengine.api.utils.SystemProperty;

/**
 * Created by Will.wc.Hsu on 2015/2/22.
 */
public class EnglishServlet extends HttpServlet{
    private static final Logger log = Logger.getLogger(EnglishServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = null;
        try {
            if (SystemProperty.environment.value() ==
                    SystemProperty.Environment.Value.Production) {
                Class.forName("com.mysql.jdbc.GoogleDriver");
            } else {

                Class.forName("com.mysql.jdbc.Driver");
            }
            url = "jdbc:google:mysql://metal-appliance-864:cnnstudentnews/database";

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        PrintWriter out = resp.getWriter();
        try {
            Connection conn = DriverManager.getConnection(
                    url, "will", "P@ssw0rd");
            try {
                String statement = "SELECT NAME FROM member";
                PreparedStatement stmt = conn.prepareStatement(statement);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    String name = rs.getString("name");
                    log.info("name = " + name);
                }

            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.setHeader("Refresh", "3; url=/guestbook.jsp");
    }
}
