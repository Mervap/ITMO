// Generated from /home/Valeriy.Teplyakov/ITMO/MT/automatic-parser/src/main/java/ru/ifmo/rain/grammar/Arithmetic.g4 by ANTLR 4.7.2
package ru.ifmo.rain.grammar;

  import java.util.*;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ArithmeticParser}.
 */
public interface ArithmeticListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(ArithmeticParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(ArithmeticParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(ArithmeticParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(ArithmeticParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ArithmeticParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ArithmeticParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(ArithmeticParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(ArithmeticParser.NumberContext ctx);
}