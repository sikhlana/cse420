package name.saifmahmud.cse420.Regex.Expression;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;

class State
{
    private static int uid = 0;

    Hashtable<Character, HashSet<State>> next = new Hashtable<>();
    HashSet<State> epsilons = new HashSet<>();
    State parent;

    int id = uid++;
    int groupId;

    boolean accept = false;
    boolean end = false;
    char modifier = 0;

    void addNextState(char ch, State state)
    {
        if (!next.containsKey(ch))
        {
            next.put(ch, new HashSet<>());
        }

        if (state.parent == null)
        {
            state.parent = this;
        }

        next.get(ch).add(state);
    }

    void addNextState(char ch, Collection<State> states)
    {
        for (State state : states)
        {
            addNextState(ch, state);
        }
    }

    public String toString()
    {
        return "" + id + (accept ? "✓" : "");
    }

    boolean hasModifier()
    {
        return modifier > 0;
    }
}

class GroupedState extends State
{
    State start;

    GroupedState(State start)
    {
        this.start = start;
    }
}