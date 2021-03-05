import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SimpleFormSearch")
public class SimpleFormSearch extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SimpleFormSearch() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  String searchby = request.getParameter("Search");
	  String keyword = request.getParameter("keyword");
      search(keyword, searchby, response);
   }

   void search(String keyword, String searchby, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Database Result";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         DBConnection.getDBConnection();
         connection = DBConnection.connection;

         if (keyword.isEmpty()) {
            String selectSQL = "SELECT * FROM bookshelf";
            preparedStatement = connection.prepareStatement(selectSQL);
         } 
         else {
        	 if(searchby.equals("Title"))
        	 {
        		 String selectSQL = "SELECT * FROM bookshelf WHERE TITLE LIKE ?";
        		 String theTitle = "%" + keyword + "%";
        		 preparedStatement = connection.prepareStatement(selectSQL);
        		 preparedStatement.setString(1, theTitle);
        	 }
        	 else if(searchby.equals("Name"))
        	 {
        		 String selectSQL = "SELECT * FROM bookshelf WHERE FIRSTNAME LIKE ? OR LASTNAME LIKE ?";
        		 String theAuthor = "%" + keyword + "%";
        		 preparedStatement = connection.prepareStatement(selectSQL);
        		 preparedStatement.setString(1, theAuthor);
        		 preparedStatement.setString(2, theAuthor);
        	 }
        	 else
        	 {
        		 String selectSQL = "SELECT * FROM bookshelf WHERE ISBN LIKE ?";
        		 String theISBN = "%" + keyword + "%";
        		 preparedStatement = connection.prepareStatement(selectSQL);
        		 preparedStatement.setString(1, theISBN);
        	 }
         }
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            int id = rs.getInt("id");
            String bookTitle = rs.getString("title").trim();
            String firstname = rs.getString("firstname").trim();
            String lastname = rs.getString("lastname").trim();
            String ISBN = rs.getString("ISBN").trim();
            String publisher = rs.getString("publisher").trim();
            
            out.println("ID: " + id + ", ");
            out.println("Title: " + bookTitle + ", ");
            out.println("Author: " + lastname + ", "+ firstname + ", ");
            out.println("ISBN: " + ISBN + ", ");
            out.println("Publisher: " + publisher + "<br>");
            
         }
         out.println("<a href=/Tech_Exercise/SimpleFormSearch.html>Search Data</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         connection.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (preparedStatement != null)
               preparedStatement.close();
         } catch (SQLException se2) {
         }
         try {
            if (connection != null)
               connection.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
