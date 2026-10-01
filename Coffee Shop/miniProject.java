import java.util.ArrayList;
import java.io.*;

class MenuItems {
    private String name;
    private double price;

	// constructor for menu items
    MenuItems(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class Transaction {
    private String itemName;
    private double price;
    private String paymentType;
    private double amountTendered;
    private double changeGiven;
    private String cardType;

    // constructor for cash payments
    public Transaction(String itemName, double price, double amountTendered, double changeGiven) {
        this.itemName = itemName;
        this.price = price;
        this.paymentType = "CASH";
        this.amountTendered = amountTendered;
        this.changeGiven = changeGiven;
        this.cardType = "";
    }

    // constructor for card payments
    public Transaction(String itemName, double price, String cardType) {
        this.itemName = itemName;
        this.price = price;
        this.paymentType = "CARD";
        this.cardType = cardType;
        this.amountTendered = 0;
        this.changeGiven = 0;
    }

    //converts to string to save to file
    public String toString() {
        if (paymentType.equals("CASH")) {
            return itemName + ", " + price + ", " + amountTendered + ", " + changeGiven;
        } else {
            return itemName + ", " + price + ", " + cardType;
        }
    }
}

public class miniProject {
    public static void displayMenu(ArrayList<MenuItems> items) {
        System.out.println("COFFEE SHOP MENU");
        System.out.println("================");
        for (int i = 0; i < items.size(); i++) {
            MenuItems item = items.get(i);
            System.out.printf("%d. %-15s %.2f\n", i + 1, item.getName(), item.getPrice());
        }
        System.out.printf("%d. Exit\n", items.size() + 1);
        System.out.println("================\n");
    }

    //payment process
    public static void processPayment(double totalPrice, ArrayList<Transaction> transactions, Keyboard key, String itemNames) {
        System.out.println("Select payment method: 1. CASH  2. CARD");
        int paymentChoice = key.readInteger("Enter choice: ", "Error: Invalid input", 1, 2);

        if (paymentChoice == 1) {
            //process cash payment
            double amountTendered = key.readDouble("Enter amount tendered: ", "Error: Invalid input. Enter a valid amount.");
            while (amountTendered < totalPrice) {
                System.out.println("Error: Insufficient amount. Try again.");
                amountTendered = key.readDouble("Enter amount tendered: ", "Error: Invalid input.");
            }
            double changeGiven = amountTendered - totalPrice;
            System.out.printf("Transaction complete. Your change: %.2f\n", changeGiven);
            //store transaction
            transactions.add(new Transaction(itemNames, totalPrice, amountTendered, changeGiven));

        } else {
            //process card payment
            System.out.println("Select card type: 1. Visa  2. MasterCard");
            int cardChoice = key.readInteger("Enter choice: ", "Error: Invalid input", 1, 2);
            String cardType = (cardChoice == 1) ? "Visa" : "MasterCard";
            System.out.println("Transaction complete. Card payment successful.");
            //store transaction
            transactions.add(new Transaction(itemNames, totalPrice, cardType));
        }
    }

    //reads menu items from inventory.txt. Splits name and price, and adds to menu.
    public static void readInventoryFile(String fileName, ArrayList<MenuItems> items) {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                String tokens[] = line.split(",");
                if (tokens.length == 2) {
                    String name = tokens[0];
                    double price = Double.parseDouble(tokens[1]);
                    items.add(new MenuItems(name, price));
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error - Cannot read from file " + fileName);
        }
    }

    //saves transactions to a file
    public static void saveTransactionsToFile(String fileName, ArrayList<Transaction> transactions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Transaction t : transactions) {
                writer.write(t.toString());
                writer.newLine();
            }
            System.out.println("Transactions saved successfully.");
        } catch (IOException e) {
            System.out.println("Error - Cannot write to file " + fileName);
        }
    }

    public static void main(String[] args) {
        //variables
        int choice = 0;

        //ArrayLists
        ArrayList<MenuItems> menuItems = new ArrayList<>();
        ArrayList<Transaction> transactions = new ArrayList<>();

        readInventoryFile("inventory.txt", menuItems);

        //Keyboard object for input
        Keyboard key = new Keyboard();

        //exit option
        int EXIT = menuItems.size() + 1;

        ArrayList<Integer> cart = new ArrayList<>();
        double totalPrice = 0;
        StringBuilder itemNames = new StringBuilder();

        //loop for coffee choices and if they want to buy more
        while (true) {
            displayMenu(menuItems);
            choice = key.readInteger("Enter coffee choice: ", "Error: Invalid input", 1, EXIT);

		//if user says they want another coffee but choose extit, they will be sent to payment options
        if (choice == EXIT) {
            break;
        }

            cart.add(choice);
            MenuItems selectedItem = menuItems.get(choice - 1);
            totalPrice = totalPrice + selectedItem.getPrice();
            itemNames.append(selectedItem.getName()).append(" ");

            //ask if they want to buy another coffee
            String another;
            while (true) {
                another = key.readString("Do you want to buy another coffee? (yes/no): ", "Error: Please enter 'yes' or 'no'.");
                if (another.equalsIgnoreCase("yes") || another.equalsIgnoreCase("no")) {
                    break;
                } else {
                    System.out.println("Error: Please enter 'yes' or 'no'.");
                }
            }
            if (!another.equalsIgnoreCase("yes")) {
                break;
            }
        }

        if (cart.size() > 0) {
            System.out.printf("Total amount to pay: %.2f\n", totalPrice);
            processPayment(totalPrice, transactions, key, itemNames.toString());
        }
        //Save transactions before exiting
        saveTransactionsToFile("transactions.txt", transactions);
        System.out.println("Goodbye!");
    }
}