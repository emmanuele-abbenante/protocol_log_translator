package com.emmanuele.protocol_log.parser.protocol_log;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.Test;

public class ProtocolLogJSONTranslatorTest {

	@Test
	public void testHost() {
		final String source = "tbrx0apcm01vs";
		final String expected = "\"host\":\"tbrx0apcm01vs\"";
		testTraslateRule(source, expected, parser -> parser.host());
	}
	
	@Test
	public void testComponentNameString() {
		final String source = "tb_test_ce2";
		final String expected = "\"component\":\"tb_test_ce2\"";
		testTraslateRule(source, expected, parser -> parser.componentName());
	}
	
	@Test
	public void testComponentNameDashedString() {
		final String source = "tb_test_ce2-ce";
		final String expected = "\"component\":\"tb_test_ce2-ce\"";
		testTraslateRule(source, expected, parser -> parser.componentName());
	}
	
	@Test
	public void testColumn() {
		final String source = "[Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)] = <error double> \"Instrument definitions not received\"";
		final String expected = "{\"column\":\"Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)\",\"value\":\"Instrument definitions not received\"}";
		testTraslateRule(source, expected, parser -> parser.column());
	}

	@Test
	public void testTableCellValue() {
		final String source = "<double> 0.87687867";
		final String expected = "0.87687867";
		testTraslateRule(source, expected, parser -> parser.tableCellValue());
	}

	@Test
	public void testDoubleValue() {
		final String source = "<double> 0.87687867";
		final String expected = "0.87687867";
		testTraslateRule(source, expected, parser -> parser.doubleValue());
	}
	@Test
	public void testErrorDoubleValue() {
		final String source = "<error double> \"Instrument definitions not received\"";
		final String expected = "\"Instrument definitions not received\"";
		testTraslateRule(source, expected, parser -> parser.errorDoubleValue());
	}
	
	private void testTraslateRule(final String source, final String expected, final ParseCommand command) {
		// Arrange
		final ProtocolLogLexer lexer = new ProtocolLogLexer(CharStreams.fromString(source));
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		final ProtocolLogParser parser = new ProtocolLogParser(tokens);
		final ParserRuleContext context = command.parse(parser);
		final ParseTreeWalker walker = new ParseTreeWalker();
		final ProtocolLogJSONTranslator converter = new ProtocolLogJSONTranslator();

		// Act
		walker.walk(converter, context);
		final String actual = converter.getJSON(context);

		// Assert
		Assert.assertEquals(expected, actual);
	}

	interface ParseCommand {

		ParserRuleContext parse(ProtocolLogParser parser);

	}

}
