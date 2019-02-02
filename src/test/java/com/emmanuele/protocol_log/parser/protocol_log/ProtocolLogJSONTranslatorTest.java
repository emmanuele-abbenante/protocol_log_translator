package com.emmanuele.protocol_log.parser.protocol_log;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.Test;

public class ProtocolLogJSONTranslatorTest {

	@Test
	public void testFile() {
		final String source = "2019-01-04 12:57:44.885409 CET tbrx0apcm01vs tb_test_tp@tb_test/tb_test_tp[39102:39463] protocol.in <debug> src/common.cpp(74): Received StreamOpenRequest from tb_test_ae@tb_test [local 10.240.217.16:57820]\n" + 
				"<message> (138) StreamOpenRequest, <protocol> (18) TradeProtocol, <size> [186]\n" + 
				"{\n" + 
				"  operator = <uuid> DataEnricher (f391c898-328e-11e8-831f-a6db3c870c3e)\n" + 
				"  pet = <uuid> <empty>\n" + 
				"  stream identifier = <uuid> f26a3b96-1017-11e9-00da-58a7ff7f0000\n" + 
				"  stream message identifiers = {'Trade'}\n" + 
				"  subscription type = <subscription type> SnapshotAndLive\n" + 
				"  suppress reply = <boolean> false\n" + 
				"  use full updates only = <boolean> true\n" + 
				"}\n" + 
				"\n" + 
				"2019-01-04 12:57:44.885503 CET tbrx0apcm01vs tb_test_tp@tb_test/tb_test_tp[39102:39463] protocol.out <debug> src/common.cpp(98): Send StreamOpenReply to tb_test_ae@tb_test [local 10.240.217.16:57820]\n" + 
				"<message> (1006) StreamOpenReply, <protocol> (18) TradeProtocol, <size> [55]\n" + 
				"{\n" + 
				"  pet = <uuid> <empty>\n" + 
				"  status = <status> Ok\n" + 
				"  stream identifier = <uuid> f26a3b96-1017-11e9-00da-58a7ff7f0000\n" + 
				"}\n" + 
				"";
		final String expected = "[{\"timestamp\":\"2019-01-04 12:57:44.885409 CET\",\"host\":\"tbrx0apcm01vs\",\"localComponent\":{\"component\":\"tb_test_tp\",\"system\":\"tb_test\"},\"direction\":\"in\",\"remoteComponent\":{\"component\":\"tb_test_ae\",\"system\":\"tb_test\"},\"message\":{\"messageType\":\"StreamOpenRequest\",\"protocolType\":\"TradeProtocol\",\"operator\":\"DataEnricher (f391c898-328e-11e8-831f-a6db3c870c3e)\",\"pet\":null,\"stream identifier\":\"f26a3b96-1017-11e9-00da-58a7ff7f0000\",\"stream message identifiers\":[\"Trade\"],\"subscription type\":\"SnapshotAndLive\",\"suppress reply\":false,\"use full updates only\":true}},{\"timestamp\":\"2019-01-04 12:57:44.885503 CET\",\"host\":\"tbrx0apcm01vs\",\"localComponent\":{\"component\":\"tb_test_tp\",\"system\":\"tb_test\"},\"direction\":\"out\",\"remoteComponent\":{\"component\":\"tb_test_ae\",\"system\":\"tb_test\"},\"message\":{\"messageType\":\"StreamOpenReply\",\"protocolType\":\"TradeProtocol\",\"pet\":null,\"status\":\"Ok\",\"stream identifier\":\"f26a3b96-1017-11e9-00da-58a7ff7f0000\"}}]";
		testTraslateRule(source, expected, parser -> parser.file());
	}

	@Test
	public void testLogEntry() {
		final String source = "2019-01-04 12:57:44.885409 CET tbrx0apcm01vs tb_test_tp@tb_test/tb_test_tp[39102:39463] protocol.in <debug> src/common.cpp(74): Received StreamOpenRequest from tb_test_ae@tb_test [local 10.240.217.16:57820]\n" + 
				"<message> (138) StreamOpenRequest, <protocol> (18) TradeProtocol, <size> [186]\n" + 
				"{\n" + 
				"  operator = <uuid> DataEnricher (f391c898-328e-11e8-831f-a6db3c870c3e)\n" + 
				"  pet = <uuid> <empty>\n" + 
				"  stream identifier = <uuid> f26a3b96-1017-11e9-00da-58a7ff7f0000\n" + 
				"  stream message identifiers = {'Trade'}\n" + 
				"  subscription type = <subscription type> SnapshotAndLive\n" + 
				"  suppress reply = <boolean> false\n" + 
				"  use full updates only = <boolean> true\n" + 
				"}";
		final String expected = "{\"timestamp\":\"2019-01-04 12:57:44.885409 CET\",\"host\":\"tbrx0apcm01vs\",\"localComponent\":{\"component\":\"tb_test_tp\",\"system\":\"tb_test\"},\"direction\":\"in\",\"remoteComponent\":{\"component\":\"tb_test_ae\",\"system\":\"tb_test\"},\"message\":{\"messageType\":\"StreamOpenRequest\",\"protocolType\":\"TradeProtocol\",\"operator\":\"DataEnricher (f391c898-328e-11e8-831f-a6db3c870c3e)\",\"pet\":null,\"stream identifier\":\"f26a3b96-1017-11e9-00da-58a7ff7f0000\",\"stream message identifiers\":[\"Trade\"],\"subscription type\":\"SnapshotAndLive\",\"suppress reply\":false,\"use full updates only\":true}}";
		testTraslateRule(source, expected, parser -> parser.logEntry());
	}

