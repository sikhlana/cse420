package name.saifmahmud.cse420;

import java.io.FileInputStream;
import java.util.*;

public class Parser
{
    private FileInputStream stream;

    public Parser(FileInputStream stream)
    {
        this.stream = stream;
    }

    public Parsed process()
    {
        Scanner sc = new Scanner(stream);
        HashMap<String, Integer> variables = new HashMap<>();

        int variableCount = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < variableCount; i++)
        {
            StringTokenizer tokenizer = new StringTokenizer(sc.nextLine(), " \r\n\t=", false);
            if (tokenizer.countTokens() == 2)
            {
                variables.put(tokenizer.nextToken(), Integer.parseInt(tokenizer.nextToken()));
            }
        }

        int expressionCount = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < expressionCount; i++)
        {
            try
            {
                String exp = validateExpression(
                    sc.nextLine().trim(), variables.keySet()
                );

                System.out.println(parseExpression(exp, variables));
            }
            catch (InvalidExpressionException e)
            {
                System.out.println("Compilation Error");
            }
        }

        return null;
    }

    private String validateExpression(String str, Set<String> variables)
    {
        StringBuilder sb = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        ForLoop:
        for (int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);

            if (ch == 'x')
            {
                ch = '*';
            }

            switch (ch)
            {
                case ' ':
                    continue ForLoop;

                case '(':
                    stack.push(ch);
                    break;

                case ')':
                    char c;

                    try
                    {
                        while ((c = stack.pop()) != '(')
                        {
                            sb.append(c);
                        }
                    }
                    catch (EmptyStackException e)
                    {
                        throw new InvalidExpressionException();
                    }
                    break;

                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                    while (true)
                    {
                        if (stack.empty() || stack.peek() == '(')
                        {
                            stack.push(ch);
                            break;
                        }
                        else
                        {
                            if (OperatorPriority.getPriorityForOperator(ch) > OperatorPriority.getPriorityForOperator(stack.peek()))
                            {
                                stack.push(ch);
                                break;
                            }
                            else
                            {
                                sb.append(stack.pop());
                            }
                        }
                    }
                    break;

                default:
                    if (!variables.contains("" + ch))
                    {
                        throw new InvalidExpressionException(
                            "Invalid variable `" + ch + "` in the expression."
                        );
                    }
                    sb.append(ch);
                    break;
            }
        }

        while (!stack.empty())
        {
            sb.append(stack.pop());
        }

        return sb.toString();
    }

    private int parseExpression(String exp, Map<String, Integer> variables)
    {
        Stack<Object> stack = new Stack<>();
        for (int i = 0; i < exp.length(); i++)
        {
            char ch = exp.charAt(i);

            switch (ch)
            {
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                    if (stack.size() < 2)
                    {
                        throw new InvalidExpressionException();
                    }

                    int rgt = (Integer) stack.pop();
                    int lft = (Integer) stack.pop();
                    int result = 0;

                    switch (ch)
                    {
                        case '+':
                            result = lft + rgt;
                            break;

                        case '-':
                            result = lft - rgt;
                            break;

                        case '*':
                            result = lft * rgt;
                            break;

                        case '/':
                            result = lft / rgt;
                            break;

                        case '^':
                            result = lft ^ rgt;
                            break;
                    }

                    stack.push(result);
                    break;

                default:
                    stack.push(variables.get("" + ch));
                    break;
            }
        }

        if (stack.size() != 1)
        {
            throw new InvalidExpressionException();
        }

        return (Integer) stack.pop();
    }

    public static class Parsed
    {

    }

    public static class InvalidExpressionException extends RuntimeException
    {
        public InvalidExpressionException()
        {
            super();
        }

        public InvalidExpressionException(String msg)
        {
            super(msg);
        }
    }

    public enum OperatorPriority
    {
        ADD('+', 1),
        SUB('-', 1),
        MUL('*', 2),
        DIV('/', 2),
        POW('^', 10);

        private final char operator;
        private final int priority;

        OperatorPriority(char operator, int priority)
        {
            this.operator = operator;
            this.priority = priority;
        }

        public char getOperator()
        {
            return operator;
        }

        public int getPriority()
        {
            return priority;
        }

        public static int getPriorityForOperator(char operator)
        {
            for (OperatorPriority op : OperatorPriority.values())
            {
                if (op.getOperator() == operator)
                {
                    return op.getPriority();
                }
            }

            return -1;
        }
    }
}
