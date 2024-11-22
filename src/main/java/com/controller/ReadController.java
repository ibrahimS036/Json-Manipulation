package com.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;
import com.connection.MyConnectionClass;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/read")
public class ReadController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			resp.setContentType("application/json");

			// Creating a Json Array to hold the user data
			JSONArray jsonArray = new JSONArray();

			// Query to fetch data from the DB
			String query = "select * from userdetails";
			Connection conn = MyConnectionClass.GetConnection();
			PreparedStatement statement = conn.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();

			// Loop through the result set and create JSON objects for each user
			while (resultSet.next()) {
				JSONObject userJson = new JSONObject();
				userJson.put("name", resultSet.getString("name"));
				userJson.put("email", resultSet.getString("email"));
				userJson.put("phone", resultSet.getString("phoneNumber"));
				userJson.put("password", resultSet.getString("password"));

				// Add the user JSON object to the users array
				jsonArray.put(userJson);

				// Write the JSON array as the response
				resp.getWriter().write(jsonArray.toString());

			}

		} catch (Exception e) {
			// Handle any errors
			e.printStackTrace();

			// If an error occurs, return an error message in JSON format
			JSONObject errorJson = new JSONObject();
			errorJson.put("message", "Failed to fetch user data");
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Set the HTTP error code
			resp.getWriter().write(errorJson.toString());
		}

	}

}