	@Test
	public void testHeader() {
		final String source = "2018-12-19 15:18:14.798058 CET tbrx0apcm01vs tb_test_ae2@tb_test/tb_test_ae2[54429:55007] protocol.out <debug> src/common.cpp(98): Send StreamOpenRequest to tb_test_ce2-ce@tb_test [local 10.240.217.16:50001]";
		final String expected = "\"timestamp\":\"2018-12-19 15:18:14.798058 CET\",\"host\":\"tbrx0apcm01vs\",\"localComponent\":{\"component\":\"tb_test_ae2\",\"system\":\"tb_test\"},\"direction\":\"out\",\"remoteComponent\":{\"component\":\"tb_test_ce2-ce\",\"system\":\"tb_test\"}";
		testTraslateRule(source, expected, parser -> parser.header());
	}

	@Test
	public void testHost() {
		final String source = "tbrx0apcm01vs";
		final String expected = "\"host\":\"tbrx0apcm01vs\"";
		testTraslateRule(source, expected, parser -> parser.host());
	}

	@Test
	public void testComponent() {
		final String source = "tb_test_ae2@tb_test/tb_test_ae2";
		final String expected = "{\"component\":\"tb_test_ae2\",\"system\":\"tb_test\"}";
		testTraslateRule(source, expected, parser -> parser.component());
	}

	@Test
	public void testComponentName() {
		final String source = "tb_test_ae2";
		final String expected = "\"component\":\"tb_test_ae2\"";
		testTraslateRule(source, expected, parser -> parser.componentName());
	}

	@Test
	public void testSystemName() {
		final String source = "tb_test";
		final String expected = "\"system\":\"tb_test\"";
		testTraslateRule(source, expected, parser -> parser.systemName());
	}

	@Test
	public void testLocalComponent() {
		final String source = "tb_test_ae2@tb_test/tb_test_ae2";
		final String expected = "\"localComponent\":{\"component\":\"tb_test_ae2\",\"system\":\"tb_test\"}";
		testTraslateRule(source, expected, parser -> parser.localComponent());
	}

	@Test
	public void testRemoteComponent() {
		final String source = "tb_test_ae2@tb_test/tb_test_ae2";
		final String expected = "\"remoteComponent\":{\"component\":\"tb_test_ae2\",\"system\":\"tb_test\"}";
		testTraslateRule(source, expected, parser -> parser.remoteComponent());
	}

	@Test
	public void testDirection() {
		final String source = "in";
		final String expected = "\"direction\":\"in\"";
		testTraslateRule(source, expected, parser -> parser.direction());
	}

	@Test
	public void testMessageType() {
		final String source = "InstrumentStatus";
		final String expected = "\"messageType\":\"InstrumentStatus\"";
		testTraslateRule(source, expected, parser -> parser.messageType());
	}

