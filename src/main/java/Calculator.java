import java.util.ArrayList;

//Class for doing calculations on the imported data
public class Calculator {

    public Calculator() {

    }

    //Returns an integer array of the first ArrayList multiplied by the second
    public int[] multiply(ArrayList<Integer> arrayA, ArrayList<Integer> arrayB) {
        if (arrayA.size() != arrayB.size()) {
            System.out.println("Array size mismatch in multiply");
            return null;
        }
        int[] output = new int[arrayA.size()];
        for(int i = 0; i < output.length; i++) {
            output[i] = arrayA.get(i) * arrayB.get(i);
        }
        return output;
    }

    //Returns an integer array of the first ArrayList divided by the second
    public int[] divide(ArrayList<Integer> arrayA, ArrayList<Integer> arrayB) {
        if (arrayA.size() != arrayB.size()) {
            System.out.println("Array size mismatch in divide");
            return null;
        }
        int[] output = new int[arrayA.size()];
        for(int i = 0; i < output.length; i++) {
            if(arrayB.get(i) == 0) {
                System.out.println("Divide by 0 Error");
                output[i] = 0;
            }
            else {
                output[i] = arrayA.get(i) / arrayB.get(i);
            }
        }
        return output;
    }

    //Returns an String array of the first ArrayList concatenated with the second with a space in between
    public String[] concat(ArrayList<String> arrayA, ArrayList<String> arrayB) {
        if(arrayA.size() != arrayB.size()) {
            System.out.println("Array size mismatch in concat");
            return null;
        }
        String[] output = new String[arrayA.size()];
        for(int i = 0; i < output.length; i++) {
            output[i] = arrayA.get(i) + " " + arrayB.get(i);
        }
        return output;
    }
}
