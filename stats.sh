
lines_java=$(find . -name "*.java" -true | xargs cat | grep . |  wc -l)
lines_jsp=$(find . -name "*.jsp" -true | xargs cat | grep . | wc -l) 
lines_xml=$(find . -name "*.xml" -true | xargs cat | grep . | wc -l) 
lines_php=$(find . -name "*.php" -true | xargs cat | grep . | wc -l) 
lines_js=$(find ./WebContent/admin -name "*.js" -true | xargs cat | grep . | wc -l) 
lines_js_ext=$(find . -name "*.js" -true | xargs cat | grep . | wc -l) 

echo $lines_java "lines of Java code"
echo $lines_jsp "lines of JSP code"
echo $lines_xml "lines of XML code"
echo $lines_js "lines of JavaScript code"
let lines_js_ext=$lines_js_ext-$lines_js
echo $lines_js_ext "lines of external JavaScript code"
echo $lines_php "lines of PHP code"

let total=$lines_java+$lines_jsp+$lines_xml+$lines_js

echo $total "lines of code total without external dependencies"

let total=$total+$lines_php+$lines_js_ext

echo $total "lines of code including all."