	@Test
	public void testMessage() {
		final String source = "<message> (138) StreamOpenRequest, <protocol> (63) CalculatedValuesProtocol, <size> [785]\n"
				+ "{\n"
				+ "  calculated values request = <calculated values request> \n" + "  type = full\n"
				+ "  Columns [11] =\n"
				+ "     [Present value adjustment factor (80edca0e-3c32-11e1-bccb-99245e0d1c06)]: UPDATE: {Present value adjustment factor (80edca0e-3c32-11e1-bccb-99245e0d1c06)\n"
				+ "  }\n"
				+ "     [Rho (36b677f4-2f5e-4baf-a57a-3dd93304499b)]: UPDATE: {Rho (36b677f4-2f5e-4baf-a57a-3dd93304499b)\n"
				+ "  }\n"
				+ "     [Yield (40009b86-2b19-11e1-933b-47deb3124dc8)]: UPDATE: {Yield (40009b86-2b19-11e1-933b-47deb3124dc8)\n"
				+ "  }\n"
				+ "     [Forward price (db1b9115-347c-4c4a-a796-9e493518f705)]: UPDATE: {Forward price (db1b9115-347c-4c4a-a796-9e493518f705)\n"
				+ "  }\n"
				+ "     [Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)]: UPDATE: {Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)\n"
				+ "  }\n"
				+ "     [Gamma (8d2eb853-fb2f-4e11-9e75-64e414131951)]: UPDATE: {Gamma (8d2eb853-fb2f-4e11-9e75-64e414131951)\n"
				+ "  }\n"
				+ "     [Vega (775faa53-667f-4674-a875-66ab1f18b684)]: UPDATE: {Vega (775faa53-667f-4674-a875-66ab1f18b684)\n"
				+ "  }\n"
				+ "     [Theta (de75f175-d03f-11e3-bccb-3e99245e0d1c)]: UPDATE: {Theta (de75f175-d03f-11e3-bccb-3e99245e0d1c)\n"
				+ "  }\n"
				+ "     [Financing rate (35e24161-94c7-413c-9ca2-750b11875ee9)]: UPDATE: {Financing rate (35e24161-94c7-413c-9ca2-750b11875ee9)\n"
				+ "  }\n"
				+ "     [Fair market price (33efb879-baf3-4ce1-bec0-8600f21af8cd)]: UPDATE: {Fair market price (33efb879-baf3-4ce1-bec0-8600f21af8cd)\n"
				+ "  }\n"
				+ "  Rows [1] =\n"
				+ "     [0b981bca-2093-11e9-b23a-4bc1bb59529c]: UPDATE: ivid:{instrument:f06153e4-9654-11e8-96b0-c53d96bb3220}\n"
				+ "  Overrides [1] =\n"
				+ "     [f06153e4-9654-11e8-96b0-c53d96bb3220]: UPDATE: {\n"
				+ "    instrument parameters = [3] {\n"
				+ "      Instrument (4862f28d-10ab-46f1-93c5-4c97e2048368) = <uuid> f06153e4-9654-11e8-96b0-c53d96bb3220\n"
				+ "      Bid override price (88cf4d04-9f33-4e20-97e5-359cb9c1d66e) = <double> 1446.7\n"
				+ "      Ask override price (8c2e857b-3146-4eb7-9862-d85466993f01) = <double> 1446.7\n" + "    }\n"
				+ "  }\n"
				+ "  filter = <filter> <empty>\n"
				+ "  operator = <uuid> DataEnricher (f391c898-328e-11e8-831f-a6db3c870c3e)\n"
				+ "  parameter context ranking identifier = <uuid> Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)\n"
				+ "  pet = <uuid> <empty>\n"
				+ "  stream identifier = <uuid> 0b9841a4-2093-11e9-007c-7e66ff7f0000\n"
				+ "  stream message identifiers = {'CalculatedValues'}\n"
				+ "  subscription type = <subscription type> SnapshotAndLive\n" + "  suppress reply = <boolean> false\n"
				+ "  user identifier = <uuid> DataEnricher (f391c898-328e-11e8-831f-a6db3c870c3e)\n" + "}";
		final String expected = "{\"messageType\":\"StreamOpenRequest\",\"protocolType\":\"CalculatedValuesProtocol\",\"calculated values request\":\"<calculated values request>\",\"type\":\"full\",\"filter\":null,\"operator\":\"DataEnricher (f391c898-328e-11e8-831f-a6db3c870c3e)\",\"parameter context ranking identifier\":\"Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)\",\"pet\":null,\"stream identifier\":\"0b9841a4-2093-11e9-007c-7e66ff7f0000\",\"stream message identifiers\":[\"CalculatedValues\"],\"subscription type\":\"SnapshotAndLive\",\"suppress reply\":false,\"user identifier\":\"DataEnricher (f391c898-328e-11e8-831f-a6db3c870c3e)\",\"Columns\":[\"Present value adjustment factor (80edca0e-3c32-11e1-bccb-99245e0d1c06)\",\"Rho (36b677f4-2f5e-4baf-a57a-3dd93304499b)\",\"Yield (40009b86-2b19-11e1-933b-47deb3124dc8)\",\"Forward price (db1b9115-347c-4c4a-a796-9e493518f705)\",\"Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)\",\"Gamma (8d2eb853-fb2f-4e11-9e75-64e414131951)\",\"Vega (775faa53-667f-4674-a875-66ab1f18b684)\",\"Theta (de75f175-d03f-11e3-bccb-3e99245e0d1c)\",\"Financing rate (35e24161-94c7-413c-9ca2-750b11875ee9)\",\"Fair market price (33efb879-baf3-4ce1-bec0-8600f21af8cd)\"],\"Rows\":[{\"0b981bca-2093-11e9-b23a-4bc1bb59529c\":{\"ivid\":{\"instrument\":\"f06153e4-9654-11e8-96b0-c53d96bb3220\"}}}],\"Overrides\":{\"rowId\":\"f06153e4-9654-11e8-96b0-c53d96bb3220\",\"instrument parameters\":{\"Instrument (4862f28d-10ab-46f1-93c5-4c97e2048368)\":\"f06153e4-9654-11e8-96b0-c53d96bb3220\",\"Bid override price (88cf4d04-9f33-4e20-97e5-359cb9c1d66e)\":1446.7,\"Ask override price (8c2e857b-3146-4eb7-9862-d85466993f01)\":1446.7}}}";
		testTraslateRule(source, expected, parser -> parser.message());
	}

	@Test
	public void testMessageHeader() {
		final String source = "<message> (138) StreamOpenRequest, <protocol> (63) CalculatedValuesProtocol, <size> [401]\n";
		final String expected = "\"messageType\":\"StreamOpenRequest\",\"protocolType\":\"CalculatedValuesProtocol\"";
		testTraslateRule(source, expected, parser -> parser.messageHeader());
	}

	@Test
	public void testObjectBody() {
		final String source = "{\n" + "  pet = <uuid> <empty>\n" + "  status = <status> Ok\n"
				+ "  stream identifier = <uuid> ec6c636c-0398-11e9-0040-8155ff7f0000\n" + "}";
		final String expected = "\"pet\":null,\"status\":\"Ok\",\"stream identifier\":\"ec6c636c-0398-11e9-0040-8155ff7f0000\"";
		testTraslateRule(source, expected, parser -> parser.objectBody());
	}

	@Test
	public void testProtocolType() {
		final String source = "MarketDataProtocol";
		final String expected = "\"protocolType\":\"MarketDataProtocol\"";
		testTraslateRule(source, expected, parser -> parser.protocolType());
	}

	@Test
	public void testComponent_NameString() {
		final String source = "tb_test_ce2";
		final String expected = "\"component\":\"tb_test_ce2\"";
		testTraslateRule(source, expected, parser -> parser.componentName());
	}

	@Test
	public void testComponentName_DashedString() {
		final String source = "tb_test_ce2-ce";
		final String expected = "\"component\":\"tb_test_ce2-ce\"";
		testTraslateRule(source, expected, parser -> parser.componentName());
	}

	@Test
	public void testPair_KeyAsString() {
		final String source = "stream item identifier = <uuid> 5cb0999e-2064-11e9-9750-6de498ccf87f";
		final String expected = "\"stream item identifier\":\"5cb0999e-2064-11e9-9750-6de498ccf87f\"";
		testTraslateRule(source, expected, parser -> parser.pair());
	}

