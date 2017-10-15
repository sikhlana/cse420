package name.saifmahmud.cse420.Regex;

import java.util.ArrayList;
import java.util.LinkedList;

public class Pattern
{
    public LinkedList<Group> groups = new LinkedList<>();

    public Pattern(String str)
    {
        groups.add(new Group());
        int len = str.length();
        int i = 0;

        while (i < len)
        {
            char ch = str.charAt(i++);

            switch (ch)
            {
                case '[':
                {
                    Group g = getEmptyGroup();

                    if (str.charAt(i) == '^')
                    {
                        i++;
                        g.type = GroupType.EXCLUSIVE;
                    }
                    else
                    {
                        g.type = GroupType.INCLUSIVE;
                    }

                    i = addCharsToGroup(str, i, ']', g);
                    i = prepareOccurrencesToGroup(str, i, g);

                    if (g.chars.size() == 3 && g.chars.indexOf('-') == 1)
                    {
                        int start = (int) g.chars.get(0);
                        int end = (int) g.chars.get(2);
                        g.chars = new ArrayList<>();

                        for (int j = start; j <= end; j++)
                        {
                            g.chars.add((char) j);
                        }
                    }
                    break;
                }

                case '(':
                {
                    Group g = getEmptyGroup();
                    g.type = GroupType.GROUPED;

                    i = addCharsToGroup(str, i, ')', g);
                    i = prepareOccurrencesToGroup(str, i, g);
                    break;
                }

                default:
                {
                    int ni = i;
                    Group g = new Group();

                    if (i < len)
                    {
                        ni = prepareOccurrencesToGroup(str, i, g);
                    }

                    if (i == ni)
                    {
                        g = getLastBasicGroup();
                    }
                    else
                    {
                        i = ni;
                        groups.add(g);
                    }

                    g.chars.add(ch);
                    break;
                }
            }
        }
    }

    private int addCharsToGroup(String str, int i, char end, Group g)
    {
        while (true)
        {
            char ch = str.charAt(i++);
            if (ch == end)
            {
                break;
            }

            g.chars.add(ch);
        }

        return i;
    }

    private Group getEmptyGroup()
    {
        Group g = groups.getLast();

        if (!g.chars.isEmpty())
        {
            g = new Group();
            groups.add(g);
        }

        return g;
    }

    private Group getLastBasicGroup()
    {
        Group g = groups.getLast();

        if (g.type != GroupType.BASIC || g.occurrence != GroupOccurrence.ONCE)
        {
            g = new Group();
            groups.add(g);
        }

        return g;
    }

    private int prepareOccurrencesToGroup(String str, int i, Group g)
    {
        char ch = str.charAt(i);

        switch (ch)
        {
            case '+':
                g.occurrence = GroupOccurrence.ONE_OR_MORE;
                i++;
                break;

            case '?':
                g.occurrence = GroupOccurrence.ZERO_OR_ONCE;
                i++;
                break;

            case '*':
                g.occurrence = GroupOccurrence.ZERO_OR_MORE;
                i++;
                break;

            case '{':
                g.occurrence = GroupOccurrence.EXACT;
                StringBuilder sb = new StringBuilder();

                while (true)
                {
                    ch = str.charAt(++i);
                    if (ch == '}')
                    {
                        i++;
                        break;
                    }

                    sb.append(ch);
                }

                g.count = Integer.parseInt(sb.toString());
                break;
        }

        return i;
    }

    public class Group
    {
        public GroupOccurrence occurrence = GroupOccurrence.ONCE;
        public GroupType type = GroupType.BASIC;
        public ArrayList<Character> chars = new ArrayList<>();
        public int count = -1;

        public String toString()
        {
            return chars.toString();
        }
    }

    enum GroupType
    {
        INCLUSIVE, EXCLUSIVE, BASIC, GROUPED,
    }

    enum GroupOccurrence
    {
        ZERO_OR_ONCE, ONCE, ONE_OR_MORE, ZERO_OR_MORE, EXACT,
    }
}
