#commands to install graphviz and generate abstract syntax tree

sudo apt-get install graphviz

sudo apt-get install gawk

gcc -fdump-tree-original-raw input.c 

awk -f pre.awk ip.c.003t.original |awk -f treewiz.awk >tree.dot

dot -Tpng tree.dot >tree.png

gcc -fdump-tree-vcg -g ip.c
