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
	public void testValueStatus() {
		final String source = "<status> Ok";
		final String expected = "\"Ok\"";
		testTraslateRule(source, expected, parser -> parser.value());
	}

	@Test
	public void testValueInteger() {
		final String source = "<integer> 3";
		final String expected = "3";
		testTraslateRule(source, expected, parser -> parser.value());
	}

	@Test
	public void testValueUuid() {
		final String source = "<uuid> 54b148ce-2091-11e9-bd60-e7ffa3dffd67";
		final String expected = "\"54b148ce-2091-11e9-bd60-e7ffa3dffd67\"";
		testTraslateRule(source, expected, parser -> parser.value());
	}

	@Test
	public void testUuidValueTypedUuid() {
		final String source = "<uuid> 54b148ce-2091-11e9-bd60-e7ffa3dffd67";
		final String expected = "\"54b148ce-2091-11e9-bd60-e7ffa3dffd67\"";
		testTraslateRule(source, expected, parser -> parser.uuidValue());
	}

	@Test
	public void testUuidValueUuid() {
		final String source = "54b148ce-2091-11e9-bd60-e7ffa3dffd67";
		final String expected = "\"54b148ce-2091-11e9-bd60-e7ffa3dffd67\"";
		testTraslateRule(source, expected, parser -> parser.uuidValue());
	}

	@Test
	public void testUuidValueTypedNamedUuid() {
		final String source = "<uuid> Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)";
		final String expected = "\"Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)\"";
		testTraslateRule(source, expected, parser -> parser.uuidValue());
	}

	@Test
	public void testUuidValueNamedUuid() {
		final String source = "Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)";
		final String expected = "\"Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)\"";
		testTraslateRule(source, expected, parser -> parser.uuidValue());
	}

	@Test
	public void testUuidValueEmpty() {
		final String source = "<uuid> <empty>";
		final String expected = "null";
		testTraslateRule(source, expected, parser -> parser.uuidValue());
	}

	@Test
	public void testUuidVector() {
		final String source = "<vector of uuid> [3]\n" + "    0 = Dividends (8716833d-2951-4815-88bb-3c87e3637eb8)\n"
				+ "    1 = Dividend time fallback (0ec616b3-b779-11e4-bb45-245e0d1c06b7)\n"
				+ "    2 = Dividend tax rule (39c3f1b1-900e-11e8-9e47-f1bbe9ebb3a6)";
		final String expected = "[\"Dividends (8716833d-2951-4815-88bb-3c87e3637eb8)\",\"Dividend time fallback (0ec616b3-b779-11e4-bb45-245e0d1c06b7)\",\"Dividend tax rule (39c3f1b1-900e-11e8-9e47-f1bbe9ebb3a6)\"]";
		testTraslateRule(source, expected, parser -> parser.uuidVector());
	}

	@Test
	public void testEnumTypedValueTrend() {
		final String source = "<trend> Minus";
		final String expected = "\"Minus\"";
		testTraslateRule(source, expected, parser -> parser.enumValue());
	}

	@Test
	public void testEnumTypedValueStatus() {
		final String source = "<status> Ok";
		final String expected = "\"Ok\"";
		testTraslateRule(source, expected, parser -> parser.enumValue());
	}

	@Test
	public void testIntegerValueInteger() {
		final String source = "<integer> 3";
		final String expected = "3";
		testTraslateRule(source, expected, parser -> parser.integerValue());
	}

	@Test
	public void testIntegerValueUint32() {
		final String source = "<uint32> 250000";
		final String expected = "250000";
		testTraslateRule(source, expected, parser -> parser.integerValue());
	}

	@Test
	public void testDoubleValueDouble() {
		final String source = "<double> 0.87687867";
		final String expected = "0.87687867";
		testTraslateRule(source, expected, parser -> parser.doubleValue());
	}

	@Test
	public void testDoubleValueInteger() {
		final String source = "<double> 34";
		final String expected = "34";
		testTraslateRule(source, expected, parser -> parser.doubleValue());
	}

	@Test
	public void testBooleanValue() {
		final String source = "<boolean> false";
		final String expected = "false";
		testTraslateRule(source, expected, parser -> parser.booleanValue());
	}

	@Test
	public void testErrorValue() {
		final String source = "<error double> \"Instrument definitions not received\"";
		final String expected = "\"Instrument definitions not received\"";
		testTraslateRule(source, expected, parser -> parser.errorValue());
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
