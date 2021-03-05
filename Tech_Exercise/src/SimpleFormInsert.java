
/**
 * @file SimpleFormInsert.java
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SimpleFormInsert")
public class SimpleFormInsert extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SimpleFormInsert() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String booktitle = request.getParameter("title");
      String firstname = request.getParameter("firstname");
      String lastname = request.getParameter("lastname");
      String isbn = request.getParameter("isbn");
      String publisher = request.getParameter("publisher");

      Connection connection = null;
      String insertSql = " INSERT INTO bookshelf (id, TITLE, FIRSTNAME, LASTNAME, ISBN, PUBLISHER) values (default, ?, ?, ?, ?, ?)";

      try {
         DBConnection.getDBConnection();
         connection = DBConnection.connection;
         PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
         preparedStmt.setString(1, booktitle);
         preparedStmt.setString(2, firstname);
         preparedStmt.setString(3, lastname);
         preparedStmt.setString(4, isbn);
         preparedStmt.setString(5, publisher);
         preparedStmt.execute();
         connection.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

      // Set response content type
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Add a new book to the database";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h2 align=\"center\">" + title + "</h2>\n" + //
            "<ul>\n" + //

            "  <li><b>Title</b>: " + booktitle + "\n" + //
            "  <li><b>Author First Name</b>: " + firstname + "\n" + //
            "  <li><b>Author Last Name</b>: " + lastname + "\n" + //
            "  <li><b>ISBN</b>: " + isbn + "\n" + //
            "  <li><b>Publisher</b>: " + publisher + "\n" + //
            "</ul>\n");

      out.println("<a href=/Tech_Exercise/SimpleFormSearch.html>Search Data</a> <br>");
      out.println("</body></html>");
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
