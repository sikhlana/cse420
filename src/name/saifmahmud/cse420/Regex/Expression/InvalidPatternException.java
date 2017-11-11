package name.saifmahmud.cse420.Regex.Expression;

public class InvalidPatternException extends RuntimeException
{
    public InvalidPatternException()
    {
        super();
    }

    public InvalidPatternException(String msg)
    {
        super(msg);
    }
}
