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
	messageHeader objectBody
;

messageHeader
:
	'<message>' '(' INTEGER ')' messageType
	(
		',' '<protocol>' '(' INTEGER ')' protocolType
	)? ',' '<size>' '[' INTEGER ']' NL
;

objectBody
:
	'{' NL
	(
		(
			pair
			| columnsRequest
			| rowRequestsList
			| overrides
			| parametersRequest
		) NL
	)* '}'
;

//TODO use namedUuid as field key

pair
:
	(
		fieldKey
		| identifiedFieldKey
		| UUID
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
	| doubleValue
	| datetimeValue
	| timestampValue
	| enumValue
	| filterValue
	| streamMessageIdentifiersValue
	| requestTypeValue
	| vectorValue
	| tableValue
	| message
	| anyValue
	| parametersValue
	| viidValue
	| STRING
	| EMPTY_VALUE
;

uuidValue
:
	'<uuid>'?
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

enumValue
:
	(
		'<strategy state>'
		| '<calculated values request>'
		| '<grid view selection usage>'
		| '<grid view filter logical operation>'
		| '<grid view filter condition type>'
		| '<grid row data type>'
		| '<grid view mode>'
		| '<grid view row scope>'
		| '<instrument market data status>'
		| '<instrument trading phase>'
		| '<instrument trading status>'
		| '<surplus direction>'
		| '<trend>'
		| '<status>'
		| '<subscription type>'
	) STRING
;

booleanValue
:
	'<boolean>' STRING
;

integerValue
:
	(
		'<integer>'
		| '<uinteger>'
		| '<uint64>'
		| '<uint32>'
		| '<int32>'
	)? INTEGER
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
	DATE TIME STRING
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

anyValue
:
	'<any>' value
;

requestTypeValue
:
	'<calculated values request>'
;

parametersValue
:
	'<parameters>'
	(
		(
			'[' INTEGER ']' objectBody
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
				uuidValue
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
	'['
	(
		uuidValue
	) ']' '='
	(
		tableCellValue
	)
;

tableCellValue
:
	errorDoubleValue
	| doubleValue
	| EMPTY_VALUE
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
	objectBody NL? '}'
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

parametersRequest
:
	'Parameters' '=' '[' INTEGER ']' objectBody
;

rowRequest
:
	'[' rowId ']' ':' 'UPDATE' ':' rowRequestKey
;

rowRequestWithParams
:
	'[' rowId ']' ':' 'UPDATE' ':' '{' NL? rowRequestKey NL? STRING '=' '['
	INTEGER ']' objectBody NL? '}'
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
	'-'? INTEGER
	(
		'.' INTEGER
		(
			'e' '-'? INTEGER
		)?
	)?
;

DATE
:
	INTEGER '-' INTEGER '-' INTEGER
;

TIME
:
	INTEGER ':' INTEGER ':' DOUBLE
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
