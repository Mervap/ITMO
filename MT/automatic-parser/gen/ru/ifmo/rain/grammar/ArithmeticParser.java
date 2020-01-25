// Generated from /home/Valeriy.Teplyakov/ITMO/MT/automatic-parser/src/main/java/ru/ifmo/rain/grammar/Arithmetic.g4 by ANTLR 4.7.2
package ru.ifmo.rain.grammar;

  import java.util.*;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ArithmeticParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, WHITESPACES=5, PLUS=6, MINUS=7, POW=8, 
		MUL=9, DIV=10, NUMBER=11, IDENTIFIER=12;
	public static final int
		RULE_root = 0, RULE_assignment = 1, RULE_expression = 2, RULE_number = 3;
	private static String[] makeRuleNames() {
		return new String[] {
			"root", "assignment", "expression", "number"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'='", "'('", "')'", null, "'+'", "'-'", "'**'", "'*'", 
			"'/'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, "WHITESPACES", "PLUS", "MINUS", "POW", 
			"MUL", "DIV", "NUMBER", "IDENTIFIER"
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
	public String getGrammarFileName() { return "Arithmetic.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	  private Map<String, Integer> context = new HashMap();
	  private List<Map.Entry<String, Integer>> assignments = new ArrayList();

	  public List<Map.Entry<String, Integer>> getAllAssignments() {
	    return assignments;
	  }

	public ArithmeticParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class RootContext extends ParserRuleContext {
		public List<AssignmentContext> assignment() {
			return getRuleContexts(AssignmentContext.class);
		}
		public AssignmentContext assignment(int i) {
			return getRuleContext(AssignmentContext.class,i);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).exitRoot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ArithmeticVisitor ) return ((ArithmeticVisitor<? extends T>)visitor).visitRoot(this);
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
			setState(13);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER) {
				{
				{
				setState(8);
				assignment();
				setState(9);
				match(T__0);
				}
				}
				setState(15);
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

	public static class AssignmentContext extends ParserRuleContext {
		public int val;
		public Token IDENTIFIER;
		public ExpressionContext expression;
		public TerminalNode IDENTIFIER() { return getToken(ArithmeticParser.IDENTIFIER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).enterAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).exitAssignment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ArithmeticVisitor ) return ((ArithmeticVisitor<? extends T>)visitor).visitAssignment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			((AssignmentContext)_localctx).IDENTIFIER = match(IDENTIFIER);
			setState(17);
			match(T__1);
			setState(18);
			((AssignmentContext)_localctx).expression = expression(0);

			            Map.Entry<String, Integer> entry = Map.entry((((AssignmentContext)_localctx).IDENTIFIER!=null?((AssignmentContext)_localctx).IDENTIFIER.getText():null), ((AssignmentContext)_localctx).expression.val);
			            context.put(entry.getKey(), entry.getValue());
			            assignments.add(entry);
			            ((AssignmentContext)_localctx).val =  ((AssignmentContext)_localctx).expression.val;
			        
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

	public static class ExpressionContext extends ParserRuleContext {
		public int val;
		public ExpressionContext left;
		public Token IDENTIFIER;
		public NumberContext number;
		public ExpressionContext expression;
		public AssignmentContext assignment;
		public ExpressionContext rigth;
		public Token op;
		public TerminalNode IDENTIFIER() { return getToken(ArithmeticParser.IDENTIFIER, 0); }
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public TerminalNode MINUS() { return getToken(ArithmeticParser.MINUS, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public TerminalNode POW() { return getToken(ArithmeticParser.POW, 0); }
		public TerminalNode MUL() { return getToken(ArithmeticParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(ArithmeticParser.DIV, 0); }
		public TerminalNode PLUS() { return getToken(ArithmeticParser.PLUS, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ArithmeticVisitor ) return ((ArithmeticVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(22);
				((ExpressionContext)_localctx).IDENTIFIER = match(IDENTIFIER);
				 ((ExpressionContext)_localctx).val =  context.get((((ExpressionContext)_localctx).IDENTIFIER!=null?((ExpressionContext)_localctx).IDENTIFIER.getText():null)); 
				}
				break;
			case 2:
				{
				setState(24);
				((ExpressionContext)_localctx).number = number();
				 ((ExpressionContext)_localctx).val =  ((ExpressionContext)_localctx).number.val; 
				}
				break;
			case 3:
				{
				setState(27);
				match(MINUS);
				setState(28);
				((ExpressionContext)_localctx).expression = expression(6);
				 ((ExpressionContext)_localctx).val =  -((ExpressionContext)_localctx).expression.val; 
				}
				break;
			case 4:
				{
				setState(31);
				((ExpressionContext)_localctx).assignment = assignment();
				 ((ExpressionContext)_localctx).val =  ((ExpressionContext)_localctx).assignment.val; 
				}
				break;
			case 5:
				{
				setState(34);
				match(T__2);
				setState(35);
				((ExpressionContext)_localctx).expression = expression(0);
				setState(36);
				match(T__3);
				 ((ExpressionContext)_localctx).val =  ((ExpressionContext)_localctx).expression.val; 
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(58);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(56);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(41);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(42);
						match(POW);
						setState(43);
						((ExpressionContext)_localctx).rigth = ((ExpressionContext)_localctx).expression = expression(4);
						 ((ExpressionContext)_localctx).val =  (int) Math.pow(((ExpressionContext)_localctx).left.val, ((ExpressionContext)_localctx).rigth.val); 
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(46);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(47);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==MUL || _la==DIV) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(48);
						((ExpressionContext)_localctx).rigth = ((ExpressionContext)_localctx).expression = expression(4);
						 if ((((ExpressionContext)_localctx).op!=null?((ExpressionContext)_localctx).op.getType():0) == MUL)
						                      ((ExpressionContext)_localctx).val =  ((ExpressionContext)_localctx).left.val * ((ExpressionContext)_localctx).rigth.val;
						                    else
						                      ((ExpressionContext)_localctx).val =  ((ExpressionContext)_localctx).left.val / ((ExpressionContext)_localctx).rigth.val;
						                  
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(51);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(52);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(53);
						((ExpressionContext)_localctx).rigth = ((ExpressionContext)_localctx).expression = expression(3);
						 if ((((ExpressionContext)_localctx).op!=null?((ExpressionContext)_localctx).op.getType():0) == PLUS)
						                      ((ExpressionContext)_localctx).val =  ((ExpressionContext)_localctx).left.val + ((ExpressionContext)_localctx).rigth.val;
						                    else
						                      ((ExpressionContext)_localctx).val =  ((ExpressionContext)_localctx).left.val - ((ExpressionContext)_localctx).rigth.val;
						                  
						}
						break;
					}
					} 
				}
				setState(60);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class NumberContext extends ParserRuleContext {
		public int val;
		public Token NUMBER;
		public TerminalNode NUMBER() { return getToken(ArithmeticParser.NUMBER, 0); }
		public TerminalNode MINUS() { return getToken(ArithmeticParser.MINUS, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ArithmeticVisitor ) return ((ArithmeticVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_number);
		try {
			setState(66);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMBER:
				enterOuterAlt(_localctx, 1);
				{
				setState(61);
				((NumberContext)_localctx).NUMBER = match(NUMBER);
				 ((NumberContext)_localctx).val =  Integer.parseInt((((NumberContext)_localctx).NUMBER!=null?((NumberContext)_localctx).NUMBER.getText():null)); 
				}
				break;
			case MINUS:
				enterOuterAlt(_localctx, 2);
				{
				setState(63);
				match(MINUS);
				setState(64);
				((NumberContext)_localctx).NUMBER = match(NUMBER);
				 ((NumberContext)_localctx).val =  Integer.parseInt('-' + (((NumberContext)_localctx).NUMBER!=null?((NumberContext)_localctx).NUMBER.getText():null)); 
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 3);
		case 2:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\16G\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\7\2\16\n\2\f\2\16\2\21\13\2\3\3\3\3\3\3"+
		"\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\5\4*\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\7\4;\n\4\f\4\16\4>\13\4\3\5\3\5\3\5\3\5\3\5\5\5E\n\5\3\5\2"+
		"\3\6\6\2\4\6\b\2\4\3\2\13\f\3\2\b\t\2K\2\17\3\2\2\2\4\22\3\2\2\2\6)\3"+
		"\2\2\2\bD\3\2\2\2\n\13\5\4\3\2\13\f\7\3\2\2\f\16\3\2\2\2\r\n\3\2\2\2\16"+
		"\21\3\2\2\2\17\r\3\2\2\2\17\20\3\2\2\2\20\3\3\2\2\2\21\17\3\2\2\2\22\23"+
		"\7\16\2\2\23\24\7\4\2\2\24\25\5\6\4\2\25\26\b\3\1\2\26\5\3\2\2\2\27\30"+
		"\b\4\1\2\30\31\7\16\2\2\31*\b\4\1\2\32\33\5\b\5\2\33\34\b\4\1\2\34*\3"+
		"\2\2\2\35\36\7\t\2\2\36\37\5\6\4\b\37 \b\4\1\2 *\3\2\2\2!\"\5\4\3\2\""+
		"#\b\4\1\2#*\3\2\2\2$%\7\5\2\2%&\5\6\4\2&\'\7\6\2\2\'(\b\4\1\2(*\3\2\2"+
		"\2)\27\3\2\2\2)\32\3\2\2\2)\35\3\2\2\2)!\3\2\2\2)$\3\2\2\2*<\3\2\2\2+"+
		",\f\6\2\2,-\7\n\2\2-.\5\6\4\6./\b\4\1\2/;\3\2\2\2\60\61\f\5\2\2\61\62"+
		"\t\2\2\2\62\63\5\6\4\6\63\64\b\4\1\2\64;\3\2\2\2\65\66\f\4\2\2\66\67\t"+
		"\3\2\2\678\5\6\4\589\b\4\1\29;\3\2\2\2:+\3\2\2\2:\60\3\2\2\2:\65\3\2\2"+
		"\2;>\3\2\2\2<:\3\2\2\2<=\3\2\2\2=\7\3\2\2\2><\3\2\2\2?@\7\r\2\2@E\b\5"+
		"\1\2AB\7\t\2\2BC\7\r\2\2CE\b\5\1\2D?\3\2\2\2DA\3\2\2\2E\t\3\2\2\2\7\17"+
		"):<D";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}