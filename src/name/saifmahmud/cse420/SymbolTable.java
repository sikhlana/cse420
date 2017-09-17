package name.saifmahmud.cse420;

import name.saifmahmud.cse420.Lexer.Token;

import java.util.*;

public class SymbolTable
{
    private Map<Token.Type, List<Token>> symbols = new HashMap<>();

    public void add(Token token)
    {
        if (symbols.containsKey(token.getType()))
        {
            symbols.get(token.getType()).add(token);
        }
        else
        {
            List<Token> l = new ArrayList<>();
            l.add(token);
            symbols.put(token.getType(), l);
        }
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        if (symbols.containsKey(Token.Type.KEYWORD))
        {
            sb.append("Keywords: ");
            sb.append(String.join(", ", getValues(Token.Type.KEYWORD)));
            sb.append("\n");
        }

        if (symbols.containsKey(Token.Type.IDENTIFIER))
        {
            sb.append("Identifiers: ");
            sb.append(String.join(", ", getValues(Token.Type.IDENTIFIER)));
            sb.append("\n");
        }

        if (symbols.containsKey(Token.Type.NUMERIC_OPERATOR))
        {
            sb.append("Math Operators: ");
            sb.append(String.join(", ", getValues(Token.Type.NUMERIC_OPERATOR)));
            sb.append("\n");
        }

        if (symbols.containsKey(Token.Type.LOGICAL_OPERATOR))
        {
            sb.append("Logical Operators: ");
            sb.append(String.join(", ", getValues(Token.Type.LOGICAL_OPERATOR)));
            sb.append("\n");
        }

        if (symbols.containsKey(Token.Type.NUMERIC))
        {
            sb.append("Numerical Values: ");
            sb.append(String.join(", ", getValues(Token.Type.NUMERIC)));
            sb.append("\n");
        }

        if (symbols.containsKey(Token.Type.OTHERS))
        {
            sb.append("Others: ");
            sb.append(String.join(" ", getValues(Token.Type.OTHERS)));
            sb.append("\n");
        }

        return sb.toString();
    }

    private String[] getValues(Token.Type type)
    {
        List<Token> list = symbols.get(type);
        List<String> temp = new LinkedList<>();

        for (Token t : list)
        {
            if (!temp.contains(t.getValue()))
            {
                temp.add(t.getValue());
            }
        }

        String[] arr = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++)
        {
            arr[i] = temp.get(i);
        }

        return arr;
    }
}
