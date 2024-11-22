package com.controller;

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

@WebServlet("/delete")
public class DeleteController extends HttpServlet {

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {

			// Set response type to JSON
			resp.setContentType("application/json");

			// Get the user ID from the request (assuming it's passed as a query parameter)
			String userId = req.getParameter("id");

			// if user id not available
			if (userId == null || userId.isEmpty()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("message", "User ID is required");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				resp.getWriter().write(jsonObject.toString());
			}

			// Database Query to delete the user data
			String query = "delete from userDetails where id=?";

			// Establish Database Connection
			Connection readyConnection = MyConnectionClass.GetConnection();
			PreparedStatement statement = readyConnection.prepareStatement(query);

			// set parameter to the prepared statement
			statement.setInt(1, Integer.parseInt(userId)); // we type caste the userId because userId is String and
															// setInt method returners integer.

			// execute the query
			int rowDeleted = statement.executeUpdate();

			JSONObject jsonObject = new JSONObject();
			if (rowDeleted > 0) {
				// preparing response
				jsonObject.put("message", "User Deleted Successfully!");
			} else {
				jsonObject.put("message", "User not found");
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			// Send the response back to the client
			resp.getWriter().write(jsonObject.toString());

		} catch (Exception e) {
			// Handle other exceptions
			e.printStackTrace();
			JSONObject errorJson = new JSONObject();
			errorJson.put("message", "An error occurred while deleting user data");
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write(errorJson.toString());

		}

	}

}
