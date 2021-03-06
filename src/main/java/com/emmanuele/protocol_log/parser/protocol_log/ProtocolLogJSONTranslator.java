package com.emmanuele.protocol_log.parser.protocol_log;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ProtocolLogJSONTranslator extends ProtocolLogBaseListener implements ProtocolLogListener {

	private static final String NULL_VALUE = "null";

	private static final String TRUE_VALUE = "true";
	private static final String FALSE_VALUE = "false";

	private static final String BRACES_PATTERN = "{%s}";
	private static final String ROUND_BRACKETS_PATTERN = "(%s)";
	private static final String SQUARE_BRACKETS_PATTERN = "[%s]";
	private static final String PAIR_PATTERN = "%s:%s";
	private static final String QUOTED_STRING_PATTERN = "\"%s\"";
	private static final String TIMESTAMP_PATTERN = "%s %s %s";

	ParseTreeProperty<String> json = new ParseTreeProperty<String>();

	String getJSON(ParseTree ctx) {
		return json.get(ctx);
	}

	void setJSON(ParseTree ctx, String s) {
		json.put(ctx, s);
	}

	// PARSE TREE ROOT

	@Override
	public void exitFile(ProtocolLogParser.FileContext ctx) {
		translateVector(ctx, ctx.logEntry());
	}

	// HEADER

	@Override
	public void exitLogEntry(ProtocolLogParser.LogEntryContext ctx) {
		final StringBuilder buf = new StringBuilder();
		buf.append(getJSON(ctx.header()));
		buf.append(",");
		buf.append(buildPair(addQuotes("message"), ctx.message()));
		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitHeader(ProtocolLogParser.HeaderContext ctx) {
		final StringBuilder buf = new StringBuilder();
		buf.append(buildPair(addQuotes("timestamp"), ctx.timestampValue()));
		buf.append(",");
		buf.append(getJSON(ctx.host()));
		buf.append(",");
		buf.append(getJSON(ctx.localComponent()));
		buf.append(",");
		buf.append(getJSON(ctx.direction()));
		buf.append(",");
		buf.append(getJSON(ctx.remoteComponent()));
		setJSON(ctx, buf.toString());
	}

	@Override
	public void exitTimestampValue(ProtocolLogParser.TimestampValueContext ctx) {
		final String timestampString = String.format(TIMESTAMP_PATTERN, ctx.DATE().getText(), ctx.TIME().getText(),
				ctx.STRING().getText());
		setJSON(ctx, addQuotes(timestampString));
	}

	@Override
	public void exitHost(ProtocolLogParser.HostContext ctx) {
		final String key = addQuotes("host");
		final String value = addQuotes(ctx.STRING().getText());
		setJSON(ctx, buildPair(key, value));
	}

	@Override
	public void exitLocalComponent(ProtocolLogParser.LocalComponentContext ctx) {
		setJSON(ctx, buildPair(addQuotes("localComponent"), getJSON(ctx.component())));
	}

	@Override
	public void exitComponent(ProtocolLogParser.ComponentContext ctx) {
		final StringBuilder buf = new StringBuilder();
		buf.append(getJSON(ctx.componentName(0)));
		final ProtocolLogParser.SystemNameContext systemName = ctx.systemName();
		if (systemName != null) {
			buf.append(",");
			buf.append(getJSON(ctx.systemName()));
		}
		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitComponentName(ProtocolLogParser.ComponentNameContext ctx) {
		final String key = addQuotes("component");
		final String value = addQuotes(ctx.getText());
		setJSON(ctx, buildPair(key, value));
	}

	@Override
	public void exitSystemName(ProtocolLogParser.SystemNameContext ctx) {
		setJSON(ctx, buildPair(addQuotes("system"), addQuotes(ctx.getText())));
	}

	@Override
	public void exitDirection(ProtocolLogParser.DirectionContext ctx) {
		setJSON(ctx, buildPair(addQuotes("direction"), addQuotes(ctx.getText())));
	}

	@Override
	public void exitMessageType(ProtocolLogParser.MessageTypeContext ctx) {
		setJSON(ctx, buildPair(addQuotes("messageType"), addQuotes(ctx.getText())));
	}

	@Override
	public void exitRemoteComponent(ProtocolLogParser.RemoteComponentContext ctx) {
		setJSON(ctx, buildPair(addQuotes("remoteComponent"), getJSON(ctx.component())));
	}

	// MESSAGE

	@Override
	public void exitMessage(ProtocolLogParser.MessageContext ctx) {
		final StringBuilder buf = new StringBuilder();
		buf.append(getJSON(ctx.messageHeader()));
		buf.append(",");
		buf.append(getJSON(ctx.objectBody()));
		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitMessageHeader(ProtocolLogParser.MessageHeaderContext ctx) {
		final StringBuilder buf = new StringBuilder();
		buf.append(getJSON(ctx.messageType()));
		final ProtocolLogParser.ProtocolTypeContext protocolType = ctx.protocolType();
		if (protocolType != null) {
			buf.append(",");
			buf.append(getJSON(protocolType));
		}
		setJSON(ctx, buf.toString());
	}

	@Override
	public void exitObjectBody(ProtocolLogParser.ObjectBodyContext ctx) {
		final StringBuilder buf = new StringBuilder();
		for (ProtocolLogParser.PairContext pair : ctx.pair()) {
			buf.append(getJSON(pair)).append(",");
		}
		for (ProtocolLogParser.ColumnsRequestContext pair : ctx.columnsRequest()) {
			buf.append(getJSON(pair)).append(",");
		}
		for (ProtocolLogParser.RowRequestsListContext pair : ctx.rowRequestsList()) {
			buf.append(getJSON(pair)).append(",");
		}
		for (ProtocolLogParser.OverridesContext pair : ctx.overrides()) {
			buf.append(getJSON(pair)).append(",");
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		setJSON(ctx, buf.toString());
	}

	@Override
	public void exitProtocolType(ProtocolLogParser.ProtocolTypeContext ctx) {
		setJSON(ctx, buildPair(addQuotes("protocolType"), addQuotes(ctx.getText())));
	}

	@Override
	public void exitPair(ProtocolLogParser.PairContext ctx) {
		String fieldKey = getJSON(ctx.fieldKey());
		if (fieldKey == null) {
			fieldKey = getJSON(ctx.uuidValue());
		}
		setJSON(ctx, buildPair(fieldKey, ctx.value()));
	}

	@Override
	public void exitFieldKey(ProtocolLogParser.FieldKeyContext ctx) {
		final StringBuilder buf = new StringBuilder();
		for (final TerminalNode str : ctx.STRING()) {
			buf.append(str.getText()).append(" ");
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		setJSON(ctx, addQuotes(buf.toString()));
	}

	// VALUES

	@Override
	public void exitValue(ProtocolLogParser.ValueContext ctx) {
		// Retrieving translation of child rule
		final ParserRuleContext child = ctx.getRuleContext(ParserRuleContext.class, 0);
		String value = getJSON(child);
		if (value == null) {
			TerminalNode terminalNode = ctx.STRING();
			if (terminalNode != null) {
				value = addQuotes(terminalNode.getText());
			}
		}
		setJSON(ctx, value);
	}

	@Override
	public void exitAnyValue(ProtocolLogParser.AnyValueContext ctx) {
		// Retrieving translation of child rule
		final ParserRuleContext value = ctx.getRuleContext(ParserRuleContext.class, 0);
		setJSON(ctx, getJSON(value));
	}

	@Override
	public void exitUuidValue(ProtocolLogParser.UuidValueContext ctx) {
		String value;
		TerminalNode uuid = ctx.UUID();
		if (uuid != null) {
			value = addQuotes(uuid.getText());
		} else {
			ProtocolLogParser.NamedUuidContext namedUuid = ctx.namedUuid();
			if (namedUuid != null) {
				value = getJSON(namedUuid);
			} else {
				value = NULL_VALUE;
			}
		}
		setJSON(ctx, value);
	}

	@Override
	public void exitNamedUuid(ProtocolLogParser.NamedUuidContext ctx) {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < ctx.getChildCount() - 3; i++) {
			buf.append(ctx.getChild(i).getText()).append(" ");
		}
		buf.append(addRoundBrackets(ctx.UUID().getText()));
		setJSON(ctx, addQuotes(buf.toString()));
	}

	@Override
	public void exitEnumValue(ProtocolLogParser.EnumValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.STRING().getText()));
	}

	@Override
	public void exitBooleanValue(ProtocolLogParser.BooleanValueContext ctx) {
		setJSON(ctx, ctx.STRING().getText());
	}

	@Override
	public void exitIntegerValue(ProtocolLogParser.IntegerValueContext ctx) {
		setJSON(ctx, ctx.INTEGER().getText());
	}

	@Override
	public void exitDatetimeValue(ProtocolLogParser.DatetimeValueContext ctx) {
		setJSON(ctx, getJSON(ctx.timestampValue()));
	}

	@Override
	public void exitStreamMessageIdentifiersValue(ProtocolLogParser.StreamMessageIdentifiersValueContext ctx) {
		final StringBuilder buf = new StringBuilder();
		for (final TerminalNode str : ctx.STRING()) {
			buf.append(addQuotes(str.getText())).append(",");
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		setJSON(ctx, addSquareBrackets(buf.toString()));
	}

	@Override
	public void exitStringValue(ProtocolLogParser.StringValueContext ctx) {
		setJSON(ctx, ctx.QUOTED_STRING().getText());
	}

	@Override
	public void exitDoubleValue(ProtocolLogParser.DoubleValueContext ctx) {
		String value = null;
		if (ctx.DOUBLE() != null) {
			value = ctx.DOUBLE().getText();
		}
		if (ctx.INTEGER() != null) {
			value = ctx.INTEGER().getText();
		}
		setJSON(ctx, value);
	}

	@Override
	public void exitFilterValue(ProtocolLogParser.FilterValueContext ctx) {
		final ParserRuleContext condition = ctx.condition();
		if (condition != null) {
			setJSON(ctx, getJSON(condition));
		} else {
			setJSON(ctx, NULL_VALUE);
		}
	}

	@Override
	public void exitCondition(ProtocolLogParser.ConditionContext ctx) {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof TerminalNode) {
				final String token = ((TerminalNode) ctx.getChild(i)).getText();
				if (!"'".equals(token)) {
					buf.append(token).append(" ");
				}
			} else {
				buf.append(getJSON(ctx.getChild(i))).append(" ");
			}
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		setJSON(ctx, buf.toString());
	}

	@Override
	public void exitRequestTypeValue(ProtocolLogParser.RequestTypeValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.getText()));
	}

	@Override
	public void exitErrorValue(ProtocolLogParser.ErrorValueContext ctx) {
		setJSON(ctx, ctx.QUOTED_STRING().getText());
	}

	@Override
	public void exitVectorValue(ProtocolLogParser.VectorValueContext ctx) {
		// Retrieving translation of child rule
//		final ParserRuleContext value = ctx.getRuleContext(ParserRuleContext.class, 0);
		List<? extends ParserRuleContext> values = ctx.value();
		if (values == null || values.isEmpty()) {
			values = ctx.row();
		}
		translateVector(ctx, values);
		String vectorValue = getJSON(ctx);
		final String vectorType = ctx.getChild(0).getText();
		if ("<vector of boolean>".equals(vectorType)) {
			vectorValue = vectorValue.replaceAll("0", FALSE_VALUE).replaceAll("1", TRUE_VALUE);
		}
		setJSON(ctx, vectorValue);
	}

	private <T extends ParserRuleContext> void translateVector(final ParserRuleContext ctx, final List<T> values) {
		final StringBuilder buf = new StringBuilder();
		for (final ParseTree child : values) {
			buf.append(getJSON(child)).append(",");
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		setJSON(ctx, addSquareBrackets(buf.toString()));
	}

	@Override
	public void exitParametersValue(ProtocolLogParser.ParametersValueContext ctx) {
		String parametersValue = NULL_VALUE;
		final ProtocolLogParser.ObjectBodyContext objectBody = ctx.objectBody();
		if (objectBody != null) {
			parametersValue = addBraces(getJSON(objectBody));
		}
		setJSON(ctx, parametersValue);
	}

	@Override
	public void exitColumnsRequest(ProtocolLogParser.ColumnsRequestContext ctx) {
		final StringBuilder buf = new StringBuilder();
		for (final ParseTree child : ctx.children) {
			if (child instanceof ParserRuleContext) {
				buf.append(getJSON(child)).append(",");
			}
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		setJSON(ctx, buildPair(addQuotes("Columns"), addSquareBrackets(buf.toString())));
	}

	@Override
	public void exitColumnRequest(ProtocolLogParser.ColumnRequestContext ctx) {
		setJSON(ctx, getJSON(ctx.namedUuid(1)));
	}

	@Override
	public void exitColumnRequestWithParams(ProtocolLogParser.ColumnRequestWithParamsContext ctx) {
		final StringBuilder buf = new StringBuilder();
		buf.append(buildPair(addQuotes("columnRequestId"), addQuotes(ctx.UUID().getText()))).append(",");
		final String column = getJSON(ctx.pair().value());
		buf.append(buildPair(addQuotes("column"), column)).append(",");
		final String parameters = getJSON(ctx.objectBody());
		buf.append(buildPair(addQuotes("parameters"), addBraces(parameters)));
		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitParametersRequest(ProtocolLogParser.ParametersRequestContext ctx) {
		setJSON(ctx, buildPair(addQuotes("Parameters"), addBraces(getJSON(ctx.objectBody()))));
	}

	@Override
	public void exitRowRequestsList(ProtocolLogParser.RowRequestsListContext ctx) {
		final StringBuilder buf = new StringBuilder();
		final List<ProtocolLogParser.RowRequestContext> rowRequests = ctx.rowRequest();
		for (int i = 0; i < rowRequests.size(); i++) {
			buf.append(getJSON(rowRequests.get(i))).append(",");
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		setJSON(ctx, buildPair(addQuotes("Rows"), addSquareBrackets(buf.toString())));
	}

	@Override
	public void exitRowRequest(ProtocolLogParser.RowRequestContext ctx) {
		final StringBuilder buf = new StringBuilder();
		final String rowId = getJSON(ctx.rowId());
		final StringBuilder rowRequestKey = new StringBuilder();
		rowRequestKey.append(getJSON(ctx.rowRequestKey()));
		final ProtocolLogParser.ObjectBodyContext objectBody = ctx.objectBody();
		if (objectBody != null) {
			rowRequestKey.append(",")
					.append(buildPair(addQuotes(ctx.STRING().getText()), addBraces(getJSON(objectBody))));
		}
		buf.append(buildPair(rowId, addBraces(rowRequestKey.toString())));
		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitRowId(ProtocolLogParser.RowIdContext ctx) {
		setJSON(ctx, addQuotes(ctx.UUID().getText()));
	}

	@Override
	public void exitRowRequestKey(ProtocolLogParser.RowRequestKeyContext ctx) {
		setJSON(ctx, buildPair(ctx.fieldKey(), ctx.rowKeyValue()));
	}

	@Override
	public void exitRowKeyValue(ProtocolLogParser.RowKeyValueContext ctx) {
		final List<TerminalNode> strings = ctx.STRING();
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < strings.size(); i++) {
			final String pair = buildPair(addQuotes(strings.get(i).getText()), addQuotes(ctx.UUID(i).getText()));
			buf.append(pair).append(",");
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitOverrides(ProtocolLogParser.OverridesContext ctx) {
		final StringBuilder buf = new StringBuilder();
		buf.append(buildPair(addQuotes("rowId"), ctx.rowId()));
		buf.append(",");
		final String fieldKey = getJSON(ctx.fieldKey());
		buf.append(buildPair(fieldKey, ctx.overriddenParameters()));
		final String pair = buildPair(addQuotes("Overrides"), addBraces(buf.toString()));
		setJSON(ctx, pair);
	}

	@Override
	public void exitOverriddenParameters(ProtocolLogParser.OverriddenParametersContext ctx) {
		final StringBuilder buf = new StringBuilder();
		for (ProtocolLogParser.PairContext pair : ctx.pair()) {
			buf.append(getJSON(pair)).append(",");
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitViidValue(ProtocolLogParser.ViidValueContext ctx) {
		final StringBuilder buf = new StringBuilder();
		for (ProtocolLogParser.PairContext pair : ctx.pair()) {
			buf.append(getJSON(pair)).append(",");
		}
		buf.append(buildPair(addQuotes("mic"), addQuotes(ctx.STRING().getText())));
		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitRow(ProtocolLogParser.RowContext ctx) {
		final StringBuilder buf = new StringBuilder();
		buf.append(buildPair(addQuotes("rowId"), ctx.rowId())).append(",");

		final StringBuilder columns = new StringBuilder();
		for (ProtocolLogParser.ColumnContext column : ctx.column()) {
			columns.append(getJSON(column)).append(",");
		}
		if (columns.length() > 0) {
			columns.deleteCharAt(columns.length() - 1);
		}
		buf.append(buildPair(addQuotes("columns"), addSquareBrackets(columns.toString())));

		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitColumn(ProtocolLogParser.ColumnContext ctx) {
		final StringBuilder buf = new StringBuilder();
		buf.append(buildPair(addQuotes("column"), ctx.uuidValue()));
		buf.append(",");
		buf.append(buildPair(addQuotes("value"), ctx.value()));
		setJSON(ctx, addBraces(buf.toString()));
	}

	// UTILITY METHODS

	private String buildPair(final String key, final String value) {
		return String.format(PAIR_PATTERN, key, value);
	}

	private String buildPair(final String key, final ParseTree value) {
		return String.format(PAIR_PATTERN, key, getJSON(value));
	}

	private String buildPair(final ParseTree key, final ParseTree value) {
		return String.format(PAIR_PATTERN, getJSON(key), getJSON(value));
	}

	private String addQuotes(final String string) {
		return String.format(QUOTED_STRING_PATTERN, string);
	}

	private String addBraces(final String string) {
		return String.format(BRACES_PATTERN, string);
	}

	private String addRoundBrackets(final String string) {
		return String.format(ROUND_BRACKETS_PATTERN, string);
	}

	private String addSquareBrackets(final String string) {
		return String.format(SQUARE_BRACKETS_PATTERN, string);
	}

}
