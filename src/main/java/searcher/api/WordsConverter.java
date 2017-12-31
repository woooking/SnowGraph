package searcher.api;

import graphdb.extractors.parsers.word.corpus.Translator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.tartarus.snowball.ext.EnglishStemmer;
import webapp.SnowGraphContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class WordsConverter {

	private static Set<String> englishStopWords=new HashSet<>();

	public static String stem(String token){
		EnglishStemmer stemmer=new EnglishStemmer();
		stemmer.setCurrent(token);
		try {
			stemmer.stem();
			token=stemmer.getCurrent();
		} catch (IllegalArgumentException e){
		}
		return token;
	}

	public static Set<String> convert(String queryString){
		if (!isChinese(queryString))
			return englishConvert(queryString);
		return chineseConvert(queryString);
	}

	public static Set<String> convertWithoutStem(String queryString){

		if (isChinese(queryString))
			try {
				queryString=Translator.ch2en(queryString);
			} catch (IOException e) {
				e.printStackTrace();
			}
		System.out.println(queryString);

		Set<String> r=new HashSet<>();

		for (String token:queryString.toLowerCase().split("[^a-z]+")){
			if (token.length()<=2) // 去掉长度小于2的token
				continue;
			if (!englishStopWords.contains(token)) // 去掉停用词
				r.add(token);
		}
		return r;
	}

	public static Set<String> englishConvert(String queryString){
		EnglishStemmer stemmer=new EnglishStemmer();
		Set<String> r=new HashSet<>();

		for (String token:queryString.toLowerCase().split("[^a-z]+")){
			if (token.length()<=2) // 去掉长度小于2的token
				continue;
			stemmer.setCurrent(token);
			try {
				stemmer.stem();
				token=stemmer.getCurrent();
			} catch (IllegalArgumentException e){

			}
			if (!englishStopWords.contains(token)) // 去掉停用词
				r.add(token);
		}

		return r;
	}

	private static Set<String> chineseConvert(String queryString){
		Set<String> r=null;
		try {
			r=englishConvert(Translator.ch2en(queryString));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
	}

    private static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
		for (char c : ch) {
			if (isChinese(c)) {
				return true;
			}
		}
        return false;
    }

    private static final String SMART_STOP_WORDS[] =
    	{
    	   "a",
    	   "able",
    	   "about",
    	   "above",
    	   "according",
    	   "accordingly",
    	   "across",
    	   "actually",
    	   "after",
    	   "afterwards",
    	   "again",
    	   "against",
    	   "all",
    	   "allow",
    	   "allows",
    	   "almost",
    	   "alone",
    	   "along",
    	   "already",
    	   "also",
    	   "although",
    	   "always",
    	   "am",
    	   "among",
    	   "amongst",
    	   "an",
    	   "and",
    	   "another",
    	   "any",
    	   "anybody",
    	   "anyhow",
    	   "anyone",
    	   "anything",
    	   "anyway",
    	   "anyways",
    	   "anywhere",
    	   "apart",
    	   "appear",
    	   "appreciate",
    	   "appropriate",
    	   "are",
    	   "around",
    	   "as",
    	   "aside",
    	   "ask",
    	   "asking",
    	   "associated",
    	   "at",
    	   "available",
    	   "away",
    	   "awfully",
    	   "b",
    	   "be",
    	   "became",
    	   "because",
    	   "become",
    	   "becomes",
    	   "becoming",
    	   "been",
    	   "before",
    	   "beforehand",
    	   "behind",
    	   "being",
    	   "believe",
    	   "below",
    	   "beside",
    	   "besides",
    	   "best",
    	   "better",
    	   "between",
    	   "beyond",
    	   "both",
    	   "brief",
    	   "but",
    	   "by",
    	   "c",
    	   "came",
    	   "can",
    	   "cannot",
    	   "cant",
    	   "cause",
    	   "causes",
    	   "certain",
    	   "certainly",
    	   "changes",
    	   "clearly",
    	   "co",
    	   "com",
    	   "come",
    	   "comes",
    	   "concerning",
    	   "consequently",
    	   "consider",
    	   "considering",
    	   "contain",
    	   "containing",
    	   "contains",
    	   "corresponding",
    	   "could",
    	   "course",
    	   "currently",
    	   "d",
    	   "definitely",
    	   "described",
    	   "despite",
    	   "did",
    	   "different",
    	   "do",
    	   "does",
    	   "doing",
    	   "done",
    	   "down",
    	   "downwards",
    	   "during",
    	   "e",
    	   "each",
    	   "edu",
    	   "eg",
    	   "eight",
    	   "either",
    	   "else",
    	   "elsewhere",
    	   "enough",
    	   "entirely",
    	   "especially",
    	   "et",
    	   "etc",
    	   "even",
    	   "ever",
    	   "every",
    	   "everybody",
    	   "everyone",
    	   "everything",
    	   "everywhere",
    	   "ex",
    	   "exactly",
    	   "example",
    	   "except",
    	   "f",
    	   "far",
    	   "few",
    	   "fifth",
    	   "first",
    	   "five",
    	   "followed",
    	   "following",
    	   "follows",
    	   "for",
    	   "former",
    	   "formerly",
    	   "forth",
    	   "four",
    	   "from",
    	   "further",
    	   "furthermore",
    	   "g",
    	   "get",
    	   "gets",
    	   "getting",
    	   "given",
    	   "gives",
    	   "go",
    	   "goes",
    	   "going",
    	   "gone",
    	   "got",
    	   "gotten",
    	   "greetings",
    	   "h",
    	   "had",
    	   "happens",
    	   "hardly",
    	   "has",
    	   "have",
    	   "having",
    	   "he",
    	   "hello",
    	   "help",
    	   "hence",
    	   "her",
    	   "here",
    	   "hereafter",
    	   "hereby",
    	   "herein",
    	   "hereupon",
    	   "hers",
    	   "herself",
    	   "hi",
    	   "him",
    	   "himself",
    	   "his",
    	   "hither",
    	   "hopefully",
    	   "how",
    	   "howbeit",
    	   "however",
    	   "i",
    	   "ie",
    	   "if",
    	   "ignored",
    	   "immediate",
    	   "in",
    	   "inasmuch",
    	   "inc",
    	   "indeed",
    	   "indicate",
    	   "indicated",
    	   "indicates",
    	   "inner",
    	   "insofar",
    	   "instead",
    	   "into",
    	   "inward",
    	   "is",
    	   "it",
    	   "its",
    	   "itself",
    	   "j",
    	   "just",
    	   "k",
    	   "keep",
    	   "keeps",
    	   "kept",
    	   "know",
    	   "knows",
    	   "known",
    	   "l",
    	   "last",
    	   "lately",
    	   "later",
    	   "latter",
    	   "latterly",
    	   "least",
    	   "less",
    	   "lest",
    	   "let",
    	   "like",
    	   "liked",
    	   "likely",
    	   "little",
    	   "look",
    	   "looking",
    	   "looks",
    	   "ltd",
    	   "m",
    	   "mainly",
    	   "many",
    	   "may",
    	   "maybe",
    	   "me",
    	   "mean",
    	   "meanwhile",
    	   "merely",
    	   "might",
    	   "more",
    	   "moreover",
    	   "most",
    	   "mostly",
    	   "much",
    	   "must",
    	   "my",
    	   "myself",
    	   "n",
    	   "name",
    	   "namely",
    	   "nd",
    	   "near",
    	   "nearly",
    	   "necessary",
    	   "need",
    	   "needs",
    	   "neither",
    	   "never",
    	   "nevertheless",
    	   "new",
    	   "next",
    	   "nine",
    	   "no",
    	   "nobody",
    	   "non",
    	   "none",
    	   "noone",
    	   "nor",
    	   "normally",
    	   "not",
    	   "nothing",
    	   "novel",
    	   "now",
    	   "nowhere",
    	   "o",
    	   "obviously",
    	   "of",
    	   "off",
    	   "often",
    	   "oh",
    	   "ok",
    	   "okay",
    	   "old",
    	   "on",
    	   "once",
    	   "one",
    	   "ones",
    	   "only",
    	   "onto",
    	   "or",
    	   "other",
    	   "others",
    	   "otherwise",
    	   "ought",
    	   "our",
    	   "ours",
    	   "ourselves",
    	   "out",
    	   "outside",
    	   "over",
    	   "overall",
    	   "own",
    	   "p",
    	   "particular",
    	   "particularly",
    	   "per",
    	   "perhaps",
    	   "placed",
    	   "please",
    	   "plus",
    	   "possible",
    	   "presumably",
    	   "probably",
    	   "provides",
    	   "q",
    	   "que",
    	   "quite",
    	   "qv",
    	   "r",
    	   "rather",
    	   "rd",
    	   "re",
    	   "really",
    	   "reasonably",
    	   "regarding",
    	   "regardless",
    	   "regards",
    	   "relatively",
    	   "respectively",
    	   "right",
    	   "s",
    	   "said",
    	   "same",
    	   "saw",
    	   "say",
    	   "saying",
    	   "says",
    	   "second",
    	   "secondly",
    	   "see",
    	   "seeing",
    	   "seem",
    	   "seemed",
    	   "seeming",
    	   "seems",
    	   "seen",
    	   "self",
    	   "selves",
    	   "sensible",
    	   "sent",
    	   "serious",
    	   "seriously",
    	   "seven",
    	   "several",
    	   "shall",
    	   "she",
    	   "should",
    	   "since",
    	   "six",
    	   "so",
    	   "some",
    	   "somebody",
    	   "somehow",
    	   "someone",
    	   "something",
    	   "sometime",
    	   "sometimes",
    	   "somewhat",
    	   "somewhere",
    	   "soon",
    	   "sorry",
    	   "specified",
    	   "specify",
    	   "specifying",
    	   "still",
    	   "sub",
    	   "such",
    	   "sup",
    	   "sure",
    	   "t",
    	   "take",
    	   "taken",
    	   "tell",
    	   "tends",
    	   "th",
    	   "than",
    	   "thank",
    	   "thanks",
    	   "thanx",
    	   "that",
    	   "thats",
    	   "the",
    	   "their",
    	   "theirs",
    	   "them",
    	   "themselves",
    	   "then",
    	   "thence",
    	   "there",
    	   "thereafter",
    	   "thereby",
    	   "therefore",
    	   "therein",
    	   "theres",
    	   "thereupon",
    	   "these",
    	   "they",
    	   "think",
    	   "third",
    	   "this",
    	   "thorough",
    	   "thoroughly",
    	   "those",
    	   "though",
    	   "three",
    	   "through",
    	   "throughout",
    	   "thru",
    	   "thus",
    	   "to",
    	   "together",
    	   "too",
    	   "took",
    	   "toward",
    	   "towards",
    	   "tried",
    	   "tries",
    	   "truly",
    	   "try",
    	   "trying",
    	   "twice",
    	   "two",
    	   "u",
    	   "un",
    	   "under",
    	   "unfortunately",
    	   "unless",
    	   "unlikely",
    	   "until",
    	   "unto",
    	   "up",
    	   "upon",
    	   "us",
    	   "use",
    	   "used",
    	   "useful",
    	   "uses",
    	   "using",
    	   "usually",
    	   "uucp",
    	   "v",
    	   "value",
    	   "various",
    	   "very",
    	   "via",
    	   "viz",
    	   "vs",
    	   "w",
    	   "want",
    	   "wants",
    	   "was",
    	   "way",
    	   "we",
    	   "welcome",
    	   "well",
    	   "went",
    	   "were",
    	   "what",
    	   "whatever",
    	   "when",
    	   "whence",
    	   "whenever",
    	   "where",
    	   "whereafter",
    	   "whereas",
    	   "whereby",
    	   "wherein",
    	   "whereupon",
    	   "wherever",
    	   "whether",
    	   "which",
    	   "while",
    	   "whither",
    	   "who",
    	   "whoever",
    	   "whole",
    	   "whom",
    	   "whose",
    	   "why",
    	   "will",
    	   "willing",
    	   "wish",
    	   "with",
    	   "within",
    	   "without",
    	   "wonder",
    	   "would",
    	   "would",
    	   "x",
    	   "y",
    	   "yes",
    	   "yet",
    	   "you",
    	   "your",
    	   "yours",
    	   "yourself",
    	   "yourselves",
    	   "z",
    	   "zero"
    	};

	static{
		loadStopWords();
	}
	private static void loadStopWords(){
		EnglishStemmer stemmer=new EnglishStemmer();
		List<String> lines=new ArrayList<>();
		try {
			lines=FileUtils.readLines(new File(SnowGraphContext.class.getResource("/").getPath()+"stopwords.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		lines.forEach(n->{
			englishStopWords.add(n);
			stemmer.setCurrent(n);
			stemmer.stem();
			englishStopWords.add(stemmer.getCurrent());
		});
		for (String n:SMART_STOP_WORDS){
			englishStopWords.add(n);
			stemmer.setCurrent(n);
			stemmer.stem();
			englishStopWords.add(stemmer.getCurrent());
		}

	}
}
