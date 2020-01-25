// Generated from /home/Valeriy.Teplyakov/ITMO/MT/parser-generator/src/parserGenerator.main/java/parserGrammar/GeneratorGrammar.g4 by ANTLR 4.7.2
package parserGrammar;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link GeneratorGrammarParser}.
 */
public interface GeneratorGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link GeneratorGrammarParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(GeneratorGrammarParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link GeneratorGrammarParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(GeneratorGrammarParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link GeneratorGrammarParser#header}.
	 * @param ctx the parse tree
	 */
	void enterHeader(GeneratorGrammarParser.HeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link GeneratorGrammarParser#header}.
	 * @param ctx the parse tree
	 */
	void exitHeader(GeneratorGrammarParser.HeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link GeneratorGrammarParser#rule_}.
	 * @param ctx the parse tree
	 */
	void enterRule_(GeneratorGrammarParser.Rule_Context ctx);
	/**
	 * Exit a parse tree produced by {@link GeneratorGrammarParser#rule_}.
	 * @param ctx the parse tree
	 */
	void exitRule_(GeneratorGrammarParser.Rule_Context ctx);
	/**
	 * Enter a parse tree produced by {@link GeneratorGrammarParser#variant}.
	 * @param ctx the parse tree
	 */
	void enterVariant(GeneratorGrammarParser.VariantContext ctx);
	/**
	 * Exit a parse tree produced by {@link GeneratorGrammarParser#variant}.
	 * @param ctx the parse tree
	 */
	void exitVariant(GeneratorGrammarParser.VariantContext ctx);
}