package search;

public class BinarySearchSpan {

    //Pre: Forall 0 <= i <= j < array.length: array[i] >= array[j]
    //Post: Pre, -1 <= result - 1 < result <= array.length,
    //array[result - 1] > x, array[result] <= x (I consider that array[-1] = infinity, array[array.length] = -infinity)
    private static int binarySearchIterative(int[] array, int x) {

        int left = -1, right = array.length;
        int middle;
        //Pre, left = -1, right = array.length

        //Inv:
        //Pre, array[left] > x, array[right] <= x (I consider that array[-1] = infinity, array[array.length] = -infinity)
        //-1 <= left < right <= array.length
        while (right - left > 1) {
            //Inv, right - left > 1
            //Inv, left < right - 1
            middle = (left + right) / 2;

            //Inv, left < right - 1, left < middle < right
            if (array[middle] > x) {
                //Inv, left < right - 1, left < middle < right
                //array[middle] > x
                left = middle;
                //Pre, array[left] > x, array[right] <= x, -1 <= left' < left = middle < right = right' <= a.length
            } else {
                //Inv, left < right - 1, left < middle < right
                //array[middle] <= x
                right = middle;
                //Pre, array[left] > x, array[right] <= x, -1 <= left' = left < right = middle < right' <= a.length
            }

            //Inv, right - left < right' - left'
        }

        //Inv
        //right - left = 1
        return right;
        //result = right, left = result - 1
        //-1 <= result - 1 < result <= array.length
        //array[result - 1] > x, array[result] <= x (I consider that array[-1] = infinity, array[array.length] = -infinity)
    }

    //Pre: Forall 0 <= i <= j < array.length: array[i] >= array[j],
    //array[left] >= x, array[right] < x (I consider that array[-1] = infinity, array[array.length] = -infinity)
    //-1 <= left < right <= array.length

    //Post: Forall 0 <= i <= j < array.length: array[i] >= array[j],
    //array[result - 1] >= x, array[result] < x (I consider that array[-1] = infinity, array[array.length] = -infinity)
    private static int binarySearchRecursiveWithParams(int[] array, int x, int left, int right) {
        if (right - left < 2) {
            //Pre, right - left = 1, result = right
            return right;
            //Forall 0 <= i <= j < array.length: array[i] >= array[j]
            //result = right, left = result - 1
            //-1 <= result - 1 < result <= array.length
            //array[result - 1] > x, array[result] <= x (I consider that array[-1] = infinity, array[array.length] = -infinity)
        }

        //Pre, right - left >= 2
        //Pre, left < right - 1
        int middle = (left + right) / 2;

        //Pre, left < right - 1, left < middle < right
        if (array[middle] >= x) {
            //Pre, left < right - 1, left < middle < right
            //array[middle] >= x
            left = middle;
            //array[left] >= x, array[right] < x, -1 <= left' < left = middle < right = right' <= a.length
            //Forall 0 <= i <= j < array.length: array[i] >= array[j]
            return binarySearchRecursiveWithParams(array, x, left, right);

            //Forall 0 <= i <= j < array.length: array[i] >= array[j]
            //-1 <= result - 1 < result <= array.length,
            //array[result - 1] >= x, array[result] < x (I consider that array[-1] = infinity, array[array.length] = -infinity)
        } else {
            //Pre, left < right - 1, left < middle < right
            //array[middle] < x
            right = middle;
            //array[left] >= x, array[right] < x, -1 <= left' = left < right = middle < right' <= a.length
            //Forall 0 <= i <= j < array.length: array[i] >= array[j]
            return binarySearchRecursiveWithParams(array, x, left, right);

            //Forall 0 <= i <= j < array.length: array[i] >= array[j]
            //-1 <= result - 1 < result <= array.length,
            //array[result - 1] >= x, array[result] < x (I consider that array[-1] = infinity, array[array.length] = -infinity)
        }

        //Forall 0 <= i <= j < array.length: array[i] >= array[j]
        //-1 <= result - 1 < result <= array.length,
        //array[result - 1] >= x, array[result] < x (I consider that array[-1] = infinity, array[array.length] = -infinity)
    }

    //Pre: Forall 0 <= i <= j < array.length: array[i] >= array[j]
    //Post: Pre && -1 <= result - 1 < result <= array.length,
    //array[result - 1] >= x, array[result] < x (I consider that array[-1] = infinity, array[array.length] = -infinity)
    private static int binarySearchRecursive(int[] array, int x) {
        return binarySearchRecursiveWithParams(array, x, -1, array.length);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Wrong arguments");
        } else {
            int x = Integer.parseInt(args[0]);
            int[] array = new int[args.length - 1];

            for (int i = 1; i < args.length; ++i) {
                array[i - 1] = Integer.parseInt(args[i]);
            }

            int find = binarySearchIterative(array, x);
            int difference = binarySearchRecursive(array, x) - find;
            System.out.print(find + " " + difference);
        }

    }
}
