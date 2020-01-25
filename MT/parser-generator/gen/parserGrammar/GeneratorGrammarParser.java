// Generated from /home/Valeriy.Teplyakov/ITMO/MT/parser-generator/src/parserGenerator.main/java/parserGrammar/GeneratorGrammar.g4 by ANTLR 4.7.2
package parserGrammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class GeneratorGrammarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, WS=6, EMPTY=7, SKIPP=8, IDENTIFIER=9, 
		LEXEM=10, GRAMMAR_NAME=11, STRING=12, CODE=13, PARAM=14;
	public static final int
		RULE_root = 0, RULE_header = 1, RULE_rule_ = 2, RULE_variant = 3;
	private static String[] makeRuleNames() {
		return new String[] {
			"root", "header", "rule_", "variant"
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

	@Override
	public String getGrammarFileName() { return "GeneratorGrammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public GeneratorGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class RootContext extends ParserRuleContext {
		public InputGrammar grammar = new InputGrammar();
		public Token name;
		public HeaderContext header;
		public Rule_Context rule_;
		public HeaderContext header() {
			return getRuleContext(HeaderContext.class,0);
		}
		public List<Rule_Context> rule_() {
			return getRuleContexts(Rule_Context.class);
		}
		public Rule_Context rule_(int i) {
			return getRuleContext(Rule_Context.class,i);
		}
		public TerminalNode GRAMMAR_NAME() { return getToken(GeneratorGrammarParser.GRAMMAR_NAME, 0); }
		public TerminalNode LEXEM() { return getToken(GeneratorGrammarParser.LEXEM, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GeneratorGrammarParser.IDENTIFIER, 0); }
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GeneratorGrammarListener ) ((GeneratorGrammarListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GeneratorGrammarListener ) ((GeneratorGrammarListener)listener).exitRoot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GeneratorGrammarVisitor ) return ((GeneratorGrammarVisitor<? extends T>)visitor).visitRoot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(8);
			((RootContext)_localctx).name = _input.LT(1);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IDENTIFIER) | (1L << LEXEM) | (1L << GRAMMAR_NAME))) != 0)) ) {
				((RootContext)_localctx).name = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			 _localctx.grammar.setName((((RootContext)_localctx).name!=null?((RootContext)_localctx).name.getText():null)); 
			}
			setState(11);
			match(T__0);
			setState(15);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(12);
				((RootContext)_localctx).header = header();
				 _localctx.grammar.setHeader(((RootContext)_localctx).header.code); 
				}
			}

			setState(23);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER || _la==LEXEM) {
				{
				{
				setState(17);
				((RootContext)_localctx).rule_ = rule_();
				 _localctx.grammar.addRule(((RootContext)_localctx).rule_._rule); 
				setState(19);
				match(T__0);
				}
				}
				setState(25);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeaderContext extends ParserRuleContext {
		public String code;
		public Token CODE;
		public TerminalNode CODE() { return getToken(GeneratorGrammarParser.CODE, 0); }
		public HeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GeneratorGrammarListener ) ((GeneratorGrammarListener)listener).enterHeader(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GeneratorGrammarListener ) ((GeneratorGrammarListener)listener).exitHeader(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GeneratorGrammarVisitor ) return ((GeneratorGrammarVisitor<? extends T>)visitor).visitHeader(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeaderContext header() throws RecognitionException {
		HeaderContext _localctx = new HeaderContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_header);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			match(T__1);
			setState(27);
			((HeaderContext)_localctx).CODE = match(CODE);
			 ((HeaderContext)_localctx).code =  ((HeaderContext)_localctx).CODE == null ? "" : (((HeaderContext)_localctx).CODE!=null?((HeaderContext)_localctx).CODE.getText():null).substring(1, (((HeaderContext)_localctx).CODE!=null?((HeaderContext)_localctx).CODE.getText():null).length() - 1); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Rule_Context extends ParserRuleContext {
		public Rule _rule = new Rule();
		public Token LEXEM;
		public Token STRING;
		public Token IDENTIFIER;
		public Token PARAM;
		public VariantContext variant;
		public TerminalNode LEXEM() { return getToken(GeneratorGrammarParser.LEXEM, 0); }
		public List<TerminalNode> STRING() { return getTokens(GeneratorGrammarParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(GeneratorGrammarParser.STRING, i);
		}
		public TerminalNode SKIPP() { return getToken(GeneratorGrammarParser.SKIPP, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GeneratorGrammarParser.IDENTIFIER, 0); }
		public List<VariantContext> variant() {
			return getRuleContexts(VariantContext.class);
		}
		public VariantContext variant(int i) {
			return getRuleContext(VariantContext.class,i);
		}
		public List<TerminalNode> PARAM() { return getTokens(GeneratorGrammarParser.PARAM); }
		public TerminalNode PARAM(int i) {
			return getToken(GeneratorGrammarParser.PARAM, i);
		}
		public Rule_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rule_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GeneratorGrammarListener ) ((GeneratorGrammarListener)listener).enterRule_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GeneratorGrammarListener ) ((GeneratorGrammarListener)listener).exitRule_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GeneratorGrammarVisitor ) return ((GeneratorGrammarVisitor<? extends T>)visitor).visitRule_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Rule_Context rule_() throws RecognitionException {
		Rule_Context _localctx = new Rule_Context(_ctx, getState());
		enterRule(_localctx, 4, RULE_rule_);
		int _la;
		try {
			setState(72);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LEXEM:
				enterOuterAlt(_localctx, 1);
				{
				setState(30);
				((Rule_Context)_localctx).LEXEM = match(LEXEM);
				 _localctx._rule.setName((((Rule_Context)_localctx).LEXEM!=null?((Rule_Context)_localctx).LEXEM.getText():null)); 
				setState(32);
				match(T__2);
				setState(47);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					{
					setState(33);
					((Rule_Context)_localctx).STRING = match(STRING);
					setState(34);
					match(SKIPP);
					 _localctx._rule.addVariant(new Lexeme((((Rule_Context)_localctx).STRING!=null?((Rule_Context)_localctx).STRING.getText():null).substring(1, (((Rule_Context)_localctx).STRING!=null?((Rule_Context)_localctx).STRING.getText():null).length() - 1), true)); 
					}
					}
					break;
				case 2:
					{
					{
					{
					setState(36);
					((Rule_Context)_localctx).STRING = match(STRING);
					 _localctx._rule.addVariant(new Lexeme((((Rule_Context)_localctx).STRING!=null?((Rule_Context)_localctx).STRING.getText():null).substring(1, (((Rule_Context)_localctx).STRING!=null?((Rule_Context)_localctx).STRING.getText():null).length() - 1), false)); 
					}
					setState(44);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__3) {
						{
						{
						setState(39);
						match(T__3);
						setState(40);
						((Rule_Context)_localctx).STRING = match(STRING);
						 _localctx._rule.addVariant(new Lexeme((((Rule_Context)_localctx).STRING!=null?((Rule_Context)_localctx).STRING.getText():null).substring(1, (((Rule_Context)_localctx).STRING!=null?((Rule_Context)_localctx).STRING.getText():null).length() - 1), false)); 
						}
						}
						setState(46);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					}
					break;
				}
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(49);
				((Rule_Context)_localctx).IDENTIFIER = match(IDENTIFIER);
				 _localctx._rule.setName((((Rule_Context)_localctx).IDENTIFIER!=null?((Rule_Context)_localctx).IDENTIFIER.getText():null)); 
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PARAM) {
					{
					setState(51);
					((Rule_Context)_localctx).PARAM = match(PARAM);
					 _localctx._rule.parseParams(((Rule_Context)_localctx).PARAM == null ? "" : (((Rule_Context)_localctx).PARAM!=null?((Rule_Context)_localctx).PARAM.getText():null).substring(1, (((Rule_Context)_localctx).PARAM!=null?((Rule_Context)_localctx).PARAM.getText():null).length() - 1)); 
					}
				}

				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(55);
					match(T__4);
					setState(56);
					((Rule_Context)_localctx).PARAM = match(PARAM);
					 _localctx._rule.parseReturns(((Rule_Context)_localctx).PARAM == null ? "" : (((Rule_Context)_localctx).PARAM!=null?((Rule_Context)_localctx).PARAM.getText():null).substring(1, (((Rule_Context)_localctx).PARAM!=null?((Rule_Context)_localctx).PARAM.getText():null).length() - 1)); 
					}
				}

				setState(60);
				match(T__2);
				setState(61);
				((Rule_Context)_localctx).variant = variant();
				 _localctx._rule.addVariant(((Rule_Context)_localctx).variant._variant); 
				setState(69);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(63);
					match(T__3);
					setState(64);
					((Rule_Context)_localctx).variant = variant();
					 _localctx._rule.addVariant(((Rule_Context)_localctx).variant._variant); 
					}
					}
					setState(71);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariantContext extends ParserRuleContext {
		public Variant _variant = new Variant();
		public Token CODE;
		public Token name;
		public Token PARAM;
		public TerminalNode EMPTY() { return getToken(GeneratorGrammarParser.EMPTY, 0); }
		public List<TerminalNode> CODE() { return getTokens(GeneratorGrammarParser.CODE); }
		public TerminalNode CODE(int i) {
			return getToken(GeneratorGrammarParser.CODE, i);
		}
		public List<TerminalNode> IDENTIFIER() { return getTokens(GeneratorGrammarParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(GeneratorGrammarParser.IDENTIFIER, i);
		}
		public List<TerminalNode> LEXEM() { return getTokens(GeneratorGrammarParser.LEXEM); }
		public TerminalNode LEXEM(int i) {
			return getToken(GeneratorGrammarParser.LEXEM, i);
		}
		public List<TerminalNode> PARAM() { return getTokens(GeneratorGrammarParser.PARAM); }
		public TerminalNode PARAM(int i) {
			return getToken(GeneratorGrammarParser.PARAM, i);
		}
		public VariantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GeneratorGrammarListener ) ((GeneratorGrammarListener)listener).enterVariant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GeneratorGrammarListener ) ((GeneratorGrammarListener)listener).exitVariant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GeneratorGrammarVisitor ) return ((GeneratorGrammarVisitor<? extends T>)visitor).visitVariant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariantContext variant() throws RecognitionException {
		VariantContext _localctx = new VariantContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_variant);
		int _la;
		try {
			setState(94);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EMPTY:
				enterOuterAlt(_localctx, 1);
				{
				setState(74);
				match(EMPTY);
				setState(76);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CODE) {
					{
					setState(75);
					((VariantContext)_localctx).CODE = match(CODE);
					}
				}

				 _localctx._variant.addChild("", "", ((VariantContext)_localctx).CODE == null ? "" : (((VariantContext)_localctx).CODE!=null?((VariantContext)_localctx).CODE.getText():null).substring(1, (((VariantContext)_localctx).CODE!=null?((VariantContext)_localctx).CODE.getText():null).length() - 1)); 
				}
				break;
			case IDENTIFIER:
			case LEXEM:
				enterOuterAlt(_localctx, 2);
				{
				setState(90); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(84);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case IDENTIFIER:
						{
						setState(79);
						((VariantContext)_localctx).name = match(IDENTIFIER);
						setState(81);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==PARAM) {
							{
							setState(80);
							((VariantContext)_localctx).PARAM = match(PARAM);
							}
						}

						}
						break;
					case LEXEM:
						{
						setState(83);
						((VariantContext)_localctx).name = match(LEXEM);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(87);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==CODE) {
						{
						setState(86);
						((VariantContext)_localctx).CODE = match(CODE);
						}
					}


					        _localctx._variant.addChild((((VariantContext)_localctx).name!=null?((VariantContext)_localctx).name.getText():null), ((VariantContext)_localctx).PARAM == null ? "" : (((VariantContext)_localctx).PARAM!=null?((VariantContext)_localctx).PARAM.getText():null).substring(1, (((VariantContext)_localctx).PARAM!=null?((VariantContext)_localctx).PARAM.getText():null).length() - 1), ((VariantContext)_localctx).CODE == null ? "" : (((VariantContext)_localctx).CODE!=null?((VariantContext)_localctx).CODE.getText():null).substring(1, (((VariantContext)_localctx).CODE!=null?((VariantContext)_localctx).CODE.getText():null).length() - 1));
					     
					}
					}
					setState(92); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==IDENTIFIER || _la==LEXEM );
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\20c\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2\22\n\2\3\2\3\2\3\2"+
		"\3\2\7\2\30\n\2\f\2\16\2\33\13\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4-\n\4\f\4\16\4\60\13\4\5\4\62\n\4\3\4\3"+
		"\4\3\4\3\4\5\48\n\4\3\4\3\4\3\4\5\4=\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7"+
		"\4F\n\4\f\4\16\4I\13\4\5\4K\n\4\3\5\3\5\5\5O\n\5\3\5\3\5\3\5\5\5T\n\5"+
		"\3\5\5\5W\n\5\3\5\5\5Z\n\5\3\5\6\5]\n\5\r\5\16\5^\5\5a\n\5\3\5\2\2\6\2"+
		"\4\6\b\2\3\3\2\13\r\2l\2\n\3\2\2\2\4\34\3\2\2\2\6J\3\2\2\2\b`\3\2\2\2"+
		"\n\13\t\2\2\2\13\f\b\2\1\2\f\r\3\2\2\2\r\21\7\3\2\2\16\17\5\4\3\2\17\20"+
		"\b\2\1\2\20\22\3\2\2\2\21\16\3\2\2\2\21\22\3\2\2\2\22\31\3\2\2\2\23\24"+
		"\5\6\4\2\24\25\b\2\1\2\25\26\7\3\2\2\26\30\3\2\2\2\27\23\3\2\2\2\30\33"+
		"\3\2\2\2\31\27\3\2\2\2\31\32\3\2\2\2\32\3\3\2\2\2\33\31\3\2\2\2\34\35"+
		"\7\4\2\2\35\36\7\17\2\2\36\37\b\3\1\2\37\5\3\2\2\2 !\7\f\2\2!\"\b\4\1"+
		"\2\"\61\7\5\2\2#$\7\16\2\2$%\7\n\2\2%\62\b\4\1\2&\'\7\16\2\2\'(\b\4\1"+
		"\2(.\3\2\2\2)*\7\6\2\2*+\7\16\2\2+-\b\4\1\2,)\3\2\2\2-\60\3\2\2\2.,\3"+
		"\2\2\2./\3\2\2\2/\62\3\2\2\2\60.\3\2\2\2\61#\3\2\2\2\61&\3\2\2\2\62K\3"+
		"\2\2\2\63\64\7\13\2\2\64\67\b\4\1\2\65\66\7\20\2\2\668\b\4\1\2\67\65\3"+
		"\2\2\2\678\3\2\2\28<\3\2\2\29:\7\7\2\2:;\7\20\2\2;=\b\4\1\2<9\3\2\2\2"+
		"<=\3\2\2\2=>\3\2\2\2>?\7\5\2\2?@\5\b\5\2@G\b\4\1\2AB\7\6\2\2BC\5\b\5\2"+
		"CD\b\4\1\2DF\3\2\2\2EA\3\2\2\2FI\3\2\2\2GE\3\2\2\2GH\3\2\2\2HK\3\2\2\2"+
		"IG\3\2\2\2J \3\2\2\2J\63\3\2\2\2K\7\3\2\2\2LN\7\t\2\2MO\7\17\2\2NM\3\2"+
		"\2\2NO\3\2\2\2OP\3\2\2\2Pa\b\5\1\2QS\7\13\2\2RT\7\20\2\2SR\3\2\2\2ST\3"+
		"\2\2\2TW\3\2\2\2UW\7\f\2\2VQ\3\2\2\2VU\3\2\2\2WY\3\2\2\2XZ\7\17\2\2YX"+
		"\3\2\2\2YZ\3\2\2\2Z[\3\2\2\2[]\b\5\1\2\\V\3\2\2\2]^\3\2\2\2^\\\3\2\2\2"+
		"^_\3\2\2\2_a\3\2\2\2`L\3\2\2\2`\\\3\2\2\2a\t\3\2\2\2\20\21\31.\61\67<"+
		"GJNSVY^`";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}