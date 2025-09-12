org 0x2d4

CALL    READ            ; читаем длину слова
ST      (SYM_POINTER)+  ; сохраняем длину в АДДР0
ST      COUNTER         ; сохраняем длину для счётчика цикла

CMP     #0
BEQ     EXIT            ; если 0 - завершаем работу

M_LOOP:
	LD      SYM_2_FLAG  ; Проверка, какоё символ сеёчас читаем
	BNE     R_SYM_2

	CALL    READ
	ST      (SYM_POINTER)
	LD      #1
	ST      SYM_2_FLAG  ; устанавливаем флаг чтения 2-го символа
	JUMP    L

R_SYM_2:
	CALL    READ
	SWAB                ; По заданию 2-й символ хранится в старшем байте
	OR      (SYM_POINTER)
	ST      (SYM_POINTER)+
	LD      #0
	ST      SYM_2_FLAG  ; Сбрасываем флаг чтения 2-го символа
	
L:
    LOOP    COUNTER
	JUMP    M_LOOP
	
EXIT: HLT

COUNTER:        word    ?
SYM_2_FLAG:     word    ?
SYM_POINTER:    word    0x5cc

READ:   
    IN      7       ; Проверка готовности ВУ-3
    AND     #0x40   ; Оставляем 6-й бит
    BEQ     READ    ; Если не готов, то ждём
	IN      6       ; Читаем символ
	RET             ; Возврат