import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Interpreter {

    private ArrayList<Token> infixExpr;
    private Map<String, Double> variables = new HashMap<>();

    private int operationPriority(Token op) {
        return switch (op.getToken()) {
            case "(" -> 0;
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> -1;
        };
    }

    private double execute(Token op, double first, double second) {
        return switch (op.getToken()) {
            case "+" -> first + second;
            case "-" -> first - second;
            case "*" -> first * second;
            case "/" -> first / second;
            default -> -1;
        };
    }

    public Interpreter(ArrayList<Token> infixExpr) {
        this.infixExpr = infixExpr;
        run();
    }

    private void run() {
        int temp = 0;
        int indexVar = 0;
        for (int i = 0; i < infixExpr.size(); i++) {
            if (infixExpr.get(i).getType() == "ASSIGN_OP") {
                indexVar = i - 1;
                temp = i + 1;
            }
            if (infixExpr.get(i).getType() == "ENDL") {
                double rez = calc(toPostfix(infixExpr, temp, i));
                variables.put(infixExpr.get(indexVar).getToken(), rez);
            }
        }
    }

    private ArrayList<Token> toPostfix(ArrayList<Token> infixExpr, int start, int end) {
        //	Выходная строка, содержащая постфиксную запись
        ArrayList<Token> postfixExpr = new ArrayList<>();
        //	Инициализация стека, содержащий операторы в виде символов
        Stack<Token> stack = new Stack<>();

        //	Перебираем строку
        for (int i = start; i < end; i++) {
            //	Текущий символ
            Token c = infixExpr.get(i);

            //	Если симовол - цифра
            if (c.getType() == "DIGIT" || c.getType() == "VAR") {
                postfixExpr.add(c);
            } else if (c.getType() == "L_BC") { //	Если открывающаяся скобка
                //	Заносим её в стек
                stack.push(c);
            } else if (c.getType() == "R_BC") {//	Если закрывающая скобка
                //	Заносим в выходную строку из стека всё вплоть до открывающей скобки
                while (stack.size() > 0 && stack.peek().getType() != "L_BC")
                    postfixExpr.add(stack.pop());
                //	Удаляем открывающуюся скобку из стека
                stack.pop();
            } else if (c.getType() == "OP") { //	Проверяем, содержится ли символ в списке операторов
                Token op = c;
                //	Заносим в выходную строку все операторы из стека, имеющие более высокий приоритет
                while (stack.size() > 0 && (operationPriority(stack.peek()) >= operationPriority(op)))
                    postfixExpr.add(stack.pop());
                //	Заносим в стек оператор
                stack.push(c);
            }
        }
        //	Заносим все оставшиеся операторы из стека в выходную строку
        postfixExpr.addAll(stack);

        //	Возвращаем выражение в постфиксной записи
        return postfixExpr;
    }

    private double calc(ArrayList<Token> postfixExpr) {
        //	Стек для хранения чисел
        Stack<Double> locals = new Stack<>();
        //	Счётчик действий
        int counter = 0;

        //	Проходим по строке
        for (int i = 0; i < postfixExpr.size(); i++) {
            //	Текущий символ
            Token c = postfixExpr.get(i);

            //	Если символ число
            if (c.getType() == "DIGIT") {
                String number = c.getToken();
                locals.push(Double.parseDouble(number));
            } else if (c.getType() == "VAR") {
                locals.push(variables.get(c.getToken()));
            } else if (c.getType() == "OP") { //	Если символ есть в списке операторов
                //	Прибавляем значение счётчику
                counter += 1;

                //	Получаем значения из стека в обратном порядке
                double second = locals.size() > 0 ? locals.pop() : 0,
                        first = locals.size() > 0 ? locals.pop() : 0;

                //	Получаем результат операции и заносим в стек
                locals.push(execute(c, first, second));
            }
        }

        //	По завершению цикла возвращаем результат из стека
        return locals.pop();
    }

    public Map<String, Double> getVariables() {
        return variables;
    }
}
