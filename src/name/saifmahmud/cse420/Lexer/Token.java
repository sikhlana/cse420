package name.saifmahmud.cse420.Lexer;

public class Token
{
    private Type type;
    private String value;

    public Token(Type type, String value)
    {
        this.type = type;
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public Type getType()
    {
        return type;
    }

    public String toString()
    {
        return type.toString() + ": " + value;
    }

    public enum Type
    {
        OTHERS("\\(", "\\)", "\\{", "\\}", ",", ";"),
//        PARENTHESIS_START("\\("),
//        PARENTHESIS_END("\\)"),
//        BLOCK_START("\\{"),
//        BLOCK_END("\\}"),
        SPACE("[ |\\t]+"),
//        COMMA(","),
        EOL("\r\n", "\n", "\r"),
//        DELIMINATOR(";"),
        KEYWORD("auto", "break", "case", "char", "const", "continue", "default", "do", "double", "else", "enum", "extern",
                "float", "for", "goto", "if", "int", "long", "register", "return", "short", "signed", "sizeof", "static",
                "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while"),
        NUMERIC_OPERATOR("=", "\\+", "-", "\\/", "\\*", "%"),
        LOGICAL_OPERATOR("==", "<=", ">=", "<", ">"),
        IDENTIFIER("[a-z][a-z|0-9]*"),
        NUMERIC("[0-9]+(\\.[0-9]*)?"),
        ;

        private String pattern;

        Type(String... pattern)
        {
            this.pattern = String.join("|", pattern);
        }

        public String getPattern()
        {
            return "^" + pattern + "$";
        }
    }

}