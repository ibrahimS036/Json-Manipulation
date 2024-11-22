package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.connection.MyConnectionClass;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/insert")
public class InsertController extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			String query = "insert into userdetails (name,email,phoneNumber,password) values(?,?,?,?)";
			Connection conn = MyConnectionClass.GetConnection();

			PreparedStatement statement = conn.prepareStatement(query);
			StringBuilder sb = new StringBuilder();

			BufferedReader reader = req.getReader();

			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			String myData = sb.toString();
			Gson gson = new Gson();
			JsonObject fromJson = gson.fromJson(myData, JsonObject.class);

			String name = fromJson.get("name").getAsString();
			String email = fromJson.get("email").getAsString();
			String phone = fromJson.get("phoneNumber").getAsString();
			String password = fromJson.get("password").getAsString();

			statement.setString(1, name);
			statement.setString(2, email);
			statement.setString(3, phone);
			statement.setString(4, password);

			int rowInsert = statement.executeUpdate();
			if (rowInsert > 0) {
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.setContentType("application/json");

				String successMessage = "{\"message\": \"Data Inserted Successfully!\"}";
				resp.getWriter().write(successMessage);
			} else {
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				String failedMessage = "{\"message\": \"Insertion Failed!\"}";
				resp.getWriter().write(failedMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			String errorMessage = "{\"message\": \"An error occurred: " + e.getMessage() + "\"}";
			resp.getWriter().write(errorMessage);

		}

	}

}
