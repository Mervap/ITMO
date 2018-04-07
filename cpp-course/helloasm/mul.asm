                section         .text

                global          _start
_start:

                sub             rsp, 4 * 128 * 8
                lea             rdi, [rsp + 2 * 128 * 8]
                mov             rcx, 128
                call            read_long
                lea             rdi, [rsp + 3 * 128 * 8]
                call            read_long
				
				mov				rsi, rsp
                lea				rdi, [rsp + 128 * 8]
                lea             r8, [rsp + 2 * 128 * 8]
                lea				r9, [rsp + 3 * 128 * 8]
				
                call			set_zero
                call            mul_long_long

                call            write_long

                mov             al, 0x0a
                call            write_char

                jmp             exit
				
; mul two long number
;    r8 -- address of multiplier #1 (long number)
;    r9 -- address of multiplier #2 (long number)
;    rcx -- length of long numbers in qwords
; result:
;    prod is written to rdi
mul_long_long:
                push            r8
                push            r9
                push            rcx

                clc
				lea             r9, [r9 + 8 * 128]	
.loop:
				
				lea             r9, [r9 - 8]
                mov             rbx, [r9] 
				
				call			mul_long_short_2
				call			shl_long_long
                call 			add_long_long
				
                dec             rcx
                jnz             .loop

                pop             rcx
                pop             r9
                pop             r8
                ret		
			
; multiplies long number by a short
;    r8 -- address of multiplier #1 (long number)
;    rbx -- multiplier #2 (64-bit unsigned)
;    rcx -- length of long number in qwords
; result:
;    product is written to rsi
mul_long_short_2:
                push            r8                
				push			rax
				push            rcx
				push            rsi

				mov				rcx, 128
                xor             r10, r10
.loop:
                mov             rax, [r8]
                mul             rbx
                add             rax, r10
                adc             rdx, 0
                mov             [rsi], rax
                lea             rsi, [rsi + 8]
				lea				r8, [r8 + 8]
                mov             r10, rdx
                dec             rcx
                jnz             .loop

				pop             rsi
                pop             rcx
                pop             rax
                pop             r8
                ret

; assigns long number on qword
;    rdi -- argument (long number)
;    rcx -- length of long number in qwords		
shl_long_long:
                push            rdi
                push            rax
                push            rcx

                clc
				lea             rdi, [rdi + 8 * 128]
				mov				rcx, 127
.loop:
				
				sub             rdi, 8
                mov             rax, [rdi - 8]
				mov				[rdi], rax
                dec             rcx
                jnz             .loop
				
				xor				rax, rax 
				mov				[rdi - 8], rax

                pop             rcx
                pop             rax
                pop             rdi
                ret					

; adds two long number
;    rdi -- address of summand #1 (long number)
;    rsi -- address of summand #2 (long number)
;    rcx -- length of long numbers in qwords
; result:
;    sum is written to rdi
add_long_long:
                push            rdi
                push            rsi
                push            rcx
				push 			rax

				mov 			rcx, 128
                clc
				
.loop:	
				
                mov             rax, [rsi]
                lea             rsi, [rsi + 8]
                adc             [rdi], rax
                lea             rdi, [rdi + 8]
				
                dec             rcx
                jnz             .loop
				

				pop				rax
                pop             rcx
                pop             rsi
                pop             rdi
                ret
				

; adds 64-bit number to long number
;    rdi -- address of summand #1 (long number)
;    rax -- summand #2 (64-bit unsigned)
;    rcx -- length of long number in qwords
; result:
;    sum is written to rdi
add_long_short:
                push            rdi
                push            rcx
                push            rdx
				push			rax

				mov				rcx, 128
                xor             rdx, rdx
.loop:
                add             [rdi], rax
                adc             rdx, 0
                mov             rax, rdx
                xor             rdx, rdx
                add             rdi, 8
                dec             rcx
                jnz             .loop

				pop				rax
                pop             rdx
                pop             rcx
                pop             rdi
                ret
				

; multiplies long number by a short
;    rdi -- address of multiplier #1 (long number)
;    rbx -- multiplier #2 (64-bit unsigned)
;    rcx -- length of long number in qwords
; result:
;    product is written to rdi
mul_long_short:
                push            rax
                push            rdi
                push            rcx
				push 			rsi

				mov				rcx, 128
                xor             rsi, rsi
