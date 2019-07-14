package com.jeffery.holmes.demo.callee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CalleeDao {

    @Autowired(required = false)
    private Connection connection;

    public JSONArray getCars(Integer page, Integer pageSize) throws SQLException, IOException {
        if (connection == null) {
            throw new IOException("Could not connect to database!");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("select * from car_info limit ? offset ?");
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        preparedStatement.setInt(1, pageSize);
        preparedStatement.setInt(2, (page - 1) * pageSize);
        ResultSet resultSet = preparedStatement.executeQuery();
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", resultSet.getLong("id"));
            jsonObject.put("category", resultSet.getString("car_category"));
            jsonObject.put("type", resultSet.getString("car_type"));
            jsonObject.put("price", resultSet.getString("lowest_price") + "-" + resultSet.getString("highest_price"));
            jsonArray.add(jsonObject);
        }
        preparedStatement.close();
        return jsonArray;
    }

}
