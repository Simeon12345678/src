import java.util.Scanner;

public class Main {

    static String[][] seets;
    static long[] IDarr = new long[0];
    static long[] IDarrWindow = new long[0];

    static int windowSeets = 10;
    static int normalSeets = 10;
    static int totalSeets = windowSeets + normalSeets;
    static long ID;

    static double moneyEarned = 0;

    public static void main(String[] args) {
        final double priceAdult = 299.9;
        final double priceChild = 149.9;
        
        int iterator = 0;

        Scanner keyboard = new Scanner(System.in);
        boolean shouldBeClosed = false;

        System.out.print("\n");
        System.out.println("välkommen till den advancerade bokningsystemet för bussar!");
        System.out.println("----------------------------------------------------------");

        initSeets();

        // render loop
        while (!shouldBeClosed) {
            System.out.println("välj ett alternativ, efter namnent ser du vad för input du ska ge för att få tillgång till den");
            System.out.print("\n");
            System.out.println("Boka en plats:  B");
            System.out.println("Avboka en plats: A");
            System.out.println("leta efter din plats: L");
            System.out.println("se tillgängliga platser:  S");
            System.out.println("visa vinsten på biljätter:  V");
            System.out.println("Avsluta programmet:  X");
            System.out.println("Visa personer från yngst till äldst: Y");
            System.out.print("input: ");
            String ans = keyboard.nextLine();

            if (ans.equalsIgnoreCase("B")) {
                // allows the user to choose between a window or normal seet to book via a CLI
                // menu
                beginBooking(priceAdult, priceChild, ans, keyboard, iterator);
                iterator++;
            } else if (ans.equalsIgnoreCase("A")) {
                // uses user ID to remove their seet from the program freeing up an available
                // seet
                removeSeet(ans, keyboard);
            } else if (ans.equalsIgnoreCase("L")) {

                findSeet(ans, keyboard);
            } else if (ans.equalsIgnoreCase("S")) {

                seetAmount();
                System.out.println("finns just nu " + totalSeets + " platser kvar!");

            } else if (ans.equalsIgnoreCase("V")) {

                System.out.println(moneyEarned + " Kr");

            } else if (ans.equalsIgnoreCase("X")) {

                shouldBeClosed = true;

            } else if (ans.equalsIgnoreCase("S")) {

                concat(IDarrWindow, IDarr);
                sort(IDarr, normalSeets);
                System.out.println("Yngst: " + IDarr[0]);
                System.out.println("Generel ordning");
                System.out.println(IDarr);
                
            } else {
                System.out.print("\n");
                System.out.println("ERROR! inte gilltigt alternativ! Försök igen!");
                System.out.print("\n");
            }
        }
        keyboard.close();

    }

