package name.saifmahmud.cse420.Regex.Expression;

import java.util.HashSet;

public class Matcher
{
    private HashSet<State> currentStates = new HashSet<>();

    public Matcher(Pattern pattern)
    {
        currentStates.add(pattern.start);
        epsilonClosures();
    }

    public boolean match(String input)
    {
        for (int i = 0; i < input.length(); i++)
        {
            HashSet<State> next = new HashSet<>();
            char ch = input.charAt(i);

            for (State current : currentStates)
            {
                if (current.next.containsKey(ch))
                {
                    next.addAll(current.next.get(ch));
                }
            }

            currentStates = next;
            epsilonClosures();
        }

        for (State state : currentStates)
        {
            if (state.accept)
            {
                return true;
            }
        }

        return false;
    }

    private void epsilonClosures()
    {
        HashSet<State> add = new HashSet<>();

        for (State s : currentStates)
        {
            for (State e : s.epsilons)
            {
                if (!currentStates.contains(e))
                {
                    add.add(e);
                }
            }
        }

        if (!add.isEmpty())
        {
            currentStates.addAll(add);
            epsilonClosures();
        }
    }
}
