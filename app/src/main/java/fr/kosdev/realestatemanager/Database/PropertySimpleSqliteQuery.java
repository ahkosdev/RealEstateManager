package fr.kosdev.realestatemanager.Database;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteProgram;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.kosdev.realestatemanager.Models.Property;

public class PropertySimpleSqliteQuery{

    String select ;
    List<Object> conditions ;
    boolean containsCondition = false;
    private PropertyDao mPropertyDao;
    String type;



    public SimpleSQLiteQuery simpleSqliteQuery(String minPrice, String maxPrice,String numberOfRooms, String minSurface, String maxSurface, String searchType,
                                               String searchAddress, String searchDate){
        conditions = new ArrayList<>();
        select = "";

        select += "SELECT * FROM Property";

        if (!minPrice.isEmpty() ){
            select += " WHERE";
            select += " price >= ?";
            conditions.add(Integer.parseInt(minPrice));
            containsCondition = true;
        }
        if (!maxPrice.isEmpty()){
            if (containsCondition){
                select += " AND";
            }else {
                select += " WHERE";
                containsCondition = true;
            }
            select += " price <= ?";
            conditions.add(Integer.parseInt(maxPrice));
        }
        if (!numberOfRooms.isEmpty()){
            if (containsCondition){
                select += " AND";
            }else {
                select += " WHERE";
                containsCondition = true;
            }
            select += " numberOfRooms == ?";
            conditions.add(Integer.parseInt(numberOfRooms));
        }
        if (!minSurface.isEmpty()){
            if (containsCondition){
                select += " AND";
            }else {
                select += " WHERE";
                containsCondition = true;
            }
            select += " surfaceOfProperty >= ?";
            conditions.add(Integer.parseInt(minSurface));
        }
        if (!maxSurface.isEmpty()){
            if (containsCondition){
                select += " AND";
            }else {
                select += " WHERE";
                containsCondition = true;
            }
            select += " surfaceOfProperty <= ?";
            conditions.add(Integer.parseInt(maxSurface));
        }
        if (!searchType.isEmpty()){
            if (containsCondition){
                select += " AND";
            }else {
                select += " WHERE";
                containsCondition = true;
            }
            select += " type == ?";
            conditions.add(searchType);
        }
        if (!searchAddress.isEmpty()){
            if (containsCondition){
                select += " AND";
            }else {
                select += " WHERE";
                containsCondition = true;
            }
            select += " address LIKE %?%";
            conditions.add(searchAddress);

        }


        if (!searchDate.isEmpty()){
            if (containsCondition){
                select += " AND";
            }else {
                select += " WHERE";
                containsCondition = true;
            }
            select += " dateOfEntry <= ?";
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            try {
                date = df.parse(searchDate);
            }catch (ParseException e){
                e.printStackTrace();
            }
            conditions.add(date.getDate());
        }
        select += ";";

        SimpleSQLiteQuery newQuery = new SimpleSQLiteQuery(select, conditions.toArray());
        return newQuery;
        //LiveData<List<Property>> result = mPropertyDao.getPropertiesWithFilter(newQuery);
    }
}
