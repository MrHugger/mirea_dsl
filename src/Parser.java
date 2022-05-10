/*
VAR -> "^[a-z]\\w*$"
DIGIT -> "^0|[1-9][0-9]*$"
ASSIGN_OP -> "^=$"
OP -> "^[-|+|/|*]$"
L_BC -> "^($"
R_BC -> "^)$"
ENDL -> "^;$"
COMPARE_OP -> "^==|<|>|!=|<=|>=$"
IF -> "^IF$"
ELSE -> "^ELSE$"
WHILE -> "^WHILE$"
DO -> "^DO$"
FOR -> "^FOR$"
DIV -> "^,$"


lang -> expr+
expr -> body ENDL
body -> assign | if_op | while_op | do_while_op | for_op
expr_value -> value (OP value)*
value -> (VAR | DIGIT) | infinity
infinity -> L_BC expr_value R_BC
condition -> VAR COMPARE_OP expr_value
if_op -> IF L_BC condition R_BC body+ else_op?
else_op -> ELSE expr
while_op -> WHILE L_BC condition R_BC body+
do_while_op -> DO body+ WHILE L_BC condition R_BC
for_op -> FOR L_BC assign DIV condition DIV assign R_BC body+
assign -> VAR ASSIGN_OP expr_value
*/

import java.util.ArrayList;

public class Parser {

    private int iterator = 0;
    private ArrayList<Token> tokens;
    private int len;
    private Token curToken;
    private int curLine = 0;
    public boolean correctCode = true;

    public Parser(ArrayList<Token> tokens, int len) {
        this.tokens = tokens;
        this.len = len;
        curToken = tokens.get(iterator);
    }

