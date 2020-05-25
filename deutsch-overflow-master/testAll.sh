#!/bin/sh
# NEM FORDIT, csak futtat
# Lefutattja minden egyes tesztfajlra (13db) a jatekot olyan modban,
# hogy az outputot generaljon, majd diff-el osszehasonlitja az elvarttal
# es a diff kimenetet a megfelelo fajlokba iranyitja, ahol ez visszaolvashato

# Ezt ird at oda, ahol a repo van
cd /home/solarowl/Egyetem_targyak_4/projlab/deutsch-overflow || exit

for i in 1 2 3 4 5 6 7 8 9 10 11 12 13
do
	printf "3\ny\n%d" ${i} | java -classpath out/production/deutsch-overflow/ Main.ProtoMain
done

diff -s -r $PWD/clitestfiles/output $PWD/clitestfiles/required
