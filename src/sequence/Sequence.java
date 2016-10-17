package sequence;


import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sequence {
    private final BigInteger LIMIT = new BigInteger("4488888888888888888888888888888888888888888888888850");
    private final int MAX_LENGHT = 50;
    private final BigInteger[] array;

    private Sequence() {
        array = new BigInteger[50];
        array[0] = BigInteger.ZERO;
        for (int i = 1; i < array.length; i++) {
            array[i] = array[i - 1].add(BigInteger.valueOf(9).multiply(BigInteger.TEN.pow(i - 1)).multiply(BigInteger.valueOf(i)));
        }
    }

    private BigInteger findPositionOfTheSequence(String sequence) {
        if (isAllZeroes(sequence)) return findPositionOfTheNumber(new BigInteger("1" + sequence)).add(BigInteger.ONE);
        int length = sequence.length();
        if (length == 1) return new BigInteger(sequence);
        BigInteger result = LIMIT;
        for (int i = 0; i < Math.min(50, length); i++) {
            for (int j = 1; j <= Math.min(50, length); j++) {
                result = result.min(findPositionOfTheSubsequence(sequence, i, i + j));
            }
        }
        return result;
    }

    private boolean isAllZeroes(String number) {
        for (char digit : number.toCharArray()) {
            if (digit != '0') return false;
        }
        return true;
    }

    private BigInteger findPositionOfTheNumber(BigInteger x) {
        if (x.compareTo(BigInteger.ZERO) < 0 || x.compareTo(LIMIT) > 0) {
            return LIMIT;
        }
        if (x.compareTo(BigInteger.TEN) < 0) {
            return x;
        }
        int pos = x.toString().length() - 1;
        return array[pos].add(BigInteger.ONE).add(BigInteger.valueOf(pos + 1).multiply(x.subtract(BigInteger.TEN.pow(pos))));

    }


    private BigInteger findPositionOfTheSubsequence(String sequence, int x, int y) {
        if (sequence.charAt(x) == '0') return LIMIT;
        String sub = sequence.substring(0, x);
        String s = "";
        for (int i = x; i < y; i++) {
            if (i < sequence.length()) s += sequence.charAt(i);
            else {
                int diff = y - i;
                String tmp = new BigInteger(sub.substring(sub.length() - diff)).add(BigInteger.ONE).toString();
                while (tmp.length() < diff) tmp = "0" + tmp;
                s += tmp.substring(tmp.length() - diff);
                break;
            }
        }

        BigInteger base = new BigInteger(s);
        if (LIMIT.compareTo(base) < 0) return LIMIT;

        String hypotheses = base.toString();
        int position = x;
        int numLen = 0;

        BigInteger checkNum = base.subtract(BigInteger.ONE);

        while (position > 0) {
            if (checkNum.compareTo(BigInteger.ZERO)<=0) return LIMIT;
            String add = checkNum.toString();
            checkNum = checkNum.subtract(BigInteger.ONE);
            position -= add.length();
            numLen += add.length();
            hypotheses = add + hypotheses;

        }

        checkNum = base.add(BigInteger.ONE);
        position = y;
        while (position < sequence.length()) {
            String add = checkNum.toString();
            checkNum = checkNum.add(BigInteger.ONE);
            position += add.length();
            hypotheses += add;
        }
        for (int i = 0; i < sequence.length(); i++) {
            if (sequence.charAt(i) != hypotheses.charAt(numLen - x + i)) return LIMIT;
        }
        return findPositionOfTheNumber(base).subtract(BigInteger.valueOf(x));
    }


    public void findSequences(InputStream input, PrintStream output) {
        Scanner scanner = new Scanner(input);
        List<String> result = new ArrayList<>();
        String s;
        while ((s = scanner.nextLine())!=null && !s.isEmpty()) {
            if (s.length() > MAX_LENGHT) result.add("Maximum length of subsequence is " + MAX_LENGHT + " digits");
            else result.add(findPositionOfTheSequence(s).toString());
        }
        result.forEach(res -> System.out.println(res));
        scanner.close();
    }

    public static void main(String[] args) {
        new Sequence().findSequences(System.in, System.out);
    }

}
