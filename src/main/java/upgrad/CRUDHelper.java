package upgrad;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import javax.print.Doc;
import java.sql.*;
import java.util.Arrays;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Sorts.descending;
import static upgrad.PrintHelper.*;


public class CRUDHelper {

    /**
     * Display ALl products
     * @param collection
     */
    public static void displayAllProducts(MongoCollection<Document> collection) {
        System.out.println("------ Displaying All Products ------");
        // Call printSingleCommonAttributes to display the attributes on the Screen
        for (Document document : collection.find()) {
            printSingleCommonAttributes(document);
        }

    }

    /**
     * Display top 5 Mobiles
     * @param collection
     */

    public static void displayTop5Mobiles(MongoCollection<Document> collection) {
        System.out.println("------ Displaying Top 5 Mobiles ------");
        // Call printAllAttributes to display the attributes on the Screen
        for (Document document : collection.find(eq("Category", "Mobile")).limit(5)) {
            printSingleCommonAttributes(document);
        }
    }

    /**
     * Display products ordered by their categories in Descending order without auto generated Id
     * @param collection
     */
    public static void displayCategoryOrderedProductsDescending(MongoCollection<Document> collection) {
        System.out.println("------ Displaying Products ordered by categories ------");
        // Call printAllAttributes to display the attributes on the Screen
        for (Document document1 : collection.find().sort(descending("Category"))) {
            printSingleCommonAttributes(document1);
        }

    }


    /**
     * Display number of products in each group
     * @param collection
     */
    public static void displayProductCountByCategory(MongoCollection<Document> collection) {
        System.out.println("------ Displaying Product Count by categories ------");
        // Call printProductCountInCategory to display the attributes on the Screen
        for (Document document : collection.aggregate(Arrays.asList(Aggregates.group("$Category", Accumulators.sum("Count", 1))))){
            printProductCountInCategory(document);
        }
    }

    /**
     * Display Wired Headphones
     * @param collection
     */
    public static void displayWiredHeadphones(MongoCollection<Document> collection) {
        System.out.println("------ Displaying Wired headphones ------");
        // Call printAllAttributes to display the attributes on the Screen
        for (Document document : collection.find(eq("ConnectorType", "Wired"))) {
            printAllAttributes(document);
        }

    }
}