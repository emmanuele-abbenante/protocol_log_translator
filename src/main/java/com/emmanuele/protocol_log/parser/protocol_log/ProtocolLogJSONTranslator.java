package com.emmanuele.protocol_log.parser.protocol_log;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.emmanuele.protocol_log.parser.protocol_log.ProtocolLogParser.NamedUuidContext;

public class ProtocolLogJSONTranslator extends ProtocolLogBaseListener implements ProtocolLogListener {

	private static final String NULL_VALUE = "null";

	private static final String TRUE_VALUE = "true";
	private static final String FALSE_VALUE = "false";

	private static final String BRACES_PATTERN = "{%s}";
	private static final String ROUND_BRACKETS_PATTERN = "(%s)";
	private static final String SQUARE_BRACKETS_PATTERN = "[%s]";
	private static final String PAIR_PATTERN = "\"%s\":%s";
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
		buf.append(String.format(PAIR_PATTERN, "message", getJSON(ctx.message())));
		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitHeader(ProtocolLogParser.HeaderContext ctx) {
		final StringBuilder buf = new StringBuilder();
		buf.append(String.format(PAIR_PATTERN, "timestamp", getJSON(ctx.timestampValue())));
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
		final String timestampString = String.format(TIMESTAMP_PATTERN, ctx.date().getText(), ctx.time().getText(),
				ctx.STRING().getText());
		setJSON(ctx, addQuotes(timestampString));
	}

	@Override
	public void exitHost(ProtocolLogParser.HostContext ctx) {
		setJSON(ctx, String.format(PAIR_PATTERN, "host", addQuotes(ctx.STRING().getText())));
	}

	@Override
	public void exitLocalComponent(ProtocolLogParser.LocalComponentContext ctx) {
		setJSON(ctx, String.format(PAIR_PATTERN, "localComponent", addBraces(getJSON(ctx.component()))));
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
		setJSON(ctx, buf.toString());
	}

	@Override
	public void exitComponentName(ProtocolLogParser.ComponentNameContext ctx) {
		setJSON(ctx, String.format(PAIR_PATTERN, "component", addQuotes(ctx.getText())));
	}

	@Override
	public void exitSystemName(ProtocolLogParser.SystemNameContext ctx) {
		setJSON(ctx, String.format(PAIR_PATTERN, "system", addQuotes(ctx.getText())));
	}

	@Override
	public void exitDirection(ProtocolLogParser.DirectionContext ctx) {
		setJSON(ctx, String.format(PAIR_PATTERN, "direction", addQuotes(ctx.getText())));
	}

	@Override
	public void exitMessageType(ProtocolLogParser.MessageTypeContext ctx) {
		setJSON(ctx, String.format(PAIR_PATTERN, "messageType", addQuotes(ctx.getText())));
	}

	@Override
	public void exitRemoteComponent(ProtocolLogParser.RemoteComponentContext ctx) {
		setJSON(ctx, String.format(PAIR_PATTERN, "remoteComponent", addBraces(getJSON(ctx.component()))));
	}

	// MESSAGE

