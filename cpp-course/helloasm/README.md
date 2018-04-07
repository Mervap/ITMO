# Tests for long multiplication in assembler

## How to run:

1. Put `generate.py` and `test_x.sh` in same folder as `mul.asm`
2. Build `mul.asm` into runable - `./mul`
      * If you don't know how:  
      `nasm -f elf64 -o mul.o mul.asm`
      `ld -o mul mul.o`
3. If you have 128 qword answer, run test_128.sh:  
`chmod +x test_128.sh && ./test_128.sh`  
4. If you have 256 qword answer:  
`chmod +x test_256.sh && ./test_256.sh`

## Have fun!