	@Test
	public void testPair_KeyAsNamedUuid() {
		final String source = "Currency (82583bd9-4465-4ac6-83aa-ab76a6b8da9a) = <empty>";
		final String expected = "\"Currency (82583bd9-4465-4ac6-83aa-ab76a6b8da9a)\":null";
		testTraslateRule(source, expected, parser -> parser.pair());
	}

	@Test
	public void testPair_KeyAsUuid() {
		final String source = "fc44f1dd-aeb7-11e7-993e-529049f1f1bb = <string> \"Manual\"";
		final String expected = "\"fc44f1dd-aeb7-11e7-993e-529049f1f1bb\":\"Manual\"";
		testTraslateRule(source, expected, parser -> parser.pair());
	}

	@Test
	public void testViidValue() {
		final String source = "<viid> venue=Xetra T7 (7670ae32-46b3-11e7-a020-bedec25e9b91);instrumentid=485026;instrumenttype=1;nolegs=0;productid=20054;volumefactor=1{XETR}";
		final String expected = "{\"venue\":\"Xetra T7 (7670ae32-46b3-11e7-a020-bedec25e9b91)\",\"instrumentid\":485026,\"instrumenttype\":1,\"nolegs\":0,\"productid\":20054,\"volumefactor\":1,\"mic\":\"XETR\"}";
		testTraslateRule(source, expected, parser -> parser.viidValue());
	}

	@Test
	public void testTableValue() {
		final String source = "<table values> [2]\n" + 
				"[54b136cc-2091-11e9-bd60-e7ffa3dffd67]:\n" + 
				"          [Overnight financing factor (94c11267-2b0a-11e1-933b-47deb3124dc8)] = <double> -1.0555664200051496e-05\n" + 
				"          [Present value adjustment factor (80edca0e-3c32-11e1-bccb-99245e0d1c06)] = <double> 1\n" + 
				"[54b3fb50-2091-11e9-bd60-e7ffa3dffd67]:\n" + 
				"          [Overnight financing factor (94c11267-2b0a-11e1-933b-47deb3124dc8)] = <double> -1.0555664200051496e-05\n" + 
				"          [Present value adjustment factor (80edca0e-3c32-11e1-bccb-99245e0d1c06)] = <double> 1";
		final String expected = "[{\"rowId\":\"54b136cc-2091-11e9-bd60-e7ffa3dffd67\",\"columns\":[{\"column\":\"Overnight financing factor (94c11267-2b0a-11e1-933b-47deb3124dc8)\",\"value\":-1.0555664200051496e-05},{\"column\":\"Present value adjustment factor (80edca0e-3c32-11e1-bccb-99245e0d1c06)\",\"value\":1}]},{\"rowId\":\"54b3fb50-2091-11e9-bd60-e7ffa3dffd67\",\"columns\":[{\"column\":\"Overnight financing factor (94c11267-2b0a-11e1-933b-47deb3124dc8)\",\"value\":-1.0555664200051496e-05},{\"column\":\"Present value adjustment factor (80edca0e-3c32-11e1-bccb-99245e0d1c06)\",\"value\":1}]}]";
		testTraslateRule(source, expected, parser -> parser.tableValue());
	}

	@Test
	public void testRow() {
		final String source = "[54b136cc-2091-11e9-bd60-e7ffa3dffd67]:\n" + 
				"          [Overnight financing factor (94c11267-2b0a-11e1-933b-47deb3124dc8)] = <double> -1.0555664200051496e-05\n" + 
				"          [Present value adjustment factor (80edca0e-3c32-11e1-bccb-99245e0d1c06)] = <double> 1";
		final String expected = "{\"rowId\":\"54b136cc-2091-11e9-bd60-e7ffa3dffd67\",\"columns\":[{\"column\":\"Overnight financing factor (94c11267-2b0a-11e1-933b-47deb3124dc8)\",\"value\":-1.0555664200051496e-05},{\"column\":\"Present value adjustment factor (80edca0e-3c32-11e1-bccb-99245e0d1c06)\",\"value\":1}]}";
		testTraslateRule(source, expected, parser -> parser.row());
	}

	@Test
	public void testColumn() {
		final String source = "[Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)] = <error double> \"Instrument definitions not received\"";
		final String expected = "{\"column\":\"Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)\",\"value\":\"Instrument definitions not received\"}";
		testTraslateRule(source, expected, parser -> parser.column());
	}

	@Test
	public void testFieldKey() {
		final String source = "instrument parameters";
		final String expected = "\"instrument parameters\"";
		testTraslateRule(source, expected, parser -> parser.fieldKey());
	}

	@Test
	public void testValue_Status() {
		final String source = "<status> Ok";
		final String expected = "\"Ok\"";
		testTraslateRule(source, expected, parser -> parser.value());
	}

	@Test
	public void testValue_Integer() {
		final String source = "<integer> 3";
		final String expected = "3";
		testTraslateRule(source, expected, parser -> parser.value());
	}

	@Test
	public void testValue_Uuid() {
		final String source = "<uuid> 54b148ce-2091-11e9-bd60-e7ffa3dffd67";
		final String expected = "\"54b148ce-2091-11e9-bd60-e7ffa3dffd67\"";
		testTraslateRule(source, expected, parser -> parser.value());
	}

	@Test
	public void testAnyValue_Integer() {
		final String source = "<any> <integer> 2";
		final String expected = "2";
		testTraslateRule(source, expected, parser -> parser.anyValue());
	}