.loop:
                mov             rax, [rdi]
                mul             rbx
                add             rax, rsi
                adc             rdx, 0
                mov             [rdi], rax
                add             rdi, 8
                mov             rsi, rdx
                dec             rcx
                jnz             .loop

				pop				rsi
                pop             rcx
                pop             rdi
                pop             rax
                ret

; divides long number by a short
;    rdi -- address of dividend (long number)
;    rbx -- divisor (64-bit unsigned)
;    rcx -- length of long number in qwords
; result:
;    quotient is written to rdi
;    rdx -- remainder
div_long_short:
                push            rdi
                push            rax
                push            rcx
				
                lea             rdi, [rdi + 8 * rcx - 8]
                xor             rdx, rdx

.loop:
                mov             rax, [rdi]
                div             rbx
                mov             [rdi], rax
                sub             rdi, 8
                dec             rcx
                jnz             .loop

                pop             rcx
                pop             rax
                pop             rdi
                ret
				
; assigns a zero to long number
;    rdi -- argument (long number)
;    rcx -- length of long number in qwords
set_zero:
                push            rax
                push            rdi
                push            rcx

                xor             rax, rax
                rep stosq

                pop             rcx
                pop             rdi
                pop             rax
                ret

; checks if a long number is a zero
;    rdi -- argument (long number)
;    rcx -- length of long number in qwords
; result:
;    ZF=1 if zero
is_zero:
                push            rax
                push            rdi
                push            rcx

                xor             rax, rax
                rep scasq

                pop             rcx
                pop             rdi
                pop             rax
                ret

; read long number from stdin
;    rdi -- location for output (long number)
;    rcx -- length of long number in qwords
read_long:
                push            rcx
                push            rdi

                call            set_zero
.loop:
                call            read_char
                or              rax, rax
                js              exit
                cmp             rax, 0x0a
                je              .done
                cmp             rax, '0'
                jb              .invalid_char
                cmp             rax, '9'
                ja              .invalid_char

                sub             rax, '0'
                mov             rbx, 10
                call            mul_long_short
                call            add_long_short
                jmp             .loop

.done:
                pop             rdi
                pop             rcx
                ret

.invalid_char:
                mov             rsi, invalid_char_msg
                mov             rdx, invalid_char_msg_size
                call            print_string
                call            write_char
                mov             al, 0x0a
                call            write_char

.skip_loop:
                call            read_char
                or              rax, rax
                js              exit
                cmp             rax, 0x0a
                je              exit
                jmp             .skip_loop

; write long number to stdout
;    rdi -- argument (long number)
;    rcx -- length of long number in qwords
write_long:
                push            rax
                push            rcx

                mov             rax, 20
                mul             rcx
                mov             rbp, rsp
                sub             rsp, rax

                mov             rsi, rbp

.loop:
                mov             rbx, 10
                call            div_long_short
                add             rdx, '0'
                dec             rsi
                mov             [rsi], dl
                call            is_zero
                jnz             .loop

                mov             rdx, rbp
                sub             rdx, rsi
                call            print_string

                mov             rsp, rbp
                pop             rcx
                pop             rax
                ret

; read one char from stdin
; result:
;    rax == -1 if error occurs
;    rax \in [0; 255] if OK
read_char:
                push            rcx
                push            rdi

                sub             rsp, 1
                xor             rax, rax
                xor             rdi, rdi
                mov             rsi, rsp
                mov             rdx, 1
                syscall

                cmp             rax, 1
                jne             .error
                xor             rax, rax
                mov             al, [rsp]
                add             rsp, 1

                pop             rdi
                pop             rcx
                ret
.error:
                mov             rax, -1
                add             rsp, 1
                pop             rdi
                pop             rcx
                ret

; write one char to stdout, errors are ignored
;    al -- char
write_char:
				push 			rdi
				push 			rsi
	
                sub             rsp, 1
                mov             [rsp], al

                mov             rax, 1
                mov             rdi, 1
                mov             rsi, rsp
                mov             rdx, 1
                syscall
                add             rsp, 1
				
				pop				rsi
				pop				rdi
                ret

exit:
                mov             rax, 60
                xor             rdi, rdi
                syscall

; print string to stdout
;    rsi -- string
;    rdx -- size
print_string:
                push            rax

                mov             rax, 1
                mov             rdi, 1
                syscall

                pop             rax
                ret


                section         .rodata
invalid_char_msg:
                db              "Invalid character: "
invalid_char_msg_size: equ             $ - invalid_char_msg
