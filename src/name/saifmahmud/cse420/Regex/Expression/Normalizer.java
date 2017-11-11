package name.saifmahmud.cse420.Regex.Expression;

class Normalizer
{
    private static char[] validCharacters = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    static String normalize(String pattern)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < pattern.length(); i++)
        {
            char ch = pattern.charAt(i);

            switch (ch)
            {
                case '{':
                    int cur = i;
                    StringBuilder sbb = new StringBuilder();

                    while (true)
                    {
                        if (pattern.charAt(++i) == '}')
                        {
                            break;
                        }

                        sbb.append(pattern.charAt(i));
                    }

                    int count = Integer.parseInt(sbb.toString());
                    char chh = pattern.charAt(--cur);

                    if (chh == ')')
                    {
                        sbb = new StringBuilder();
                        int nesting = 0;

                        while (true)
                        {
                            chh = pattern.charAt(--cur);

                            if (chh == '(' && nesting == 0)
                            {
                                break;
                            }

                            if (chh == '(')
                            {
                                nesting--;
                            }
                            else if (chh == ')')
                            {
                                nesting++;
                            }

                            sbb.insert(0, chh);
                        }

                        sbb.insert(0, '(');
                        sbb.append(')');

                        for (int j = 1; j < count; j++)
                        {
                            sb.append(sbb.toString());
                        }
                    }
                    else if (chh == ']')
                    {
                        sbb = new StringBuilder();

                        while (true)
                        {
                            chh = pattern.charAt(--cur);

                            if (chh == '[')
                            {
                                break;
                            }

                            sbb.insert(0, chh);
                        }

                        sbb.insert(0, '[');
                        sbb.append(']');

                        for (int j = 1; j < count; j++)
                        {
                            sb.append(sbb.toString());
                        }
                    }
                    else
                    {
                        for (int j = 1; j < count; j++)
                        {
                            sb.append(chh);
                        }
                    }

                    break;

                case '}':
                    throw new InvalidPatternException("Mismatched curly bracket closed.");

                default:
                    sb.append(ch);
                    break;
            }
        }

        return sb.toString();
    }

    private static boolean isOperator(char ch)
    {
        switch (ch)
        {
            default:              return false;
            case '|': case '.':   return true;
        }
    }

    private static boolean isModifier(char ch)
    {
        switch (ch)
        {
            default:                        return false;
            case '+': case '?': case '*':   return true;
        }
    }

    private static boolean isValidCharacter(char ch)
    {
        for (char c : validCharacters)
        {
            if (ch == c)
            {
                return true;
            }
        }

        return false;
    }

    private static int getPrecedence(char ch)
    {
        switch (ch)
        {
            case '|':
                return 1;
            case '.':
                return 2;
            default:
                return 3;
        }
    }
}