	@Test
	public void testUuidValue_TypedUuid() {
		final String source = "<uuid> 54b148ce-2091-11e9-bd60-e7ffa3dffd67";
		final String expected = "\"54b148ce-2091-11e9-bd60-e7ffa3dffd67\"";
		testTraslateRule(source, expected, parser -> parser.uuidValue());
	}

	@Test
	public void testUuidValue_Uuid() {
		final String source = "54b148ce-2091-11e9-bd60-e7ffa3dffd67";
		final String expected = "\"54b148ce-2091-11e9-bd60-e7ffa3dffd67\"";
		testTraslateRule(source, expected, parser -> parser.uuidValue());
	}

	@Test
	public void testUuidValue_TypedNamedUuid() {
		final String source = "<uuid> Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)";
		final String expected = "\"Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)\"";
		testTraslateRule(source, expected, parser -> parser.uuidValue());
	}

	@Test
	public void testUuidValue_NamedUuid() {
		final String source = "Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)";
		final String expected = "\"Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)\"";
		testTraslateRule(source, expected, parser -> parser.uuidValue());
	}

	@Test
	public void testUuidValue_Empty() {
		final String source = "<uuid> <empty>";
		final String expected = "null";
		testTraslateRule(source, expected, parser -> parser.uuidValue());
	}

	@Test
	public void testNamedUuid() {
		final String source = "Host modified datetime (434d98ff-23b7-4489-81a0-188fedf4728a)";
		final String expected = "\"Host modified datetime (434d98ff-23b7-4489-81a0-188fedf4728a)\"";
		testTraslateRule(source, expected, parser -> parser.namedUuid());
	}

	@Test
	public void testEnumTypedValue_Trend() {
		final String source = "<trend> Minus";
		final String expected = "\"Minus\"";
		testTraslateRule(source, expected, parser -> parser.enumValue());
	}

	@Test
	public void testEnumTypedValue_Status() {
		final String source = "<status> Ok";
		final String expected = "\"Ok\"";
		testTraslateRule(source, expected, parser -> parser.enumValue());
	}

	@Test
	public void testIntegerValue_Integer() {
		final String source = "<integer> 3";
		final String expected = "3";
		testTraslateRule(source, expected, parser -> parser.integerValue());
	}

	@Test
	public void testIntegerValue_Uint32() {
		final String source = "<uint32> 250000";
		final String expected = "250000";
		testTraslateRule(source, expected, parser -> parser.integerValue());
	}

	@Test
	public void testDatetimeValue() {
		final String source = "<datetime> 2019-01-03 22:45:15.293281 CET";
		final String expected = "\"2019-01-03 22:45:15.293281 CET\"";
		testTraslateRule(source, expected, parser -> parser.datetimeValue());
	}

	@Test
	public void testTimestampValue() {
		final String source = "2019-01-03 22:45:15.293281 CET";
		final String expected = "\"2019-01-03 22:45:15.293281 CET\"";
		testTraslateRule(source, expected, parser -> parser.timestampValue());
	}

	@Test
	public void testStreamMessageIdentifiersValue() {
		final String source = "{'Trade'}";
		final String expected = "[\"Trade\"]";
		testTraslateRule(source, expected, parser -> parser.streamMessageIdentifiersValue());
	}

	@Test
	public void testStringValue() {
		final String source = "<string> \"Extraction at time 22:05:00 completed\"";
		final String expected = "\"Extraction at time 22:05:00 completed\"";
		testTraslateRule(source, expected, parser -> parser.stringValue());
	}

	@Test
	public void testDoubleValue_Double() {
		final String source = "<double> 0.87687867";
		final String expected = "0.87687867";
		testTraslateRule(source, expected, parser -> parser.doubleValue());
	}

	@Test
	public void testDoubleValue_Integer() {
		final String source = "<double> 34";
		final String expected = "34";
		testTraslateRule(source, expected, parser -> parser.doubleValue());
	}

	// TODO testFilterValue

	@Test
	public void testBooleanValue() {
		final String source = "<boolean> false";
		final String expected = "false";
		testTraslateRule(source, expected, parser -> parser.booleanValue());
	}

	@Test
	public void testRequestTypeValue() {
		final String source = "<calculated values request>";
		final String expected = "\"<calculated values request>\"";
		testTraslateRule(source, expected, parser -> parser.requestTypeValue());
	}

	@Test
	public void testErrorValue() {
		final String source = "<error double> \"Instrument definitions not received\"";
		final String expected = "\"Instrument definitions not received\"";
		testTraslateRule(source, expected, parser -> parser.errorValue());
	}

