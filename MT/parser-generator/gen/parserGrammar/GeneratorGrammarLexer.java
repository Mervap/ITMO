// Generated from /home/Valeriy.Teplyakov/ITMO/MT/parser-generator/src/parserGenerator.main/java/parserGrammar/GeneratorGrammar.g4 by ANTLR 4.7.2
package parserGrammar;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class GeneratorGrammarLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, WS=6, EMPTY=7, SKIPP=8, IDENTIFIER=9, 
		LEXEM=10, GRAMMAR_NAME=11, STRING=12, CODE=13, PARAM=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "WS", "EMPTY", "SKIPP", "LOWER_LETTER", 
			"UPPER_LETTER", "DIGIT", "IDENTIFIER", "LEXEM", "GRAMMAR_NAME", "STRING", 
			"CODE", "PARAM"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'header'", "':'", "'|'", "'returns'", null, "'EMPTY'", 
			"'skip'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, "WS", "EMPTY", "SKIPP", "IDENTIFIER", 
			"LEXEM", "GRAMMAR_NAME", "STRING", "CODE", "PARAM"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public GeneratorGrammarLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "GeneratorGrammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20\u0089\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3"+
		"\t\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\7\rT\n\r\f\r\16\r"+
		"W\13\r\3\16\3\16\3\16\3\16\7\16]\n\16\f\16\16\16`\13\16\3\17\3\17\5\17"+
		"d\n\17\3\17\3\17\3\17\7\17i\n\17\f\17\16\17l\13\17\3\20\3\20\3\20\3\20"+
		"\6\20r\n\20\r\20\16\20s\3\20\3\20\3\21\3\21\7\21z\n\21\f\21\16\21}\13"+
		"\21\3\21\3\21\3\22\3\22\7\22\u0083\n\22\f\22\16\22\u0086\13\22\3\22\3"+
		"\22\4{\u0084\2\23\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\2\25\2\27\2\31"+
		"\13\33\f\35\r\37\16!\17#\20\3\2\7\5\2\13\f\17\17\"\"\4\2aac|\4\2C\\aa"+
		"\3\2\62;\3\2))\2\u0093\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2"+
		"\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\31\3\2\2\2\2\33"+
		"\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\3%\3\2\2\2\5"+
		"\'\3\2\2\2\7.\3\2\2\2\t\60\3\2\2\2\13\62\3\2\2\2\r:\3\2\2\2\17>\3\2\2"+
		"\2\21D\3\2\2\2\23I\3\2\2\2\25K\3\2\2\2\27M\3\2\2\2\31O\3\2\2\2\33X\3\2"+
		"\2\2\35c\3\2\2\2\37m\3\2\2\2!w\3\2\2\2#\u0080\3\2\2\2%&\7=\2\2&\4\3\2"+
		"\2\2\'(\7j\2\2()\7g\2\2)*\7c\2\2*+\7f\2\2+,\7g\2\2,-\7t\2\2-\6\3\2\2\2"+
		"./\7<\2\2/\b\3\2\2\2\60\61\7~\2\2\61\n\3\2\2\2\62\63\7t\2\2\63\64\7g\2"+
		"\2\64\65\7v\2\2\65\66\7w\2\2\66\67\7t\2\2\678\7p\2\289\7u\2\29\f\3\2\2"+
		"\2:;\t\2\2\2;<\3\2\2\2<=\b\7\2\2=\16\3\2\2\2>?\7G\2\2?@\7O\2\2@A\7R\2"+
		"\2AB\7V\2\2BC\7[\2\2C\20\3\2\2\2DE\7u\2\2EF\7m\2\2FG\7k\2\2GH\7r\2\2H"+
		"\22\3\2\2\2IJ\t\3\2\2J\24\3\2\2\2KL\t\4\2\2L\26\3\2\2\2MN\t\5\2\2N\30"+
		"\3\2\2\2OU\5\23\n\2PT\5\23\n\2QT\5\27\f\2RT\7a\2\2SP\3\2\2\2SQ\3\2\2\2"+
		"SR\3\2\2\2TW\3\2\2\2US\3\2\2\2UV\3\2\2\2V\32\3\2\2\2WU\3\2\2\2X^\5\25"+
		"\13\2Y]\5\25\13\2Z]\5\27\f\2[]\7a\2\2\\Y\3\2\2\2\\Z\3\2\2\2\\[\3\2\2\2"+
		"]`\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_\34\3\2\2\2`^\3\2\2\2ad\5\25\13\2bd\5"+
		"\23\n\2ca\3\2\2\2cb\3\2\2\2dj\3\2\2\2ei\5\25\13\2fi\5\23\n\2gi\5\27\f"+
		"\2he\3\2\2\2hf\3\2\2\2hg\3\2\2\2il\3\2\2\2jh\3\2\2\2jk\3\2\2\2k\36\3\2"+
		"\2\2lj\3\2\2\2mq\7)\2\2nr\n\6\2\2op\7^\2\2pr\7)\2\2qn\3\2\2\2qo\3\2\2"+
		"\2rs\3\2\2\2sq\3\2\2\2st\3\2\2\2tu\3\2\2\2uv\7)\2\2v \3\2\2\2w{\7}\2\2"+
		"xz\13\2\2\2yx\3\2\2\2z}\3\2\2\2{|\3\2\2\2{y\3\2\2\2|~\3\2\2\2}{\3\2\2"+
		"\2~\177\7\177\2\2\177\"\3\2\2\2\u0080\u0084\7]\2\2\u0081\u0083\13\2\2"+
		"\2\u0082\u0081\3\2\2\2\u0083\u0086\3\2\2\2\u0084\u0085\3\2\2\2\u0084\u0082"+
		"\3\2\2\2\u0085\u0087\3\2\2\2\u0086\u0084\3\2\2\2\u0087\u0088\7_\2\2\u0088"+
		"$\3\2\2\2\16\2SU\\^chjqs{\u0084\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}