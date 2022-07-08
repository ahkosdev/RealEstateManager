package fr.kosdev.realestatemanager.Database;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteProgram;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.ArrayList;
import java.util.List;

import fr.kosdev.realestatemanager.Models.Property;

public class PropertySimpleSqliteQuery{

    String select = new String();
    List<Object> conditions ;
    boolean containsCondition = false;
    private PropertyDao mPropertyDao;
    String type;
    String minPrice;
    String maxPrice;



    public SimpleSQLiteQuery simpleSqliteQuery(String query, List<Object> objects){
        conditions = new ArrayList<>();
        select = query;
        conditions = objects;

        select += "SELECT * FROM Property";

        if (minPrice != null ){
            select += "WHERE";
            select += " price >=";
            conditions.add(minPrice);
            containsCondition = true;
        }
        if (maxPrice != null){
            if (containsCondition){
                select += "AND";
            }else {
                select += "WHERE";
                containsCondition = true;
            }
            select += "price <=";
            conditions.add(maxPrice);
        }
        select += ";";

        SimpleSQLiteQuery newQuery = new SimpleSQLiteQuery(select, conditions.toArray());
        return newQuery;
        //LiveData<List<Property>> result = mPropertyDao.getPropertiesWithFilter(newQuery);
    }
}
