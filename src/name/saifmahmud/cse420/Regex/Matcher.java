package name.saifmahmud.cse420.Regex;

import java.util.ArrayList;

public class Matcher
{
    private Pattern pattern;

    public Matcher(Pattern pattern)
    {
        this.pattern = pattern;
    }

    public boolean match(String str)
    {
        int len = str.length();
        int i = 0;

        for (Pattern.Group g : pattern.groups)
        {
            try
            {
                switch (g.type)
                {
                    case BASIC:
                    case GROUPED:
                        switch (g.occurrence)
                        {
                            case ONCE:
                            case EXACT:
                                for (int j = 0; j < g.count; j++)
                                {
                                    for (char ch : g.chars)
                                    {
                                        if (ch != str.charAt(i++))
                                        {
                                            return false;
                                        }
                                    }
                                }
                                break;

                            default:
                                int count = 0;

                                BasicWhileLoop:
                                while (i < len)
                                {
                                    for (char ch : g.chars)
                                    {
                                        if (ch != str.charAt(i++))
                                        {
                                            break BasicWhileLoop;
                                        }
                                    }

                                    count++;
                                }

                                switch (g.occurrence)
                                {
                                    case ZERO_OR_ONCE:
                                        if (count > 1)
                                        {
                                            return false;
                                        }
                                        break;

                                    case ONE_OR_MORE:
                                        if (count < 1)
                                        {
                                            return false;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;

                    case EXCLUSIVE:
                        ArrayList<Character> chars = new ArrayList<>();

                        ExclusiveForLoop:
                        for (int j = 1; j < 256; j++)
                        {
                            if (g.chars.contains((char) j))
                            {
                                continue;
                            }

                            switch ((char) j)
                            {
                                case '(':
                                case ')':
                                case '+':
                                case '*':
                                case '?':
                                case '[':
                                case ']':
                                case '{':
                                case '}':
                                    continue ExclusiveForLoop;
                            }

                            chars.add((char) j);
                        }

                        g.chars = chars;
                    case INCLUSIVE:
                        switch (g.occurrence)
                        {
                            case ONCE:
                            case EXACT:

                                for (int j = 0; j < g.count; j++)
                                {
                                    if (!g.chars.contains(str.charAt(i++)))
                                    {
                                        return false;
                                    }
                                }
                                break;

                            default:
                                int count = 0;

                                while (i < len)
                                {
                                    if (!g.chars.contains(str.charAt(i++)))
                                    {
                                        break;
                                    }

                                    count++;
                                }

                                switch (g.occurrence)
                                {
                                    case ZERO_OR_ONCE:
                                        if (count > 1)
                                        {
                                            return false;
                                        }
                                        break;

                                    case ONE_OR_MORE:
                                        if (count < 1)
                                        {
                                            return false;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                }
            }
            catch (IndexOutOfBoundsException ignore)
            {

            }
        }

        if (len == i)
        {
            return true;
        }

        return false;
    }
}
