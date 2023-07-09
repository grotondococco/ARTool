package it.unict.artool.codesamples;

public class SingleMethodSample {

    public static String methodCalledOnce(String s) {
        String s2 = s.replace("_", "");
        return s2.toUpperCase();
    }

    public static String methodCalledOnce(String s1, String s2) {
        return s1.concat(s2);
    }

    public static String methodCalledTwiceOrMore(String s) {
        return s.toLowerCase();
    }

    public static void main(String[] args) {
        String testString = "TEST_STRING";
        String resultFromFirstMethodCalledOnce = methodCalledOnce(testString);
        System.out.println(resultFromFirstMethodCalledOnce);
        String resultFromSecondMethodCalledOnce = methodCalledOnce(testString, resultFromFirstMethodCalledOnce);
        System.out.println(resultFromSecondMethodCalledOnce);
        String resultFromFirstMethodCalledTwice = methodCalledTwiceOrMore(resultFromFirstMethodCalledOnce);
        System.out.println(resultFromFirstMethodCalledTwice);
        String resultFromSecondMethodCalledTwice = methodCalledTwiceOrMore(resultFromSecondMethodCalledOnce);
        System.out.println(resultFromSecondMethodCalledTwice);
    }

}
