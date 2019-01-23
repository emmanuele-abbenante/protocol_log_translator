package com.emmanuele.protocol_log.parser.protocol_log;

import java.io.File;
import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {

	public static void main(String[] args) throws IOException {
		String source = args[0];
		ProtocolLogLexer lexer = new File(source).exists() ? new ProtocolLogLexer(CharStreams.fromFileName(source))
				: new ProtocolLogLexer(CharStreams.fromString(source));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		ProtocolLogParser parser = new ProtocolLogParser(tokens);
		ParserRuleContext context = parser.file();
		if (args.length > 1 && args[1].equals("--debug")) {
			printTokens(tokens);
			printPrettyLispTree(parser, context);
		}
		ParseTreeWalker walker = new ParseTreeWalker();
		ProtocolLogJSONTranslator converter = new ProtocolLogJSONTranslator();
		walker.walk(converter, context);
		System.out.println(converter.getJSON(context));
	}

	private static void printTokens(CommonTokenStream tokens) {
		System.out.println("\n[TOKENS]");
		for (Token t : tokens.getTokens()) {
			String symbolicName = ProtocolLogLexer.VOCABULARY.getSymbolicName(t.getType());
			String literalName = ProtocolLogLexer.VOCABULARY.getLiteralName(t.getType());
			System.out.printf("  %-20s '%s'\n", symbolicName == null ? literalName : symbolicName,
					t.getText().replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t"));
		}
	}

	private static void printPrettyLispTree(ProtocolLogParser parser, ParserRuleContext context) {
		System.out.println("\n[PARSE-TREE]");
		String tree = context.toStringTree(parser);
		int indentation = 1;
		for (char c : tree.toCharArray()) {
			if (c == '(') {
				if (indentation > 1) {
					System.out.println();
				}
				for (int i = 0; i < indentation; i++) {
					System.out.print("  ");
				}
				indentation++;
			} else if (c == ')') {
				indentation--;
			}
			System.out.print(c);
		}
		System.out.println();
	}

}