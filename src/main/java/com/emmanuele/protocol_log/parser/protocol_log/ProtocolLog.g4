grammar ProtocolLog;

// PARSE TREE ROOT

file
:
	logEntry* EOF
;

logEntry
:
	header NL+ message NL*
;

// HEADER

header
:
	timestampValue host localComponent '[' INTEGER ':' INTEGER ']' 'protocol' '.'
	direction '<debug>' path '(' INTEGER ')' ':'
	(
		'Received'
		| 'Send'
	) messageType STRING remoteComponent '[' 'local'? IP ':' INTEGER ']'
;

host
:
	STRING
;

localComponent
:
	component
;

component
:
	componentName
	(
		'(' VERSION ')'
	)?
	(
		'@' systemName
	)?
	(
		'/' componentName
	)?
;

componentName
:
	DASHED_STRING
	| STRING
;

systemName
:
	STRING
;

direction
:
	'in'
	| 'out'
;

path
:
	(
		(
			STRING
			| '.'
		) '/'
	)* STRING '.' STRING
;

messageType
:
	STRING
;

remoteComponent
:
	component
;

// MESSAGE

message
:
	messageHeader messageBody
;

messageHeader
:
	'<message>' '(' INTEGER ')' messageType
	(
		',' '<protocol>' '(' INTEGER ')' protocolType
	)? ',' '<size>' '[' INTEGER ']' NL
;

//TODO rename in objectBody

messageBody
:
	'{' NL
	(
		(
			pair
			| columnsRequest
			| rowRequestsList
			| overrides
		) NL
	)* '}'
;

//TODO use namedUuid as field key

pair
:
	(
		fieldKey
		| identifiedFieldKey
	) '=' value
;

protocolType
:
	STRING
;

fieldKey
:
	STRING+
;

identifiedFieldKey
:
	STRING+ '(' UUID ')'
;

// VALUES

value
:
	uuidValue
	| booleanValue
	| stringValue
	| integerValue
	| uintegerValue
	| int32Value
	| uint32Value
	| uint64Value
	| doubleValue
	| datetimeValue
	| timestampValue
	| filterValue
	| statusValue
	| gridViewRowScopeValue
	| gridViewModeValue
	| gridRowDataTypeValue
	| gridViewFilterConditionTypeValue
	| gridViewFilterLogicalOperationValue
	| gridViewSelectionUsageValue
	| streamMessageIdentifiersValue
	| subscriptionTypeValue
	| requestTypeValue
	| strategyStateValue
	| vectorValue
	| tableValue
	| message
	| anyValue
	| namedUuid
	| parametersValue
	| viidValue
	| surplusDirectionValue
	| trendValue
	| instrumentMarketDataStatusValue
	| instrumentTradingPhaseValue
	| instrumentTradingStatusValue
	| STRING
	| EMPTY_VALUE
;

uuidValue
:
	'<uuid>'
	(
		UUID
		| namedUuid
		| EMPTY_VALUE
	)
;

namedUuid
:
	(
		DASHED_STRING
		| STRING
	)+ '(' UUID ')'
;

instrumentMarketDataStatusValue
:
	'<instrument market data status>' STRING
;

instrumentTradingPhaseValue
:
	'<instrument trading phase>' STRING
;

instrumentTradingStatusValue
:
	'<instrument trading status>' STRING
;

surplusDirectionValue
:
	'<surplus direction>' STRING
;

trendValue
:
	'<trend>' STRING
;

statusValue
:
	'<status>' STRING
;

booleanValue
:
	'<boolean>'
	(
		'true'
		| 'false'
	)
;

integerValue
:
	'<integer>' INTEGER
;

uintegerValue
:
	'<uinteger>' INTEGER
;

doubleValue
:
	(
		'<double>'
		| '<doublevalue>'
	)
	(
		DOUBLE
		| INTEGER
		| EMPTY_VALUE
	)
;

datetimeValue
:
	'<datetime>' timestampValue
;

timestampValue
:
	date time STRING
;

date
:
	INTEGER '-' INTEGER '-' INTEGER
;

time
:
	INTEGER ':' INTEGER ':' DOUBLE
;

subscriptionTypeValue
:
	'<subscription type>'
	(
		'Snapshot'
		| 'SnapshotAndLive'
	)
;

streamMessageIdentifiersValue
:
	'{' '\'' STRING '\''
	(
		',' '\'' STRING '\''
	)* '}'
;

filterValue
:
	'<filter>'
	(
		condition
		| EMPTY_VALUE
	)
;

condition
:
	'(' condition ')'
	| condition '&&' condition
	| condition '||' condition
	| '\'' fieldKey '\''
	(
		'>'
		| '>='
		| '=='
		| '<='
		| '<'
	) timestampValue
;

stringValue
:
	'<string>' QUOTED_STRING
;

uint64Value
:
	'<uint64>' INTEGER
;

uint32Value
:
	'<uint32>' INTEGER
;

int32Value
:
	'<int32>' INTEGER
;

anyValue
:
	'<any>' value
;

gridViewRowScopeValue
:
	'<grid view row scope>' STRING
;

gridViewModeValue
:
	'<grid view mode>' STRING
