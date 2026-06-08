import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        InventoryInsight inventory = new InventoryInsight();
        Scanner scanner = new Scanner(System.in);
        System.out.println("-------------Welcome to inventory-------------- ");
        System.out.println("The products are: ");
        inventory.viewAllProducts();

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Product");
            System.out.println("2. Delete Product");
            System.out.println("3. View Products");
            System.out.println("4. Update Product Details");
            System.out.println("5. Stock Out (Deduct Units)");
            System.out.println("6. Calculate Gross Profit or Loss");
            System.out.println("7. Sold unit");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            int choice = getIntInput(scanner);

        switch (choice) {
        case 1:
                    System.out.print("Enter product ID: ");
                    int id = getIntInput(scanner);
                    scanner.nextLine();  // Consume newline left-over
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter initial units: ");  
                    int Units = getIntInput(scanner);  
                    System.out.print("Enter cost price: ");
                    double costPrice = getDoubleInput(scanner);
                    inventory.addProduct(id, category, name, Units, costPrice);  // Updated parameter to soldUnits
                    break;

                case 2:
                    System.out.print("Enter product ID to delete: ");
                    int deleteId = getIntInput(scanner);
                    scanner.nextLine(); // Consume newline left-over
                    System.out.print("Enter name: ");
                    String deletename = scanner.nextLine();
                    inventory.deleteProduct(deleteId, deletename);
                    break;
                case 3: 
                    inventory.viewAllProducts(); 
                    while (true) {
                        System.out.println("\nOptions:");
                        System.out.println("1. View by ID");
                        System.out.println("2. View by Category or Name");
                        System.out.println("3. Go Back");
                        System.out.print("Choose an option: ");
                        int subChoice = getIntInput(scanner);
                        switch (subChoice) {
                            case 1:
                                System.out.print("Enter Product ID: ");
                                int productId = getIntInput(scanner);
                                inventory.viewProductById(productId);
                                break;
                            case 2:
                                scanner.nextLine(); // Consume newline left-over
                                System.out.print("Enter category : ");
                                String subCategory = scanner.nextLine();
                                System.out.print("Enter name : ");
                                String subName = scanner.nextLine();
                                inventory.viewProductsByCategoryOrName(subCategory, subName);
                                break;
                            case 3:
                                break;
                            default:
                                System.out.println("Invalid choice. Try again.");
                        }
                        if (subChoice == 3) {
                            break; 
                        }
                    }
                    break;
                case 4:
                    System.out.print("Enter Product ID to update: ");
                    int updateId = getIntInput(scanner);
                    scanner.nextLine(); 
                    System.out.print("Enter new category: ");
                    String newCategory = scanner.nextLine();
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new sold units: "); 
                    int newSoldUnits = getIntInput(scanner);  
                    System.out.print("Enter new cost price: ");
                    double newCostPrice = getDoubleInput(scanner);
                    System.out.print("Enter new selling price: ");
                    double newSellingPrice = getDoubleInput(scanner);
                    System.out.println("Enter new initial units:");
                    int newinitialUnits = getIntInput(scanner); 
                    inventory.updateProductDetails(updateId, newCategory, newName, newSoldUnits, newCostPrice, newSellingPrice,newinitialUnits);  
                    break;

                case 5:
                    System.out.print("Enter Product ID: ");
                    int stockOutId = getIntInput(scanner);
                    scanner.nextLine(); 
                    System.out.print("Enter name: ");
                    String stockOutName = scanner.nextLine();  
                    System.out.print("Enter sold units to deduct: "); 
                    int soldUnitsToDeduct = getIntInput(scanner);  
                    System.out.print("Enter selling price: ");
                    double stockOutSellingPrice = getDoubleInput(scanner);
                    inventory.stockOut(stockOutId, stockOutName, soldUnitsToDeduct, stockOutSellingPrice); 
                    break;
                case 6:
                    inventory.calculateGrossProfitOrLoss();
                    break;
                case 7:
                    inventory.totalUnitsSold();
                    break;
                case 8:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    public static int getIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a valid integer.");
            scanner.next(); 
        }
        return scanner.nextInt();
    }
    public static double getDoubleInput(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input! Please enter a valid number.");
            scanner.next(); 
        }
        return scanner.nextDouble();
    }
}
