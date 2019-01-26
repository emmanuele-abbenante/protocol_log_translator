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

	@Test
	public void testColumnsRequest() {
		final String source = "Columns [2] =\n"
				+ "     [Underlying price (b320a884-d8e4-4a0a-9cec-d6ace19f04ee)]: UPDATE: {Underlying price (b320a884-d8e4-4a0a-9cec-d6ace19f04ee)\n"
				+ "  }\n"
				+ "     [Present value adjustment factor (80edca0e-3c32-11e1-bccb-99245e0d1c06)]: UPDATE: {Present value adjustment factor (80edca0e-3c32-11e1-bccb-99245e0d1c06)\n"
				+ "  }";
		final String expected = "\"Columns\":[\"Underlying price (b320a884-d8e4-4a0a-9cec-d6ace19f04ee)\",\"Present value adjustment factor (80edca0e-3c32-11e1-bccb-99245e0d1c06)\"]";
		testTraslateRule(source, expected, parser -> parser.columnsRequest());
	}

	@Test
	public void testColumnsRequestWithParams() {
		final String source = "Columns [2] =\n"
				+ "     [Underlying price (b320a884-d8e4-4a0a-9cec-d6ace19f04ee)]: UPDATE: {Underlying price (b320a884-d8e4-4a0a-9cec-d6ace19f04ee)\n"
				+ "  }\n" + "     [54b3a3e4-2091-11e9-bd60-e7ffa3dffd67]: UPDATE: {\n"
				+ "    id = Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)\n" + "    parameters = [1] {\n"
				+ "      Parameter context ranking (68aa0456-b6eb-11e1-9b1c-06b747deb312) = <uuid> Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)\n"
				+ "    }\n" + "  }";
		final String expected = "\"Columns\":[\"Underlying price (b320a884-d8e4-4a0a-9cec-d6ace19f04ee)\",{\"columnRequestId\":\"54b3a3e4-2091-11e9-bd60-e7ffa3dffd67\",\"column\":\"Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)\",\"parameters\":{\"Parameter context ranking (68aa0456-b6eb-11e1-9b1c-06b747deb312)\":\"Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)\"}}]";
		testTraslateRule(source, expected, parser -> parser.columnsRequest());
	}

	@Test
	public void testColumnRequest() {
		final String source = "[Underlying price (b320a884-d8e4-4a0a-9cec-d6ace19f04ee)]: UPDATE: {Underlying price (b320a884-d8e4-4a0a-9cec-d6ace19f04ee)\n}";
		final String expected = "\"Underlying price (b320a884-d8e4-4a0a-9cec-d6ace19f04ee)\"";
		testTraslateRule(source, expected, parser -> parser.columnRequest());
	}

	@Test
	public void testColumnRequestWithParams() {
		final String source = "[54b3a3e4-2091-11e9-bd60-e7ffa3dffd67]: UPDATE: {\n"
				+ "    id = Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)\n" + "    parameters = [1] {\n"
				+ "      Parameter context ranking (68aa0456-b6eb-11e1-9b1c-06b747deb312) = <uuid> Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)\n"
				+ "    }\n" + "  }";
		final String expected = "{\"columnRequestId\":\"54b3a3e4-2091-11e9-bd60-e7ffa3dffd67\",\"column\":\"Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)\",\"parameters\":{\"Parameter context ranking (68aa0456-b6eb-11e1-9b1c-06b747deb312)\":\"Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)\"}}";
		testTraslateRule(source, expected, parser -> parser.columnRequestWithParams());
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
		System.out.println("===================================================================");
		System.out.println("Protocol log: " + source);
		System.out.println("JSON: " + actual);
		
		// Assert
		Assert.assertEquals(expected, actual);
	}

	interface ParseCommand {

		ParserRuleContext parse(ProtocolLogParser parser);

	}

}
