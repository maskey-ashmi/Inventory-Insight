import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class InventoryInsight {
    public void addProduct(int id, String category, String name, int units, double costPrice) {
        String query = "INSERT INTO products (id, category, name, initial_units, cost_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setString(2, category);
            stmt.setString(3, name);
            stmt.setInt(4, units);
            stmt.setDouble(5, costPrice);
            stmt.executeUpdate();
            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }
    public void stockOut(int productId, String name, int unitsSold, double sellingPrice) {
        String query = "SELECT initial_units, sold_units, cost_price FROM products WHERE id = ? AND name = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.setString(2, name);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                int currentUnits = rs.getInt("initial_units");
                int currentSoldUnits = rs.getInt("sold_units"); // Get current sold units
                currentUnits=currentUnits-currentSoldUnits;
                double costPrice = rs.getDouble("cost_price");
    
                if (unitsSold > currentUnits) {
                    System.out.println("Not enough stock to complete the transaction.");
                } else {
                    double profit = (sellingPrice - costPrice) * unitsSold;
                    String updateQuery = "UPDATE products SET sold_units = IFNULL(sold_units, 0) + ?, selling_price = ? WHERE id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, unitsSold);  
                        updateStmt.setDouble(2, sellingPrice);  
                        updateStmt.setInt(3, productId);
                        updateStmt.executeUpdate();
                    }
                    System.out.println("Stock updated. Units sold: " + unitsSold);
                    System.out.println("Profit for this transaction: " + profit);
                    CalculateProfit(profit,productId);/*function call */
                }
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error during stock out: " + e.getMessage());
        }
    }
    public void CalculateProfit(double profit,int id) {
        String query = "UPDATE products SET profit = ? WHERE id = ?;";
        try (Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, profit);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }
    public void totalUnitsSold() {
        String query = "SELECT SUM(sold_units) AS total_units_sold FROM products";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int totalUnitsSold = rs.getInt("total_units_sold");
                System.out.println("Total Units Sold: " + totalUnitsSold);
            } else {
                System.out.println("No products found.");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total units sold: " + e.getMessage());
        }
    }
    public void calculateGrossProfitOrLoss() {
        String query = "SELECT id, name, cost_price, selling_price, sold_units FROM products";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            double totalGrossProfitOrLoss = 0;
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double costPrice = rs.getDouble("cost_price");
                double sellingPrice = rs.getDouble("selling_price");
                int soldUnits = rs.getInt("sold_units");
                if (Double.isNaN(costPrice) || Double.isNaN(sellingPrice) || costPrice == 0 || sellingPrice == 0) {
                    System.out.println("Skipping product with insufficient data (Cost or Selling Price is NULL or Zero).");
                } else {
                    double grossProfitOrLoss = (sellingPrice - costPrice) * soldUnits;
                    totalGrossProfitOrLoss += grossProfitOrLoss; 
                }
            }
            System.out.println("Total Gross Profit or Loss: " + totalGrossProfitOrLoss);
        } catch (SQLException e) {
            System.out.println("Error calculating gross profit or loss: " + e.getMessage());
        }
    }

    public void viewAllProducts() {
        String query = "SELECT * FROM products";
        try (Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Category: " + rs.getString("category") +
                        ", Name: " + rs.getString("name") +
                        ", Initial Units: " + rs.getInt("initial_units") +
                        ", Sold Units: " + rs.getInt("sold_units") +
                        ", Cost Price: " + rs.getDouble("cost_price") +
                        ", Selling Price: " + rs.getDouble("selling_price"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing products: " + e.getMessage());
        }
    }

    public void viewProductById(int id) {
        String query = "SELECT * FROM products WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Category: " + rs.getString("category") +
                        ", Name: " + rs.getString("name") +
                        ", Sold Units: " + rs.getInt("sold_units") +
                        ", Cost Price: " + rs.getDouble("cost_price") +
                        ", Selling Price: " + rs.getDouble("selling_price"));
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing product by ID: " + e.getMessage());
        }
    }

    public void updateProductDetails(int id, String category, String name, int units, double costPrice, double sellingPrice, int initialUnits) {
        String query = "UPDATE products SET category = ?, name = ?, sold_units = ?, cost_price = ?, selling_price = ?, initial_units = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, category);
            stmt.setString(2, name);
            stmt.setInt(3, units);
            stmt.setDouble(4, costPrice);
            stmt.setDouble(5, sellingPrice);
            stmt.setInt(6, initialUnits);  // Corrected this line
            stmt.setInt(7, id);  // Corrected index for the product ID
            int rowsAffected = stmt.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("Product details updated successfully.");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating product details: " + e.getMessage());
        }
    }
    
    public void viewProductsByCategoryOrName(String category, String name) {
        String query = "SELECT * FROM products WHERE category LIKE ? AND name LIKE ?";
        try (Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + category + "%");
            stmt.setString(2, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Category: " + rs.getString("category") +
                        ", Name: " + rs.getString("name") +
                        ", Sold Units: " + rs.getInt("sold_units") +
                        ", Cost Price: " + rs.getDouble("cost_price") +
                        ", Selling Price: " + rs.getDouble("selling_price"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing products by category or name: " + e.getMessage());
        }
    }
    public void deleteProduct(int id, String name) {
        String query = "DELETE FROM products WHERE id = ? AND name = ?";
        try (Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setString(2, name);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product removed successfully!");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }
    public void calculateTotalSales() {
        String query = "SELECT SUM(selling_price * units_sold) AS total_sales FROM sales";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                double totalSales = rs.getDouble("total_sales");
                System.out.println("Total Sales: " + totalSales);
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total sales: " + e.getMessage());
        }
    }
}