	@Test
	public void testVectorValue() {
		final String source = "<vector of boolean> [2]\n" + "    0 = 1\n" + "    1 = 0";
		final String expected = "[true,false]";
		testTraslateRule(source, expected, parser -> parser.vectorValue());
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
	public void testBooleanVector() {
		final String source = "<vector of boolean> [2]\n" + "    0 = 1\n" + "    1 = 0";
		final String expected = "[true,false]";
		testTraslateRule(source, expected, parser -> parser.booleanVector());
	}

	@Test
	public void testIntegerVectorPair() {
		final String source = "1 = 0";
		final String expected = "0";
		testTraslateRule(source, expected, parser -> parser.integerVectorPair());
	}

	@Test
	public void testUint32Vector() {
		final String source = "<vector of uint32> [4]\n" + "    0 = 0\n" + "    1 = 0\n" + "    2 = 0\n" + "    3 = 0";
		final String expected = "[0,0,0,0]";
		testTraslateRule(source, expected, parser -> parser.uint32Vector());
	}

	@Test
	public void testAnyVector() {
		final String source = "<vector of any> [4]\n" + "    0 = <integer> 1\n"
				+ "    1 = <datetime> 2019-01-03 22:45:16.841148 CET\n" + "    2 = <uinteger> 481\n"
				+ "    3 = <string> \"Extraction at time 22:45:15 completed\"";
		final String expected = "[1,\"2019-01-03 22:45:16.841148 CET\",481,\"Extraction at time 22:45:15 completed\"]";
		testTraslateRule(source, expected, parser -> parser.anyVector());
	}

	@Test
	public void testMessageVector() {
		final String source = "<vector of message> [3]\n" + 
				"  0 = <message> (201) GridViewFilterElement, <size> [74]\n" + 
				"	  {\n" + 
				"		grid view filter condition = <message> (202) GridViewFilterCondition, <size> [61]\n" + 
				"		{\n" + 
				"		  grid view field = <message> (30) GridViewField, <size> [41]\n" + 
				"		  {\n" + 
				"			grid view field value = <any> <integer> 1\n" + 
				"			grid view property identifier = <uuid> State (ebd07201-bd28-4079-b526-e99b9287ca81)\n" + 
				"		  }\n" + 
				"		  grid view filter condition type = <grid view filter condition type> Equal\n" + 
				"		}\n" + 
				"	  }\n" + 
				"  1 = <message> (201) GridViewFilterElement, <size> [74]\n" + 
				"	  {\n" + 
				"		grid view filter condition = <message> (202) GridViewFilterCondition, <size> [61]\n" + 
				"		{\n" + 
				"		  grid view field = <message> (30) GridViewField, <size> [41]\n" + 
				"		  {\n" + 
				"			grid view field value = <any> <integer> 1\n" + 
				"			grid view property identifier = <uuid> Transaction state (32cba9f8-87dd-423d-94f4-f6080c457234)\n" + 
				"		  }\n" + 
				"		  grid view filter condition type = <grid view filter condition type> Equal\n" + 
				"		}\n" + 
				"	  }\n" + 
				"  2 = <message> (201) GridViewFilterElement, <size> [74]\n" + 
				"	  {\n" + 
				"		grid view filter condition = <message> (202) GridViewFilterCondition, <size> [61]\n" + 
				"		{\n" + 
				"		  grid view field = <message> (30) GridViewField, <size> [41]\n" + 
				"		  {\n" + 
				"			grid view field value = <any> <integer> 2\n" + 
				"			grid view property identifier = <uuid> Transaction operation (5fede20f-a105-406a-8fca-79b06afaacbd)\n" + 
				"		  }\n" + 
				"		  grid view filter condition type = <grid view filter condition type> Equal\n" + 
				"		}\n" + 
				"	  }";
		final String expected = "[{\"messageType\":\"GridViewFilterElement\",\"grid view filter condition\":{\"messageType\":\"GridViewFilterCondition\",\"grid view field\":{\"messageType\":\"GridViewField\",\"grid view field value\":1,\"grid view property identifier\":\"State (ebd07201-bd28-4079-b526-e99b9287ca81)\"},\"grid view filter condition type\":\"Equal\"}},{\"messageType\":\"GridViewFilterElement\",\"grid view filter condition\":{\"messageType\":\"GridViewFilterCondition\",\"grid view field\":{\"messageType\":\"GridViewField\",\"grid view field value\":1,\"grid view property identifier\":\"Transaction state (32cba9f8-87dd-423d-94f4-f6080c457234)\"},\"grid view filter condition type\":\"Equal\"}},{\"messageType\":\"GridViewFilterElement\",\"grid view filter condition\":{\"messageType\":\"GridViewFilterCondition\",\"grid view field\":{\"messageType\":\"GridViewField\",\"grid view field value\":2,\"grid view property identifier\":\"Transaction operation (5fede20f-a105-406a-8fca-79b06afaacbd)\"},\"grid view filter condition type\":\"Equal\"}}]";
		testTraslateRule(source, expected, parser -> parser.messageVector());
	}

	@Test
	public void testParametersValue() {
		final String source = "<parameters> [14] {\n" + 
				"    Dividend instrument (2996f7af-b4f9-11df-b1f1-0836482d5ab0) = <uuid> f05664de-9654-11e8-96b0-c53d96bb3220\n" + 
				"    Fair bid price adjustment (2164f3f8-4586-11e0-b3de-245e0d1c06b7) = <double> 0\n" + 
				"    Fair ask price adjustment (94130584-4586-11e0-b3de-245e0d1c06b7) = <double> 0\n" + 
				"    Fair bid volatility adjustment (bdab5f2c-4586-11e0-b3de-245e0d1c06b7) = <double> -0.01\n" + 
				"    Fair ask volatility adjustment (c314a798-4586-11e0-b3de-245e0d1c06b7) = <double> 0.01\n" + 
				"    Settlement days (5ae4e74a-280d-11e1-bb43-2923be84e16c) = <integer> 2\n" + 
				"    Convenience yield (7c9cb3ec-2b12-11e1-933b-47deb3124dc8) = <double> 0\n" + 
				"    Pricing model ranking (71ed8a32-fa82-11e2-b3de-245e0d1c06b7) = <uuid> tbrx0 Pricing Model (e62e5c6a-b502-11e8-8c87-ae529049f1f1)\n" + 
				"    ZCYC override group (2dd8d4c0-cbed-11e5-9f25-870c3e99245e) = <uuid> EUR_EUR yield curve (d476ec7a-965e-11e8-9110-65008bc7fd23)\n" + 
				"    Fair price multiplier (101a01cb-de39-11e6-8a89-0d1c06b747de) = <double> 1\n" + 
				"    Final settlement days (556709ac-9497-11e7-993e-529049f1f1bb) = <integer> 2\n" + 
				"    Underlying instrument (4727abae-6a17-42e0-9bc0-45425e005893) = <uuid> f05664de-9654-11e8-96b0-c53d96bb3220\n" + 
				"    Bid override price (88cf4d04-9f33-4e20-97e5-359cb9c1d66e) = <double> 11.109999999999999\n" + 
				"    Ask override price (8c2e857b-3146-4eb7-9862-d85466993f01) = <double> 1.1200000000000001\n" + 
				"  }";
		final String expected = "{\"Dividend instrument (2996f7af-b4f9-11df-b1f1-0836482d5ab0)\":\"f05664de-9654-11e8-96b0-c53d96bb3220\",\"Fair bid price adjustment (2164f3f8-4586-11e0-b3de-245e0d1c06b7)\":0,\"Fair ask price adjustment (94130584-4586-11e0-b3de-245e0d1c06b7)\":0,\"Fair bid volatility adjustment (bdab5f2c-4586-11e0-b3de-245e0d1c06b7)\":-0.01,\"Fair ask volatility adjustment (c314a798-4586-11e0-b3de-245e0d1c06b7)\":0.01,\"Settlement days (5ae4e74a-280d-11e1-bb43-2923be84e16c)\":2,\"Convenience yield (7c9cb3ec-2b12-11e1-933b-47deb3124dc8)\":0,\"Pricing model ranking (71ed8a32-fa82-11e2-b3de-245e0d1c06b7)\":\"tbrx0 Pricing Model (e62e5c6a-b502-11e8-8c87-ae529049f1f1)\",\"ZCYC override group (2dd8d4c0-cbed-11e5-9f25-870c3e99245e)\":\"EUR_EUR yield curve (d476ec7a-965e-11e8-9110-65008bc7fd23)\",\"Fair price multiplier (101a01cb-de39-11e6-8a89-0d1c06b747de)\":1,\"Final settlement days (556709ac-9497-11e7-993e-529049f1f1bb)\":2,\"Underlying instrument (4727abae-6a17-42e0-9bc0-45425e005893)\":\"f05664de-9654-11e8-96b0-c53d96bb3220\",\"Bid override price (88cf4d04-9f33-4e20-97e5-359cb9c1d66e)\":11.109999999999999,\"Ask override price (8c2e857b-3146-4eb7-9862-d85466993f01)\":1.1200000000000001}";
		testTraslateRule(source, expected, parser -> parser.parametersValue());
	}

	@Test
	public void testParametersValue_Empty() {
		final String source = "<parameters> <empty>";
		final String expected = "null";
		testTraslateRule(source, expected, parser -> parser.parametersValue());
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
	public void testColumnRequest() {
		final String source = "[Underlying price (b320a884-d8e4-4a0a-9cec-d6ace19f04ee)]: UPDATE: {Underlying price (b320a884-d8e4-4a0a-9cec-d6ace19f04ee)\n}";
		final String expected = "\"Underlying price (b320a884-d8e4-4a0a-9cec-d6ace19f04ee)\"";
		testTraslateRule(source, expected, parser -> parser.columnRequest());
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
	public void testRowRequestsList() {
		final String source = "Rows [10] =\n"
				+ "    [0c909246-2093-11e9-bd60-e7ffa3dffd67]: UPDATE: ivid:{instrument:f8b279be-9655-11e8-96b0-c53d96bb3220}\n"
				+ "    [0c90f48e-2093-11e9-bd60-e7ffa3dffd67]: UPDATE: ivid:{instrument:7b983838-e439-11e8-abb0-d32a28bad6d5}";
		final String expected = "\"Rows\":[{\"0c909246-2093-11e9-bd60-e7ffa3dffd67\":{\"ivid\":{\"instrument\":\"f8b279be-9655-11e8-96b0-c53d96bb3220\"}}},{\"0c90f48e-2093-11e9-bd60-e7ffa3dffd67\":{\"ivid\":{\"instrument\":\"7b983838-e439-11e8-abb0-d32a28bad6d5\"}}}]";
		testTraslateRule(source, expected, parser -> parser.rowRequestsList());
	}

	@Test
	public void testRowRequest() {
		final String source = "[ec6c3f72-0398-11e9-3a85-8d362a43669d]: UPDATE: ivid:{instrument:2e79bd1a-b73e-11e8-a320-76ea49461d8a}";
		final String expected = "{\"ec6c3f72-0398-11e9-3a85-8d362a43669d\":{\"ivid\":{\"instrument\":\"2e79bd1a-b73e-11e8-a320-76ea49461d8a\"}}}";
		testTraslateRule(source, expected, parser -> parser.rowRequest());
	}

	@Test
	public void testRowRequest_WithParams() {
		final String source = "[0c909246-2093-11e9-bd60-e7ffa3dffd67]: UPDATE: {\n" + 
				"    ivid:{instrument:f8b279be-9655-11e8-96b0-c53d96bb3220}\n" + 
				"    parameters = [1] {\n" + 
				"      Parameter portfolio (658cf8b8-0329-11e3-b706-ae529049f1f1) = <uuid> 81a31e5a-1413-11e9-8d13-5e0d1c06b747\n" + 
				"    }\n" + 
				"  }";
		final String expected = "{\"0c909246-2093-11e9-bd60-e7ffa3dffd67\":{\"ivid\":{\"instrument\":\"f8b279be-9655-11e8-96b0-c53d96bb3220\"},\"parameters\":{\"Parameter portfolio (658cf8b8-0329-11e3-b706-ae529049f1f1)\":\"81a31e5a-1413-11e9-8d13-5e0d1c06b747\"}}}";
		testTraslateRule(source, expected, parser -> parser.rowRequest());
	}

	@Test
	public void testRowId() {
		final String source = "b320a884-d8e4-4a0a-9cec-d6ace19f04ee";
		final String expected = "\"b320a884-d8e4-4a0a-9cec-d6ace19f04ee\"";
		testTraslateRule(source, expected, parser -> parser.rowId());
	}

	@Test
	public void testColumnRequest_WithParams() {
		final String source = "[54b3a3e4-2091-11e9-bd60-e7ffa3dffd67]: UPDATE: {\n"
				+ "    id = Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)\n" + "    parameters = [1] {\n"
				+ "      Parameter context ranking (68aa0456-b6eb-11e1-9b1c-06b747deb312) = <uuid> Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)\n"
				+ "    }\n" + "  }";
		final String expected = "{\"columnRequestId\":\"54b3a3e4-2091-11e9-bd60-e7ffa3dffd67\",\"column\":\"Delta (46acd072-d577-443e-a13f-1ea90b5c62e1)\",\"parameters\":{\"Parameter context ranking (68aa0456-b6eb-11e1-9b1c-06b747deb312)\":\"Risk (586cf076-32d2-11e1-9cc9-eda4fff76405)\"}}";
		testTraslateRule(source, expected, parser -> parser.columnRequestWithParams());
	}

	@Test
	public void testRowRequestKey() {
		final String source = "ivid:{instrument:f044a41a-9654-11e8-96b0-c53d96bb3220;venue:0b97a38e-2093-11e9-004f-8b62ff7f0000}";
		final String expected = "\"ivid\":{\"instrument\":\"f044a41a-9654-11e8-96b0-c53d96bb3220\",\"venue\":\"0b97a38e-2093-11e9-004f-8b62ff7f0000\"}";
		testTraslateRule(source, expected, parser -> parser.rowRequestKey());
	}

	@Test
	public void testRowKeyValue_SingleField() {
		final String source = "{instrument:f044a41a-9654-11e8-96b0-c53d96bb3220}";
		final String expected = "{\"instrument\":\"f044a41a-9654-11e8-96b0-c53d96bb3220\"}";
		testTraslateRule(source, expected, parser -> parser.rowKeyValue());
	}

	@Test
	public void testRowKeyValue_MultipleFields() {
		final String source = "{instrument:f044a41a-9654-11e8-96b0-c53d96bb3220;venue:0b97a38e-2093-11e9-004f-8b62ff7f0000}";
		final String expected = "{\"instrument\":\"f044a41a-9654-11e8-96b0-c53d96bb3220\",\"venue\":\"0b97a38e-2093-11e9-004f-8b62ff7f0000\"}";
		testTraslateRule(source, expected, parser -> parser.rowKeyValue());
	}

	@Test
	public void testOverrides() {
		final String source = "Overrides [1] =\n" + "     [2e79bd1a-b73e-11e8-a320-76ea49461d8a]: UPDATE: {\n"
				+ "    instrument parameters = [3] {\n"
				+ "      Instrument (4862f28d-10ab-46f1-93c5-4c97e2048368) = <uuid> 2e79bd1a-b73e-11e8-a320-76ea49461d8a\n"
				+ "      Bid override price (88cf4d04-9f33-4e20-97e5-359cb9c1d66e) = <double> 0.57457111995836085\n"
				+ "      Ask override price (8c2e857b-3146-4eb7-9862-d85466993f01) = <double> 0.57457111995836085\n"
				+ "    }\n" + "  }";
		final String expected = "\"Overrides\":{\"rowId\":\"2e79bd1a-b73e-11e8-a320-76ea49461d8a\",\"instrument parameters\":{\"Instrument (4862f28d-10ab-46f1-93c5-4c97e2048368)\":\"2e79bd1a-b73e-11e8-a320-76ea49461d8a\",\"Bid override price (88cf4d04-9f33-4e20-97e5-359cb9c1d66e)\":0.57457111995836085,\"Ask override price (8c2e857b-3146-4eb7-9862-d85466993f01)\":0.57457111995836085}}";
		testTraslateRule(source, expected, parser -> parser.overrides());
	}

	@Test
	public void testOverriddenParameters() {
		final String source = "[3] {\n"
				+ "      Instrument (4862f28d-10ab-46f1-93c5-4c97e2048368) = <uuid> 2e79bd1a-b73e-11e8-a320-76ea49461d8a\n"
				+ "      Bid override price (88cf4d04-9f33-4e20-97e5-359cb9c1d66e) = <double> 0.57457111995836085\n"
				+ "      Ask override price (8c2e857b-3146-4eb7-9862-d85466993f01) = <double> 0.57457111995836085\n"
				+ "    }";
		final String expected = "{\"Instrument (4862f28d-10ab-46f1-93c5-4c97e2048368)\":\"2e79bd1a-b73e-11e8-a320-76ea49461d8a\",\"Bid override price (88cf4d04-9f33-4e20-97e5-359cb9c1d66e)\":0.57457111995836085,\"Ask override price (8c2e857b-3146-4eb7-9862-d85466993f01)\":0.57457111995836085}";
		testTraslateRule(source, expected, parser -> parser.overriddenParameters());
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
