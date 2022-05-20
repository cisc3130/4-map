import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Bigrams {

	public static class Pair<T1, T2> {
		public T1 first;
		public T2 second;
		public int hashCode;
		public Pair(T1 first, T2 second) {
			this.first = first;
			this.second = second;
			this.hashCode = Objects.hash(first, second);
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) return true;
			if (other == null || getClass() != other.getClass()) return false;
			Pair<String, String> otherPair = (Pair<String, String>) other;
			return this.first.equals(otherPair.first) && this.second.equals(otherPair.second);
		}

		@Override
		public int hashCode() { return this.hashCode; }
	}

	protected Map<Pair<String, String>, Float> bigramCounts;
	protected Map<String, Float> unigramCounts;

	// TODO: Given filename fn, read in the file word by word
	// For each word:
	// 1. call process(word)
	// 2. increment count of that word in unigramCounts
	// 3. increment count of new Pair(prevword, word) in bigramCounts
	public Bigrams(String fn) {
		bigramCounts = new HashMap<Pair<String,String>,Float>();
		unigramCounts = new HashMap<String,Float>();

		String prevWord = null;
		try {
			Scanner sc = new Scanner(new File(fn));
			while (sc.hasNext()) {
				String word = sc.next();
				word = process(word);
				if (unigramCounts.containsKey(word)) {
					unigramCounts.put(word, unigramCounts.get(word) + 1);
				}
				else {
					unigramCounts.put(word, 1f);
				}
				if (prevWord != null) {
					Pair<String, String> p = new Pair<String, String>(prevWord, word);

					if (bigramCounts.containsKey(p)) {
						bigramCounts.put(p, bigramCounts.get(p) + 1);
					}
					else {
						bigramCounts.put(p, 1f);
					}
				}
				prevWord = word;
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// TODO: Given words w1 and w2,
	// 1. replace w1 and w2 with process(w1) and process(w2)
	// 2. print the words
	// 3. if bigram(w1, w2) is not found, print "Bigram not found"
	// 4. print how many times w1 appears
	// 5. print how many times (w1, w2) appears
	// 6. print count(w1, w2)/count(w1)
	public float lookupBigram(String w1, String w2) {
		w1 = process(w1);
		w2 = process(w2);

		System.out.println(w1 +"," + w2);
		Pair<String, String> p = new Pair<String, String>(w1, w2);
		if (!bigramCounts.containsKey(p)) {
			System.out.println("Bigram not found");
		}
		else {
			System.out.println(w1 + "count is " + unigramCounts.get(w1));
			System.out.println(w1 + "," + w2 + "appears " + bigramCounts.get(p) + " times.");
			System.out.println("Count (w1,w2)/Count(w1): " + (bigramCounts.get(p)) / unigramCounts.get(w1));
			return bigramCounts.get(p) / unigramCounts.get(w1);
		}
		return (float) 0.0; // if nothing is found.
	}

	protected String process(String str) {
		return str.toLowerCase().replaceAll("[^a-z]", "");
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java Bigrams <FILENAME>");
			System.out.println(args.length);
			return;
		}

		Bigrams bg = new Bigrams(args[0]);

		List<Pair<String, String>> wordpairs = Arrays.asList(
				new Pair("with", "me"),
				new Pair("the", "grass"),
				new Pair("the", "king"),
				new Pair("to", "you")
		);

		for (Pair<String, String> p : wordpairs) {
			bg.lookupBigram(p.first, p.second);
		}

		System.out.println(bg.process("adddaWEFEF38234---+"));
	}
}