import java.util.Scanner;

class Keyboard {
    private Scanner in;
    
    Keyboard() {
        in = new Scanner(System.in);
    }
    
    public int readInteger(String prompt, String errorMessage, int min, int max) {
        int input;
        while (true) {
            System.out.print(prompt);
            if (in.hasNextInt()) {
                input = in.nextInt();
                if (input >= min && input <= max) {
                    return input;
                }
            }
            System.out.println(errorMessage);
            in.nextLine(); 
        }
    }

    public double readDouble(String prompt, String errorMessage) {
        double input;
        while (true) {
            System.out.print(prompt);
            if (in.hasNextDouble()) {
                input = in.nextDouble();
                return input;
            }
            System.out.println(errorMessage);
            in.nextLine(); 
        }
    }

    public String readString(String prompt, String errorMessage) {
        String input = "";
        while (true) {
            System.out.print(prompt);
            input = (in.nextLine()); {
                input = in.nextLine();
                return input;
            } 
        }
    }
}