;

gridRowDataTypeValue
:
	'<grid row data type>' STRING
;

gridViewFilterConditionTypeValue
:
	'<grid view filter condition type>' STRING
;

gridViewFilterLogicalOperationValue
:
	'<grid view filter logical operation>' STRING
;

gridViewSelectionUsageValue
:
	'<grid view selection usage>' STRING
;

requestTypeValue
:
	'<calculated values request>'
;

strategyStateValue
:
	'<strategy state>' STRING
;

parametersValue
:
	'<parameters>'
	(
		(
			'[' INTEGER ']' '{' NL
			(
				UUID '=' value NL
			)+ '}'
		)
		| EMPTY_VALUE
	)
;

viidValue
:
	'<viid>' pair
	(
		';' pair
	)* '{' STRING '}'
;

vectorValue
:
	uuidVector
	| booleanVector
	| anyVector
	| uint32Vector
	| messageVector
;

uuidVector
:
	'<vector of uuid>'
	(
		'[' INTEGER ']'
		(
			NL INTEGER '='
			(
				UUID
				| namedUuid
			)
		)+
		| EMPTY_VALUE
	)
;

booleanVector
:
	'<vector of boolean>' '[' INTEGER ']'
	(
		NL integerVectorPair
	)*
;

integerVectorPair
:
	INTEGER '=' INTEGER
;

anyVector
:
	'<vector of any>' '[' INTEGER ']'
	(
		NL INTEGER '=' value
	)*
;

uint32Vector
:
	'<vector of uint32>' '[' INTEGER ']'
	(
		NL integerVectorPair
	)*
;

messageVector
:
	'<vector of message>' '[' INTEGER ']'
	(
		NL INTEGER '=' message
	)*
;

tableValue
:
	'<table values>' '[' INTEGER ']' table
;

table
:
	(
		NL row
	)*
;

row
:
	'[' rowId ']' ':'
	(
		NL column
	)*
;

rowId
:
	UUID
;

column
:
	'[' namedUuid ']' '='
	(
		tableCellValue
	)
;

tableCellValue
:
	errorDoubleValue
	| doubleValue
;

errorDoubleValue
:
	'<error double>' QUOTED_STRING
;

columnsRequest
:
	'Columns' '[' INTEGER ']' '='
	(
		NL
		(
			columnRequest
			| columnRequestWithParams
		)
	)*
;

columnRequest
:
	'[' namedUuid ']' ':' 'UPDATE' ':' '{' namedUuid NL? '}'
;

columnRequestWithParams
:
	'[' UUID ']' ':' 'UPDATE' ':' '{' NL? pair NL? STRING '=' '[' INTEGER ']'
	messageBody NL? '}'
;

rowRequestsList
:
	'Rows' '[' INTEGER ']' '='
	(
		NL
		(
			rowRequest
			| rowRequestWithParams
		)
	)*
;

rowRequest
:
	'[' rowId ']' ':' 'UPDATE' ':' rowRequestKey
;

rowRequestWithParams
:
	'[' rowId ']' ':' 'UPDATE' ':' '{' NL? rowRequestKey NL? STRING '=' '['
	INTEGER ']' messageBody NL? '}'
;

rowRequestKey
:
	fieldKey ':' rowKeyValue
;

rowKeyValue
:
	'{'
	(
		STRING ':' UUID ';'?
	)* '}'
;

overrides
:
	'Overrides' '[' INTEGER ']' '=' NL '[' rowId ']' ':' 'UPDATE' ':' '{' NL
	fieldKey '=' overriddenParameters NL? '}'
;

overriddenParameters
:
	'[' INTEGER ']' '{' NL
	(
		pair NL
	)* '}'
;

// TOKENS

DASHED_STRING
:
	STRING
	(
		'-' STRING
	)+
;

EMPTY_VALUE
:
	'<empty>'
	| '<empty integer>'
	| '<empty string>'
	| '<empty uuid>'
	| '<no value>'
;

UUID
:
	HEX_STRING '-' HEX_STRING '-' HEX_STRING '-' HEX_STRING '-' HEX_STRING
;

VERSION
:
	INTEGER '.' INTEGER '.' INTEGER '.' INTEGER '-' HEX_STRING
;

IP
:
	INTEGER '.' INTEGER '.' INTEGER '.' INTEGER
;

INTEGER
:
	DIGIT+
;

fragment
HEX_STRING
:
	HEX_DIGIT+
;

DOUBLE
:
	INTEGER
	(
		'.' INTEGER
	)?
;

STRING
:
	(
		'_'
		| LOW_CASE_LETTER
		| UP_CASE_LETTER
	)
	(
		'_'
		| DIGIT
		| LOW_CASE_LETTER
		| UP_CASE_LETTER
	)*
;

QUOTED_STRING
:
	'"' .*? '"'
;

HEX_DIGIT
:
	[0-9a-f]
;

DIGIT
:
	[0-9]
;

LOW_CASE_LETTER
:
	[a-z]
;

UP_CASE_LETTER
:
	[A-Z]
;

NL
:
	'\r'? '\n'
;

WS
:
	[ \t]+ -> skip
;
