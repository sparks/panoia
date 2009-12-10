mkdir ../bin
javac -d ../bin/ -classpath /Applications/Processing.app/Contents/Resources/Java/core.jar *.java
cd ../bin/
jar -cf panoia.jar ./panoia/
mv panoia.jar ../library
rm -rf panoia
cd ..
rm -rf bin