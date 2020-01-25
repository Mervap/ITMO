// Generated from /home/Valeriy.Teplyakov/ITMO/MT/parser-generator/src/parserGenerator.main/java/parserGrammar/GeneratorGrammar.g4 by ANTLR 4.7.2
package parserGrammar;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GeneratorGrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GeneratorGrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GeneratorGrammarParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(GeneratorGrammarParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link GeneratorGrammarParser#header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeader(GeneratorGrammarParser.HeaderContext ctx);
	/**
	 * Visit a parse tree produced by {@link GeneratorGrammarParser#rule_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRule_(GeneratorGrammarParser.Rule_Context ctx);
	/**
	 * Visit a parse tree produced by {@link GeneratorGrammarParser#variant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariant(GeneratorGrammarParser.VariantContext ctx);
}