    public void VAR() throws ParserException {
        if (curToken.getType() != "VAR") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "VAR");
        }
    }

    public void DIGIT() throws ParserException {
        if (curToken.getType() != "DIGIT") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "DIGIT");
        }
    }

    public void ASSIGN_OP() throws ParserException {
        if (curToken.getType() != "ASSIGN_OP") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "ASSIGN_OP");
        }
    }

    public void OP() throws ParserException {
        if (curToken.getType() != "OP") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "OP");
        }
    }

    public void L_BC() throws ParserException {
        if (curToken.getType() != "L_BC") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "L_BC");
        }
    }

    public void R_BC() throws ParserException {
        if (curToken.getType() != "R_BC") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "R_BC");
        }
    }

    public void ENDL() throws ParserException {
        if (curToken.getType() != "ENDL") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "ENDL");
        }
    }

    public void COMPARE_OP() throws ParserException {
        if (curToken.getType() != "COMPARE_OP") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "COMPARE_OP");
        }
    }

    public void IF() throws ParserException {
        if (curToken.getType() != "IF") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "IF");
        }
    }

    public void ELSE() throws ParserException {
        if (curToken.getType() != "ELSE") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "ELSE");
        }
    }

    public void DO() throws ParserException {
        if (curToken.getType() != "DO") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "DO");
        }
    }

    public void WHILE() throws ParserException {
        if (curToken.getType() != "WHILE") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "WHILE");
        }
    }

    public void FOR() throws ParserException {
        if (curToken.getType() != "FOR") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "FOR");
        }
    }

    public void DIV() throws ParserException {
        if (curToken.getType() != "DIV") {
            correctCode = false;
            throw new ParserException(curLine, iterator, curToken, "DIV");
        }
    }

    public void lang() throws ParserException {
        for (int i = 0; i < len; i++) {
            curLine++;
            expr();
        }
    }

    public void expr() {
        body();
        try {
            ENDL();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);
    }

    public void body() {
        if (curToken.getType() == "VAR") {
            assign();
        } else if (curToken.getType() == "IF") {
            if_op();
        } else if (curToken.getType() == "WHILE") {
            while_op();
        } else if (curToken.getType() == "DO") {
            do_while_op();
        } else if (curToken.getType() == "FOR") {
            for_op();
        } else {
            try {
                VAR();
            } catch (ParserException e) {
                e.getInfo(curLine, iterator, e.current, e.expected);
            }
            curToken = tokens.get(++iterator);
        }
    }

    public void expr_value() {
        if ((curToken.getType() == "VAR") || (curToken.getType() == "DIGIT")) {
            value();
        }
        else if (curToken.getType() == "L_BC") {
            infinity();
        } else {
            try {
                VAR();
            } catch (ParserException e) {
                e.getInfo(curLine, iterator, e.current, e.expected);
            }
        }
        while (curToken.getType() == "OP") {
            try {
                OP();
            } catch (ParserException e) {
                e.getInfo(curLine, iterator, e.current, e.expected);
                curToken = tokens.get(--iterator);
            }
            curToken = tokens.get(++iterator);

            value();
        }
    }

    public void value() {
        if (curToken.getType() == "VAR") {
            try {
                VAR();
            } catch (ParserException e) {
                e.getInfo(curLine, iterator, e.current, e.expected);
                curToken = tokens.get(--iterator);
            }
            curToken = tokens.get(++iterator);
        } else if (curToken.getType() == "DIGIT") {
            try {
                DIGIT();
            } catch (ParserException e) {
                e.getInfo(curLine, iterator, e.current, e.expected);
                curToken = tokens.get(--iterator);
            }
            curToken = tokens.get(++iterator);
        } else if (curToken.getType() == "L_BC") {
            infinity();
        } else {
            try {
                VAR();
            } catch (ParserException e) {
                e.getInfo(curLine, iterator, e.current, e.expected);
            }
            curToken = tokens.get(++iterator);
        }
    }

    public void infinity() {
        try {
            L_BC();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        expr_value();

        try {
            R_BC();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);
    }

    public void condition() {
        try {
            VAR();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        try {
            COMPARE_OP();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            //curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        expr_value();
    }

    public void if_op() {
        try {
            IF();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        try {
            L_BC();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        condition();

        try {
            R_BC();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        do {
            body();
        } while (curToken.getType() == "VAR" || curToken.getType() == "IF" || curToken.getType() == "FOR"
                || curToken.getType() == "WHILE" || curToken.getType() == "DO");

        if (curToken.getType() == "ELSE") {
            else_op();
        }
    }

    public void else_op() {
        try {
            ELSE();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        do {
            expr();
        } while (curToken.getType() == "VAR" || curToken.getType() == "IF" || curToken.getType() == "FOR"
                || curToken.getType() == "WHILE" || curToken.getType() == "DO");
    }

    public void while_op() {
        try {
            WHILE();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        try {
            L_BC();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        condition();

        try {
            R_BC();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        do {
            body();
        } while (curToken.getType() == "VAR" || curToken.getType() == "IF" || curToken.getType() == "FOR"
                || curToken.getType() == "WHILE" || curToken.getType() == "DO");
    }

    public void do_while_op() {
        try {
            DO();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        do {
            body();
        } while (curToken.getType() == "VAR" || curToken.getType() == "IF" || curToken.getType() == "FOR"
                || curToken.getType() == "WHILE" || curToken.getType() == "DO");

        try {
            WHILE();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            //curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        try {
            IF();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        try {
            L_BC();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        condition();

        try {
            R_BC();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);
    }

    public void for_op() {
        try {
            FOR();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        try {
            L_BC();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        assign();

        try {
            DIV();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        condition();

        try {
            DIV();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        assign();

        try {
            R_BC();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        do {
            body();
        } while (curToken.getType() == "VAR" || curToken.getType() == "IF" || curToken.getType() == "FOR"
                || curToken.getType() == "WHILE" || curToken.getType() == "DO");
    }

    public void assign() {
        try {
            VAR();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        try {
            ASSIGN_OP();
        } catch (ParserException e) {
            e.getInfo(curLine, iterator, e.current, e.expected);
            curToken = tokens.get(--iterator);
        }
        curToken = tokens.get(++iterator);

        expr_value();
    }
}
