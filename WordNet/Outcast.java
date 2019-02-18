public class Outcast {

    private final WordNet words;

    public Outcast(WordNet wordnet) {
        words = wordnet;
    }        // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        int max;
        String ocast;
        int dis;
        max = -1;
        ocast = "";
        for (String word: nouns) {
            dis = 0;
            for (String i: nouns) {
                dis += words.distance(word, i);
            }
            if (max == -1 || dis > max) {
                max = dis;
                ocast = word;
            }
        }
        return (ocast);
    }   // given an array of WordNet nouns, return an outcast

    public static void main(String[] args) {
        //empty
    }  // see test client below
}