    // draws a diagram of the bus seet layout
    static void seetAmount() {
        int rows = 4;
        int columns = 5;
        int iterator = 0;
        System.out.println("------------------------------------");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(" | " + seets[i][j] + " | ");
                iterator++;
                // uses remainder, each right side window seet is a multiple of 4. 
                // Therefore dividing by 4 to place the newline character to make it neater
                if (iterator % 4 == 0) {
                    System.out.print("\n");
                }
            }
        }
        System.out.println("------------------------------------");
    }

    // initializes the total seets in the program
    static void initSeets() {   
        int rows = 4;
        int columns = 5;
 
        seets = new String[rows][columns];
 
        int value = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seets[i][j] = Integer.toString(value);
                value++;
            }
        }
    }

    static void beginBooking(double priceAdult, double priceChild, String ans, Scanner keyboard, int iterator) {
        while (true) {
            System.out.print("skriv ditt födelsedatum YY/MM/DD för att boka: ");
            // breaks loop if user fails to input correctly bringing them back to main menu so they can try again
            try {
                String idText = keyboard.nextLine();
                ID = Long.parseLong(idText);
            } catch (Exception e) {
                System.err.println("ERROR! inte gilltigt alternativ!");
                break;
            }

            System.err.println("\n\n\n\n\n");
            System.out.print(
                    "Vill du boka en vanlig plats eller fönster plats? Skriv N för vanlig plats eller W för fönster: ");
            ans = keyboard.nextLine();

            if (ans.equalsIgnoreCase("N")) {
                if (ID < 20070101) {
                    assignSeetviaID(iterator); // pushes user value to the array
                    moneyEarned += priceAdult;
                } else {
                    assignSeetviaID(iterator);
                    moneyEarned += priceChild;
                }
            } else if (ans.equalsIgnoreCase("W")) {
                if (ID < 20070101) {
                    assignWindowSeetviaID(iterator); // pushes user value to the array
                    moneyEarned += priceAdult;
                } else {
                    assignWindowSeetviaID(iterator);
                    moneyEarned += priceChild;
                }
            } else {
                System.out.print("\n");
                System.out.println("ERROR! inte gilltigt alternativ!");
                System.out.print("\n");
            }

            System.out.println("\n\n\n\n\n");
            System.out.println("boka en till plats eller lämna? Skriv Y för att fortsätta!");
            System.out.print("input: ");
            ans = keyboard.nextLine();

            if (ans.equalsIgnoreCase("Y")) {
                continue;
            } else {
                break;
            }
        }

    }

    // will assign a seet based of the user input
    static void assignSeetviaID(int iterator) {
        if (totalSeets > 0 && normalSeets > 0) {
            IDarr = push(IDarr, ID);

            if (iterator == 4) {
                seets[iterator - 4][2] = "X";
            } else {
                seets[iterator][1] = "X";
            }
            normalSeets--;
            totalSeets--;
        } else if (normalSeets == 0 && windowSeets > 0) {
            System.out.println("Tyvär har vi slut på icke fönster platser. Men det finns " + windowSeets
                    + " fönster platser att boka kvar!");
        } else {
            System.out.println("Det finns inga tillgängliga platser kvar. Tack för din tid");
        }
        System.out.println(totalSeets);

    }

    // will assign a window seet based of the user input
    static void assignWindowSeetviaID(int iterator) {
        if (totalSeets > 0 && windowSeets > 0) {
            IDarrWindow = push(IDarr, ID);

            if (iterator == 4) {
                seets[iterator - 4][3] = "X";
            } else {
                seets[iterator][0] = "X";
            }
            windowSeets--;
            totalSeets--;
        } else if (windowSeets == 0 && windowSeets > 0) {
            System.out.println("Tyvär har vi slut på fönster platser. Men det finns " + normalSeets
                    + " vanliga platser att boka kvar!");
        } else {
            System.out.println("Det finns inga tillgängliga platser kvar. Tack för din tid");
        }

    }

    // lets the user find their seet by specifying whether they booked a window or
    // normal seet
    static void findSeet(String ans, Scanner keyboard) {
        while (true) {
            System.out.println("Ange ditt personnummer för att hitta platsen: ");
            
            try {
                String idText = keyboard.nextLine();
                System.out.print("bokade du fönster plays Y/N: ");
                if (ans.equalsIgnoreCase("Y")) {
                    findSeetAlgorithm(idText, IDarrWindow);
                } else {
                    findSeetAlgorithm(idText, IDarr);
                }
            } catch (Exception e) {
                System.err.println("ERROR! inte gilltigt alternativ!");
                break;
            }
        }
    }

    // finds the specified seet that the user has inputed. Made to handle both
    // window and normal seets
    static void findSeetAlgorithm(String idText, long[] generalIDarr) {
        // converts users ID to their birthdate to find their seat
        ID = Long.parseLong(idText);
        ID /= 10000;
        for (int i = 0; i < generalIDarr.length; i++) {
            if (generalIDarr[i] == ID) {
                System.out.println("Din plats är " + seets[i]);
                break;
            } else {
                System.out.println("Finns ingen plats som är bokat under dena personnummer");
                break;
            }
        }
    }

    static void removeSeet(String ans, Scanner keyboard) {
        while (true) {
            System.out.print("Ange ditt personnummer för att avboka: ");
            try {
                String idText = keyboard.nextLine();
                System.out.print("Bokade du en Fönsterplats Y/N: ");
                if (ans.equalsIgnoreCase("Y")) {
                    removeSeetAlgorithm(idText, IDarrWindow);
                } else {
                    removeSeetAlgorithm(idText, IDarr);
                }
            } catch (Exception e) {
                System.err.println("ERROR! inte gilltigt alternativ!");
                break;
            }

            System.out.println("\n\n\n\n\n");
            System.out.println("Avboka flera platser eller lämna? Skriv Y för att förtsätta!");
            System.out.print("Input:");
            ans = keyboard.nextLine();

            if (ans.equalsIgnoreCase("Y")) {
                continue;
            } else {
                break;
            }
        }

    }

    static void removeSeetAlgorithm(String idText, long[] generalIDarr) {
        // converts users ID to their birthdate for the cancellation of their reserv
        ID = Long.parseLong(idText);
        ID /= 10000;
        for (int i = 0; i < generalIDarr.length; i++) {
            if (generalIDarr[i] == ID) {
                generalIDarr = pop(generalIDarr, ID);
                System.out.println("Din plats är nu avbokat tack för din tid!");
                totalSeets++;
            }
            if (i == generalIDarr.length && generalIDarr[i] != ID) {
                System.out.println("Finns ingen plats som är bokat under dena personnummer");
            }
        }
    }

    // due to java arrays being static. A custom push function has been implemented
    static long[] push(long[] arr, long push) {
        long[] longArr = new long[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            longArr[i] = arr[i];
        }
        longArr[arr.length] = push;
        return longArr;
    }

    // same as push just removes the desired element instead of pushing a new one
    static long[] pop(long[] arr, long index) {
        // makes sure we do not for example try to access a negative index or going out
        // of bounds
        if (arr == null || index < 0 || index >= arr.length) {
            return arr;
        }
        long[] shortArr = new long[arr.length - 1];
        for (int i = 0, j = 0; i < arr.length; i++) {
            if (i == index) {
                continue;
            }
            shortArr[j++] = arr[i];
        }
        return shortArr;
    }

    // ----------------------------------------------------------------------------
    // serves same purpose as previous push but is used for names


    // ------------------------------------------------------------------------------

    static void sort(long[] arr, int n) {
        long temp;
        boolean isSwapped;
        for (int i = 0; i < n; i++) {
            isSwapped = false;
            for (int j = 0; j < n - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // swap arr[j] and arr[j+1]
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    isSwapped = true;
                }
            }
            // if no swapping occured break the loop
            if (!isSwapped) {
                break;
            }

        }

    }

    static long[] concat(long[] a, long[] b) {

        int length = a.length + b.length;

        long[] result = new long[length];
        int pos = 0;
        for (int i = 0; i < a.length; i++) {
            result[pos] = i;
            pos++;
        }

        for (int i = 0; i < b.length; i++) {
            result[pos] = i;
            pos++;
        }

        return result;
    }
}
