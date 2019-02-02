// Generated from src/main/java/com/emmanuele/protocol_log/parser/protocol_log/ProtocolLog.g4 by ANTLR 4.7.2
package com.emmanuele.protocol_log.parser.protocol_log;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ProtocolLogParser}.
 */
public interface ProtocolLogListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(ProtocolLogParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(ProtocolLogParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#logEntry}.
	 * @param ctx the parse tree
	 */
	void enterLogEntry(ProtocolLogParser.LogEntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#logEntry}.
	 * @param ctx the parse tree
	 */
	void exitLogEntry(ProtocolLogParser.LogEntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#header}.
	 * @param ctx the parse tree
	 */
	void enterHeader(ProtocolLogParser.HeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#header}.
	 * @param ctx the parse tree
	 */
	void exitHeader(ProtocolLogParser.HeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#host}.
	 * @param ctx the parse tree
	 */
	void enterHost(ProtocolLogParser.HostContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#host}.
	 * @param ctx the parse tree
	 */
	void exitHost(ProtocolLogParser.HostContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#localComponent}.
	 * @param ctx the parse tree
	 */
	void enterLocalComponent(ProtocolLogParser.LocalComponentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#localComponent}.
	 * @param ctx the parse tree
	 */
	void exitLocalComponent(ProtocolLogParser.LocalComponentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#component}.
	 * @param ctx the parse tree
	 */
	void enterComponent(ProtocolLogParser.ComponentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#component}.
	 * @param ctx the parse tree
	 */
	void exitComponent(ProtocolLogParser.ComponentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#componentName}.
	 * @param ctx the parse tree
	 */
	void enterComponentName(ProtocolLogParser.ComponentNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#componentName}.
	 * @param ctx the parse tree
	 */
	void exitComponentName(ProtocolLogParser.ComponentNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#systemName}.
	 * @param ctx the parse tree
	 */
	void enterSystemName(ProtocolLogParser.SystemNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#systemName}.
	 * @param ctx the parse tree
	 */
	void exitSystemName(ProtocolLogParser.SystemNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#direction}.
	 * @param ctx the parse tree
	 */
	void enterDirection(ProtocolLogParser.DirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#direction}.
	 * @param ctx the parse tree
	 */
	void exitDirection(ProtocolLogParser.DirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(ProtocolLogParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(ProtocolLogParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#messageType}.
	 * @param ctx the parse tree
	 */
	void enterMessageType(ProtocolLogParser.MessageTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#messageType}.
	 * @param ctx the parse tree
	 */
	void exitMessageType(ProtocolLogParser.MessageTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#remoteComponent}.
	 * @param ctx the parse tree
	 */
	void enterRemoteComponent(ProtocolLogParser.RemoteComponentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#remoteComponent}.
	 * @param ctx the parse tree
	 */
	void exitRemoteComponent(ProtocolLogParser.RemoteComponentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#message}.
	 * @param ctx the parse tree
	 */
	void enterMessage(ProtocolLogParser.MessageContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#message}.
	 * @param ctx the parse tree
	 */
	void exitMessage(ProtocolLogParser.MessageContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#messageHeader}.
	 * @param ctx the parse tree
	 */
	void enterMessageHeader(ProtocolLogParser.MessageHeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#messageHeader}.
	 * @param ctx the parse tree
	 */
	void exitMessageHeader(ProtocolLogParser.MessageHeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#objectBody}.
	 * @param ctx the parse tree
	 */
	void enterObjectBody(ProtocolLogParser.ObjectBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#objectBody}.
	 * @param ctx the parse tree
	 */
	void exitObjectBody(ProtocolLogParser.ObjectBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterPair(ProtocolLogParser.PairContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitPair(ProtocolLogParser.PairContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#protocolType}.
	 * @param ctx the parse tree
	 */
	void enterProtocolType(ProtocolLogParser.ProtocolTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#protocolType}.
	 * @param ctx the parse tree
	 */
	void exitProtocolType(ProtocolLogParser.ProtocolTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#fieldKey}.
	 * @param ctx the parse tree
	 */
	void enterFieldKey(ProtocolLogParser.FieldKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#fieldKey}.
	 * @param ctx the parse tree
	 */
	void exitFieldKey(ProtocolLogParser.FieldKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(ProtocolLogParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(ProtocolLogParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#uuidValue}.
	 * @param ctx the parse tree
	 */
	void enterUuidValue(ProtocolLogParser.UuidValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#uuidValue}.
	 * @param ctx the parse tree
	 */
	void exitUuidValue(ProtocolLogParser.UuidValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#namedUuid}.
	 * @param ctx the parse tree
	 */
	void enterNamedUuid(ProtocolLogParser.NamedUuidContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#namedUuid}.
	 * @param ctx the parse tree
	 */
	void exitNamedUuid(ProtocolLogParser.NamedUuidContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#enumValue}.
	 * @param ctx the parse tree
	 */
	void enterEnumValue(ProtocolLogParser.EnumValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#enumValue}.
	 * @param ctx the parse tree
	 */
	void exitEnumValue(ProtocolLogParser.EnumValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void enterBooleanValue(ProtocolLogParser.BooleanValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void exitBooleanValue(ProtocolLogParser.BooleanValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#integerValue}.
	 * @param ctx the parse tree
	 */
	void enterIntegerValue(ProtocolLogParser.IntegerValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#integerValue}.
	 * @param ctx the parse tree
	 */
	void exitIntegerValue(ProtocolLogParser.IntegerValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#doubleValue}.
	 * @param ctx the parse tree
	 */
	void enterDoubleValue(ProtocolLogParser.DoubleValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#doubleValue}.
	 * @param ctx the parse tree
	 */
	void exitDoubleValue(ProtocolLogParser.DoubleValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#datetimeValue}.
	 * @param ctx the parse tree
	 */
	void enterDatetimeValue(ProtocolLogParser.DatetimeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#datetimeValue}.
	 * @param ctx the parse tree
	 */
	void exitDatetimeValue(ProtocolLogParser.DatetimeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#timestampValue}.
	 * @param ctx the parse tree
	 */
	void enterTimestampValue(ProtocolLogParser.TimestampValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#timestampValue}.
	 * @param ctx the parse tree
	 */
	void exitTimestampValue(ProtocolLogParser.TimestampValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#streamMessageIdentifiersValue}.
	 * @param ctx the parse tree
	 */
	void enterStreamMessageIdentifiersValue(ProtocolLogParser.StreamMessageIdentifiersValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#streamMessageIdentifiersValue}.
	 * @param ctx the parse tree
	 */
	void exitStreamMessageIdentifiersValue(ProtocolLogParser.StreamMessageIdentifiersValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#filterValue}.
	 * @param ctx the parse tree
	 */
	void enterFilterValue(ProtocolLogParser.FilterValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#filterValue}.
	 * @param ctx the parse tree
	 */
	void exitFilterValue(ProtocolLogParser.FilterValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(ProtocolLogParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(ProtocolLogParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#stringValue}.
	 * @param ctx the parse tree
	 */
	void enterStringValue(ProtocolLogParser.StringValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#stringValue}.
	 * @param ctx the parse tree
	 */
	void exitStringValue(ProtocolLogParser.StringValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#anyValue}.
	 * @param ctx the parse tree
	 */
	void enterAnyValue(ProtocolLogParser.AnyValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#anyValue}.
	 * @param ctx the parse tree
	 */
	void exitAnyValue(ProtocolLogParser.AnyValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#requestTypeValue}.
	 * @param ctx the parse tree
	 */
	void enterRequestTypeValue(ProtocolLogParser.RequestTypeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#requestTypeValue}.
	 * @param ctx the parse tree
	 */
	void exitRequestTypeValue(ProtocolLogParser.RequestTypeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#parametersValue}.
	 * @param ctx the parse tree
	 */
	void enterParametersValue(ProtocolLogParser.ParametersValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#parametersValue}.
	 * @param ctx the parse tree
	 */
	void exitParametersValue(ProtocolLogParser.ParametersValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#viidValue}.
	 * @param ctx the parse tree
	 */
	void enterViidValue(ProtocolLogParser.ViidValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#viidValue}.
	 * @param ctx the parse tree
	 */
	void exitViidValue(ProtocolLogParser.ViidValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#errorValue}.
	 * @param ctx the parse tree
	 */
	void enterErrorValue(ProtocolLogParser.ErrorValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#errorValue}.
	 * @param ctx the parse tree
	 */
	void exitErrorValue(ProtocolLogParser.ErrorValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#vectorValue}.
	 * @param ctx the parse tree
	 */
	void enterVectorValue(ProtocolLogParser.VectorValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#vectorValue}.
	 * @param ctx the parse tree
	 */
	void exitVectorValue(ProtocolLogParser.VectorValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#row}.
	 * @param ctx the parse tree
	 */
	void enterRow(ProtocolLogParser.RowContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#row}.
	 * @param ctx the parse tree
	 */
	void exitRow(ProtocolLogParser.RowContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#rowId}.
	 * @param ctx the parse tree
	 */
	void enterRowId(ProtocolLogParser.RowIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#rowId}.
	 * @param ctx the parse tree
	 */
	void exitRowId(ProtocolLogParser.RowIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#column}.
	 * @param ctx the parse tree
	 */
	void enterColumn(ProtocolLogParser.ColumnContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#column}.
	 * @param ctx the parse tree
	 */
	void exitColumn(ProtocolLogParser.ColumnContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#columnsRequest}.
	 * @param ctx the parse tree
	 */
	void enterColumnsRequest(ProtocolLogParser.ColumnsRequestContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#columnsRequest}.
	 * @param ctx the parse tree
	 */
	void exitColumnsRequest(ProtocolLogParser.ColumnsRequestContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#columnRequest}.
	 * @param ctx the parse tree
	 */
	void enterColumnRequest(ProtocolLogParser.ColumnRequestContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#columnRequest}.
	 * @param ctx the parse tree
	 */
	void exitColumnRequest(ProtocolLogParser.ColumnRequestContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#columnRequestWithParams}.
	 * @param ctx the parse tree
	 */
	void enterColumnRequestWithParams(ProtocolLogParser.ColumnRequestWithParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#columnRequestWithParams}.
	 * @param ctx the parse tree
	 */
	void exitColumnRequestWithParams(ProtocolLogParser.ColumnRequestWithParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#rowRequestsList}.
	 * @param ctx the parse tree
	 */
	void enterRowRequestsList(ProtocolLogParser.RowRequestsListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#rowRequestsList}.
	 * @param ctx the parse tree
	 */
	void exitRowRequestsList(ProtocolLogParser.RowRequestsListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#parametersRequest}.
	 * @param ctx the parse tree
	 */
	void enterParametersRequest(ProtocolLogParser.ParametersRequestContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#parametersRequest}.
	 * @param ctx the parse tree
	 */
	void exitParametersRequest(ProtocolLogParser.ParametersRequestContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#rowRequest}.
	 * @param ctx the parse tree
	 */
	void enterRowRequest(ProtocolLogParser.RowRequestContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#rowRequest}.
	 * @param ctx the parse tree
	 */
	void exitRowRequest(ProtocolLogParser.RowRequestContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#rowRequestKey}.
	 * @param ctx the parse tree
	 */
	void enterRowRequestKey(ProtocolLogParser.RowRequestKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#rowRequestKey}.
	 * @param ctx the parse tree
	 */
	void exitRowRequestKey(ProtocolLogParser.RowRequestKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#rowKeyValue}.
	 * @param ctx the parse tree
	 */
	void enterRowKeyValue(ProtocolLogParser.RowKeyValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#rowKeyValue}.
	 * @param ctx the parse tree
	 */
	void exitRowKeyValue(ProtocolLogParser.RowKeyValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#overrides}.
	 * @param ctx the parse tree
	 */
	void enterOverrides(ProtocolLogParser.OverridesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#overrides}.
	 * @param ctx the parse tree
	 */
	void exitOverrides(ProtocolLogParser.OverridesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProtocolLogParser#overriddenParameters}.
	 * @param ctx the parse tree
	 */
	void enterOverriddenParameters(ProtocolLogParser.OverriddenParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProtocolLogParser#overriddenParameters}.
	 * @param ctx the parse tree
	 */
	void exitOverriddenParameters(ProtocolLogParser.OverriddenParametersContext ctx);
}