package it.unict.artool.playground.codesamples;

public class MultipleIfSample {

    public int methodWithIf(int n) {
        int result = 0;
        if (n == 0) {
            result = result + 1;
        }
        if (n == 1) {
            result = 1 + 2;
        }
        if (n == 2) {
            result = 1 + 3;
        }
        return result;
    }

    public int methodWithIf2(int k) {
        int result = 0;
        if (k == 0) {
            result = result + 1;
        }
        if (k % 2 == 0) {
            result = result + 1;
        }
        if (k % 2 == 1) {
            result = result + 1;
        }
        if (k % 3 == 0) {
            result = result + 1;
        }
        return result;
    }

}
