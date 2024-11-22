package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.json.JSONObject;
import com.connection.MyConnectionClass;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/update")
public class UpdateController extends HttpServlet {

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
//	      set response type to JSON
			resp.setContentType("application/json");

			// fetching data from HttpServletRequest's object
			BufferedReader reader = req.getReader();

			// creating a Builder object to avoid multiple object creation and String line
			// for holding the data which is come from client/server/JSON
			String line;
			StringBuilder sb = new StringBuilder();

			// Reading incoming JSON Data
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			String myData = sb.toString();

			JSONObject jsonObject = new JSONObject(myData);
			String userEmail = jsonObject.getString("email"); // Use the email to identify the user to update
			String newName = jsonObject.getString("name");
			String newPhone = jsonObject.getString("phoneNumber");
			String newPassword = jsonObject.getString("password");

			// Database query to update the user data
			String query = "UPDATE userdetails SET name=?,phoneNumber=?,password=? WHERE email=?";

			// Establish dataBase Connection calling GetConnection method from
			// MyConnectionClass
			Connection readyConnection = MyConnectionClass.GetConnection();
			PreparedStatement statement = readyConnection.prepareStatement(query);

			// Set the parameters for the prepared statement
			statement.setString(1, newName);
			statement.setString(2, newPhone);
			statement.setString(3, newPassword);
			statement.setString(4, userEmail);

			// Execute the update
			int rowsUpdate = statement.executeUpdate();

			// Check if the update was successful
			JSONObject responseJson = new JSONObject();
			if (rowsUpdate > 0) {
				responseJson.put("message", "User data  Update Successfully!");
				resp.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseJson.put("message", "User not found or no changes made");
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			// Send the response back to the client
			resp.getWriter().write(responseJson.toString());

		} catch (Exception e) {
			// Handle SQL Exception
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("message", "Failed to update user data");
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write(jsonObject.toString());

		}

	}

}
