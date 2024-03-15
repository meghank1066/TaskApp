package com.dkit.oop.sd2.DAOs;

import com.dkit.oop.sd2.DTOs.Task;
import com.dkit.oop.sd2.Exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MySqlTaskDAO extends MySqlDao implements TaskDaoInterface {

    /**
     * Meghan Keightley 9 Mar 2024
     */
    @Override
    public List<Task> getAllTasks() throws DaoException {
        List<Task> tasksList = new ArrayList<>();
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tasks");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String status = resultSet.getString("status");
                String priority = resultSet.getString("priority");
                String description = resultSet.getString("description");
                java.sql.Date dueDate = resultSet.getDate("due_date");

                Task task = new Task(id, title, status, priority, description, dueDate);
                tasksList.add(task);
            }
        } catch (SQLException e) {
            throw new DaoException("Error in getAllTasks(): " + e.getMessage());
        }
        return tasksList;
    }

    /**
     * Meghan Keightley 9 Mar 2024
     */
    @Override
    public Task getTaskById(int id) throws DaoException {
        Task task = null;
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tasks WHERE id = ?");
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String status = resultSet.getString("status");
                    String priority = resultSet.getString("priority");
                    String description = resultSet.getString("description");
                    java.sql.Date dueDate = resultSet.getDate("due_date");

                    task = new Task(id, title, status, priority, description, dueDate);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error in getTaskById(): " + e.getMessage());
        }
        return task;
    }

    /**
     * Meghan Keightley 9 Mar 2024.
     */
    @Override
    public Task deleteTaskById(int id) throws DaoException {
        Task deletedTask = null;
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM tasks WHERE id = ?")) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // If a task was deleted, create a Task object with the deleted task_id
                deletedTask = new Task(id, null, null, null, null, null);
            }

        } catch (SQLException e) {
            throw new DaoException("Error in deleteTaskById(): " + e.getMessage());
        }
        return deletedTask;
    }


    /* Feature 4 - Insert new Task to Database */
    public Task insertTask(Task task) throws DaoException {

        String query = "INSERT INTO tasks (title, status, priority, description, due_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getStatus());
            preparedStatement.setString(3, task.getPriority());
            preparedStatement.setString(4, task.getDescription());
            preparedStatement.setDate(5, new Date(task.getDueDate().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error in insertData(): " + e.getMessage());
        }

        return null;
    }

//    @Override
//    public Task updateTaskbyID (int taskId) throws DaoException {
    //new task
//        try (Connection connection = this.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(
//                     "UPDATE tasks SET title = ?, status = ?, priority = ?, description = ?, due_date = ? WHERE id = ?")) {
//
//            // Set parameters for the prepared statement
//            preparedStatement.setString(1, updatedTask.getTitle());
//            preparedStatement.setString(2, updatedTask.getStatus());
//            preparedStatement.setString(3, updatedTask.getPriority());
//            preparedStatement.setString(4, updatedTask.getDescription());
//            preparedStatement.setDate(5, new Date(updatedTask.getDueDate().getTime()));
//            preparedStatement.setInt(6, taskId);
//
//            // Execute the update operation
//            int rowsAffected = preparedStatement.executeUpdate();
//
//            if (rowsAffected > 0) {
//                // If the update was successful, return the updated task
//                return updatedTask;
//            }
//
//        } catch (SQLException e) {
//            throw new DaoException("Error in updateTaskById(): " + e.getMessage());
//        }
//
//        // If no rows were affected, return null indicating update failure
//        return null;
//    }


}