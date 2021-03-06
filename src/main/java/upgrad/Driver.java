package upgrad;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Driver {

    /**
     * Driver class main method
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        // MySql credentials

          String dbhost = "jdbc:mysql://pgc-sd-bigdata.cyaielc9bmnf.us-east-1.rds.amazonaws.com:3306/pgcdata";
          String username = "student";
          String password = "STUDENT123";
          Connection conn = null;
          Statement statement ;
      
        // MongoDB Configurations
        MongoClient mongoClient = MongoClients.create("mongodb://ec2-3-237-172-185.compute-1.amazonaws.com");
        System.out.println(mongoClient);

        MongoDatabase db=mongoClient.getDatabase("upgrad");
        MongoCollection<Document> collection=db.getCollection("products");

        // Connection Default Value Initialization
        Connection sqlConnection = null;
        MongoClient mongoClient1 = null;

        try {
            // Creating database connections
            conn = DriverManager.getConnection(
                    dbhost, username, password);
            if (conn != null) {
                System.out.println("Connection Successful! Enjoy. Now it's time to push data");
            }

            System.out.println("Collection: " + collection);

            // Import data into MongoDb
             statement=conn.createStatement();

          ResultSet resultSet = statement.executeQuery("select * from mobiles");
          importDataToMongoDB(resultSet,collection,0);
          ResultSet resultSet1 = statement.executeQuery("select * from cameras");
          importDataToMongoDB(resultSet1,collection,2);
          ResultSet resultSet2 = statement.executeQuery("select * from headphones");
          importDataToMongoDB(resultSet2,collection,1);

    // List all products in the inventory
    CRUDHelper.displayAllProducts(collection);

            // Display top 5 Mobiles
         CRUDHelper.displayTop5Mobiles(collection);

            // Display products ordered by their categories in Descending Order Without autogenerated Id
            CRUDHelper.displayCategoryOrderedProductsDescending(collection);

            // Display product count in each category
            CRUDHelper.displayProductCountByCategory(collection);

            // Display wired headphones
            CRUDHelper.displayWiredHeadphones(collection);
        }
        catch(Exception ex) {
            System.out.println("Got Exception.");
            ex.printStackTrace();
        }
        finally {
            // Close Connections
       conn.close();
       mongoClient.close();
        }
    }


    public static void importDataToMongoDB(ResultSet resultSet,MongoCollection<Document> collection, int flag) throws SQLException {
       try{
           int columnCount = -1, count = 0;

        List<Document> documents = new ArrayList<Document>();
        while (resultSet.next()) {
            if (columnCount == -1) {
                columnCount = resultSet.getMetaData().getColumnCount();
            }
            Document document = new Document();
            for (int i = 1; i <= columnCount; i ++) {
                String columnName = resultSet.getMetaData().getColumnName(i);
                Object value = resultSet.getObject(i);
                document.append(columnName, value);

            }
 if(flag==0) {
           document.append("Category", "Mobile");
       }
       else if(flag==1) {
           document.append("Category", "Headphones");
       }
       else  {
           document.append("Category", "Cameras");
       }


            documents.add(document);

            count ++;

            //if (count% 1000 == 0) {// Insert every 1000 pieces of data
            collection.insertMany(documents);
            documents = new ArrayList<Document>();

            System.out.println("transfer " + count + " lines from mysql to mongodb  ...");

        }
        System.out.println("total transfer " + count + " lines!");
    }
       catch(Exception ex) {
           System.out.println("Got Exception.");
           ex.printStackTrace();

        }
    }
}