	@Override
	public void exitMessage(ProtocolLogParser.MessageContext ctx) {
		final StringBuilder buf = new StringBuilder();
		buf.append(getJSON(ctx.messageHeader()));
		buf.append(",");
		buf.append(getJSON(ctx.messageBody()));
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
	public void exitMessageBody(ProtocolLogParser.MessageBodyContext ctx) {
		final StringBuilder buf = new StringBuilder();
		for (ProtocolLogParser.PairContext pair : ctx.pair()) {
			buf.append(getJSON(pair)).append(",");
		}
		for (ProtocolLogParser.ColumnsListContext pair : ctx.columnsList()) {
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
		setJSON(ctx, String.format(PAIR_PATTERN, "protocolType", addQuotes(ctx.getText())));
	}

	@Override
	public void exitPair(ProtocolLogParser.PairContext ctx) {
		String fieldKey = getJSON(ctx.fieldKey());
		if (fieldKey == null) {
			fieldKey = getJSON(ctx.identifiedFieldKey());
		}
		setJSON(ctx, String.format(PAIR_PATTERN, fieldKey, getJSON(ctx.value())));
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
		setJSON(ctx, buf.toString());
	}

	@Override
	public void exitIdentifiedFieldKey(ProtocolLogParser.IdentifiedFieldKeyContext ctx) {
		final StringBuilder buf = new StringBuilder();
		for (final TerminalNode str : ctx.STRING()) {
			buf.append(str.getText()).append(" ");
		}
		buf.append(addRoundBrackets(ctx.UUID().getText()));
		setJSON(ctx, buf.toString());
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
		setJSON(ctx.getParent(), value);
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
	public void exitStatusValue(ProtocolLogParser.StatusValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.STRING().getText()));
	}

	@Override
	public void exitBooleanValue(ProtocolLogParser.BooleanValueContext ctx) {
		setJSON(ctx, ctx.getChild(1).getText());
	}

	@Override
	public void exitIntegerValue(ProtocolLogParser.IntegerValueContext ctx) {
		setJSON(ctx, ctx.getChild(1).getText());
	}

	@Override
	public void exitUintegerValue(ProtocolLogParser.UintegerValueContext ctx) {
		setJSON(ctx, ctx.getChild(1).getText());
	}

	@Override
	public void exitUint64Value(ProtocolLogParser.Uint64ValueContext ctx) {
		setJSON(ctx, ctx.getChild(1).getText());
	}

	@Override
	public void exitUint32Value(ProtocolLogParser.Uint32ValueContext ctx) {
		setJSON(ctx, ctx.getChild(1).getText());
	}

	@Override
	public void exitInt32Value(ProtocolLogParser.Int32ValueContext ctx) {
		setJSON(ctx, ctx.getChild(1).getText());
	}

	@Override
	public void exitDatetimeValue(ProtocolLogParser.DatetimeValueContext ctx) {
		ProtocolLogParser.TimestampValueContext timestamp = ctx.timestampValue();
		final String timestampString = String.format(TIMESTAMP_PATTERN, timestamp.date().getText(),
				timestamp.time().getText(), timestamp.STRING().getText());
		setJSON(ctx, addQuotes(timestampString));
	}

	@Override
	public void exitSubscriptionTypeValue(ProtocolLogParser.SubscriptionTypeValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.getChild(1).getText()));
	}

	@Override
	public void exitStreamMessageIdentifiersValue(ProtocolLogParser.StreamMessageIdentifiersValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.STRING().getText()));
	}

	@Override
	public void exitStringValue(ProtocolLogParser.StringValueContext ctx) {
		setJSON(ctx, ctx.QUOTED_STRING().getText());
	}

	@Override
	public void exitDoubleValue(ProtocolLogParser.DoubleValueContext ctx) {
		setJSON(ctx, ctx.DOUBLE().getText());
	}

	@Override
	public void exitFilterValue(ProtocolLogParser.FilterValueContext ctx) {
		final ParserRuleContext condition = ctx.condition();
		if (condition != null) {
			setJSON(ctx, addQuotes(condition.getText()));
		} else {
			setJSON(ctx, NULL_VALUE);
		}
	}

	@Override
	public void exitRequestTypeValue(ProtocolLogParser.RequestTypeValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.getText()));
	}

	@Override
	public void exitStrategyStateValue(ProtocolLogParser.StrategyStateValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.STRING().getText()));
	}

	@Override
	public void exitErrorDoubleValue(ProtocolLogParser.ErrorDoubleValueContext ctx) {
		setJSON(ctx, ctx.QUOTED_STRING().getText());
	}

	@Override
	public void exitGridViewRowScopeValue(ProtocolLogParser.GridViewRowScopeValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.STRING().getText()));
	}

	@Override
	public void exitGridViewModeValue(ProtocolLogParser.GridViewModeValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.STRING().getText()));
	}

	@Override
	public void exitGridRowDataTypeValue(ProtocolLogParser.GridRowDataTypeValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.STRING().getText()));
	}

	@Override
	public void exitGridViewFilterConditionTypeValue(ProtocolLogParser.GridViewFilterConditionTypeValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.STRING().getText()));
	}

	@Override
	public void exitGridViewFilterLogicalOperationValue(
			ProtocolLogParser.GridViewFilterLogicalOperationValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.STRING().getText()));
	}

	@Override
	public void exitGridViewSelectionUsageValue(ProtocolLogParser.GridViewSelectionUsageValueContext ctx) {
		setJSON(ctx, addQuotes(ctx.STRING().getText()));
	}

	@Override
	public void exitVectorValue(ProtocolLogParser.VectorValueContext ctx) {
		// Retrieving translation of child rule
		final ParserRuleContext value = ctx.getRuleContext(ParserRuleContext.class, 0);
		setJSON(ctx, getJSON(value));
	}

	@Override
	public void exitUuidVector(ProtocolLogParser.UuidVectorContext ctx) {
		final StringBuilder buf = new StringBuilder();
		final List<NamedUuidContext> namedUuids = ctx.namedUuid();
		final List<TerminalNode> uuids = ctx.UUID();
		if (!namedUuids.isEmpty()) {
			for (final ParserRuleContext child : namedUuids) {
				final String value = getJSON(child);
				if (value != null) {
					buf.append(value).append(",");
				}
			}
		} else if (!uuids.isEmpty()) {
			for (final ParseTree child : uuids) {
				final String value = child.getText();
				if (value != null) {
					buf.append(addQuotes(value)).append(",");
				}
			}
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		setJSON(ctx, addSquareBrackets(buf.toString()));
	}

	@Override
	public void exitBooleanVector(ProtocolLogParser.BooleanVectorContext ctx) {
		translateVector(ctx, ctx.integerVectorPair());
	}

	@Override
	public void exitUint32Vector(ProtocolLogParser.Uint32VectorContext ctx) {
		translateVector(ctx, ctx.integerVectorPair());
	}

	@Override
	public void exitIntegerVectorPair(ProtocolLogParser.IntegerVectorPairContext ctx) {
		final ParserRuleContext parent = ctx.getParent();
		if (parent instanceof ProtocolLogParser.BooleanVectorContext) {
			setJSON(ctx, "1".equals(ctx.INTEGER(1).getText()) ? TRUE_VALUE : FALSE_VALUE);
		} else if (parent instanceof ProtocolLogParser.Uint32VectorContext) {
			setJSON(ctx, ctx.INTEGER(1).getText());
		}
	}

	@Override
	public void exitAnyVector(ProtocolLogParser.AnyVectorContext ctx) {
		translateVector(ctx, ctx.value());
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
	public void exitMessageVector(ProtocolLogParser.MessageVectorContext ctx) {
		translateVector(ctx, ctx.message());
	}

	@Override
	public void exitColumnsList(ProtocolLogParser.ColumnsListContext ctx) {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < ctx.namedUuid().size(); i++) {
			if (i % 2 == 0) {
				buf.append(getJSON(ctx.namedUuid(i))).append(",");
			}
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		setJSON(ctx, String.format(PAIR_PATTERN, "Columns", addSquareBrackets(buf.toString())));
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
		setJSON(ctx, String.format(PAIR_PATTERN, "Rows", addSquareBrackets(buf.toString())));
	}

	@Override
	public void exitRowRequest(ProtocolLogParser.RowRequestContext ctx) {
		final StringBuilder buf = new StringBuilder();
		final String rowIdPair = String.format(PAIR_PATTERN, "rowId", getJSON(ctx.rowId()));
		buf.append(rowIdPair).append(",");
		final String rowKeyPair = String.format(PAIR_PATTERN, "rowKey", getJSON(ctx.rowRequestKey()));
		buf.append(rowKeyPair);
		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitRowId(ProtocolLogParser.RowIdContext ctx) {
		setJSON(ctx, addQuotes(ctx.UUID().getText()));
	}

	@Override
	public void exitRowRequestKey(ProtocolLogParser.RowRequestKeyContext ctx) {
		final StringBuilder buf = new StringBuilder();
		final String fieldKey = getJSON(ctx.fieldKey());
		buf.append(String.format(PAIR_PATTERN, "rowKeyField", addQuotes(fieldKey)));
		buf.append(",");
		final String rowKeyValue = getJSON(ctx.rowKeyValue());
		buf.append(String.format(PAIR_PATTERN, "rowKeyValue", rowKeyValue));
		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitRowKeyValue(ProtocolLogParser.RowKeyValueContext ctx) {
		final List<TerminalNode> strings = ctx.STRING();
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < strings.size(); i++) {
			final String pair = String.format(PAIR_PATTERN, strings.get(i), addQuotes(ctx.UUID(i).getText()));
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
		final String rowId = getJSON(ctx.rowId());
		buf.append(String.format(PAIR_PATTERN, "rowId", rowId));
		buf.append(",");
		final String fieldKey = getJSON(ctx.fieldKey());
		final String overriddenParameters = getJSON(ctx.overriddenParameters());
		buf.append(String.format(PAIR_PATTERN, fieldKey, addBraces(overriddenParameters)));
		final String pair = String.format(PAIR_PATTERN, "Overrides", addBraces(buf.toString()));
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
		setJSON(ctx, buf.toString());
	}

	@Override
	public void exitColumn(ProtocolLogParser.ColumnContext ctx) {
		final StringBuilder buf = new StringBuilder();
		buf.append(String.format(PAIR_PATTERN, "column", getJSON(ctx.namedUuid())));
		buf.append(",");
		buf.append(String.format(PAIR_PATTERN, "value", getJSON(ctx.tableCellValue())));
		setJSON(ctx, addBraces(buf.toString()));
	}

	@Override
	public void exitTableCellValue(ProtocolLogParser.TableCellValueContext ctx) {
		// Retrieving translation of child rule
		final ParserRuleContext value = ctx.getRuleContext(ParserRuleContext.class, 0);
		setJSON(ctx, getJSON(value));
	}

	// UTILITY METHODS

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
