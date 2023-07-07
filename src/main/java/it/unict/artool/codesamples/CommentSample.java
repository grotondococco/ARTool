package it.unict.artool.codesamples;

// The class below represents the sample of code with comments
public class CommentSample {
    /**
     * This method always returns the given String
     *
     * @param sampleString a String given to the method
     * @return the given String
     */
    public String sampleMethod(String sampleString) {

        /*The code below will print the words Hello World
        to the screen followed by the given string, and it is amazing*/
        System.out.println("Hello World - input: " + sampleString);
        return /* --inline comments-- */ sampleString;
    }

}
