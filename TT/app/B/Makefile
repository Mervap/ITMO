SOURCES=Main.hs ../Common/Types.hs ../Common/Utils.hs
GEN_SOURCES=../Common/Lexer.x ../Common/Parser.y
GENERATED=../Generated/Lexer.hs ../Generated/Parser.hs
PACKAGE=../artifacts/B.zip

.PHONY: pack all run clean

all: parser

run: parser
	./parser

parser:
	ghc -O2 Main.hs -o parser

$(GENERATED): $(GEN_SOURCES) $(SOURCES)
	alex -g ../Common/Lexer.x -o ../Generated/Lexer.hs
	happy -g -a -c ../Common/Parser.y -o ../Generated/Parser.hs

pack: $(GENERATED)
	mkdir tmp
	cp Makefile $(SOURCES) $(GEN_SOURCES) $(GENERATED) ./tmp/
	cd tmp && zip ../$(PACKAGE) -r .
	rm -r tmp