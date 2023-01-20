package chucknorris;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        boolean isMenuRunning = true;
        do {
            isMenuRunning = menu(s);
        } while (isMenuRunning);
    }

    public static boolean menu(Scanner s) {
        boolean isMenuRunning = true;
        System.out.println("Please input operation (encode/decode/exit): ");
        String option = s.nextLine();

        switch (option) {
            case "encode":
                callEncode(s);
                break;
            case "decode":
                callDecode(s);
                break;
            case "exit":
                System.out.println("Bye!");
                isMenuRunning = false;
                break;
            default:
                String msg = "There is no '%s' operation"
                        .formatted(option);

                System.out.println(msg);
                System.out.println();
        }

        return isMenuRunning;
    }

    public static void callEncode(Scanner s) {

        /* ENCODE STRING */
        System.out.println("Input string:");
        String str = readString(s);

        String stringInBinary = convertStringToBinary(str);
        String infoEncode = encodeStringBinary(stringInBinary, "", 0, ' ');
        String infoEncodeTrimEnd = infoEncode.substring(0, infoEncode.length()-1);

        System.out.println("Encoded string:");
        System.out.println(infoEncodeTrimEnd);
        System.out.println();
    }

    public static void callDecode(Scanner s) {
        /*DECODE STRING*/
        System.out.println("Input encoded string:");
        String strZerosEncoded = readString(s);
        boolean isValidString = checkIsValidString(strZerosEncoded);

        if (isValidString) {
            String[] arrayStrZerosEncoded = convertStrIntoArray(strZerosEncoded);
            String stringInBinaries = decodeZerosToBinaries(arrayStrZerosEncoded);

            isValidString = stringInBinaries.length() % 7 == 0;

            if (isValidString) {
                String result = decodeBinaries(stringInBinaries, true);

                System.out.println("Decoded string:");
                System.out.println(result);
                System.out.println();
            }
        }

        if (!isValidString) {
            System.out.println("Encoded string is not valid.");
            System.out.println();
        }
    }

    public static boolean checkIsValidString(String strZerosEncoded) {
        boolean isValidString = true;

        isValidString = checkOnlyZeros(strZerosEncoded.split("")) && checkIsSequence(strZerosEncoded.split(" "))
        && checkIsEven(strZerosEncoded.split(" "));

        return isValidString;
    }

    public static boolean checkOnlyZeros(String[] onlyItems) {
        boolean isValidString = true;

        for (String item: onlyItems) {
            isValidString = item.equals("0") || item.equals(" ");
            if (!isValidString) {
                break;
            }
        }

        return isValidString;
    }

    public static boolean checkIsSequence(String[] onlyItems) {
        boolean isValidString = true;

        for (int i = 0; i < onlyItems.length; i += 2) {
            String item = onlyItems[i];

            isValidString = item.length() == 1 || item.length() == 2;

            if (!isValidString) {
                break;
            }
        }

        return isValidString;
    }

    public static boolean checkIsEven(String[] onlyItems) {
        return onlyItems.length % 2 == 0;
    }

    public static String readString(Scanner s) {
        return s.nextLine();
    }

    public static String convertStringToBinary(String str) {
        int items = str.length();
        String stringInBinary = "";

        for (int i =0; i < items; i++) {
            char charItem = str.charAt(i);
            stringInBinary += convertCharToBinary(charItem, true);
        }

        return stringInBinary;
    }

    public static String convertCharToBinary(char charItem, boolean isAscii) {
        Integer charToBinary = Integer.valueOf(Integer.toBinaryString(charItem));
        return isAscii ? String.format("%07d", charToBinary) : String.format("%08d", charToBinary);
    }

    public static String encodeStringBinary(String stringInBinary, String infoEncode, Integer count, char lastItem) {
        int items = stringInBinary.length();

        if (items == 0) {
            String zeros = putZeros(count + 1);
            infoEncode += lastItem == '1' ? "0 " + zeros : "00 " + zeros;
            return infoEncode;
        }

        char actualItem = stringInBinary.charAt(0);

        if (actualItem == lastItem) {
            count++;
        } else if (lastItem != ' ') {
            count++;

            String zeros = putZeros(count);
            count = 0;

            infoEncode += lastItem == '1' ? "0 " + zeros : "00 " + zeros;
        }

        String newStringInBinary = stringInBinary.substring(1);
        return encodeStringBinary(newStringInBinary, infoEncode, count, actualItem);
    }

    public static String putZeros(int count) {
        String zeros = "";
        while (count > 0) {
            zeros += "0";
            count--;
        }
        zeros += " ";
        return zeros;
    }

    /* STAGE 4/5 */
    public static String[] convertStrIntoArray(String strEncoded) {
        return strEncoded.split("\\s");
    }

    public static String decodeZerosToBinaries(String[] arrayStrEncoded) {
        int size = arrayStrEncoded.length;
        String binaries = "";


        for (int i = 0; i < size; i += 2) {
            String binary =  arrayStrEncoded[i].length() == 1 ? "1" : "0";
            int binSize = arrayStrEncoded[i + 1].length();
            binaries += decodeZeroToBinary(binary, binSize);
        }

        return binaries;
    }

    public static String decodeZeroToBinary(String binary, int binSize) {
        String binaries = "";

        for (int i = 0; i < binSize; i++) {
            binaries += binary;
        }

        return binaries;
    }

    public static String decodeBinaries(String stringInBinaries, boolean isAscii) {

        char[] binaries = stringInBinaries.toCharArray();
        int asciiValue = isAscii ? 7 : 8;
        int sizeArray = binaries.length;
        int pos = 0;
        int base = 2;
        int total = 0;
        String mirrorMsg = "";

        for (int i = sizeArray - 1; i >= 0; i--) {
            int value = Character.getNumericValue(binaries[i]);
            int pow = (int) Math.pow(base, pos);

            total += value == 1 ? pow : 0;

            if (pos == asciiValue - 1) {
                mirrorMsg += Character.toString(total);
                pos = 0;
                total = 0;
            } else {
                pos++;
            }
        }

        return orderFinalMsg(mirrorMsg);
    }

    public static String orderFinalMsg(String mirrorMsg) {
        String finalMsg = "";

        for (int i = mirrorMsg.length() - 1; i >= 0; i--) {
            finalMsg += mirrorMsg.charAt(i);
        }


        return finalMsg;
    }

    /* First Stage
    public static String newString(String str) {
        String newStr = "";

        for (int i =0; i < str.length(); i++) {
            newStr += str.charAt(i) + " ";
        }

        return newStr;
    } */
}