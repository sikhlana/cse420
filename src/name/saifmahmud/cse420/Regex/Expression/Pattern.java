package name.saifmahmud.cse420.Regex.Expression;

import java.util.*;

public class Pattern
{
    private static char[] validCharacters = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    State start;

    public Pattern(String regex)
    {
        start = compile(regex);
    }

    private int groupId = 0;

    private State compile(String regex)
    {
        groupId++;

        Stack<State> stack = new Stack<>();
        stack.push(new State());

        StringTokenizer tokenizer = new StringTokenizer(
                Normalizer.normalize(regex), "()[]+?.*", true
        );

        while (tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken();

            switch (token)
            {
                default:
                {
                    for (int i = 0; i < token.length(); i++)
                    {
                        State top = stack.peek();
                        stack.add(new State());

                        top.addNextState(token.charAt(i), stack.peek());
                    }

                    break;
                }

                case "*":
                case "?":
                case "+":
                {
                    stack.peek().modifier = token.charAt(0);
                    break;
                }

                case "(":
                {
                    int nesting = 0;
                    StringBuilder sb = new StringBuilder();

                    while (true)
                    {
                        String nextToken = tokenizer.nextToken();

                        if (nextToken.equals(")") && nesting == 0)
                        {
                            break;
                        }

                        if (nextToken.equals(")"))
                        {
                            nesting--;
                        }
                        else if (nextToken.equals("("))
                        {
                            nesting++;
                        }

                        sb.append(nextToken);
                    }

                    State top = stack.peek();
                    stack.add(new GroupedState(compile(sb.toString())));
                    top.addNextState((char) 0, stack.peek());
                    break;
                }

                case "[":
                {
                    String charClass = tokenizer.nextToken();
                    tokenizer.nextToken();
                    boolean exclude = false;

                    if (charClass.charAt(0) == '^')
                    {
                        exclude = true;
                        charClass = charClass.substring(1);
                    }

                    ArrayList<Character> chars = new ArrayList<>();

                    if (charClass.length() == 3 && charClass.charAt(1) == '-')
                    {
                        int start = charClass.charAt(0);
                        int end = charClass.charAt(2);

                        for (int i = start; i <= end; i++)
                        {
                            chars.add((char) i);
                        }
                    }
                    else
                    {
                        for (char ch : charClass.toCharArray())
                        {
                            chars.add(ch);
                        }
                    }

                    if (exclude)
                    {
                        ArrayList<Character> charss = new ArrayList<>();

                        for (char ch : validCharacters)
                        {
                            if (!chars.contains(ch))
                            {
                                charss.add(ch);
                            }
                        }

                        chars = charss;
                    }

                    State state = new State();

                    for (char ch : chars)
                    {
                        stack.peek().addNextState(ch, state);
                    }

                    stack.push(state);
                    break;
                }
            }
        }

        if (--groupId == 0)
        {
            stack.peek().accept = true;
        }
        else
        {
            stack.peek().end = true;
        }

        processModifiers(stack);
        normalizeGroups(stack);

        return stack.get(0);
    }

    private void normalizeGroups(Stack<State> stack)
    {
        for (State state : stack)
        {
            if (!(state instanceof GroupedState))
            {
                continue;
            }

            GroupedState g = (GroupedState) state;

            HashSet<State> ref = getAllReferenced(stack, g);

            for (State rr : ref)
            {
                rr.next.get((char) 0).remove(g);
                rr.epsilons.add(g.start);
            }

            HashSet<State> ends = getEnds(g.start);

            for (HashSet<State> ss : g.next.values())
            {
                for (State end : ends)
                {
                    end.epsilons.addAll(ss);
                }
            }
        }
    }

    private HashSet<State> getAllReferenced(Collection<State> states, State ref)
    {
        HashSet<State> referenced = new HashSet<>();

        for (State state : states)
        {
            for (HashSet<State> ss : state.next.values())
            {
                if (ss.contains(ref))
                {
                    referenced.add(state);
                }
            }
        }

        return referenced;
    }

    private HashSet<State> getEnds(State start)
    {
        HashSet<State> ends = new HashSet<>();

        if (start.end)
        {
            ends.add(start);
        }

        for (HashSet<State> ss : start.next.values())
        {
            for (State s : ss)
            {
                ends.addAll(getEnds(s));
            }
        }

        return ends;
    }

    private void processModifiers(Stack<State> stack)
    {
        for (State state : stack)
        {
            if (state.hasModifier())
            {
                switch (state.modifier)
                {
                    case '*':
                    {
                        state.epsilons.add(state.parent);
                        state.parent.epsilons.add(state);

                        if (state.accept && !state.parent.accept)
                        {
                            state.parent.accept = true;
                            processModifiers(stack);
                        }

                        if (state.end && !state.parent.end)
                        {
                            state.parent.end = true;
                            processModifiers(stack);
                        }

                        break;
                    }

                    case '+':
                    {
                        state.epsilons.add(state.parent);
                        break;
                    }

                    case '?':
                    {
                        state.parent.epsilons.add(state);

                        if (state.accept && !state.parent.accept)
                        {
                            state.parent.accept = true;
                            processModifiers(stack);
                        }

                        if (state.end && !state.parent.end)
                        {
                            state.parent.end = true;
                            processModifiers(stack);
                        }

                        break;
                    }
                }
            }
        }
    }
}
