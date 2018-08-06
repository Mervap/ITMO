package expression.generic;

public class Main {
    public static void main(String[] args) throws Exception {
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < args.length; ++i) {
            s.append(args[i]);
            s.append(" ");
        }

        GenericTabulator tabulator = new GenericTabulator();
        Object[][][] ans = tabulator.tabulate(args[0].substring(1, args[0].length()), String.valueOf(s), 0, 10, 0, 10, 0, 10);

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                for (int k = 0; k < 10; ++k) {
                    System.out.print(ans[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
