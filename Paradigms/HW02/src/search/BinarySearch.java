package search;

import java.util.Random;

public class BinarySearch {

    //Forall 0 <= i <= j < array.length: array[i] >= array[j]
    private static int binarySearchIterative(long[] array, long x) {

        int left = -1, right = array.length;
        int middle;
        //Forall 0 <= i <= j < array.length: array[i] >= array[j]
        //left = -1, right = array.length

        //Inv:
        //array[left] > x, array[right] <= x (I consider that array[-1] = infinity, array[array.length] = -infinity)
        //-1 <= left < right <= array.length
        //Forall 0 <= i <= j < array.length: array[i] >= array[j]
        while (right - left > 1) {
            //Inv, right - left > 1
            //Inv, left < right - 1
            middle = (left + right) / 2;

            //Inv, left < right - 1, left < middle < right
            if (array[middle] > x) {
                //Inv, left < right - 1, left < middle < right
                //array[middle] > x
                left = middle;
                //array[left] > x, array[right] <= x, -1 <= left' < left = middle < right = right' <= a.length
                //Forall 0 <= i <= j < array.length: array[i] >= array[j]
            } else {
                //Inv, left < right - 1, left < middle < right
                //array[middle] <= x
                right = middle;
                //array[left] > x, array[right] <= x, -1 <= left' = left < right = middle < right' <= a.length
                //Forall 0 <= i <= j < array.length: array[i] >= array[j]
            }

            //Inv, right - left < right' - left'
        }

        //Inv
        //right - left = 1
        return right;
    }

    //Inv:
    //array[left] > x, array[right] <= x (I consider that array[-1] = infinity, array[array.length] = -infinity)
    //-1 <= left < right <= array.length
    //Forall 0 <= i <= j < array.length: array[i] >= array[j]
    private static int binarySearchRecursiveWithParams(long[] array, long x, int left, int right) {
        if (right - left < 2) {
            //Inv
            //right - left = 1
            return right;
        }

        //Inv, right - left >= 2
        //Inv, left < right - 1
        int middle = (left + right) / 2;

        //Inv, left < right - 1, left < middle < right
        if (array[middle] > x) {
            //Inv, left < right - 1, left < middle < right
            //array[middle] >= x
            left = middle;
            //array[left] > x, array[right] <= x, -1 <= left' < left = middle < right = right' <= a.length
            //Forall 0 <= i <= j < array.length: array[i] >= array[j]
            return binarySearchRecursiveWithParams(array, x, left, right);

            //Forall 0 <= i <= j < array.length: array[i] >= array[j]
            //-1 <= result - 1 < result <= array.length,
            //array[result - 1] > x, array[result] <= x (I consider that array[-1] = infinity, array[array.length] = -infinity)
        } else {
            //Inv, left < right - 1, left < middle < right
            //array[middle] < x
            right = middle;
            //array[left] > x, array[right] <= x, -1 <= left' = left < right = middle < right' <= a.length
            //Forall 0 <= i <= j < array.length: array[i] >= array[j]
            return binarySearchRecursiveWithParams(array, x, left, right);

            //Forall 0 <= i <= j < array.length: array[i] >= array[j]
            //-1 <= result - 1 < result <= array.length,
            //array[result - 1] > x, array[result] <= x (I consider that array[-1] = infinity, array[array.length] = -infinity)
        }
    }

    //Forall 0 <= i <= j < array.length: array[i] >= array[j]
    private static int binarySearchRecursive(long[] array, long x) {
        return binarySearchRecursiveWithParams(array, x, -1, array.length);
        //Forall 0 <= i <= j < array.length: array[i] >= array[j]
        //-1 <= result - 1 < result <= array.length,
        //array[result - 1] > x, array[result] <= x (I consider that array[-1] = infinity, array[array.length] = -infinity)
    }


    public static void main(String[] args) {

        try {
            long x = Long.parseLong(args[0]);
            long[] array = new long[args.length - 1];

            for (int i = 1; i < args.length; ++i) {
                array[i - 1] = Long.parseLong(args[i]);
            }

            Random random = new Random();
            if (random.nextBoolean()) {
                System.out.println(binarySearchRecursive(array, x));
            } else {
                System.out.println(binarySearchIterative(array, x));
            }

        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("Exception (Wrong arguments): " + e);
        } catch (NumberFormatException e) {
            System.err.println("Exception (Wrong arguments): " + e.getMessage());
        }
    }
}
